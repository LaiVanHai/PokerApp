package jp.co.netprotections.pokerapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btnResult;
    private TextView tvAdd;
    private EditText edtPoker1;
    private EditText edtPoker2;
    private EditText edtPoker3;
    private EditText edtPoker4;
    private EditText edtPoker5;
    private LinearLayout container;
    private ArrayList<String> listPoker = new ArrayList<String>();

    private boolean firstLoadFag = true;

    private HomeFragmentListener homeFragmentListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (savedInstanceState != null) {
            String value = savedInstanceState.getString("KEY");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (savedInstanceState != null) {
            String ste = savedInstanceState.getString("KEY");
            // Do something with value if needed
        }
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnResult = (Button) getView().findViewById(R.id.button_result);
        tvAdd = (TextView) getView().findViewById(R.id.add_poker);
        container = (LinearLayout) getView().findViewById(R.id.input_container);
        LayoutInflater layoutInflater =
            (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View subView = layoutInflater.inflate(R.layout.shared_input_poker, null);
        container.addView(subView);
        CheckInput(subView);
        if (firstLoadFag) {
            listPoker.add("");
            firstLoadFag = false;
        }
        
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = layoutInflater.inflate(R.layout.shared_input_poker, null);
                int childCount = container.getChildCount();
                View beforeView = container.getChildAt(childCount-1);
                View line = beforeView.findViewById(R.id.line);
                line.setVisibility(View.VISIBLE);
                listPoker.add("");
                container.addView(view);
                Toast.makeText(getContext(), "追加した", Toast.LENGTH_LONG).show();
                CheckInput(view);
            }
        });

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "推した", Toast.LENGTH_LONG).show();
                Log.e("Home Activity", "Request Start");
                String url = "http://p0kerhands.herokuapp.com/api/v1/cards/check";
                JSONObject postparams = new JSONObject();
                JSONArray jsArrayPoker = new JSONArray(listPoker);
                try {
                    postparams.put("cards", jsArrayPoker);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Success Callback
                            Log.e("Home Activity", "JsonObjectRequest onResponse: " + response.toString());
                            if (!response.isNull("error")) {
                                try {
                                    JSONArray errorResponse = response.getJSONArray("error");
                                    for (int i= 0; i < errorResponse.length(); i++) {
                                        JSONObject currentError = errorResponse.getJSONObject(i);
                                        int currentErrPoker = listPoker.indexOf(currentError.getString("card"));
                                        String currentErrPokerMsg = currentError.getString("msg");
                                        View thisChild = container.getChildAt(currentErrPoker);
                                        final String regex = "[1-9]+";
                                        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                                        Matcher matcher = pattern.matcher(currentErrPokerMsg);
                                        if (matcher.find()) {
                                            switch(matcher.group(0)) {
                                                case "1": {
                                                    TextView tvError = (TextView) thisChild.findViewById(R.id.poker_1_error_msg);
                                                    edtPoker1 = (EditText) thisChild.findViewById(R.id.input_poker_1);
                                                    tvError.setText(currentErrPokerMsg);
                                                    break;
                                                }
                                                case "2": {
                                                    TextView tvError = (TextView) thisChild.findViewById(R.id.poker_2_error_msg);
                                                    tvError.setText(currentErrPokerMsg);
                                                    break;
                                                }
                                                case "3": {
                                                    TextView tvError = (TextView) thisChild.findViewById(R.id.poker_3_error_msg);
                                                    tvError.setText(currentErrPokerMsg);
                                                    break;
                                                }
                                                case "4": {
                                                    TextView tvError = (TextView) thisChild.findViewById(R.id.poker_4_error_msg);
                                                    tvError.setText(currentErrPokerMsg);
                                                    break;
                                                }
                                                case "5": {
                                                    TextView tvError = (TextView) thisChild.findViewById(R.id.poker_5_error_msg);
                                                    tvError.setText(currentErrPokerMsg);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                ArrayList<Poker> pokerResults = new ArrayList<Poker>();
                                try {
                                    JSONArray resultResponse = response.getJSONArray("result");
                                    for (int i= 0; i < resultResponse.length(); i++) {
                                        JSONObject currentPokerObj = resultResponse.getJSONObject(i);
                                        String currentPokerCard = currentPokerObj.getString("card");
                                        String currentPokerPosition = currentPokerObj.getString("hand");
                                        boolean currentPokerStrong = currentPokerObj.getBoolean("best");
                                        Poker currentPoker = new Poker(currentPokerCard, currentPokerPosition, currentPokerStrong);
                                        pokerResults.add(currentPoker);
                                    }
                                    if (homeFragmentListener != null) {
                                        homeFragmentListener.activityChange(pokerResults);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            int i = 0;
                            //Failure Callback
                            Log.e("Volley", error.getMessage());
                        }
                    });

                PokerSingleton.getInstance(getContext()).getRequestQueue().add(jsonObjReq);
            }
        });
    }

    private void CheckInput(View view) {
        btnResult.setEnabled(false);
        edtPoker1 = (EditText) view.findViewById(R.id.input_poker_1);
        edtPoker2 = (EditText) view.findViewById(R.id.input_poker_2);
        edtPoker3 = (EditText) view.findViewById(R.id.input_poker_3);
        edtPoker4 = (EditText) view.findViewById(R.id.input_poker_4);
        edtPoker5 = (EditText) view.findViewById(R.id.input_poker_5);
        edtPoker1.addTextChangedListener(textWatcher);
        edtPoker2.addTextChangedListener(textWatcher);
        edtPoker3.addTextChangedListener(textWatcher);
        edtPoker4.addTextChangedListener(textWatcher);
        edtPoker5.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String strElement;
            Boolean currentStatus, btnActiveStatus = true;

            int childCount = container.getChildCount();
            for (int i = 0; i < childCount; i++){
                View thisChild = container.getChildAt(i);
                edtPoker1 = (EditText) thisChild.findViewById(R.id.input_poker_1);
                edtPoker2 = (EditText) thisChild.findViewById(R.id.input_poker_2);
                edtPoker3 = (EditText) thisChild.findViewById(R.id.input_poker_3);
                edtPoker4 = (EditText) thisChild.findViewById(R.id.input_poker_4);
                edtPoker5 = (EditText) thisChild.findViewById(R.id.input_poker_5);
                String pokerInput1 = edtPoker1.getText().toString().trim();
                String pokerInput2 = edtPoker2.getText().toString().trim();
                String pokerInput3 = edtPoker3.getText().toString().trim();
                String pokerInput4 = edtPoker4.getText().toString().trim();
                String pokerInput5 = edtPoker5.getText().toString().trim();
                currentStatus = !pokerInput1.isEmpty() && !pokerInput2.isEmpty() &&
                    !pokerInput3.isEmpty() && !pokerInput4.isEmpty() && !pokerInput5.isEmpty();
                if (currentStatus) {
                    strElement = pokerInput1 + " " + pokerInput2 + " " + pokerInput3 + " " +
                       pokerInput4 + " " + pokerInput5;
                    listPoker.set(i, strElement);
                }
                btnActiveStatus = currentStatus && btnActiveStatus;
            }
            btnResult.setEnabled(btnActiveStatus);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragmentListener) {
            homeFragmentListener = (HomeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement HomeFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        homeFragmentListener = null;
    }

    public interface HomeFragmentListener {
        void activityChange(ArrayList<Poker> content);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("KEY", "value");
        super.onSaveInstanceState(outState);
    }
}
