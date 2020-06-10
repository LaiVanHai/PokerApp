package jp.co.netprotections.pokerapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import jp.co.netprotections.pokerapp.R;
import jp.co.netprotections.pokerapp.common.MyStorage;
import jp.co.netprotections.pokerapp.fragments.HistoryFragment;
import jp.co.netprotections.pokerapp.fragments.ResultFragment;
import jp.co.netprotections.pokerapp.model.Poker;

public class ResultActivity extends AppCompatActivity {
    public static final String KEY_POKER_RESULTS = "poker_results";
    private ArrayList<Poker> pokerResultList;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().setTitle(R.string.result_title);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            pokerResultList = bundle.getParcelableArrayList(KEY_POKER_RESULTS);
        }
        openFragment(ResultFragment.newInstance(pokerResultList));
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.poker:
                        getSupportActionBar().setTitle(R.string.result_title);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setHomeButtonEnabled(true);
                        openFragment(ResultFragment.newInstance(pokerResultList));
                        return true;
                    case R.id.history:
                        getSupportActionBar().setTitle(R.string.history_title);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        getSupportActionBar().setHomeButtonEnabled(false);
                        ArrayList<Poker> listCheckedPoker = MyStorage.loadHistories(getBaseContext());
                        openFragment(HistoryFragment.newInstance(listCheckedPoker));
                        return true;
                }
                return false;
                }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        MenuItem selectedMenu = bottomNavigation.getMenu().findItem(bottomNavigation.getSelectedItemId());
        if (R.id.poker == selectedMenu.getItemId()) {
            finish();
        } else {
            bottomNavigation.setSelectedItemId(R.id.poker);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            openFragment(ResultFragment.newInstance(pokerResultList));
        }
        super.onBackPressed();  // optional depending on your needs
    }

}
