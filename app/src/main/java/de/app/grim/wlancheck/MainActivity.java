package de.app.grim.wlancheck;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    private static final String YOUTUBE_PACKAGE_NAME = "com.google.android.youtube";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView messageView = (TextView) findViewById(R.id.messageView);
        Button endButton = (Button) findViewById(R.id.endButton);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Process.killProcess(Process.myPid());
                System.exit(0);
            }
        });

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected()) {
            Intent searchYT = new Intent(Intent.ACTION_MAIN);
            searchYT.addCategory(Intent.CATEGORY_LAUNCHER);

            PackageManager packMgr = getPackageManager();
            List<ResolveInfo> packageInfo = packMgr.queryIntentActivities(searchYT, 0);

            for (ResolveInfo info : packageInfo) {
                if (info.activityInfo.packageName.equals(YOUTUBE_PACKAGE_NAME)) {
                    Intent startYT = new Intent(Intent.ACTION_MAIN);
                    startYT.addCategory(Intent.CATEGORY_LAUNCHER);
                    startYT.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
                    startYT.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startYT);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                    return;
                }
            }
            messageView.setText(R.string.no_YouTube);
        } else {
            messageView.setText(R.string.no_wlan);
        }
    }
}
