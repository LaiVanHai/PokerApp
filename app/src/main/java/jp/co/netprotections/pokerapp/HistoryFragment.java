package jp.co.netprotections.pokerapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "param";

    // TODO: Rename and change types of parameters
    private ArrayList<Poker> mParam;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(ArrayList<Poker> params) {
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
        for(int i = 0; i < mParam.size(); i++) {
            Poker childResult = mParam.get(i);
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
