package com.manassorn.shopbox;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.manassorn.shopbox.BillFolderListAdapter.BillFolder;
import com.manassorn.shopbox.db.BillDao;
import com.manassorn.shopbox.db.DbHelper;
import com.manassorn.shopbox.value.Bill;

public class SelectBillByDateActivity extends Activity {
	Calendar calendar;
	SelectYearFragment selectYearFragment;
	SelectMonthFragment selectMonthFragment;
	SelectDateFragment selectDateFragment;
	SelectBillFragment selectBillFragment;
	Fragment currentFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_date);

		calendar = Calendar.getInstance();
		
		setFragment(selectYearFragment());
	}
	
	Calendar getCalendar() {
		return calendar;
	}

	void setFragment(Fragment fragment) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		if(currentFragment != null) {
			ft.addToBackStack(currentFragment.getClass().getName());
		}
		
		currentFragment = fragment;
		//TODO - animate
//		ft.setCustomAnimations(R.animator.slide_left_in, R.animator.slide_left_out, R.animator.slide_right_in, R.animator.slide_right_out);
        ft.replace(R.id.select_frame, fragment).commit();
	}

	void selectBill(int billId) {
		Intent intent = new Intent();
		intent.putExtra("BILL_ID", billId);
		setResult(RESULT_OK, intent);
		finish();
	}
	
	void selectYear(int year) {
		calendar.set(Calendar.YEAR, year);
		setFragment(selectMonthFragment());
	}
	
	void selectMonth(int month) {
		month = month - 1;
		calendar.set(Calendar.MONTH, month);
		setFragment(selectDateFragment());
	}
	
	void selectDate(int date) {
		calendar.set(Calendar.DATE, date);
		setFragment(selectBillFragment());
	}
	
	SelectYearFragment selectYearFragment() {
		if(selectYearFragment == null) {
			selectYearFragment = new SelectYearFragment();
		}
		return selectYearFragment;
	}
	
	SelectMonthFragment selectMonthFragment() {
		if(selectMonthFragment == null) {
			selectMonthFragment = new SelectMonthFragment();
		}
		return selectMonthFragment;
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

	public static class SelectYearFragment extends Fragment implements OnItemClickListener {
		SelectBillByDateActivity activity;
		ListView listView;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			activity = (SelectBillByDateActivity) getActivity();
			DbHelper dbHelper = DbHelper.getHelper(activity);
			BillDao billDao = BillDao.getInstance(dbHelper);
			
			List<BillFolder> folders = billDao.getYearFolder();
			BillFolderListAdapter adapter = new BillFolderListAdapter(activity, folders);
			listView = new ListView(activity);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
			return listView;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int year = ((Integer) view.getTag()).intValue();
			activity.selectYear(year);
		}
		
	}

	public static class SelectMonthFragment extends Fragment implements OnItemClickListener {
		SelectBillByDateActivity activity;
		ListView listView;
		List<BillFolder> folders;
		BillDao billDao;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			activity = (SelectBillByDateActivity) getActivity();
			DbHelper dbHelper = DbHelper.getHelper(activity);
			billDao = BillDao.getInstance(dbHelper);
			
			folders = new ArrayList<BillFolder>();
			BillFolderListAdapter adapter = new BillFolderListAdapter(activity, folders);
			listView = new ListView(activity);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
			return listView;
		}

		@Override
		public void onResume() {
			super.onResume();
			int year = activity.getCalendar().get(Calendar.YEAR);
			folders.clear();
			folders.addAll(billDao.getMonthFolder(year));
			listView.invalidate();
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int month = ((Integer) view.getTag()).intValue();
			activity.selectMonth(month);
		}
		
	}

	public static class SelectDateFragment extends Fragment implements OnItemClickListener {
		SelectBillByDateActivity activity;
		ListView listView;
		List<BillFolder> folders;
		BillDao billDao;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			activity = (SelectBillByDateActivity) getActivity();
			DbHelper dbHelper = DbHelper.getHelper(activity);
			billDao = BillDao.getInstance(dbHelper);
			
			folders = new ArrayList<BillFolder>();
			BillFolderListAdapter adapter = new BillFolderListAdapter(activity, folders);
			listView = new ListView(activity);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
			return listView;
		}

		@Override
		public void onResume() {
			super.onResume();
			Calendar cal = activity.getCalendar();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			folders.clear();
			folders.addAll(billDao.getDateFolder(year, month));
			listView.invalidate();
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int date = ((Integer) view.getTag()).intValue();
			activity.selectDate(date);
		}
		
	}

	public static class SelectBillFragment extends Fragment implements OnItemClickListener {
		SelectBillByDateActivity activity;
		ListView listView;
		List<Bill> bills;
		BillDao billDao;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			activity = (SelectBillByDateActivity) getActivity();
			DbHelper dbHelper = DbHelper.getHelper(activity);
			billDao = BillDao.getInstance(dbHelper);
			
			bills = new ArrayList<Bill>();
			BillListAdapter adapter = new BillListAdapter(activity, bills);
			listView = new ListView(activity);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
			return listView;
		}

		@Override
		public void onResume() {
			super.onResume();
			Calendar cal = activity.getCalendar();
			bills.clear();
			bills.addAll(billDao.getForDate(cal.getTime()));
			listView.invalidate();
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int billId = ((Integer) view.getTag()).intValue();
			activity.selectBill(billId);
		}
		
	}
}
