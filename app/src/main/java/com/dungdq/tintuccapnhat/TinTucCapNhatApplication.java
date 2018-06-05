package com.dungdq.tintuccapnhat;

//import com.facebook.SessionDefaultAudience;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

import android.app.Application;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TinTucCapNhatApplication extends Application {
	public static ProgressBar progressBar;
	public static TextView tvError;
//	public static int currentColor = 0xFF666666;
	
	@Override
	public void onCreate() {
		super.onCreate();

		// initialize facebook configuration
		Permission[] permissions = new Permission[] {
				Permission.PUBLIC_PROFILE, Permission.PUBLISH_ACTION,
				Permission.USER_BIRTHDAY, Permission.EMAIL,
				Permission.USER_FRIENDS };

		SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
				.setAppId(getString(R.string.facebook_id))
				.setPermissions(permissions)
//				.setDefaultAudience(SessionDefaultAudience.FRIENDS)
				.setAskForAllPermissionsAtOnce(false).build();

		SimpleFacebook.setConfiguration(configuration);
	}
}
