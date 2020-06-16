package jp.co.netprotections.pokerapp.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import jp.co.netprotections.pokerapp.R;
import jp.co.netprotections.pokerapp.common.MyStorage;
import jp.co.netprotections.pokerapp.fragments.HistoryFragment;
import jp.co.netprotections.pokerapp.fragments.HomeFragment;
import jp.co.netprotections.pokerapp.fragments.ResultFragment;
import jp.co.netprotections.pokerapp.model.PokerResponse;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeFragmentListener{
    private static final int SCREEN_ORIENTATION_PORTRAIT = 1;
    private HomeFragment fragmentHome = new HomeFragment();
    private HistoryFragment historyFragment;
    private ResultFragment resultFragment;
    BottomNavigationView bottomNavigation;
    final String TAG_HISTORY_FRAGMENT = "HISTORY";
    final String TAG_RESULT_FRAGMENT = "RESULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportActionBar().setTitle(R.string.poker_title);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, fragmentHome).commit();
    }

    public void openFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content_frame, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.poker:
                        historyFragment = (HistoryFragment)getSupportFragmentManager().findFragmentByTag(TAG_HISTORY_FRAGMENT);
                        if (historyFragment != null && historyFragment.isVisible()) {
                            getSupportFragmentManager().popBackStack();
                        }
                        resultFragment = (ResultFragment) getSupportFragmentManager().findFragmentByTag(TAG_RESULT_FRAGMENT);
                        if (resultFragment != null && resultFragment.isVisible()) {
                            setToolbar(getResources().getString(R.string.result_title), true);
                        } else {
                            getSupportActionBar().setTitle(R.string.poker_title);
                        }
                        return true;
                    case R.id.history:
                        setToolbar(getResources().getString(R.string.history_title), false);
                        historyFragment = HistoryFragment.newInstance(MyStorage.loadHistories(getBaseContext()));
                        openFragment(historyFragment, TAG_HISTORY_FRAGMENT);
                        return true;
                }
                return false;
            }
        };

    @Override
    public void fragmentChange(PokerResponse content) {
        setToolbar(getResources().getString(R.string.result_title), true);
        openFragment(ResultFragment.newInstance(content), TAG_RESULT_FRAGMENT);
    }

    private void setToolbar(String title, boolean backIsEnable) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(backIsEnable);
        getSupportActionBar().setHomeButtonEnabled(backIsEnable);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();  // optional depending on your needs
        MenuItem selectedMenu = bottomNavigation.getMenu().findItem(bottomNavigation.getSelectedItemId());
        if (R.id.history == selectedMenu.getItemId()) {
            bottomNavigation.setSelectedItemId(R.id.poker);
            getSupportActionBar().setTitle(R.string.poker_title);
        }
        resultFragment = (ResultFragment) getSupportFragmentManager().findFragmentByTag(TAG_RESULT_FRAGMENT);
        if (resultFragment != null && resultFragment.isVisible()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
