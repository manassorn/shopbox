package com.manassorn.shopbox;

import java.lang.reflect.Field;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.manassorn.shopbox.value.Order;
import com.manassorn.shopbox.value.OrderProduct;
import com.manassorn.shopbox.value.Product;

public class SellFragment extends Fragment implements OnClickListener {
	private static final String TAG = "SellFragment";
	private InputType inputType = InputType.SELECT_GRID;
	private InputType lastBarcodeInputType = InputType.SCAN_BARCODE;
	private OrderManager orderManager;
	private BeepManager beepManager;
	private ProductGridFragment productGridFragment;
	private ScanBarcodeFragment scanBarcodeFragment;
	private EnterBarcodeFragment enterBarcodeFragment;

	private enum InputType {
		SELECT_GRID, SCAN_BARCODE, ENTER_BARCODE
	}

	public SellFragment() {
		orderManager = new OrderManager(new Order());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		beepManager = new BeepManager(getActivity());

		View view = inflater.inflate(R.layout.activity_select_product, null);
		//
		Button totalPriceButton = (Button) view.findViewById(R.id.total_price_button);
		totalPriceButton.setOnClickListener(this);
		Button addButton = (Button) view.findViewById(R.id.add_button);
		addButton.setOnClickListener(this);
		Button minusButton = (Button) view.findViewById(R.id.minus_button);
		minusButton.setOnClickListener(this);
		//
		replaceFragment(InputType.SELECT_GRID);

		view.post(new Runnable() {
			@Override
			public void run() {
				((AutoHideKeyboardActivity) getActivity())
						.addObservedViewResource(R.id.last_product_amount1);
			}
		});
		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Order order = (Order) data.getParcelableExtra("ORDER");
		orderManager = new OrderManager(order);
		updateLastProductItem();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.sell_product, menu);
		if (inputType == InputType.SELECT_GRID) {
			menu.findItem(R.id.action_barcode).setVisible(true);
			menu.findItem(R.id.action_product_grid).setVisible(false);
		} else if (inputType == InputType.SCAN_BARCODE || inputType == InputType.ENTER_BARCODE) {
			menu.findItem(R.id.action_barcode).setVisible(false);
			menu.findItem(R.id.action_product_grid).setVisible(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_barcode:
				replaceFragment(lastBarcodeInputType);
				return true;
			case R.id.action_product_grid:
				replaceFragment(InputType.SELECT_GRID);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		// to prevent error
		// java.lang.IllegalStateException: Activity has been destroyed
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	private void replaceFragment(InputType inputType) {
		Fragment fragment = null;

		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		switch (inputType) {
			case SELECT_GRID:
			default:
				fragment = productGridFragment();
				if (this.inputType != InputType.SELECT_GRID) {
					lastBarcodeInputType = this.inputType;
				}
				break;
			case SCAN_BARCODE:
				fragment = scanBarcodeFragment();
				if (this.inputType == InputType.ENTER_BARCODE) {
					ft.setCustomAnimations(0, R.animator.slide_down);
				}
				break;
			case ENTER_BARCODE:
				fragment = enterBarcodeFragment();
				if (this.inputType == InputType.SCAN_BARCODE) {
					ft.setCustomAnimations(R.animator.slide_up, 0);
				}
		}
		ft.replace(R.id.fragment_container, fragment).commit();
		this.inputType = inputType;

		getActivity().invalidateOptionsMenu();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.total_price_button:
				startEditBillActivity();
				break;
			case R.id.add_button:
				if (orderManager.getLastOrderProduct() != null) {
					addProduct(orderManager.getLastOrderProduct().getProduct());
					playBarcodeBeep();
				}
				break;
			case R.id.minus_button:
				if (orderManager.getLastOrderProduct() != null) {
					minusProduct(orderManager.getLastOrderProduct().getProduct());
				}
				break;
		}

	}

	void addProduct(Product product) {
		orderManager.addProduct(product, 1);
		updateLastProductItem();
		playBarcodeBeep();
	}

	private void minusProduct(Product product) {
		orderManager.addProduct(product, -1);
		updateLastProductItem();
		playBarcodeBeep();
	}

	private void updateLastProductItem() {
		OrderProduct lastOrderProduct = orderManager.getLastOrderProduct();
		if (lastOrderProduct == null) {
			clearLastOrderProduct();
			return;
		}
		View view = getView();
		TextView lastProductName = (TextView) view.findViewById(R.id.last_product_name);
		TextView lastProductPrice = (TextView) view.findViewById(R.id.last_product_price);
		EditText lastProductAmount = (EditText) view.findViewById(R.id.last_product_amount1);
		TextView productCount = (TextView) view.findViewById(R.id.product_count);
		Button totalPrice = (Button) view.findViewById(R.id.total_price_button);

		Product product = lastOrderProduct.getProduct();
		lastProductName.setText(product.getName());
		lastProductPrice.setText(String.format("�%,.2f", product.getPrice()));
		lastProductAmount.setText(String.valueOf(lastOrderProduct.getAmount()));
		productCount.setText(orderManager.getOrderProductCount() + " ��¡��");
		totalPrice.setText(String.format("�%,.2f >", orderManager.getTotal()));

		Button addButton = (Button) view.findViewById(R.id.add_button);
		Button minusButton = (Button) view.findViewById(R.id.minus_button);
		totalPrice.setEnabled(orderManager.getOrderProductCount() > 0);
		addButton.setEnabled(orderManager.getOrderProductCount() > 0);
		minusButton.setEnabled(orderManager.getOrderProductCount() > 0);
	}

	private void clearLastOrderProduct() {
		View view = getView();
		TextView lastProductName = (TextView) view.findViewById(R.id.last_product_name);
		lastProductName.setText("");
		TextView lastProductPrice = (TextView) view.findViewById(R.id.last_product_price);
		lastProductPrice.setText("");
		EditText lastProductAmount = (EditText) view.findViewById(R.id.last_product_amount1);
		lastProductAmount.setText("");
		TextView productCount = (TextView) view.findViewById(R.id.product_count);
		productCount.setText("�������¡��");
		Button totalPrice = (Button) view.findViewById(R.id.total_price_button);
		totalPrice.setText("�0.00 >");
	}

	protected void playBarcodeBeep() {
		beepManager.playBeepSoundAndVibrate();
	}

	protected void startEditBillActivity() {
		Intent intent = new Intent(getActivity(), EditOrderActivity.class);
		intent.putExtra("ORDER", orderManager.getOrder());
		startActivityForResult(intent, 0);
	}

	public void openEnterBarcode() {
		replaceFragment(InputType.ENTER_BARCODE);
	}

	public void openScanBarcode() {
		replaceFragment(InputType.SCAN_BARCODE);
	}

	private ProductGridFragment productGridFragment() {
		if (this.productGridFragment == null) {
			this.productGridFragment = new ProductGridFragment();
		}
		return this.productGridFragment;
	}

	private ScanBarcodeFragment scanBarcodeFragment() {
		if (this.scanBarcodeFragment == null) {
			this.scanBarcodeFragment = new ScanBarcodeFragment();
		}
		return this.scanBarcodeFragment;
	}

	private EnterBarcodeFragment enterBarcodeFragment() {
		if (this.enterBarcodeFragment == null) {
			this.enterBarcodeFragment = new EnterBarcodeFragment();
		}
		return this.enterBarcodeFragment;
	}
}
