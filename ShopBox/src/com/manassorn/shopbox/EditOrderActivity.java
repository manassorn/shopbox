package com.manassorn.shopbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.manassorn.shopbox.EditOrderAdapter.OnProductAmountChangedListener;
import com.manassorn.shopbox.value.BillItem;
import com.manassorn.shopbox.value.BillProductItem;
import com.manassorn.shopbox.value.BillSupplementItem;
import com.manassorn.shopbox.value.Order;
import com.manassorn.shopbox.value.OrderProduct;
import com.manassorn.shopbox.value.Supplement;

public class EditOrderActivity extends Activity implements OnClickListener,
		DiscountDialogFragment.OnClickListener, EditOrderAdapter.OnCheckBoxClickedListener,
		OnProductAmountChangedListener {
	private static final String TAG = EditOrderActivity.class.getSimpleName();
	private MarkUpDialogFragment markUpDialog;
	private DiscountDialogFragment discountDialog;
	private ArrayList<Integer> selectedItems;
	private View selectedItemBar;
	private ListView billItemListView;
	private EditOrderAdapter billItemAdapter;
	private List<BillItem> billItemList;
	private Order order;
	OrderManager orderManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_order);

		// action bar
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// bill
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			order = (Order) bundle.getParcelable("ORDER");
		} else {
			order = new Order();
		}
		orderManager = new OrderManager(order);

		billItemListView = (ListView) findViewById(R.id.final_product_list);
		billItemList = orderManager.getBillItems();
		billItemAdapter = new EditOrderAdapter(this, billItemList);
		billItemListView.setAdapter(billItemAdapter);
		billItemAdapter.setOnCheckBoxClickedListener(this);
		billItemAdapter.setOnProductAmountChangedListener(this);

		setTotalView(orderManager.getTotal());

		Button discount = (Button) findViewById(R.id.discount_button);
		discount.setOnClickListener(this);

		Button markUp = (Button) findViewById(R.id.markup_button);
		markUp.setOnClickListener(this);

		Button clearSelected = (Button) findViewById(R.id.clear_selected_button);
		clearSelected.setOnClickListener(this);

		Button delete = (Button) findViewById(R.id.delete_button);
		delete.setOnClickListener(this);
		
		Button submit = (Button) findViewById(R.id.submit_button);
		submit.setOnClickListener(this);

		selectedItems = new ArrayList<Integer>();
		selectedItemBar = findViewById(R.id.selected_item_bar);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.finalize_price, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra("ORDER", orderManager.getOrder());
		setResult(RESULT_OK, intent);
		finish();
//		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.markup_button:
				showMarkUpDialog();
				break;
			case R.id.discount_button:
				showDiscountDialog();
				break;
			case R.id.clear_selected_button:
				clearSelectedItems();
				break;
			case R.id.delete_button:
				if(isAllProductSelected()) {
					showCancelBillDialog();
				} else {
					showConfirmDeleteDialog();
				}
				break;
			case R.id.submit_button:
				startConfirmBillActivity();
		}
	}

	@Override
	public void onDiscountDialogClick(DialogFragment dialog, Supplement discount) {
		orderManager.addSupplement(discount);
		billItemList = orderManager.getBillItems();
		billItemAdapter.notifyDataSetChanged();
		setTotalView(orderManager.getTotal());
	}

	@Override
	public void onCheckBoxClicked(CheckBox checkBox, int position) {
		if (checkBox.isChecked()) {
			selectedItems.add(position);
		} else if (selectedItems.contains(position)) {
			selectedItems.remove(Integer.valueOf(position));
		}
		View v = findViewById(R.id.selected_item_bar);
		if (selectedItems.size() > 0) {
			v.setVisibility(View.VISIBLE);
		} else {
			v.setVisibility(View.GONE);
		}
	}

	@Override
	public void onProductAmountChanged(EditText editText, int position) {
		OrderProduct orderProduct = order.getOrderProducts().get(position);
		orderProduct.setAmount(Integer.parseInt(editText.toString()));
		
		billItemList = orderManager.getBillItems();
		billItemAdapter.notifyDataSetChanged();
		setTotalView(orderManager.getTotal());
	}

	private DiscountDialogFragment discountDialog() {
		if (discountDialog == null) {
			discountDialog = new DiscountDialogFragment();
		}
		return discountDialog;
	}

	private MarkUpDialogFragment markUpDialog() {
		if (markUpDialog == null) {
			markUpDialog = new MarkUpDialogFragment();
		}
		return markUpDialog;
	}

	private void clearSelectedItems() {
		for (int i = billItemListView.getCount() - 1; i >= 0; i--) {
			View view = billItemListView.getChildAt(i);
			CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox);
			// check if this is subtotal row
			if(cb != null) {
				cb.setChecked(false);
			}
		}
		selectedItems.clear();
		selectedItemBar.setVisibility(View.GONE);
	}

	private void deleteSelectedItems() {
		for (int index : selectedItems) {
			BillItem billItem = billItemList.get(index);
			if (billItem instanceof BillProductItem) {
				int productId = ((BillProductItem) billItem).getProductId();
				orderManager.removeProduct(productId);
			} else if (billItem instanceof BillSupplementItem) {
				int supplementId = ((BillSupplementItem) billItem).getSupplementId();
				orderManager.removeSupplement(supplementId);
			}
		}
		billItemList = orderManager.getBillItems();
		billItemAdapter.notifyDataSetChanged();
		selectedItems.clear();
		selectedItemBar.setVisibility(View.GONE);
		setTotalView(orderManager.getTotal());
	}
	
	protected void cancelBill() {
        startMainActivityClearTop();
	}

	protected void showConfirmDeleteDialog() {
		new AlertDialog.Builder(this).setTitle("ลบรายการ")
				.setMessage("ต้องการลบรายการ?")
				.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						deleteSelectedItems();
					}
				}).setNegativeButton("ไม่ใช่", null).show();
	}
	
	protected void showCancelBillDialog() {
		new AlertDialog.Builder(this).setTitle("ยกเลิกรายการ")
		.setMessage("คุณกำลังลบสินค้าทั้งหมด ยกเลิกรายการนี้?")
		.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				cancelBill();
			}
		}).setNegativeButton("ไม่ใช่", null).show();
	}
	
	protected void showMarkUpDialog() {
		List<Supplement> selectedSupplements = orderManager.getOrder().getSupplements();
		ArrayList<Supplement> selectedArrayList = new ArrayList<Supplement>(selectedSupplements);
		
		DialogFragment dialog = markUpDialog();
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList("selectedSupplements", selectedArrayList);
		dialog.setArguments(bundle);
		dialog.show(getFragmentManager(), "MarkUpDialogFragment");
	}
	
	protected void showDiscountDialog() {
		List<Supplement> selectedSupplements = orderManager.getOrder().getSupplements();
		ArrayList<Supplement> selectedArrayList = new ArrayList<Supplement>(selectedSupplements);
		
		DialogFragment dialog = discountDialog();
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList("selectedSupplements", selectedArrayList);
		dialog.setArguments(bundle);
		dialog.show(getFragmentManager(), "DiscountDialogFragment");
	}
	
	protected void setTotalView(double total) {
		TextView totalView = (TextView) findViewById(R.id.total);
		totalView.setText(String.format("฿%,.2f", total));
	}
	
	protected void startConfirmBillActivity() {
		ArrayList<BillItem> billItemList = orderManager.getBillItems();
		
		Intent intent = new Intent(this, ConfirmBillActivity.class);
		intent.putParcelableArrayListExtra("BILL_ITEM_ARRAY", billItemList);
		intent.putExtra("TOTAL", orderManager.getTotal());
		startActivity(intent);
	}
	
	protected void startMainActivityClearTop() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
	}
	
	protected boolean isAllProductSelected() {
		Collections.sort(selectedItems);
		for(int i=0; i<orderManager.getOrderProductCount(); i++) {
			if(i != selectedItems.get(i)) {
				return false;
			}
		}
		return true;
	}
}
