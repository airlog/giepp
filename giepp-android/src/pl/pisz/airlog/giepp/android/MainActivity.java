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
import android.view.Menu;

public class MainActivity extends FragmentActivity {
	
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

		@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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
	    actionBar.addTab(actionBar.newTab().setIcon(R.drawable.account).setTabListener(tabListener));
	    actionBar.addTab(actionBar.newTab().setIcon(R.drawable.stock).setTabListener(tabListener));
	    actionBar.addTab(actionBar.newTab().setIcon(R.drawable.observed).setTabListener(tabListener));
	    actionBar.addTab(actionBar.newTab().setIcon(R.drawable.stats).setTabListener(tabListener));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
	    return true;
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
