package com.dungdq.tintuccapnhat;

import java.util.ArrayList;

//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.InterstitialAd;
import com.sromku.simple.fb.SimpleFacebook;

import com.dungdq.tintuccapnhat.adapter.NavDrawerListAdapter;
import com.dungdq.tintuccapnhat.model.NavDrawerItem;
import com.dungdq.tintuccapnhat.receiver.NetworkStatusReceiver;
import com.dungdq.tintuccapnhat.services.NotificationService;
import com.dungdq.tintuccapnhat.utils.Const;
import com.dungdq.tintuccapnhat.utils.Utils;

import android.app.Activity;
//import android.app.Fragment;
//import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	public static DrawerLayout mDrawerLayout;
	public static ListView mDrawerList;
	public static ActionBarDrawerToggle mDrawerToggle;

	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;

	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	
	public static Toolbar toolbar;
	private SimpleFacebook mSimpleFacebook;
	
	Handler mHandler;

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
//			MainActivity.this.finish();
			this.onBackPressed();
//			System.exit(0);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Start service notification
		startService(new Intent(MainActivity.this, NotificationService.class));
		
		Utils.getKeyHash(MainActivity.this);
		mSimpleFacebook = SimpleFacebook.getInstance(this);
		
		// Display full screen banner ad
		Utils.displayInterstitial(MainActivity.this);
//		mHandler = new Handler();
//		mHandler.postDelayed(updateTimerThread, 5 * 60 * 1000);
		
		toolbar = (Toolbar) findViewById(R.id.my_toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		}
		
//		TinTucCapNhatApplication.progressBar = (ProgressBar) findViewById(R.id.marker_progress);
//		TinTucCapNhatApplication.tvError = (TextView) findViewById(R.id.tvError);
		
		/**
		 * Rate application
		 */
		Const.APP_PACKAGE_NAME = getApplicationInfo().packageName;
		SharedPreferences prefs = getSharedPreferences("rate_app", 0);
		if (!prefs.getBoolean("dontshowagain", false)) {
			SharedPreferences.Editor editor = prefs.edit();
			
			//Add to launch Counter
	        long launch_count = prefs.getLong("launch_count", 0) + 1;
	        editor.putLong("launch_count", launch_count);
	        
	        //Get Date of first launch
	        Long date_firstLaunch = prefs.getLong("date_first_launch", 0);
	        if (date_firstLaunch == 0) {
	            date_firstLaunch = System.currentTimeMillis();
	            editor.putLong("date_first_launch", date_firstLaunch);
	        }
	        
	        //Wait at least X days to launch
	        if (launch_count >= Const.LAUNCH_UNTIL_PROMPT) {
	            if (System.currentTimeMillis() >= date_firstLaunch + (12 * 60 * 60 * 1000)){
	                Utils.showRateDialog(MainActivity.this, editor);
	                editor.putLong("date_first_launch", System.currentTimeMillis());
	            }
	        }
	        
	        editor.commit();
		}
        /**
         * End rate application
         */
		

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.lv_navigation_menu);
		
		// Add header to navigation drawer
		View headerNavigationMenu = (View) getLayoutInflater().inflate(
				R.layout.header_drawer_list, null);
		mDrawerList.addHeaderView(headerNavigationMenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Doc gi hom nay
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		// Tin ua thich
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		// Lien he
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));
		// Danh gia
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));
		// Moi ban be
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new NavigationMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeButtonEnabled(true);
//		getSupportActionBar().setElevation(0); // Remove shadow below actionbar

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(MainActivity.this);
		mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
//				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.navigation_drawer_open, // nav drawer open -
													// description for
													// accessibility
				R.string.navigation_drawer_close // nav drawer close -
													// description for
													// accessibility
		) {
			public void onDrawerClosed(View drawerView) {
				// getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				super.onDrawerClosed(drawerView);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				// getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				super.onDrawerOpened(drawerView);

				if (!mUserLearnedDrawer) {
					// The user manually opened the drawer; store this flag to
					// prevent auto-showing
					// the navigation drawer automatically in the future.
					mUserLearnedDrawer = true;
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(MainActivity.this);
					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true)
							.apply();
				}

				invalidateOptionsMenu();
			}
		};

		if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(mDrawerList);
		}

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		if (savedInstanceState == null) {
			displayView(1);
		}
	}

	private class NavigationMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}
	
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 1:
			fragment = new ReadTodayFragment();
			break;
		case 2:
			fragment = new FavoriteFragment();
			break;
		case 3:
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("message/rfc822");
			i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"dungtoho@gmail.com"});
			i.putExtra(Intent.EXTRA_SUBJECT, "Tiêu đề");
			i.putExtra(Intent.EXTRA_TEXT   , "Nội dung");
			try {
			    startActivity(Intent.createChooser(i, "Góp ý qua ..."));
			} catch (android.content.ActivityNotFoundException ex) {
//			    Toast.makeText(MyActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
			}
			
//			Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//		    emailIntent.setData(Uri.parse("mailto:" + "dungdq1987@gmail.com")); 
//		    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My email's subject");
//		    emailIntent.putExtra(Intent.EXTRA_TEXT, "My email's body");
//
//		    try {
//		        startActivity(Intent.createChooser(emailIntent, "Send email using..."));
//		    } catch (android.content.ActivityNotFoundException ex) {
////		        Toast.makeText(Activity.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
//		    	Log.e("error", "No email clients installed.");
//		    }
			
			break;
		case 4:
			if (mSimpleFacebook.isLogin()) {
				Utils.inviteViaFacebook(MainActivity.this);
			} else
				Utils.loginViaFacebook(MainActivity.this);
			break;
		case 5:
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://details?id=" + Const.APP_PACKAGE_NAME)));
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			mDrawerLayout.closeDrawer(mDrawerList);
			Log.e("MainActivity", "Error in creating fragment");
		}
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    mSimpleFacebook = SimpleFacebook.getInstance(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    mSimpleFacebook.onActivityResult(requestCode, resultCode, data);
	    super.onActivityResult(requestCode, resultCode, data);
	}
	
	// Timer to display full screen banner
//	private Runnable updateTimerThread = new Runnable() {
//        public void run() {
//        	if (Const.adBanner == false) {
//        		Utils.displayInterstitial(MainActivity.this);
//                mHandler.postDelayed(this, 5 * 60 * 1000);
//        	} else
//        		mHandler.postDelayed(this, 2 * 60 * 1000);
//        }
//	};
}
