package com.dungdq.tintuccapnhat.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import com.dungdq.tintuccapnhat.database.FavoriteDB;
import com.dungdq.tintuccapnhat.model.FavoriteList;
import com.dungdq.tintuccapnhat.utils.Const;
import com.dungdq.tintuccapnhat.utils.Utils;

import com.dungdq.tintuccapnhat.R;
import com.dungdq.tintuccapnhat.WebActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

	private ArrayList<HashMap<String, String>> listTinTuc = new ArrayList<HashMap<String, String>>();
	ImageLoader imageLoader;
	Activity activity;
	private FavoriteDB favoriteDB;
	protected static final String TAG = "face";
	private int color;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		public LinearLayout llNews;
		public TextView tvTitle, tvSummary, tvAuthor;
		public ImageView ivThumbnail;
		public ProgressBar imageProgress;

		public ViewHolder(View itemView) {
			super(itemView);
			llNews = (LinearLayout) itemView.findViewById(R.id.llNews);
			tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
			tvSummary = (TextView) itemView.findViewById(R.id.tvSummary);
			tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
			ivThumbnail = (ImageView) itemView.findViewById(R.id.ivThumbnail);
			imageProgress = (ProgressBar) itemView.findViewById(R.id.image_progress);
//			ivFavorite = (ImageView) itemView.findViewById(R.id.ivFavorite);
//			ivFacebook = (ImageView) itemView.findViewById(R.id.ivFacebook);
		}
	}

	// Provide a suitable constructor (depends on the kind of dataset)
	public MyAdapter(Activity context,
			ArrayList<HashMap<String, String>> listTinTuc, int color) {
		this.listTinTuc = listTinTuc;
		this.activity = context;
		imageLoader = Utils.createImageLoader(context);
		favoriteDB = new FavoriteDB(context);
		this.color = color;
	}

	@Override
	public int getItemCount() {
		return listTinTuc.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
		viewHolder.tvTitle.setText(listTinTuc.get(position)
				.get(Const.TAG_TITLE));
		viewHolder.tvSummary.setText(listTinTuc.get(position).get(
				Const.TAG_SUMMARY));
		viewHolder.tvAuthor.setText(listTinTuc.get(position).get(
				Const.TAG_AUTHOR));
		if (listTinTuc.get(position).get(Const.TAG_THUMBNAIL).equals(""))
			viewHolder.ivThumbnail.setVisibility(View.GONE);
		else
			imageLoader.displayImage(
					listTinTuc.get(position).get(Const.TAG_THUMBNAIL),
					viewHolder.ivThumbnail, new ImageLoadingListener() {
						
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							viewHolder.imageProgress.setVisibility(View.VISIBLE);
						}
						
						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							viewHolder.imageProgress.setVisibility(View.GONE);
						}
						
						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
							viewHolder.imageProgress.setVisibility(View.GONE);
						}
						
						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub
							
						}
					});

//		if (favoriteDB.getFavoriteItem(listTinTuc.get(position).get(
//				Const.TAG_ID)) != null) {
//			// Favorite
//			viewHolder.ivFavorite.setImageResource(R.drawable.ic_bookmark);
//		} else
//			// Not favorite;
//			viewHolder.ivFavorite.setImageResource(R.drawable.ic_not_bookmark);

		viewHolder.llNews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("web", "web");
				Intent i = new Intent(activity, WebActivity.class);
				i.putExtra(Const.TAG_ID,
						listTinTuc.get(position).get(Const.TAG_ID));
				i.putExtra(Const.TAG_TITLE,
						listTinTuc.get(position).get(Const.TAG_TITLE));
				i.putExtra(Const.TAG_SUMMARY,
						listTinTuc.get(position).get(Const.TAG_SUMMARY));
				i.putExtra(Const.TAG_THUMBNAIL,
						listTinTuc.get(position).get(Const.TAG_THUMBNAIL));
				i.putExtra(Const.TAG_AUTHOR,
						listTinTuc.get(position).get(Const.TAG_AUTHOR));
				i.putExtra(Const.TAG_URL,
						listTinTuc.get(position).get(Const.TAG_URL));
				Log.e("put color", color + "");
				i.putExtra("color", color);
				// context.startActivity(i);

				ActivityOptionsCompat options = ActivityOptionsCompat
						.makeSceneTransitionAnimation(activity, v, "url");
				ActivityCompat.startActivity(activity, i, options.toBundle());
			}
		});

		// Bookmark
//		viewHolder.ivFavorite.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (favoriteDB.getFavoriteItem(listTinTuc.get(position).get(
//						Const.TAG_ID)) == null) {
//					viewHolder.ivFavorite
//							.setImageResource(R.drawable.ic_bookmark);
//
//					Log.d("Insert: ", "Inserting ..");
//					favoriteDB.addFavorite(new FavoriteList(listTinTuc.get(
//							position).get(Const.TAG_ID), listTinTuc.get(
//							position).get(Const.TAG_TITLE), listTinTuc.get(
//							position).get(Const.TAG_SUMMARY), listTinTuc.get(
//							position).get(Const.TAG_THUMBNAIL), listTinTuc.get(
//							position).get(Const.TAG_AUTHOR), listTinTuc.get(
//							position).get(Const.TAG_URL)));
//
//					Toast.makeText(activity, "Đã thêm vào Danh sách yêu thích",
//							Toast.LENGTH_SHORT).show();
//				} else {
//					viewHolder.ivFavorite
//							.setImageResource(R.drawable.ic_not_bookmark);
//
//					Log.d("Delete: ", "Deleting ..");
//					favoriteDB.deleteFavorite(listTinTuc.get(position).get(
//							Const.TAG_ID));
//
//					Toast.makeText(activity, "Đã xóa khỏi Danh sách yêu thích",
//							Toast.LENGTH_SHORT).show();
//				}
//			}
//		});

//		viewHolder.ivFacebook.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Utils.publishFeed(activity, listTinTuc.get(position)
//						.get(Const.TAG_TITLE),
//						listTinTuc.get(position).get(Const.TAG_SUMMARY),
//						listTinTuc.get(position).get(Const.TAG_THUMBNAIL),
//						listTinTuc.get(position).get(Const.TAG_URL));
//			}
//		});
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View v = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.recycleview_item, parent, false);

		ViewHolder vh = new ViewHolder(v);
		return vh;
	}
}
