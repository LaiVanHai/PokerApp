package jp.co.netprotections.pokerapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.co.netprotections.pokerapp.R;
import jp.co.netprotections.pokerapp.common.CheckNetwork;
import jp.co.netprotections.pokerapp.model.PokerRequest;
import jp.co.netprotections.pokerapp.model.PokerResponse;
import jp.co.netprotections.pokerapp.services.PokerService;

public class HomeFragment extends Fragment {
    private Button btnResult;
    private TextView tvAdd;
    private EditText edtPoker1;
    private EditText edtPoker2;
    private EditText edtPoker3;
    private EditText edtPoker4;
    private EditText edtPoker5;
    private LinearLayout container;
    private PokerRequest listPokerRequest = new PokerRequest(new ArrayList<String>());
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
        listPokerRequest.addCard("");

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
                listPokerRequest.addCard("");
                container.addView(view);
                Toast.makeText(getContext(), R.string.added_notice, Toast.LENGTH_LONG).show();
                CheckInput(view);
            }
        });

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckNetwork.isNetworkConnectionAvailable(getActivity())) {
                    deleteOldError();
                    sendRequestCheck();
                }
            }
        });
    }

    private void CheckInput(View view) {
        btnResult.setEnabled(false);
        ((EditText) view.findViewById(R.id.input_poker_1)).addTextChangedListener(textWatcher);
        ((EditText) view.findViewById(R.id.input_poker_2)).addTextChangedListener(textWatcher);
        ((EditText) view.findViewById(R.id.input_poker_3)).addTextChangedListener(textWatcher);
        ((EditText) view.findViewById(R.id.input_poker_4)).addTextChangedListener(textWatcher);
        ((EditText) view.findViewById(R.id.input_poker_5)).addTextChangedListener(textWatcher);
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
                String pokerInput1 = ((EditText) thisChild.findViewById(R.id.input_poker_1)).getText().toString().trim();
                String pokerInput2 = ((EditText) thisChild.findViewById(R.id.input_poker_2)).getText().toString().trim();
                String pokerInput3 = ((EditText) thisChild.findViewById(R.id.input_poker_3)).getText().toString().trim();
                String pokerInput4 = ((EditText) thisChild.findViewById(R.id.input_poker_4)).getText().toString().trim();
                String pokerInput5 = ((EditText) thisChild.findViewById(R.id.input_poker_5)).getText().toString().trim();
                currentStatus = !pokerInput1.isEmpty() && !pokerInput2.isEmpty() &&
                    !pokerInput3.isEmpty() && !pokerInput4.isEmpty() && !pokerInput5.isEmpty();
                if (currentStatus) {
                    strElement = pokerInput1 + " " + pokerInput2 + " " + pokerInput3 + " " +
                            pokerInput4 + " " + pokerInput5;
                    listPokerRequest.changeCard(i, strElement);
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
        void fragmentChange(PokerResponse content);
    }

    private void setError(View currentView, int errorMsgID, int pokerTitleID, int inputPokerID, String errMsg) {
        TextView tvErrorMsg = (TextView) currentView.findViewById(errorMsgID);
        tvErrorMsg.setText(errMsg);
        TextView tvTitle = (TextView) currentView.findViewById(pokerTitleID);
        EditText edText = (EditText) currentView.findViewById(inputPokerID);
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

    private void selectPokerError(View view, int errorCardNo, String errPokerMsg) {
        switch (errorCardNo) {
            case 1: {
                setError(view, R.id.poker_1_error_msg, R.id.input_poker_1_title, R.id.input_poker_1, errPokerMsg);
                break;
            }
            case 2: {
                setError(view, R.id.poker_2_error_msg, R.id.input_poker_2_title, R.id.input_poker_2, errPokerMsg);
                break;
            }
            case 3: {
                setError(view, R.id.poker_3_error_msg, R.id.input_poker_3_title, R.id.input_poker_3, errPokerMsg);
                break;
            }
            case 4: {
                setError(view, R.id.poker_4_error_msg, R.id.input_poker_4_title, R.id.input_poker_4, errPokerMsg);
                break;
            }
            case 5: {
                setError(view, R.id.poker_5_error_msg, R.id.input_poker_5_title, R.id.input_poker_5, errPokerMsg);
                break;
            }
        }
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
        progressdialog.show();
        PokerService.getResponseCheck(listPokerRequest, getContext(), new PokerService.CheckPokerCallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                if (response != null) {
                    PokerResponse pokerResponse = new Gson().fromJson(response.toString(), PokerResponse.class);
                    progressdialog.dismiss();
                    if (pokerResponse.getErrors() == null) {
                        if (homeFragmentListener != null) {
                            homeFragmentListener.fragmentChange(pokerResponse);
                        }
                    } else {
                        List<PokerResponse.Error> errorResponse = pokerResponse.getErrors();
                        for (int i=0; i<errorResponse.size(); i++) {
                            PokerResponse.Error currentError = errorResponse.get(i);
                            int currentErrPoker = listPokerRequest.getCards().indexOf(currentError.getCard());
                            String currentErrPokerMsg = currentError.getMsg();
                            View thisChild = container.getChildAt(currentErrPoker);
                            int errorCardNo = PokerService.getCardNo(currentError.getMsg());
                            if (errorCardNo > 0) {
                                selectPokerError(thisChild, errorCardNo, currentErrPokerMsg);
                            } else {
                                Toast toast = Toast.makeText(getActivity(), currentErrPokerMsg, Toast.LENGTH_LONG);
                                View view = toast.getView();
                                view.getBackground().setColorFilter(getResources().getColor(R.color.colorRed), PorterDuff.Mode.SRC_IN);
                                TextView text = view.findViewById(android.R.id.message);
                                text.setTextColor(getResources().getColor(R.color.colorWhite));
                                toast.show();
                                ArrayList<Integer> duplicationCardList = PokerService.getDuplicationCardNo(currentError.getCard());
                                for (int j=0; j<duplicationCardList.size(); j++) {
                                    selectPokerError(thisChild, duplicationCardList.get(j), currentErrPokerMsg);
                                }
                            }
                        }
                    }
                } else {
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
            }
        });
    }

}
