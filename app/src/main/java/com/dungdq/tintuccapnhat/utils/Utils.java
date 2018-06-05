package com.dungdq.tintuccapnhat.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.InterstitialAd;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.listeners.OnInviteListener;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnPublishListener;

import com.dungdq.tintuccapnhat.R;

public class Utils {

	public static ImageLoader createImageLoader(Context context) {
		ImageLoader imageLoader = ImageLoader.getInstance();

		// DisplayImageOptions defaultOptions = new
		// DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
		// .build();

		if (!imageLoader.isInited()) {

			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
					// .showImageOnLoading(R.drawable.gray_bg)
					// .showImageForEmptyUri(R.drawable.gray_bg)
					// .showImageOnFail(R.drawable.gray_bg)
					.delayBeforeLoading(50)
					.imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(true)
					.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
					.resetViewBeforeLoading(true).build();

			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					context).defaultDisplayImageOptions(defaultOptions).build();

			imageLoader.init(config);
		}

		return imageLoader;
	}

	public static void displayInterstitial(Context context) {
		/*final InterstitialAd interstitial = new InterstitialAd(context);
		interstitial.setAdUnitId(context.getString(R.string.banner_ad_fullscreen_unit_id));

		// Create ad request.
		AdRequest adRequest = new AdRequest.Builder()
		
//		.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//		.addTestDevice(Const.adTestDeviceID)
		
		.build();

		// Begin loading your interstitial.
		interstitial.loadAd(adRequest);

		interstitial.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				if (interstitial.isLoaded()) {
					interstitial.show();
					Const.adBanner = true;
				}
			}
			
			@Override
			public void onAdClosed() {
				super.onAdClosed();
				Log.e("close ad", "close ad");
				Const.adBanner = false;
			}
		});*/
	}

	public static String getKeyHash(Activity ac) {
		String re = null;
		try {
			PackageInfo info = ac.getPackageManager().getPackageInfo(
					ac.getApplicationContext().getPackageName(),
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				re = Base64.encodeToString(md.digest(), Base64.DEFAULT);
				Log.d("key hash:", re);
			}
		} catch (NameNotFoundException e) {
			Log.d("NameNotFoundException",
					"getKeyHash - NameNotFoundException " + e);

		} catch (NoSuchAlgorithmException e) {
			Log.d("NoAlgorithmException",
					"getKeyHash - NoSuchAlgorithmException " + e);
		}

		return re;
	}

	/**
	 * Publish link
	 */
	public static void publishFeed(Activity activity, String title,
			String summary, String thumbnail, String url) {

		SimpleFacebook mSimpleFacebook = SimpleFacebook.getInstance(activity);

		Feed feed = new Feed.Builder().setMessage("Hello ....").setName(title)
				.setDescription(summary).setPicture(thumbnail).setLink(url)
				.build();

		OnPublishListener onPublishListener = new OnPublishListener() {
			@Override
			public void onComplete(String postId) {
				Log.e("Facebook", "Published successfully. The new post id = "
						+ postId);
			}

			/*
			 * You can override other methods here: onThinking(), onFail(String
			 * reason), onException(Throwable throwable)
			 */
		};

		mSimpleFacebook.publish(feed, true, onPublishListener);
	}
	
	/**
	 * Login facebook
	 */
	public static void loginViaFacebook(final Activity activity) {
		
		SimpleFacebook mSimpleFacebook = SimpleFacebook.getInstance(activity);
		
		OnLoginListener onLoginListener = new OnLoginListener() {
			@Override
			public void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
				Log.e("Facebook", "Logged in");
				Utils.inviteViaFacebook(activity);
			}

			@Override
			public void onCancel() {

			}

//			@Override
//		    public void onLogin() {
//		        // change the state of the button or do whatever you want
//		        Log.e("Facebook", "Logged in");
//		        Utils.inviteViaFacebook(activity);
//		    }

//		    @Override
//		    public void onNotAcceptingPermissions(Permission.Type type) {
//		        // user didn't accept READ or WRITE permission
//		        Log.e("Facebook", String.format("You didn't accept %s permissions", type.name()));
//		    }

//			@Override
//			public void onThinking() {
//				// TODO Auto-generated method stub
//			}

			@Override
			public void onException(Throwable throwable) {
				Log.e("error exception login", throwable.getMessage());
			}

			@Override
			public void onFail(String reason) {
				Log.e("error login", reason);
			}
		};
		
		mSimpleFacebook.login(onLoginListener);
		
		Log.e("doing login facebook", "doing login facebook");
	}
	
	/**
	 * Invite friends facebook
	 */
	public static void inviteViaFacebook(Activity activity) {
		
		SimpleFacebook mSimpleFacebook = SimpleFacebook.getInstance(activity);
		
		OnInviteListener onInviteListener = new OnInviteListener() {
		    @Override
		    public void onComplete(List<String> invitedFriends, String requestId) {
		        Log.e("Facebook", "Invitation was sent to " + invitedFriends.size() + " users with request id " + requestId); 
		    }

		    @Override
		    public void onCancel() {
		        Log.e("Facebook", "Canceled the dialog");
		    }

			@Override
			public void onException(Throwable throwable) {
				Log.e("exception", throwable.getMessage());
			}

			@Override
			public void onFail(String reason) {
				Log.e("error invite", reason);
			}
		};
		
		mSimpleFacebook.invite("I invite you to use this app", onInviteListener, null);
	}

	/**
	 * Rate application dialog
	 */
	public static void showRateDialog(final Context mContext,
			final SharedPreferences.Editor mEditor) {
		Dialog dialog = new Dialog(mContext);

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		String message = mContext.getString(R.string.rate_app_message);
		builder.setMessage(message)
				.setTitle("Vote " + mContext.getString(R.string.app_name))
				.setIcon(mContext.getApplicationInfo().icon)
				.setCancelable(false)
				.setPositiveButton("Vote ngay",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mEditor.putBoolean("dontshowagain", true);
								mEditor.commit();
								mContext.startActivity(new Intent(
										Intent.ACTION_VIEW,
										Uri.parse("market://details?id="
												+ Const.APP_PACKAGE_NAME)));
								dialog.dismiss();
							}
						})
				.setNegativeButton("Để sau",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

							}
						});
//				.setNegativeButton("Không, cảm ơn ",
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								if (mEditor != null) {
//									mEditor.putBoolean("dontshowagain", true);
//									mEditor.commit();
//								}
//								dialog.dismiss();
//
//							}
//						});
		dialog = builder.create();

		dialog.show();
	}
}
