package com.manassorn.shopbox;

import com.manassorn.shopbox.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DrawerActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] navs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer);
		
	}

    @Override
    public void setContentView(final int layoutResID) {
        View fullLayout = getLayoutInflater().inflate(R.layout.activity_drawer, null); // Your base layout here
        FrameLayout contentFrame = (FrameLayout) fullLayout.findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutResID, contentFrame, true); // Setting the content of layout your provided to the act_content frame
        super.setContentView(fullLayout);
        
        // drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        navs = getResources().getStringArray(R.array.drawer_nav_array);
//        mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, navs));
        mDrawerList.setAdapter(new DrawerListArrayAdapter(this, navs));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            onBackPressed();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch(position) {
        	default: fragment = new SellNavFragment();
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
		mDrawerLayout.closeDrawer(mDrawerList);
    	
    }
    
    public static class DrawerListArrayAdapter extends ArrayAdapter<String> {
        private LayoutInflater mInflater;

        public enum RowType {
            LIST_ITEM, HEADER_ITEM
        }

        public DrawerListArrayAdapter(Context context, String[] items) {
        	super(context, 0, items);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getViewTypeCount() {
            return RowType.values().length;

        }

        @Override
        public int getItemViewType(int position) {
        	if(isHeader(position))
        		return RowType.HEADER_ITEM.ordinal();
        	return RowType.LIST_ITEM.ordinal();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	View view;
        	if(convertView != null) {
        		view = convertView;
        	} else if(isHeader(position)) {
                view = (View) mInflater.inflate(R.layout.drawer_list_section, null);
        	} else {
                view = (View) mInflater.inflate(R.layout.drawer_list_item, null);
        	}
        	
        	String text = getItem(position);
        	if(isHeader(position))
        		text = text.substring(1);
        	
            TextView textView = (TextView) view.findViewById(R.id.text1);
            textView.setText(text);

            return view;
        }
        
        public boolean isHeader(int position) {
        	return getItem(position).startsWith("+");
        }
    }
}
