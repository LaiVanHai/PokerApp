package jp.co.netprotections.pokerapp.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.netprotections.pokerapp.model.PokerCheckHistory;

public class MyStorage {
    public static final String PREFS_NAME = "POKER_APP";
    public static final String CHECKED_POKER = "Checked_poker";

    public void storeToHistories(Context context, ArrayList<PokerCheckHistory> checkedList) {
        // used for store arrayList in json format
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(checkedList);
        editor.putString(CHECKED_POKER, jsonFavorites);
        editor.commit();
    }

    public ArrayList<PokerCheckHistory> loadHistories(Context context) {
        // used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        List checkedList;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (settings.contains(CHECKED_POKER)) {
            String jsonFavorites = settings.getString(CHECKED_POKER, null);
            Gson sExposeGson = new Gson();
            PokerCheckHistory[] favoriteItems = sExposeGson.fromJson(jsonFavorites, PokerCheckHistory[].class);
            checkedList = Arrays.asList(favoriteItems);
            checkedList = new ArrayList(checkedList);
        } else
            return null;
        return (ArrayList) checkedList;
    }

    public void addCheckedResult(Context context, PokerCheckHistory checkedPoker) {
        ArrayList<PokerCheckHistory> checkedList = loadHistories(context);
        if (checkedList == null)
            checkedList = new ArrayList<PokerCheckHistory>();
        checkedList.add(checkedPoker);
        storeToHistories(context, checkedList);
    }

    public void removeCheckedResult(Context context, PokerCheckHistory checkedPoker) {
        ArrayList<PokerCheckHistory> checkedList = loadHistories(context);
        if (checkedList != null) {
            checkedList.remove(checkedPoker);
            storeToHistories(context, checkedList);
        }
    }

    public boolean removeAllCheckedResult(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        return editor.remove(CHECKED_POKER).commit();
    }
}