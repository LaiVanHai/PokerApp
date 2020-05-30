package jp.co.netprotections.pokerapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnResult = (Button) getView().findViewById(R.id.button_result);
        tvAdd = (TextView) getView().findViewById(R.id.add_poker);
        container = (LinearLayout) getView().findViewById(R.id.container);
        LayoutInflater layoutInflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View subView = layoutInflater.inflate(R.layout.shared_input_poker, null);
        container.addView(subView);
        listPoker.add("");
        CheckInput(subView);

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
                int size = listPoker.size();
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


}
