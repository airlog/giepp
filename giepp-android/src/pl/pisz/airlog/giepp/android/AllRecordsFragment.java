package pl.pisz.airlog.giepp.android;

import java.util.ArrayList;

import pl.pisz.airlog.giepp.data.CurrentStock;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class AllRecordsFragment extends Fragment {
		
		public AllRecordsFragment() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.all_records,container, false);			
			ListView list =  (ListView) rootView.findViewById(R.id.all_records_list);
						
//			list.setOnItemClickListener(this);
			ArrayList<CurrentStock> c1 = GiePPSingleton.getInstance().getCurrent();
			ArrayList<CurrentStock> c2 = new ArrayList<CurrentStock>();
			for(int i =0; i< c1.size();i++) {
				c2.add(c1.get(i));
			}
			AllRecordsAdapter adapter1 = new AllRecordsAdapter(getActivity(),
					R.layout.all_records_list_layout,c2);
			GiePPSingleton.getInstance().setAdapter1(adapter1);
			list.setAdapter(adapter1);
			
			return rootView;
		}
}
