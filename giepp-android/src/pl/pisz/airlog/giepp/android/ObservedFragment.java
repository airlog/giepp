package pl.pisz.airlog.giepp.android;

import java.util.ArrayList;

import pl.pisz.airlog.giepp.data.CurrentStock;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ObservedFragment extends Fragment {
		
	public ObservedFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.observed,container, false);			
		ListView list =  (ListView) rootView.findViewById(R.id.observed_list);
					
//		list.setOnItemClickListener(this);
		ArrayList<CurrentStock> c1 = GiePPSingleton.getInstance().getCurrent();
		ArrayList<CurrentStock> c2 = new ArrayList<CurrentStock>();
		for(int i = 0; i < c1.size(); i++){
			if(GiePPSingleton.getInstance().getObserved().contains(c1.get(i).getName())){
				c2.add(c1.get(i));
			}
		}
		ObservedAdapter adapter2 = new ObservedAdapter(getActivity(), R.layout.all_records_list_layout,c2);
		GiePPSingleton.getInstance().setAdapter2(adapter2);
		list.setAdapter(adapter2);
		
		return rootView;
	}
}
