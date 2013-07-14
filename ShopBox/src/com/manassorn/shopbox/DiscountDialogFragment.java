package com.manassorn.shopbox;

import android.database.Cursor;

public class DiscountDialogFragment extends SupplementDialogFragment {

	@Override
	protected Cursor querySupplement() {
		return getDbAdapter().queryDiscount();
	}

}
