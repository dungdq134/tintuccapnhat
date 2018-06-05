package com.dungdq.tintuccapnhat;

//import android.app.Fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.InterstitialAd;

import com.dungdq.tintuccapnhat.adapter.MyAdapter;
import com.dungdq.tintuccapnhat.database.FavoriteDB;
import com.dungdq.tintuccapnhat.model.FavoriteList;
import com.dungdq.tintuccapnhat.utils.Const;
import com.dungdq.tintuccapnhat.utils.Utils;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FavoriteFragment extends Fragment {

	FavoriteDB favoriteDB;

	// private ProgressBar progressBar;
	private ArrayList<HashMap<String, String>> listTinTucFavorite = new ArrayList<HashMap<String, String>>();
	private MyAdapter mAdapter;

	private RecyclerView mRecyclerView;
	private LinearLayoutManager mLayoutManager;
	
	private ProgressBar progressBar;
	private TextView tvFavoriteError;
	
//	private AdView mAdView;
	private int color;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_favorite, container,
				false);
		
		progressBar = (ProgressBar) rootView.findViewById(R.id.favorite_progress);
		tvFavoriteError = (TextView) rootView.findViewById(R.id.tvFavoriteError);

		// Utils.displayInterstitial(getActivity());
		
		color = 0xFF5161BC;
		MainActivity.toolbar.setBackgroundColor(color);
		// Check android version for change status bar color
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			getActivity().getWindow().setStatusBarColor(0xFF5161BC);

		// progressBar = (ProgressBar)
		// rootView.findViewById(R.id.data_progress_favorite);
		mRecyclerView = (RecyclerView) rootView
				.findViewById(R.id.my_recycler_view_favorite);
		mRecyclerView.setHasFixedSize(true);

		mLayoutManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		favoriteDB = new FavoriteDB(getActivity());

		new getFavoriteList().execute();
		
//		mAdView = (AdView) rootView.findViewById(R.id.adView);

		return rootView;
	}

	private class getFavoriteList extends AsyncTask<Void, Void, Void> {
		
		List<FavoriteList> favoriteList = favoriteDB.getAllFavorites();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			if (favoriteList.size() != 0) {
				for (FavoriteList fv : favoriteList) {
					HashMap<String, String> m = new HashMap<String, String>();
					m.put(Const.TAG_ID, fv.getID());
					m.put(Const.TAG_TITLE, fv.getTitle());
					m.put(Const.TAG_SUMMARY, fv.getSummary());
					m.put(Const.TAG_THUMBNAIL, fv.getThumbnail());
					m.put(Const.TAG_AUTHOR, fv.getAuthor());
					m.put(Const.TAG_URL, fv.getUrl());

					listTinTucFavorite.add(m);
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressBar.setVisibility(View.GONE);
			
			if (favoriteList.size() == 0) {
				tvFavoriteError.setVisibility(View.VISIBLE);
				tvFavoriteError.setText(getString(R.string.alert_bookmark));
			}
			
			mAdapter = new MyAdapter(getActivity(), listTinTucFavorite, color);
			mRecyclerView.setAdapter(mAdapter);
			
			// Banner Ads
	        /*AdRequest adRequest = new AdRequest.Builder()
	        
//	        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//			.addTestDevice(Const.adTestDeviceID)
	        
	        .build();
	        
	        mAdView.loadAd(adRequest);*/
		}
	}
}
