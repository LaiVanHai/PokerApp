package jp.co.netprotections.pokerapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "param";
    // TODO: Rename and change types of parameters
    private ArrayList<Poker> mParam;

    public ResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultFragment newInstance(ArrayList<Poker> params) {
        ResultFragment fragment = new ResultFragment();
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
        LayoutInflater layoutInflater =
            (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        String url = getString(R.string.get_img_url);
        for(int i = 0; i < mParam.size(); i++) {
            Poker childResult = mParam.get(i);
            final View subView = layoutInflater.inflate(R.layout.shared_result_poker, null);
            TextView title = (TextView) subView.findViewById(R.id.result_title);
            TextView subtitle = (TextView) subView.findViewById(R.id.result_subtitle);
            final ImageView imageView = (ImageView) subView.findViewById(R.id.result_img);
            title.setText(childResult.getInputPoker());
            subtitle.setText(childResult.getPokerPosition());
            ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                        resultContainer.addView(subView);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        imageView.setImageResource(R.drawable.ic_notifications);
                        resultContainer.addView(subView);
                    }
                });
            PokerSingleton.getInstance(getContext()).getRequestQueue().add(request);
        }
        return view;
    }
}
