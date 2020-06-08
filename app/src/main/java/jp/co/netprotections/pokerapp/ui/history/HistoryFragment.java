package jp.co.netprotections.pokerapp.ui.history;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import jp.co.netprotections.pokerapp.MyStorage;
import jp.co.netprotections.pokerapp.Poker;
import jp.co.netprotections.pokerapp.R;

public class HistoryFragment extends Fragment {

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        final LinearLayout historyContainer = (LinearLayout) view.findViewById(R.id.history_container);
        LayoutInflater layoutInflater =
            (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ArrayList<Poker> listCheckedPoker = MyStorage.loadHistories(getContext());
        for(int i = 0; i < listCheckedPoker.size(); i++) {
            Poker childResult = listCheckedPoker.get(i);
            final View subView = layoutInflater.inflate(R.layout.shared_history_poker, null);
            TextView pokerRole = (TextView) subView.findViewById(R.id.poker_role);
            TextView pokerCard = (TextView) subView.findViewById(R.id.poker_card);
            TextView checkedDate = (TextView) subView.findViewById(R.id.checked_date);
            pokerCard.setText(childResult.getInputPoker());
            pokerRole.setText(childResult.getPokerPosition());
            checkedDate.setText(childResult.getCheckTime());
            historyContainer.addView(subView);
        }
        return view;
    }
}
