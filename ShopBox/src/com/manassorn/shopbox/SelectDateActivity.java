package com.manassorn.shopbox;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.manassorn.shopbox.BillGroupListAdapter.BillGroup;
import com.manassorn.shopbox.db.BillDao;
import com.manassorn.shopbox.db.DbHelper;

public class SelectDateActivity extends FragmentActivity implements OnItemClickListener {
	ViewPager pager;
	PagerAdapter pagerAdapter;
	Calendar date;
	BillDao billDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_date);

		// Instantiate a ViewPager and a PagerAdapter.
		pager = (ViewPager) findViewById(R.id.pager);
		pagerAdapter = new SelectDatePagerAdapter();
		pager.setAdapter(pagerAdapter);
		
		ListView listView = new ListView(this);
		List<BillGroup> billGroups = new ArrayList<BillGroup>();
		BillGroupListAdapter adapter = new BillGroupListAdapter(this, billGroups);
		listView.setOnItemClickListener(this);
		
		DbHelper dbHelper = DbHelper.getHelper(this);
		billDao = BillDao.getInstance(dbHelper);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();
	}
	
	void initDate() {
		date = Calendar.getInstance();
		date.set(0, 0, 0, 0, 0, 0);
	}
	
//	ListView createYearListView() {
//		List<Integer> years = billDao.getAsOfYear();
//	}
	
	class SelectDatePagerAdapter extends PagerAdapter {
		List<View> views = new ArrayList<View>();
		
		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
		    View v = views.get (position);
		    container.addView (v);
		    return v;
		}

	}
}
