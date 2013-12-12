package pl.pisz.airlog.giepp.android;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {
	
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

		@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		GiePPSingleton.getInstance().setActivity(this);
		try{
			GiePPSingleton.getInstance().addToObserved("ALIOR");
			GiePPSingleton.getInstance().addToObserved("ZYWIEC");
			GiePPSingleton.getInstance().addToObserved("CCC");
		}catch(Exception e){
			Log.i("giepp","Blad: " + e);
		}
		final ActionBar actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    
		setContentView(R.layout.activity_main);
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
	    mViewPager.setOnPageChangeListener(
	            new ViewPager.SimpleOnPageChangeListener() {
	                @Override
	                public void onPageSelected(int position) {
	                    getActionBar().setSelectedNavigationItem(position);
	                }
	            }
	    );

		mViewPager.setAdapter(mSectionsPagerAdapter);

	    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
	        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	            mViewPager.setCurrentItem(tab.getPosition());
	        }

	        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	            // hide the given tab
	        }

	        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	            // probably ignore this event
	        }	    
	    };
	    actionBar.addTab(actionBar.newTab().setIcon(R.drawable.ic_tab_account).setTabListener(tabListener));
	    actionBar.addTab(actionBar.newTab().setIcon(R.drawable.ic_tab_records).setTabListener(tabListener));
	    actionBar.addTab(actionBar.newTab().setIcon(R.drawable.ic_tab_observed).setTabListener(tabListener));
	    actionBar.addTab(actionBar.newTab().setIcon(R.drawable.ic_tab_stats).setTabListener(tabListener));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
			    MenuInflater inflater = getMenuInflater();
			    inflater.inflate(R.menu.main_menu, menu);
			    return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_refresh:
				GiePPSingleton.getInstance().refresh();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
				
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment;
			switch (position) {
				default:
				case 0: 
					fragment = new MyAccountFragment();
					GiePPSingleton.getInstance().setFragment1((MyAccountFragment)fragment);
					break;
				case 1: 
					fragment = new AllRecordsFragment();
					break;
				case 2: 
					fragment = new ObservedFragment();
					break;
				case 3: 
					fragment = new StatsFragment();
					break;
			}
			Bundle args = new Bundle();
			fragment.setArguments(args);
			return fragment;				
		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return getString(R.string.title_section4).toUpperCase(l);
			}
			return null;
		}
	}
}
