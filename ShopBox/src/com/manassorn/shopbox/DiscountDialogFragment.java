package com.manassorn.shopbox;

import java.util.List;

import com.manassorn.shopbox.value.Supplement;

public class DiscountDialogFragment extends SupplementDialogFragment {

	@Override
	protected List<Supplement> getSupplements() {
		return getDao().getForDiscount();
	}

}
