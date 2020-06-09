package jp.co.netprotections.pokerapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import jp.co.netprotections.pokerapp.R;
import jp.co.netprotections.pokerapp.common.MyStorage;
import jp.co.netprotections.pokerapp.fragments.HistoryFragment;
import jp.co.netprotections.pokerapp.fragments.HomeFragment;
import jp.co.netprotections.pokerapp.model.Poker;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeFragmentListener{
    private HomeFragment fragmentHome = new HomeFragment();
    BottomNavigationView bottomNavigation;

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

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.poker:
                        getSupportActionBar().setTitle(R.string.poker_title);
                        getSupportFragmentManager().popBackStack();
                        return true;
                    case R.id.history:
                        getSupportActionBar().setTitle(R.string.history_title);
                        ArrayList<Poker> listCheckedPoker = MyStorage.loadHistories(getBaseContext());
                        openFragment(HistoryFragment.newInstance(listCheckedPoker));
                        return true;
                }
                return false;
            }
        };

    @Override
    public void activityChange(ArrayList<Poker> content) {
        Intent intent = new Intent(this, ResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ResultActivity.KEY_POKER_RESULTS, content);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void checkNetworkConnection(){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean isNetworkConnectionAvailable(){
        ConnectivityManager cm =
            (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
        if(isConnected) {
            Log.d("Network", "Connected");
            return true;
        }
        else{
            checkNetworkConnection();
            Log.d("Network","Not Connected");
            return false;
        }
    }
}
