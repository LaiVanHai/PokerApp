package jp.co.netprotections.pokerapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import jp.co.netprotections.pokerapp.R;
import jp.co.netprotections.pokerapp.model.PokerCheckHistory;


public class HistoryFragment extends Fragment {
    private static final String ARG_PARAM = "param";

    // TODO: Rename and change types of parameters
    private ArrayList<PokerCheckHistory> mParam;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(ArrayList<PokerCheckHistory> params) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getParcelableArrayList(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        final LinearLayout historyContainer = (LinearLayout) view.findViewById(R.id.history_container);
        LayoutInflater layoutInflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if( mParam != null ) {
            for (int i = 0; i < mParam.size(); i++) {
                PokerCheckHistory childResult = mParam.get(i);
                final View subView = layoutInflater.inflate(R.layout.shared_history_poker, null);
                TextView pokerRole = (TextView) subView.findViewById(R.id.poker_role);
                TextView pokerCard = (TextView) subView.findViewById(R.id.poker_card);
                TextView checkedDate = (TextView) subView.findViewById(R.id.checked_date);
                pokerCard.setText(childResult.getInputPoker());
                pokerRole.setText(childResult.getPokerPosition());
                checkedDate.setText(childResult.getCheckedTime());
                historyContainer.addView(subView);
            }
        }
        return view;
    }
}
