package pl.pisz.airlog.giepp.android;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
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
import android.view.View;
import android.widget.ProgressBar;

/**Główne Activity aplikacji. */
public class MainActivity extends FragmentActivity {
	
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private ProgressBar progressBar;

	/**Wyświetlany jest obraz na podstawie layoutu activity_main.xml. Tworzone są zakładki służące
	 * do nawigacji między fragmentami aplikacji. Tworzony jest ProgressBar
	 * informujący o tym, czy w danej chwili pobierane są aktualne dane o firmach. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.setProperty("http.agent", "Mozilla/5.0 (X11; Linux x86_64; rv:17.0) Gecko/20121202 Firefox/17.0 Iceweasel/17.0.1");
		GiePPSingleton.getInstance().setActivity(this);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		setContentView(R.layout.activity_main);
		
		progressBar = (ProgressBar) findViewById(R.id.main_progressBar);
		updateProgressBar();
		
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
			public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {}

			public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {}
		};
		
		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.ic_tab_account).setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.ic_tab_records).setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.ic_tab_observed).setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.ic_tab_stats).setTabListener(tabListener));
	}

	/** Tworzy menu na podstawie main_menu.xml.*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	
	/** Po wybraniu opcji z menu wyświetlane jest activity zawierające informacje 
	 * o aplikacji {@link AboutActivity} albo odświeżane są aktualne dane o firmach
	 * ({@link GiePPSingleton#refreshCurrent()})
	 * @param item opcja w menu, która została wybrana*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_refresh:
				GiePPSingleton.getInstance().refreshCurrent();
				return true;
			case R.id.menu_about:
				Log.i("giepp","info");
				Intent intent = new Intent(this, AboutActivity.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/** Metoda zmieniająca zachowanie się ProgressBaru informującego, czy 
	 * w danym momencie pobierane są aktualne dane o firmach.*/
	public void updateProgressBar() {
		if (GiePPSingleton.getInstance().isRefreshingCurrent()) {
			progressBar.setVisibility(View.VISIBLE);
		}
		else {
			progressBar.setVisibility(View.GONE);
		}

	}

	private class SectionsPagerAdapter extends FragmentPagerAdapter {
				
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
