package com.manassorn.shopbox;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class StatsFragment extends Fragment implements OnTabChangeListener {
	static final String TAB_VALUE = "tab_value";
	static final String TAB_TOP5 = "tab_top5";
	StatsValueFragment statsValueFragment;
	StatsTop5Fragment statsTop5Fragment;
	TabHost tabHost;
	Spinner spinner;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_stats, null);
		tabHost = (TabHost) view.findViewById(android.R.id.tabhost);
		tabHost.setup();
		tabHost.addTab(newTab(TAB_VALUE, "มูลค่า", R.id.tab1));
		tabHost.addTab(newTab(TAB_TOP5, "5 อันดับแรก", R.id.tab2));
		tabHost.setOnTabChangedListener(this);
		// TODO - do it beautifully
		onTabChanged(TAB_VALUE);
		
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		createSpinner(activity);
		DrawerFragmentActivity drawerFragmentActivity = ((DrawerFragmentActivity) activity);
		drawerFragmentActivity.setTitle("สถิติ");
		drawerFragmentActivity.addMyActionBarView(spinner);
		drawerFragmentActivity.setLogoTextVisible(View.GONE);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		DrawerFragmentActivity drawerFragmentActivity = ((DrawerFragmentActivity) getActivity());
		drawerFragmentActivity.removeMyActionBarView(spinner);
		drawerFragmentActivity.setLogoTextVisible(View.VISIBLE);
	}
	
	TabSpec newTab(String tag, String label, int contentId) {
		TabSpec tabSpec = tabHost.newTabSpec(tag);
		tabSpec.setIndicator(label);
		tabSpec.setContent(contentId);
		return tabSpec;
	}

	void createSpinner(Context context) {
		if(spinner == null) {
			spinner = new Spinner(context);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
			        R.array.stats_type_array, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		}
	}

	@Override
	public void onTabChanged(String tabId) {
		if(tabId.equals(TAB_VALUE)) {
			replaceFragment(statsValueFragment());
		} else if(tabId.equals(TAB_TOP5)) {
			replaceFragment(statsTop5Fragment());
		}
	}
	
	void replaceFragment(Fragment fragment) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(android.R.id.tabcontent, fragment).commit();
	}
	
	StatsValueFragment statsValueFragment() {
		if(statsValueFragment == null) {
			statsValueFragment = new StatsValueFragment();
		}
		return statsValueFragment;
	}
	
	StatsTop5Fragment statsTop5Fragment() {
		if(statsTop5Fragment == null) {
			statsTop5Fragment = new StatsTop5Fragment();
		}
		return statsTop5Fragment;
	}
}
