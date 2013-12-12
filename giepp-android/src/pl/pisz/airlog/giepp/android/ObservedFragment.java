package pl.pisz.airlog.giepp.android;

import java.util.ArrayList;

import pl.pisz.airlog.giepp.data.CurrentStock;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ObservedFragment extends Fragment implements OnItemClickListener{
		
	private ObservedAdapter adapter2;

	public ObservedFragment() {
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(getActivity(), CompanyDetailsActivity.class);
		String name = adapter2.getName(position);
		GiePPSingleton.getInstance().setName(name);
		getActivity().startActivity(intent);      
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.observed,container, false);			
		ListView list =  (ListView) rootView.findViewById(R.id.observed_list);
					
		ArrayList<CurrentStock> c1 = GiePPSingleton.getInstance().getCurrent();
		ArrayList<CurrentStock> c2 = new ArrayList<CurrentStock>();
		for(int i = 0; i < c1.size(); i++){
			if(GiePPSingleton.getInstance().getObserved().contains(c1.get(i).getName())){
				c2.add(c1.get(i));
			}
		}
		adapter2 = new ObservedAdapter(getActivity(), R.layout.all_records_list_layout,c2);
		GiePPSingleton.getInstance().setAdapter2(adapter2);
		list.setAdapter(adapter2);
		list.setOnItemClickListener(this);
		
		return rootView;
	}
}
