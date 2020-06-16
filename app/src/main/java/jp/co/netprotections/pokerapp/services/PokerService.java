package jp.co.netprotections.pokerapp.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.netprotections.pokerapp.common.PokerSingleton;
import jp.co.netprotections.pokerapp.model.PokerRequest;

public class PokerService {
    public static int getCardNo(String msg) {
        final String regex = "[1-9]+";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(0));
        } else {
            return 0;
        }
    }

    public static ArrayList<Integer> getDuplicationCardNo(String card) {
        ArrayList<Integer> duplicationList = new ArrayList<Integer>();
        String[] pokerList = card.split(" ");
        int pokerListSize = pokerList.length;
        for(int i=0; i<pokerListSize-1; i++) {
            for( int j=i+1; j<pokerListSize; j++) {
                if(pokerList[i].equals(pokerList[j])) {
                    duplicationList.add(i+1);
                    duplicationList.add(j+1);
                }
            }
        }
        return duplicationList;
    }

    public static void getResponseCheck(PokerRequest listPokerRequest, Context context, final CheckPokerCallback callback) {
        final String URL = "http://p0kerhands.herokuapp.com/api/v1/cards/check";
        String json = new Gson().toJson(listPokerRequest);
        JSONObject postParams = null;
        try {
            postParams = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, URL, postParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Success Callback
                        Log.e("Home Activity", "JsonObjectRequest onResponse: " + response.toString());
                        callback.onSuccessResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onSuccessResponse(null);
                    }
                });

        PokerSingleton.getInstance(context).getRequestQueue().add(jsonObjReq);
    }

    public static void getImage(Context context, final GetImageCallback callback) {
        final String URL = "https://source.unsplash.com/random/80x80";
        ImageRequest request = new ImageRequest(URL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        callback.onSuccessResponse(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onSuccessResponse(null);
                    }
                });
        PokerSingleton.getInstance(context).getRequestQueue().add(request);
    }

    public interface CheckPokerCallback {
        void onSuccessResponse(JSONObject response);
    }

    public interface GetImageCallback {
        void onSuccessResponse(Bitmap bitmap);
    }
}
