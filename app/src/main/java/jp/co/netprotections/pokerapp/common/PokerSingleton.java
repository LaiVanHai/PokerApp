package jp.co.netprotections.pokerapp.common;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class PokerSingleton {
    private static final String TAG = "VolleySingleton";
    private static PokerSingleton sInstance;
    private RequestQueue mRequestQueue;

    private PokerSingleton(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public static synchronized PokerSingleton getInstance(Context context) {
        if (sInstance == null)
            sInstance = new PokerSingleton(context);
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}
