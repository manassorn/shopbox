package com.manassorn.shopbox;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.manassorn.shopbox.BillFolderListAdapter.BillFolder;
import com.manassorn.shopbox.db.DbHelper;
import com.manassorn.shopbox.db.SellBillDao;
import com.manassorn.shopbox.value.Bill;

public class SelectBillByDateActivity extends FragmentActivity {
	Calendar calendar;
	SelectYearFragment selectYearFragment;
	SelectMonthFragment selectMonthFragment;
	SelectDateFragment selectDateFragment;
	SelectBillFragment selectBillFragment;
	Fragment currentFragment;
	TextView dateText;
	ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_date);

		calendar = Calendar.getInstance();
		
		dateText = (TextView) findViewById(R.id.date);
		
//		pager = (ViewPager) findViewById(R.id.pager);
//		pager.setAdapter(new MyPageAdapter(getSupportFragmentManager()));
		
		setFragment(selectYearFragment());
	}
//
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

	void setFragment(Fragment fragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if(currentFragment != null) {
			ft.addToBackStack(currentFragment.getClass().getName());
		}
		
		currentFragment = fragment;
		//TODO - animate
//		ft.setCustomAnimations(R.animator.slide_left_in, R.animator.slide_left_out, R.animator.slide_right_in, R.animator.slide_right_out);
//		ft.setCustomAnimations(R.anim.enter, R.anim.exit);
        ft.replace(R.id.select_frame, fragment).commit();
	}
	
	void onFragmentResume(Fragment fragment) {
		Date date = calendar.getTime();
		if(fragment == selectYearFragment) {
			dateText.setText("");
		} else if(fragment == selectMonthFragment) {
			dateText.setText(String.format("%tY", date));
		} else if(fragment == selectDateFragment) {
			dateText.setText(String.format("%tY %tB", date, date));
		} else if(fragment == selectBillFragment) {
			dateText.setText(String.format("%tY %tB %te", date, date, date));
		}
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
//		pager.setCurrentItem(1);
	}
	
	void selectMonth(int month) {
		month = month - 1;
		calendar.set(Calendar.MONTH, month);
		setFragment(selectDateFragment());
//		pager.setCurrentItem(2);
	}
	
	void selectDate(int day) {
		calendar.set(Calendar.DATE, day);
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
	
	class MyPageAdapter extends FragmentPagerAdapter {

		public MyPageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch(position) {
				case 0: return selectYearFragment();
				case 1: return selectMonthFragment();
				case 2: return selectDateFragment();
				case 3: return selectBillFragment();
			}
			return null;
		}

		@Override
		public int getCount() {
			return 4;
		}
		
	}

	public static class SelectYearFragment extends Fragment implements OnItemClickListener {
		SelectBillByDateActivity activity;
		ListView listView;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			activity = (SelectBillByDateActivity) getActivity();
			DbHelper dbHelper = DbHelper.getHelper(activity);
			SellBillDao sellBillDao = SellBillDao.getInstance(dbHelper);
			
			List<BillFolder> folders = sellBillDao.getYearFolder();
			BillFolderListAdapter adapter = new BillFolderListAdapter(activity, folders);
			listView = new ListView(activity);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
			listView.setBackgroundColor(0xffff00);
			return listView;
		}

		@Override
		public void onResume() {
			super.onResume();
			activity.onFragmentResume(this);
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
		SellBillDao sellBillDao;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			activity = (SelectBillByDateActivity) getActivity();
			DbHelper dbHelper = DbHelper.getHelper(activity);
			sellBillDao = SellBillDao.getInstance(dbHelper);
			
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
			activity.onFragmentResume(this);
			int year = activity.getCalendar().get(Calendar.YEAR);
			folders.clear();
			folders.addAll(sellBillDao.getMonthFolder(year));
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
		SellBillDao sellBillDao;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			activity = (SelectBillByDateActivity) getActivity();
			DbHelper dbHelper = DbHelper.getHelper(activity);
			sellBillDao = SellBillDao.getInstance(dbHelper);
			
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
			activity.onFragmentResume(this);
			Calendar cal = activity.getCalendar();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			folders.clear();
			folders.addAll(sellBillDao.getDateFolder(year, month));
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
		SellBillDao sellBillDao;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			activity = (SelectBillByDateActivity) getActivity();
			DbHelper dbHelper = DbHelper.getHelper(activity);
			sellBillDao = SellBillDao.getInstance(dbHelper);
			
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
			activity.onFragmentResume(this);
			Calendar cal = activity.getCalendar();
			bills.clear();
			bills.addAll(sellBillDao.getForDate(cal.getTime()));
			listView.invalidate();
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int billId = ((Integer) view.getTag()).intValue();
			activity.selectBill(billId);
		}
		
	}
}
