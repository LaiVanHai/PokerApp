package jp.co.netprotections.pokerapp.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.netprotections.pokerapp.model.Poker;


public class MyStorage {
    public static final String PREFS_NAME = "POKER_APP";
    public static final String CHECKED_POKER = "Checked_poker";

    public static void storeToHistories(Context context, ArrayList<Poker> checkedList) {
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

    public static ArrayList<Poker> loadHistories(Context context) {
        // used for retrieving arraylist from json formatted string
        SharedPreferences settings;
        List checkedList;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (settings.contains(CHECKED_POKER)) {
            String jsonFavorites = settings.getString(CHECKED_POKER, null);
            Gson sExposeGson = new Gson();
            Poker[] favoriteItems = sExposeGson.fromJson(jsonFavorites, Poker[].class);
            checkedList = Arrays.asList(favoriteItems);
            checkedList = new ArrayList(checkedList);
        } else
            return null;
        return (ArrayList) checkedList;
    }

    public static void addCheckedResult(Context context, Poker checkedPoker) {
        ArrayList<Poker> checkedList = loadHistories(context);
        if (checkedList == null)
            checkedList = new ArrayList<Poker>();
        checkedList.add(checkedPoker);
        storeToHistories(context, checkedList);
    }

    public static void removeCheckedResult(Context context, Poker checkedPoker) {
        ArrayList<Poker> checkedList = loadHistories(context);
        if (checkedList != null) {
            checkedList.remove(checkedPoker);
            storeToHistories(context, checkedList);
        }
    }

    public static boolean removeAllCheckedResult(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        return editor.remove(CHECKED_POKER).commit();
    }
}