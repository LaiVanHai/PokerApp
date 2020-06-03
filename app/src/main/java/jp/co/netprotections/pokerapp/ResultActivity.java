package jp.co.netprotections.pokerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    public static final String KEY_POKER_RESULTS = "poker_results";
    private ArrayList<Poker> pokerResultList;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().setTitle(R.string.result_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            pokerResultList = bundle.getParcelableArrayList(KEY_POKER_RESULTS);
        }

        openFragment(ResultFragment.newInstance(pokerResultList));
//        LinearLayout resultContainer = (LinearLayout) findViewById(R.id.result_container);
//        LayoutInflater layoutInflater =
//                (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        for(int i = 0; i < pokerResultList.size(); i++) {
//            Poker childResult = pokerResultList.get(i);
//            View subView = layoutInflater.inflate(R.layout.shared_result_poker, null);
//            TextView title = (TextView) subView.findViewById(R.id.result_title);
//            TextView subtitle = (TextView) subView.findViewById(R.id.result_subtitle);
//            title.setText(childResult.getInputPoker());
//            subtitle.setText(childResult.getPokerPosition());
//            resultContainer.addView(subView);
//        }
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
                        openFragment(HistoryFragment.newInstance("", ""));
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


}
