package jp.co.netprotections.pokerapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.netprotections.pokerapp.R;
import jp.co.netprotections.pokerapp.activities.ResultActivity;
import jp.co.netprotections.pokerapp.common.MyStorage;
import jp.co.netprotections.pokerapp.common.PokerSingleton;
import jp.co.netprotections.pokerapp.model.Poker;

public class HomeFragment extends Fragment {
    private Button btnResult;
    private TextView tvAdd;
    private EditText edtPoker1;
    private EditText edtPoker2;
    private EditText edtPoker3;
    private EditText edtPoker4;
    private EditText edtPoker5;
    private LinearLayout container;
    private ArrayList<String> listPoker = new ArrayList<String>();
    private ArrayList<TextView> listTitleErr = new ArrayList<TextView>();
    private ArrayList<TextView> listMsgErr = new ArrayList<TextView>();
    private ArrayList<EditText> listInputErr = new ArrayList<EditText>();

    private boolean firstLoadFag = true;

    private HomeFragmentListener homeFragmentListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        listPoker.add("");

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = layoutInflater.inflate(R.layout.shared_input_poker, null);
                int childCount = container.getChildCount();
                View beforeView = container.getChildAt(childCount - 1);
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
                deleteOldError();
                if (homeFragmentListener.isNetworkConnectionAvailable()) {
                    sendRequestCheck();
                }
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
            for (int i = 0; i < childCount; i++) {
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
        void activityChange(Context context, ArrayList<Poker> content);
        boolean isNetworkConnectionAvailable();
    }

    private void setError(TextView tvTitle, EditText edText, TextView tvErrorMsg) {
        listTitleErr.add(tvTitle);
        listMsgErr.add(tvErrorMsg);
        listInputErr.add(edText);
        Drawable error_icon = getResources().getDrawable(R.drawable.ic_error);
        error_icon.setBounds(0, 0, 40, 40);
        edText.setCompoundDrawables(null, null, error_icon, null);
        edText.getBackground().setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.SRC_ATOP);
        edText.setTextColor(getResources().getColor(R.color.colorRed));
        tvTitle.setTextColor(getResources().getColor(R.color.colorRed));
    }

    private void deleteOldError() {
        while (listTitleErr.size() > 0) {
            listTitleErr.get(0).setTextColor(getResources().getColor(R.color.colorBlack));
            listTitleErr.remove(0);
        }
        while (listMsgErr.size() > 0) {
            listMsgErr.get(0).setText("");
            listMsgErr.remove(0);
        }
        while (listInputErr.size() > 0) {
            EditText edText = listInputErr.get(0);
            edText.setCompoundDrawables(null, null, null, null);
            edText.getBackground().setColorFilter(getResources().getColor(R.color.colorGrey2), PorterDuff.Mode.SRC_ATOP);
            edText.setTextColor(getResources().getColor(R.color.colorBlack));
            listInputErr.remove(0);
        }
    }

    private void sendRequestCheck() {
        final ProgressDialog progressdialog = new ProgressDialog(getContext());
        progressdialog.setMessage(getString(R.string.wating_response));
        Log.e("Home Activity", "Request Start");
        String url = getString(R.string.check_poker_url);
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
                        progressdialog.dismiss();
                        //Success Callback
                        Log.e("Home Activity", "JsonObjectRequest onResponse: " + response.toString());
                        if (!response.isNull("error")) {
                            try {
                                JSONArray errorResponse = response.getJSONArray("error");
                                for (int i = 0; i < errorResponse.length(); i++) {
                                    JSONObject currentError = errorResponse.getJSONObject(i);
                                    int currentErrPoker = listPoker.indexOf(currentError.getString("card"));
                                    String currentErrPokerMsg = currentError.getString("msg");
                                    View thisChild = container.getChildAt(currentErrPoker);
                                    final String regex = "[1-9]+";
                                    Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                                    Matcher matcher = pattern.matcher(currentErrPokerMsg);
                                    if (matcher.find()) {
                                        switch (matcher.group(0)) {
                                            case "1": {
                                                TextView tvError = (TextView) thisChild.findViewById(R.id.poker_1_error_msg);
                                                tvError.setText(currentErrPokerMsg);
                                                TextView tvInputPoker1 = (TextView) thisChild.findViewById(R.id.input_poker_1_title);
                                                edtPoker1 = (EditText) thisChild.findViewById(R.id.input_poker_1);
                                                setError(tvInputPoker1, edtPoker1, tvError);
                                                break;
                                            }
                                            case "2": {
                                                TextView tvError = (TextView) thisChild.findViewById(R.id.poker_2_error_msg);
                                                tvError.setText(currentErrPokerMsg);
                                                TextView tvInputPoker2 = (TextView) thisChild.findViewById(R.id.input_poker_2_title);
                                                edtPoker2 = (EditText) thisChild.findViewById(R.id.input_poker_2);
                                                setError(tvInputPoker2, edtPoker2, tvError);
                                                break;
                                            }
                                            case "3": {
                                                TextView tvError = (TextView) thisChild.findViewById(R.id.poker_3_error_msg);
                                                tvError.setText(currentErrPokerMsg);
                                                TextView tvInputPoker3 = (TextView) thisChild.findViewById(R.id.input_poker_3_title);
                                                edtPoker3 = (EditText) thisChild.findViewById(R.id.input_poker_3);
                                                setError(tvInputPoker3, edtPoker3, tvError);
                                                break;
                                            }
                                            case "4": {
                                                TextView tvError = (TextView) thisChild.findViewById(R.id.poker_4_error_msg);
                                                tvError.setText(currentErrPokerMsg);
                                                TextView tvInputPoker4 = (TextView) thisChild.findViewById(R.id.input_poker_4_title);
                                                edtPoker4 = (EditText) thisChild.findViewById(R.id.input_poker_4);
                                                setError(tvInputPoker4, edtPoker4, tvError);
                                                break;
                                            }
                                            case "5": {
                                                TextView tvError = (TextView) thisChild.findViewById(R.id.poker_5_error_msg);
                                                tvError.setText(currentErrPokerMsg);
                                                TextView tvInputPoker5 = (TextView) thisChild.findViewById(R.id.input_poker_5_title);
                                                edtPoker5 = (EditText) thisChild.findViewById(R.id.input_poker_5);
                                                setError(tvInputPoker5, edtPoker5, tvError);
                                                break;
                                            }
                                        }
                                    } else {
                                        Toast toast = Toast.makeText(getActivity(), currentErrPokerMsg, Toast.LENGTH_LONG);
                                        View view = toast.getView();
                                        view.getBackground().setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.SRC_IN);
                                        TextView text = view.findViewById(android.R.id.message);
                                        text.setTextColor(getResources().getColor(R.color.colorWhite));
                                        toast.show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ArrayList<Poker> pokerResults = new ArrayList<Poker>();
                            try {
                                JSONArray resultResponse = response.getJSONArray("result");
                                for (int i = 0; i < resultResponse.length(); i++) {
                                    JSONObject currentPokerObj = resultResponse.getJSONObject(i);
                                    String currentPokerCard = currentPokerObj.getString("card");
                                    String currentPokerPosition = currentPokerObj.getString("hand");
                                    boolean currentPokerStrong = currentPokerObj.getBoolean("best");
                                    SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm", Locale.getDefault());
                                    String currentDateandTime = sdf.format(new Date());
                                    Poker currentPoker = new Poker(currentPokerCard, currentPokerPosition, currentPokerStrong, currentDateandTime);
                                    pokerResults.add(currentPoker);
                                    MyStorage.addCheckedResult(getContext(), currentPoker);
                                }
                                if (homeFragmentListener != null) {
                                    homeFragmentListener.activityChange(getActivity(), pokerResults);
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
                        //Failure Callback
//                        Log.e("Volley", error.getMessage());
                        progressdialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("サーバーエラー");
                        builder.setMessage("後程、お試しください！");
                        builder.setNegativeButton("閉じる", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });

        PokerSingleton.getInstance(getContext()).getRequestQueue().add(jsonObjReq);
        progressdialog.show();
    }
}
