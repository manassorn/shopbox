package com.manassorn.shopbox;

import java.util.List;

import com.manassorn.shopbox.value.Supplement;


public class MarkUpDialogFragment extends SupplementDialogFragment {

	@Override
	protected List<Supplement> getSupplements() {
		return getDao().getForMarkUp();
	}

}
