package pl.pisz.airlog.giepp.android;

import android.app.Activity;
import android.os.Bundle;

/** Activity z podstawowymi informacjami o aplikacji.*/
public class AboutActivity extends Activity {
	
	/** Wyświetlany jest layout about.xml, w którym zawarte są podstawowe informacje o aplikacji.*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	}
}