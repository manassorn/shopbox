package com.manassorn.shopbox;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.manassorn.shopbox.db.CategoryDbAdapter;
import com.manassorn.shopbox.db.Dao;
import com.manassorn.shopbox.db.DaoManager;
import com.manassorn.shopbox.db.ProductDbAdapter;
import com.manassorn.shopbox.value.Category;
import com.manassorn.shopbox.value.Product;

public class ProductGridFragment extends Fragment implements OnItemClickListener, OnClickListener {
	private Category category;
	private Category[] childCategories;
	private Product[][] childCategoryProducts;
	private Product[] products;
	private ProductDbAdapter productDbAdapter;
	private CategoryDbAdapter categoryDbAdapter;
	private View theView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		theView = inflater.inflate(R.layout.fragment_product_grid, container, false);

		View view = theView;

		createView(CategoryDbAdapter.TOP_CATEGORY_ID);

		GridView gridview = (GridView) view.findViewById(R.id.product_grid);
		gridview.setOnItemClickListener(this);

		View noProductLabel = view.findViewById(R.id.no_product_label);
		View productGridLayout = view.findViewById(R.id.product_grid_layout);
		if (products.length == 0) {
			noProductLabel.setVisibility(View.VISIBLE);
			productGridLayout.setVisibility(View.GONE);
		} else {
			noProductLabel.setVisibility(View.GONE);
			productGridLayout.setVisibility(View.VISIBLE);
		}

		Button categoryBack = (Button) view.findViewById(R.id.category_back_icon);
		categoryBack.setOnClickListener(this);

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		productDbAdapter = new ProductDbAdapter(getActivity());
		productDbAdapter.open();
		categoryDbAdapter = new CategoryDbAdapter(getActivity());
		categoryDbAdapter.open();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (productDbAdapter != null) {
			productDbAdapter.close();
		}
		if (categoryDbAdapter != null) {
			categoryDbAdapter.close();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position < childCategories.length) {
			createView(childCategories[position].getId());
		} else {
			position = position - childCategories.length;
			((SelectProductActivity) getActivity()).addProduct(products[position]);
		}
	}

	protected void createView(int categoryId) {
		View view = theView;
		// category
		Cursor cursor = categoryDbAdapter.queryById(categoryId);
		category = CategoryDbAdapter.cursorToCategory(cursor);
		TextView categoryText = (TextView) view.findViewById(R.id.category_name);
		categoryText.setText(category.getName());

		// products & child categories
//		Dao<Product, Integer> dao = DaoManager.createDao(Product.class);
//		List<Product> productList = dao.queryForEq("CategoryId", categoryId);
//		products = productList.toArray(new Product[productList.size()]);
		
		cursor = productDbAdapter.queryByCategoryId(categoryId);
		products = ProductDbAdapter.cursorToProduct(cursor);
		cursor = categoryDbAdapter.queryByParentId(categoryId);
		childCategories = CategoryDbAdapter.cursorToCategoryArray(cursor);
		childCategoryProducts = new Product[childCategories.length][];
		for(int i=0; i<childCategoryProducts.length; i++) {
			cursor = productDbAdapter.queryByCategoryId(childCategories[i].getId());
			childCategoryProducts[i] = ProductDbAdapter.cursorToProduct(cursor);
		}

		GridView gridview = (GridView) view.findViewById(R.id.product_grid);
		gridview.setAdapter(new ProductGridAdapter(getActivity(), childCategories,
				childCategoryProducts, products));
		
		Button categoryBack = (Button) view.findViewById(R.id.category_back_icon);
		if(categoryId == CategoryDbAdapter.TOP_CATEGORY_ID ) {
			categoryBack.setVisibility(View.GONE);
		} else {
			categoryBack.setVisibility(View.VISIBLE);
		}
	}

	public static class ProductGridAdapter extends BaseAdapter {
		private final String TAG = ProductGridAdapter.class.getSimpleName();
		private Context context;
		private LayoutInflater mInflater;
		private Product[] products;
		private Category[] categories;
		private Product[][] categoryProducts;

		private enum GridType {
			CATEGORY, PRODUCT
		}

		public ProductGridAdapter(Context context, Category[] categories,
				Product[][] categoryProducts, Product[] products) {
			this.context = context;
			this.categories = categories;
			this.products = products;
			this.categoryProducts = categoryProducts;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return categories.length + products.length;
		}

		@Override
		public Object getItem(int position) {
			if (position < categories.length)
				return categories[position];
			return products[position - categories.length];
		}

		@Override
		public long getItemId(int position) {
			if (position < categories.length)
				return categories[position].getId() + 10000000L;
			return products[position - categories.length].getId();
		}

		@Override
		public int getItemViewType(int position) {
			if (position < categories.length)
				return GridType.CATEGORY.ordinal();
			return GridType.PRODUCT.ordinal();
		}

		@Override
		public int getViewTypeCount() {
			return GridType.values().length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (position < categories.length) {
				return getCategoryView(position, convertView, parent);
			} else {
				return getProductView(position, convertView, parent);
			}
		}

		public View getCategoryView(int position, View convertView, ViewGroup parent) {
			View grid = mInflater.inflate(R.layout.category_grid_item, parent, false);

			Category category = categories[position];
			Product[] childProducts = categoryProducts[position];

			int[] imageViewIndices = new int[] { R.id.category_image_1, R.id.category_image_2,
					R.id.category_image_3, R.id.category_image_4 };
			for (int i = 0; i < 4; i++) {
				ImageView imageView = (ImageView) grid.findViewById(imageViewIndices[i]);
				if (i < childProducts.length) {
					String path = context.getExternalFilesDir(null) + "/products/"
							+ childProducts[i].getId() + ".jpg";
					Bitmap bmp = BitmapFactory.decodeFile(path);
					if(bmp != null) {
						imageView.setImageBitmap(bmp);
					} else {
						imageView.setImageResource(R.drawable.no_product_image);
					}
				} else {
					imageView.setImageResource(R.drawable.no_product_image);
				}
			}
			TextView nameText = (TextView) grid.findViewById(R.id.category_name);
			nameText.setText(category.getName());
			return grid;
		}

		public View getProductView(int position, View convertView, ViewGroup parent) {
			View grid = mInflater.inflate(R.layout.product_grid_item, parent, false);

			ImageView imageView = (ImageView) grid.findViewById(R.id.product_image);
			TextView priceText = (TextView) grid.findViewById(R.id.product_price);
			TextView nameText = (TextView) grid.findViewById(R.id.product_name);

			Product product = products[position - categories.length];
			String path = context.getExternalFilesDir(null) + "/products/" + product.getId()
					+ ".jpg";
			Bitmap bmp = BitmapFactory.decodeFile(path);
			if(bmp != null) {
				imageView.setImageBitmap(bmp);
			} else {
				imageView.setImageResource(R.drawable.no_product_image);
			}
			priceText.setText("ß" + String.valueOf(product.getPrice()));
			nameText.setText(product.getName());
			return grid;
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.category_back_icon:
				if (category.getId() != 0) {
					createView(category.getParentId());
				}
		}

	}

}
