package com.manassorn.shopbox;

import com.manassorn.shopbox.R;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SellNavFragment extends Fragment {
    private ListView mSellMenuList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sell_nav, container, false);
		getActivity().setTitle(getResources().getString(R.string.selling));
        
        // content
		String[] sellMenus = getResources().getStringArray(R.array.sell_menu_array);
        mSellMenuList = (ListView) rootView.findViewById(R.id.sell_menu);
        mSellMenuList.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.menu_list_item, sellMenus));
        mSellMenuList.setOnItemClickListener(new SellMenuItemClickListener());
        getActivity().setTitle(R.string.sell);
		return rootView;
	}

    private class SellMenuItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
    	Intent intent = null;
    	switch(position) {
    		case 0:
    		default: 
    			intent = new Intent(getActivity(), SelectProductActivity.class);
    			break;
    		case 1: 
    			intent = new Intent(getActivity(), TodayBillActivity.class);
    	}
    	startActivity(intent);
    }

}
