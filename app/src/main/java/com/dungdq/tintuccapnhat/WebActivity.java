package com.dungdq.tintuccapnhat;

import org.json.JSONException;
import org.json.JSONObject;

//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.InterstitialAd;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import com.dungdq.tintuccapnhat.database.FavoriteDB;
import com.dungdq.tintuccapnhat.model.FavoriteList;
import com.dungdq.tintuccapnhat.utils.Const;
import com.dungdq.tintuccapnhat.utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebActivity extends AppCompatActivity {

	private WebView webView;
	private ProgressBar progressBar;
	private Toolbar toolbar;
	private Menu mOptionMenu;
	private Intent i;

	private FavoriteDB favoriteDB;

//	private ShareActionProvider mShareActionProvider;

	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		i = getIntent();
		favoriteDB = new FavoriteDB(WebActivity.this);

//		// Check timer to display fullscreen banner
//		SharedPreferences pref = getSharedPreferences("timer_banner", 0);
//		SharedPreferences.Editor editor = pref.edit();
//		//Get Date of first launch
//        Long time_firstLaunch = pref.getLong("time_first_launch", 0);
//        if (time_firstLaunch == 0) {
//        	time_firstLaunch = System.currentTimeMillis();
//            editor.putLong("time_first_launch", time_firstLaunch);
//            editor.commit();
//        }
//        if (System.currentTimeMillis() >= time_firstLaunch + (5 * 60 * 1000)) {
//        	Utils.displayInterstitial(WebActivity.this);
//        	time_firstLaunch = System.currentTimeMillis();
//        	editor.putLong("time_first_launch", time_firstLaunch);
//        	editor.commit();
//        }

		toolbar = (Toolbar) findViewById(R.id.my_toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		}

		// Display back activity on toolbar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Set background toolbar
		Log.e("web color", i.getIntExtra("color", 0) + "");
		toolbar.setBackgroundColor(i.getIntExtra("color", 0));
		// Check android version for change status bar color
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			getWindow().setStatusBarColor(i.getIntExtra("color", 0));
		// Set title toolbar
		setTitle(i.getStringExtra(Const.TAG_TITLE));

		webView = (WebView) findViewById(R.id.webView);

		progressBar = (ProgressBar) findViewById(R.id.web_progress);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webView.loadUrl(url);
				
				return true;
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				progressBar.setVisibility(View.GONE);
			}
		});
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setDisplayZoomControls(false);
		webView.getSettings().setUseWideViewPort(true);
//		 webView.getSettings().setLoadWithOverviewMode(true);

		webView.loadUrl(i.getStringExtra(Const.TAG_URL));
//		parserURL(i.getStringExtra(Const.TAG_URL), webView);
		
		// Banner Ads
		/*AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
        
//        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//		.addTestDevice(Const.adTestDeviceID)
        
        .build();
        
        mAdView.loadAd(adRequest);*/
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar actions click
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.action_share:
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent
					.putExtra(Intent.EXTRA_TEXT, i.getStringExtra(Const.TAG_URL));
			startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua ..."));
//			Utils.publishFeed(WebActivity.this,
//					i.getStringExtra(Const.TAG_TITLE),
//					i.getStringExtra(Const.TAG_SUMMARY),
//					i.getStringExtra(Const.TAG_THUMBNAIL),
//					i.getStringExtra(Const.TAG_URL));
			return true;
		case R.id.action_bookmark:
			// mOptionMenu.findItem(R.id.action_bookmark).setIcon(
			// R.drawable.ic_action_important);

			if (favoriteDB.getFavoriteItem(i.getStringExtra(Const.TAG_ID)) == null) {
				mOptionMenu.findItem(R.id.action_bookmark).setIcon(
						R.drawable.ic_action_favorite);

				Log.d("Insert: ", "Inserting ..");
				favoriteDB.addFavorite(new FavoriteList(i
						.getStringExtra(Const.TAG_ID), i
						.getStringExtra(Const.TAG_TITLE), i
						.getStringExtra(Const.TAG_SUMMARY), i
						.getStringExtra(Const.TAG_THUMBNAIL), i
						.getStringExtra(Const.TAG_AUTHOR), i
						.getStringExtra(Const.TAG_URL)));

				Toast.makeText(WebActivity.this, getString(R.string.bookmark),
						Toast.LENGTH_SHORT).show();
			} else {
				mOptionMenu.findItem(R.id.action_bookmark).setIcon(
						R.drawable.ic_action_not_favorite);

				Log.d("Delete: ", "Deleting ..");
				favoriteDB.deleteFavorite(i.getStringExtra(Const.TAG_ID));

				Toast.makeText(WebActivity.this,
						getString(R.string.unbookmark), Toast.LENGTH_SHORT)
						.show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.submain, menu);
		mOptionMenu = menu;

		// Check bookmark new item
		if (favoriteDB.getFavoriteItem(i.getStringExtra(Const.TAG_ID)) != null) {
			// Favorite
			mOptionMenu.findItem(R.id.action_bookmark).setIcon(
					R.drawable.ic_action_favorite);
		} else
			// Not favorite;
			mOptionMenu.findItem(R.id.action_bookmark).setIcon(
					R.drawable.ic_action_not_favorite);

		// Locate MenuItem with ShareActionProvider
//		MenuItem item = menu.findItem(R.id.action_share);

		// Fetch and store ShareActionProvider
//		mShareActionProvider = new ShareActionProvider(this);
//		MenuItemCompat.setActionProvider(item, mShareActionProvider);
		// mShareActionProvider = (ShareActionProvider)
		// MenuItemCompat.getActionProvider(item);
//		mShareActionProvider.setShareIntent(createShareIntent());

		return true;
	}

	// Call to update the share intent
	// private void setShareIntent(Intent shareIntent) {
	// if (mShareActionProvider != null) {
	// mShareActionProvider.setShareIntent(shareIntent);
	// }
	// }

//	private Intent createShareIntent() {
//		Intent shareIntent = new Intent(Intent.ACTION_SEND);
//		shareIntent.setType("text/plain");
//		shareIntent
//				.putExtra(Intent.EXTRA_TEXT, i.getStringExtra(Const.TAG_URL));
//		return shareIntent;
//	}
	
	private void parserURL(String url, final WebView webView) {
		AsyncHttpClient myAsync = new AsyncHttpClient();
		myAsync.get(Const.urlParser + url, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onSuccess(String response) {
				if (response != null) {
					try {
						JSONObject jsonObjNews = new JSONObject(response);
						String content = jsonObjNews.getString("content");
						
						webView.loadData(content, "text/html", null);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
			}
			
		});
	}

}
