package com.manassorn.shopbox;

import com.manassorn.shopbox.R;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ReturnMenuFragment extends Fragment implements OnItemClickListener {
    private ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_list, container, false);
        
        // content
		String[] sellMenus = getResources().getStringArray(R.array.return_menu_array);
        listView = (ListView) rootView.findViewById(R.id.menu_list);
        listView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.menu_list_item, sellMenus));
        listView.setOnItemClickListener(this);
		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	Intent intent = null;
    	switch(position) {
    		case 0:
    		default: 
    			intent = new Intent(getActivity(), ReturnOptionMenuActivity.class);
    			break;
    		case 1: 
    			intent = new Intent(getActivity(), TodayBillActivity.class);
    	}
    	startActivity(intent);
//    	getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
	}

}
