package com.manassorn.shopbox;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DrawerActivity extends AutoHideKeyboardActivity implements DrawerListener {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ViewGroup contentFrame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_drawer);
	}

    @Override
    public void setContentView(final int layoutResID) {
    	contentFrame = (ViewGroup) findViewById(R.id.drawer_frame);
    	getLayoutInflater().inflate(layoutResID, contentFrame, true);
        
        // drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(0);
        mDrawerLayout.setDrawerListener(this);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
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
    
    protected ListView getDrawerListView() {
    	return mDrawerList;
    }
    
    protected DrawerLayout getDrawerLayout() {
    	return mDrawerLayout;
    }
    
    public void openDrawer() {
    	mDrawerLayout.openDrawer(mDrawerList);
    }
    
    public void closeDrawer() {
    	mDrawerLayout.closeDrawer(mDrawerList);
    }
    
    public void toggleDrawer() {
    	if(isDrawerOpen()) {
    		closeDrawer();
    	} else {
    		openDrawer();
    	}
    }
    
    public boolean isDrawerOpen() {
		return mDrawerLayout.isDrawerOpen(mDrawerList);
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

	@Override
	public void onDrawerClosed(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDrawerOpened(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDrawerSlide(View drawerView, float slideOffset) {
		contentFrame.setX(slideOffset * mDrawerList.getMeasuredWidth());
		
	}

	@Override
	public void onDrawerStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
}
