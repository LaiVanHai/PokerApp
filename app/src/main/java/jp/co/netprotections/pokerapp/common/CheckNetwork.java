package jp.co.netprotections.pokerapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import jp.co.netprotections.pokerapp.R;

public class CheckNetwork {
    public void checkNetworkConnection(Activity curActivity){
        AlertDialog.Builder builder = new AlertDialog.Builder(curActivity);
        builder.setTitle(R.string.no_internet_title);
        builder.setMessage(R.string.no_internet_content);
        builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean isNetworkConnectionAvailable(Activity curActivity){
        ConnectivityManager cm =
                (ConnectivityManager) curActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
        if(isConnected) {
            Log.d("Network", "Connected");
            return true;
        }
        else{
            checkNetworkConnection(curActivity);
            Log.d("Network","Not Connected");
            return false;
        }
    }
}
