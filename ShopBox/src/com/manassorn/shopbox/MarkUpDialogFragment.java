package com.manassorn.shopbox;

import android.database.Cursor;


public class MarkUpDialogFragment extends SupplementDialogFragment {

	@Override
	protected Cursor querySupplement() {
		return getDbAdapter().queryMarkUp();
	}

}
