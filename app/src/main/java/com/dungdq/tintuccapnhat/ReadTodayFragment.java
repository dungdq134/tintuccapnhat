package com.dungdq.tintuccapnhat;

import com.astuetz.PagerSlidingTabStrip;

import android.support.v7.app.ActionBar;
import android.annotation.SuppressLint;
import android.app.ActionBar.Tab;
import android.app.Activity;
//import android.app.Fragment;
//import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
//import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressWarnings("deprecation")
public class ReadTodayFragment extends Fragment {

	public static PagerSlidingTabStrip tabs;
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;

	private int currentColor = 0xFF5161BC;
	public static final int[] colors = { 0xFF5161BC, 0xFF96AA39, 0xFFC74B46, 0xFFF4842D,
		0xFF3F9FE0, 0xFF666666, 0xFF006600, 0xFF9900FF, 0xFF663300, 0xFF96AA39, 0xFFC74B46, 0xFFF4842D,
		0xFF3F9FE0, 0xFF666666 };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_readtoday,
				container, false);

		tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
		viewPager = (ViewPager) rootView.findViewById(R.id.pager);

		mAdapter = new TabsPagerAdapter(((AppCompatActivity)getActivity()).getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);

		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		viewPager.setPageMargin(pageMargin);
		viewPager.setOffscreenPageLimit(2);

		tabs.setViewPager(viewPager);
		tabs.setTextColor(Color.WHITE);
		tabs.setIndicatorColor(Color.WHITE);

		changeColor(currentColor);

		// Adding Tabs
		// for (String tab_name : tabs) {
		// actionBar.addTab(actionBar.newTab().setText(tab_name)
		// .setTabListener(this));
		// }

		tabs.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					changeColor(colors[0]);
					break;
				case 1:
					changeColor(colors[1]);
					break;
				case 2:
					changeColor(colors[2]);
					break;
				case 3:
					changeColor(colors[3]);
					break;
				case 4:
					changeColor(colors[4]);
					break;
				case 5:
					changeColor(colors[5]);
					break;
				case 6:
					changeColor(colors[6]);
					break;
				case 7:
					changeColor(colors[7]);
					break;
				case 8:
					changeColor(colors[8]);
					break;
				case 9:
					changeColor(colors[9]);
					break;
				case 10:
					changeColor(colors[10]);
					break;
				case 11:
					changeColor(colors[11]);
					break;
				case 12:
					changeColor(colors[12]);
					break;
				case 13:
					changeColor(colors[13]);
					break;

				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		return rootView;
	}

//	@Override
//	public void onTabSelected(Tab tab, FragmentTransaction ft) {
//		viewPager.setCurrentItem(tab.getPosition());
//	}
//
//	@Override
//	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onTabReselected(Tab tab, FragmentTransaction ft) {
//		// TODO Auto-generated method stub
//
//	}

	@SuppressLint("NewApi")
	private void changeColor(int newColor) {

		// tabs.setIndicatorColor(newColor);
		tabs.setBackgroundColor(newColor);
//		ActionBar ac = ((ActionBarActivity)getActivity()).getSupportActionBar();
//		ac.setBackgroundDrawable(new ColorDrawable(newColor));
//		ac.setDisplayShowTitleEnabled(true);
		MainActivity.toolbar.setBackgroundColor(newColor);
		
		// Check android version for change status bar color
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			getActivity().getWindow().setStatusBarColor(newColor);
		
//		getActivity().getActionBar().setBackgroundDrawable(
//				new ColorDrawable(newColor));
//		getActivity().getActionBar().setDisplayShowTitleEnabled(false);
//		getActivity().getActionBar().setDisplayShowTitleEnabled(true);

		currentColor = newColor;
	}

	public class TabsPagerAdapter extends FragmentStatePagerAdapter {

		// Tab titles
		private String[] tabs = { "Tiêu điểm", "Giải trí", "Thể thao",
				"Sức khỏe", "Văn hóa", "Kinh tế", "Khoa học", "Công nghệ",
				"Thực phẩm", "Du lịch", "Ô tô", "Trò chơi", "Thời trang", "Cuộc sống" };

		public TabsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return tabs[position];
		}

		@Override
		public Fragment getItem(int index) {
			return HomeFragment.newInstance(index, colors[index]);
		}

		@Override
		public int getCount() {
			// get item count - equal to number of tabs
			return tabs.length;
		}

	}
}
