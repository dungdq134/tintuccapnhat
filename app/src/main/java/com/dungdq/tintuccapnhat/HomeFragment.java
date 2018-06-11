package com.dungdq.tintuccapnhat;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

import com.dungdq.tintuccapnhat.adapter.MyAdapter;
import com.dungdq.tintuccapnhat.receiver.NetworkStatusReceiver;
import com.dungdq.tintuccapnhat.utils.Const;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HomeFragment extends Fragment implements OnRefreshListener {
	private RecyclerView mRecyclerView;
	private MyAdapter mAdapter;
	private LinearLayoutManager mLayoutManager;
	private SwipeRefreshLayout swipeContainer;
	
	private ArrayList<HashMap<String, String>> listTinTuc = new ArrayList<HashMap<String, String>>();
	
	private boolean loading = true;
	int pastVisiblesItems, visibleItemCount, totalItemCount;
	
	int i = 0;
	
	private ProgressBar progressBar, largeProgressBar;
	private TextView tvError;
	
	private static final String ARG_POSITION = "position";
	private static final String ARG_COLOR = "color";
	private int position, color;
	private String category;
	
	private String urlAPI = "";
	
	public static HomeFragment newInstance(int position, int color) {
		HomeFragment f = new HomeFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		b.putInt(ARG_COLOR, color);
		f.setArguments(b);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		position = getArguments().getInt(ARG_POSITION);
		color = getArguments().getInt(ARG_COLOR);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);
		
		largeProgressBar = (ProgressBar) rootView.findViewById(R.id.marker_progress);
		largeProgressBar.setVisibility(View.VISIBLE);
		tvError = (TextView) rootView.findViewById(R.id.tvError);
//		TinTucCapNhatApplication.progressBar.setVisibility(View.VISIBLE);
//		TinTucCapNhatApplication.tvError.setVisibility(View.GONE);
		
		// Check internet connection
		if (!NetworkStatusReceiver.isConnected(getActivity())) {
			tvError.setVisibility(View.VISIBLE);
			tvError.setText(getResources().getString(R.string.alert_error));
		}
		
		progressBar = (ProgressBar) rootView.findViewById(R.id.data_progress);

		mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
		
		// Swipe to refresh
		swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
		swipeContainer.setOnRefreshListener(this);
		swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, 
	            android.R.color.holo_green_light, 
	            android.R.color.holo_orange_light, 
	            android.R.color.holo_red_light);
		
		// Banner Ads
		/*AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
        
//        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//		.addTestDevice(Const.adTestDeviceID)
        
        .build();
        
        mAdView.loadAd(adRequest);*/
		
		switch (position) {
		case 0:
			category = "&hint=ar%2Cne%2Csp%2Cte%2Cbu%2Cen%2Cli%2Csc%2Clv%2Che%2Cmo%2Cfo%2Ctr";
			break;
		case 1:
			category = "&category=en";
			break;
		case 2:
			category = "&category=sp";
			break;
		case 3:
			category = "&category=he";
			break;
		case 4:
			category = "&category=ar";
			break;
		case 5:
			category = "&category=bu";
			break;
		case 6:
			category = "&category=sc";
			break;
		case 7:
			category = "&category=te";
			break;
		case 8:
			category = "&category=fo";
			break;
		case 9:
			category = "&category=tr";
			break;
		case 10:
			category = "&category=mo";
			break;
		case 11:
			category = "&category=ga";
			break;
		case 12:
			category = "&category=li";
			break;
		case 13:
			category = "&category=lv";
			break;

		default:
			break;
		}

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(true);

		// use a linear layout manager
		mLayoutManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		
		mRecyclerView.setOnScrollListener(new OnScrollListener() {
			int mLastFirstVisibleItem = 0;
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				
				visibleItemCount = mLayoutManager.getChildCount();
	            totalItemCount = mLayoutManager.getItemCount();
	            pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
	            
	            int topRowVerticalPosition = 
	            	      (mRecyclerView == null || mRecyclerView.getChildCount() == 0) ? 
	            	        0 : mRecyclerView.getChildAt(0).getTop();
	            	    swipeContainer.setEnabled(topRowVerticalPosition >= 0);
	            
//	            if (pastVisiblesItems > mLastFirstVisibleItem) {
////	                MainActivity.toolbar.setVisibility(View.GONE);
//	            	MainActivity.toolbar.animate().translationY(-MainActivity.toolbar.getBottom()).setInterpolator(new DecelerateInterpolator());
//	            	ReadTodayFragment.tabs.animate().translationY(-ReadTodayFragment.tabs.getBottom()).setInterpolator(new DecelerateInterpolator());
//	            } else if (pastVisiblesItems < mLastFirstVisibleItem) {
////	                MainActivity.toolbar.setVisibility(View.VISIBLE);
//	            	MainActivity.toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator());
//	            	ReadTodayFragment.tabs.animate().translationY(0).setInterpolator(new DecelerateInterpolator());
//	            }
//
//	            mLastFirstVisibleItem = pastVisiblesItems;

	            if (loading) {
	                if ( (visibleItemCount+pastVisiblesItems) >= totalItemCount) {
	                    loading = false;
	                    Log.e("...", "Last Item Wow !");
	                    i++;
	                    progressBar.setVisibility(View.VISIBLE);
	                    apiGetTinTuc(Const.urlAPI + category + "&page=" + i);
	                    Log.e("link", Const.urlAPI + category + "&page=" + i);
	                }
	            }
			}
		});

//		apiGetTinTuc(Const.urlAPI + category);
//		Log.e("link", Const.urlAPI + category);
		
//		getUrlAPI(Const.urlAPI);
		apiGetTinTuc(Const.urlAPI + category);

		return rootView;
	}
	
	private void getUrlAPI(String url) {
		AsyncHttpClient myAsync = new AsyncHttpClient();
		myAsync.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onSuccess(String response) {
				try {
					JSONObject jsonObj = new JSONObject(response);
					urlAPI = jsonObj.getString("url");
					apiGetTinTuc(urlAPI + category);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
			}
		});
	}
	
	private void apiGetTinTuc(String url) {
		AsyncHttpClient myAsync = new AsyncHttpClient();
		myAsync.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(String response) {
//				Log.e("response: ", response);
				if (response == null) {
					tvError.setVisibility(View.VISIBLE);
				} else {
					try {
						JSONObject jsonObj = new JSONObject(response);
						JSONArray jsonArray = jsonObj.getJSONArray("articles");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObjChild = jsonArray.getJSONObject(i);
							JSONObject feed = jsonObjChild.getJSONObject("feed");
							String author = feed.getString("title");
							String id = jsonObjChild.getString("id");
							String title = jsonObjChild.getString("title");
							String summary = jsonObjChild.getString("summary");
							JSONObject image = jsonObjChild.getJSONObject("image");
							String thumbnail = image.getString("url");
							String url = jsonObjChild.getString("url");

							HashMap<String, String> m = new HashMap<String, String>();
							m.put(Const.TAG_ID, id);
							m.put(Const.TAG_TITLE, title);
							m.put(Const.TAG_SUMMARY, summary);
							m.put(Const.TAG_THUMBNAIL, thumbnail);
							m.put(Const.TAG_AUTHOR, author);
							m.put(Const.TAG_URL, url);

							listTinTuc.add(m);
						}
						
						int currentPosition = mLayoutManager.findFirstVisibleItemPosition();

						mAdapter = new MyAdapter(getActivity(), listTinTuc, color);
						mRecyclerView.setAdapter(mAdapter);
						
						if (!loading)
							mLayoutManager.scrollToPositionWithOffset(currentPosition + 1, 0);
						
						loading = true;

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				progressBar.setVisibility(View.GONE);
//				TinTucCapNhatApplication.progressBar.setVisibility(View.GONE);
				largeProgressBar.setVisibility(View.GONE);
				swipeContainer.setRefreshing(false);
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				if (listTinTuc.size() == 0)
					tvError.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public void onRefresh() {
		apiGetTinTuc(Const.urlAPI + category);
		tvError.setVisibility(View.GONE);
	}
}
