package com.dungdq.tintuccapnhat.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.dungdq.tintuccapnhat.R;

public class NetworkStatusReceiver extends BroadcastReceiver {

	/**
	 * 
	 */
	private static ConnectivityManager connectivityManager;
	/**
	 * 
	 */
	private static boolean connected = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (!isConnected(context)) {
			System.out.println(context.getString(R.string.not_net_work));

			displayDialogMessageNoNetwork(context);
//			Utils.showAlert((Activity) context, context.getString(R.string.net_work));
		} else {
			System.out.println(context.getString(R.string.net_work));
		}
	}

	@SuppressLint("ShowToast")
	public static void displayDialogMessageNoNetwork(Context context) {
		Toast.makeText(context, context.getString(R.string.not_net_work),
				Toast.LENGTH_LONG);
	}

	public static boolean isConnected(Context context) {
		try {
			connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			connected = networkInfo != null && networkInfo.isAvailable()
					&& networkInfo.isConnected();
		} catch (Exception e) {
			connected = false;
		}

		return connected;
	}

}
