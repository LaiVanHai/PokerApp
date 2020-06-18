package jp.co.netprotections.pokerapp.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.co.netprotections.pokerapp.R;
import jp.co.netprotections.pokerapp.common.MyStorage;
import jp.co.netprotections.pokerapp.model.PokerCheckHistory;
import jp.co.netprotections.pokerapp.model.PokerResponse;
import jp.co.netprotections.pokerapp.services.PokerService;

public class ResultFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "param";
    // TODO: Rename and change types of parameters
    private PokerResponse mParam;

    public ResultFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ResultFragment newInstance(PokerResponse params) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getParcelable(ARG_PARAM);
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        final LinearLayout resultContainer = (LinearLayout) view.findViewById(R.id.result_container);
        createViewData(resultContainer);
        return view;
    }

    private void createViewData(final LinearLayout container) {
        LayoutInflater layoutInflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        List<PokerResponse.Result> results = mParam.getResults();
        MyStorage myStorage = new MyStorage();
        for (int i = 0; i < results.size(); i++) {
            PokerResponse.Result childResult = results.get(i);
            final View subView = layoutInflater.inflate(R.layout.shared_result_poker, null);
            TextView title = (TextView) subView.findViewById(R.id.result_title);
            TextView subtitle = (TextView) subView.findViewById(R.id.result_subtitle);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd HH:mm", Locale.getDefault());
            String currentDateandTime = simpleDateFormat.format(new Date());
            String card = childResult.getCard();
            String hand = childResult.getHand();
            boolean isBest = childResult.isBest();
            PokerCheckHistory currentPoker = new PokerCheckHistory(card, hand, currentDateandTime);
            myStorage.addCheckedResult(getContext(), currentPoker);
            if (isBest) {
                Drawable error_icon = getResources().getDrawable(R.drawable.ic_check_circle_blue_24dp);
                error_icon.setBounds(0, 0, 40, 40);
                title.setCompoundDrawables(null, null, error_icon, null);
            }
            final ImageView imageView = (ImageView) subView.findViewById(R.id.result_img);
            title.setText(card);
            subtitle.setText(hand);
            PokerService.getImage(getContext(), new PokerService.GetImageCallback(){
                @Override
                public void onSuccessResponse(Bitmap bitmap) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                        container.addView(subView);
                    } else {
                        imageView.setImageResource(R.drawable.ic_notifications);
                        container.addView(subView);
                    }
                }
            });
        }
        myStorage = null;
    }
}
