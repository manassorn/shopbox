package com.manassorn.shopbox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.manassorn.shopbox.BillFolderListAdapter.BillFolder;
import com.manassorn.shopbox.db.BillDao;
import com.manassorn.shopbox.db.DbHelper;
import com.manassorn.shopbox.value.Bill;

public class SelectBillByDateFragment extends Fragment {
	private static final String TAG = "SelectBillByDateFragment";
	Calendar calendar = Calendar.getInstance();
	SelectYearMonthFragment selectYearMonthFragment;
	SelectDateFragment selectDateFragment;
	SelectBillFragment selectBillFragment;
	OnSelectBillByDate onSelectBillByDate;
	TextView dateText;
	BillDao billDao;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_select_date, null);

		dateText = (TextView) view.findViewById(R.id.date);
		
		Class<BillDao> clazz = (Class<BillDao>) getActivity().getIntent().getSerializableExtra(
				"BILL_DAO_CLASS");
		DbHelper dbHelper = DbHelper.getHelper(getActivity());
		try {
			Method method = clazz.getMethod("getInstance", DbHelper.class);
			billDao = (BillDao) method.invoke(null, dbHelper);
		} catch (NoSuchMethodException e) {
			Log.e(TAG, "Instantiate Dao Error", e);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "Instantiate Dao Error", e);
		} catch (IllegalAccessException e) {
			Log.e(TAG, "Instantiate Dao Error", e);
		} catch (InvocationTargetException e) {
			Log.e(TAG, "Instantiate Dao Error", e);
		}
		
		replaceFragment(selectYearMonthFragment());
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof OnSelectBillByDate) {
			onSelectBillByDate = (OnSelectBillByDate) activity;
		}
	}
	
//	@Override
//	public void onBackPressed() {
//		if(pager.getCurrentItem() == 0) {
//			super.onBackPressed();
//		} else {
//			pager.setCurrentItem(pager.getCurrentItem() - 1);
//		}
//	}

	Calendar getCalendar() {
		return calendar;
	}
	
	BillDao getBillDao() {
		return billDao;
	}

	void replaceFragment(Fragment fragment) {
		FragmentManager fragmentManager = getChildFragmentManager();
		Fragment currentFragment = fragmentManager.findFragmentById(R.id.select_frame);
		FragmentTransaction ft = fragmentManager.beginTransaction();
		if(currentFragment != null) {
			ft.addToBackStack(currentFragment.getClass().getName());
		}
		
		currentFragment = fragment;
		//TODO - animate
//		ft.setCustomAnimations(R.animator.slide_left_in, R.animator.slide_left_out, R.animator.slide_right_in, R.animator.slide_right_out);
//		ft.setCustomAnimations(R.anim.enter, R.anim.exit);
        ft.replace(R.id.select_frame, fragment).commit();
	}
	
	void updateDateOnResumeFragment(Fragment fragment) {
		Date date = calendar.getTime();
		if(fragment == selectYearMonthFragment) {
			dateText.setText("");
		} else if(fragment == selectDateFragment) {
			dateText.setText(new SimpleDateFormat("MMM yyyy").format(date));
		} else if(fragment == selectBillFragment) {
			dateText.setText(new SimpleDateFormat("d MMM yyyy").format(date));
		}
	}

	void selectBill(int billId) {
		if(onSelectBillByDate != null) {
			onSelectBillByDate.onSelectBillId(billDao, billId);
		}
	}
	
	void selectYearMonth(int year, int month) {
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		replaceFragment(selectDateFragment());
	}
	
	void selectDate(int day) {
		calendar.set(Calendar.DATE, day);
		replaceFragment(selectBillFragment());
	}
	
	void selectDate(Date date) {
		calendar.setTime(date);
		replaceFragment(selectBillFragment());
	}
	
	SelectYearMonthFragment selectYearMonthFragment() {
		if(selectYearMonthFragment == null) {
			selectYearMonthFragment = new SelectYearMonthFragment();
		}
		return selectYearMonthFragment;
	}
	
	SelectDateFragment selectDateFragment() {
		if(selectDateFragment == null) {
			selectDateFragment = new SelectDateFragment();
		}
		return selectDateFragment;
	}
	
	SelectBillFragment selectBillFragment() {
		if(selectBillFragment == null) {
			selectBillFragment = new SelectBillFragment();
		}
		return selectBillFragment;
	}

	public static class SelectYearMonthFragment extends Fragment implements OnItemClickListener {
		SelectBillByDateFragment parentFragment;
		ListView listView;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			parentFragment = (SelectBillByDateFragment) getParentFragment();
			
			Context context = getActivity().getBaseContext();
			List<BillFolder> folders = parentFragment.getBillDao().getMonthsAgoFolder(2);
			BillFolderListAdapter adapter = new BillFolderListAdapter(context, folders);
			listView = new ListView(context);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);

			return listView;
		}

		@Override
		public void onResume() {
			super.onResume();
			parentFragment.updateDateOnResumeFragment(this);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			long milliseconds = ((Long) view.getTag()).longValue();
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date(milliseconds));
			parentFragment.selectYearMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
		}
		
	}

	public static class SelectDateFragment extends Fragment implements OnItemClickListener {
		SelectBillByDateFragment parentFragment;
		ListView listView;
		List<BillFolder> folders;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			parentFragment = (SelectBillByDateFragment) getParentFragment();
			
			Context context = getActivity().getBaseContext();
			folders = new ArrayList<BillFolder>();
			BillFolderListAdapter adapter = new BillFolderListAdapter(context, folders);
			listView = new ListView(context);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
			return listView;
		}

		@Override
		public void onResume() {
			super.onResume();
			parentFragment.updateDateOnResumeFragment(this);
			Calendar cal = parentFragment.getCalendar();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			folders.clear();
			folders.addAll(parentFragment.getBillDao().getDateFolder(year, month));
			listView.invalidate();
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			long milliseconds = ((Long) view.getTag()).longValue();
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date(milliseconds));
			parentFragment.selectDate(cal.get(Calendar.DATE));
		}
		
	}

	public static class SelectBillFragment extends Fragment implements OnItemClickListener {
		SelectBillByDateFragment parentFragment;
		ListView listView;
		List<Bill> bills;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			parentFragment = (SelectBillByDateFragment) getParentFragment();
			
			Context context = getActivity().getBaseContext();
			bills = new ArrayList<Bill>();
			BillListAdapter adapter = new BillListAdapter(context, bills);
			listView = new ListView(context);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
			return listView;
		}

		@Override
		public void onResume() {
			super.onResume();
			parentFragment.updateDateOnResumeFragment(this);
			bills.clear();
			bills.addAll(parentFragment.getBillDao().getForDate(parentFragment.getCalendar().getTime()));
			listView.invalidate();
			// TODO - if list's empty, display some texts
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int billId = ((Integer) view.getTag()).intValue();
			parentFragment.selectBill(billId);
		}
		
	}
	
	public static interface OnSelectBillByDate {
		public void onSelectBillId(BillDao dao, int billId);
	}
}
