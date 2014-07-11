package com.inspiredo.inspiredo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayFragment extends MyAbstractFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // URL of the API
    private static final String API_URL = "https://script.google.com/macros/s/" +
            "AKfycbxHQSc9ryLVy1LmEqQN_9z3yfH_dLjoJXvB0AB4rR8LTkY8wbc/exec";
    private static final String API_PARAM_TODO = "?what=todo";
    private static final String API_PARAM_HIST = "?what=history";
    private static final String API_PARAM_MATH = "?what=math";
    private static final String API_PARAM_VOCAB = "?what=vocab";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView mHistCount;
    private TextView mHistReq;
    private TextView mMathCount;
    private TextView mMathReq;
    private TextView mVocabCount;
    private TextView mVocabReq;

    private LinearLayout mHistoryBtn;
    private LinearLayout mMathBtn;
    private LinearLayout mVocabBtn;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodayFragment newInstance(String param1, String param2) {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TodayFragment() {
        // Required empty public constructor
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
        View TodayView = inflater.inflate(R.layout.fragment_today, container, false);

        // Instantiate the views
        mHistCount = (TextView) TodayView.findViewById(R.id.today_history_count);
        mHistReq = (TextView) TodayView.findViewById(R.id.today_history_req);
        mMathCount = (TextView) TodayView.findViewById(R.id.today_math_count);
        mMathReq = (TextView) TodayView.findViewById(R.id.today_math_req);
        mVocabCount = (TextView) TodayView.findViewById(R.id.today_vocab_count);
        mVocabReq = (TextView) TodayView.findViewById(R.id.today_vocab_req);

        mHistoryBtn = (LinearLayout) TodayView.findViewById(R.id.today_history_button);
        mMathBtn = (LinearLayout) TodayView.findViewById(R.id.today_math_button);
        mVocabBtn = (LinearLayout) TodayView.findViewById(R.id.today_vocab_button);

        mHistoryBtn.setOnClickListener(new ClickListener());
        mMathBtn.setOnClickListener(new ClickListener());
        mVocabBtn.setOnClickListener(new ClickListener());

        refreshData();
        return TodayView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void refreshData() {
        TodayJSON task = new TodayJSON();
        task.execute(API_URL + API_PARAM_TODO);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private class TodayJSON extends JSONTask {

        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage(getString(R.string.loading));
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        @Override
        public void handleJSON(JSONObject json) {
            if(json != null) {
                try {
                    JSONArray acts = json.getJSONArray("activities");
                    JSONObject historyJSON = acts.getJSONObject(0);
                    JSONObject mathJSON = acts.getJSONObject(1);
                    JSONObject vocabJSON = acts.getJSONObject(2);
                    mHistCount.setText(historyJSON.getString("complete"));
                    mHistReq.setText(historyJSON.getString("needed"));
                    mMathCount.setText(mathJSON.getString("complete"));
                    mMathReq.setText(mathJSON.getString("needed"));
                    mVocabCount.setText(vocabJSON.getString("complete"));
                    mVocabReq.setText(vocabJSON.getString("needed"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                mHistCount.setText('-');
                mHistReq.setText('-');
                mMathCount.setText('-');
                mMathReq.setText('-');
                mVocabCount.setText('-');
                mVocabReq.setText('-');
            }
        }

    }

    private class PostJSON extends JSONTask {


        @Override
        public void handleJSON(JSONObject json) {
            Log.d("PostJSON", json.toString());

            String response = "";
            String what = "";
            try {
                response = json.getString("status");
                what = json.getString("what");
            } catch (JSONException e) {
                response = "fail";
            }
            if (json != null && response.equals("success")) {
                if (what.equals("history")) {
                    incrementCount(mHistCount);
                } else if (what.equals("math")) {
                    incrementCount(mMathCount);
                } else if (what.equals("vocab")) {
                    incrementCount(mVocabCount);
                }
                Toast.makeText(getActivity(), "Nice Work!",
                        Toast.LENGTH_SHORT).show();

            }
        }

        private void incrementCount(TextView v) {
            v.setText(Integer.parseInt(v.getText().toString()) + 1 + "");
        }
    }

    private class ClickListener implements View.OnClickListener {


        @Override
        public void onClick(View v) {
            if (v == mHistoryBtn) {
                new PostJSON().execute(API_URL + API_PARAM_HIST, "POST");
            } else if (v == mMathBtn) {
                new PostJSON().execute(API_URL + API_PARAM_MATH, "POST");
            } else if (v == mVocabBtn) {
                new PostJSON().execute(API_URL + API_PARAM_VOCAB, "POST");
            } else {
                Toast.makeText(getActivity(), "You clicked me!", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
