package com.hw.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Random;


public class Fragment_List extends Fragment {

    private AppCompatActivity activity;
    private CallBack_List callBackList;
    private final int NUM_OF_ROWS = 10;
    private LinearLayout[]  list_ROW_rows       ;
    private TextView[]      list_TXT_userNames  ;
    private TextView[]      list_TXT_scores     ;

    private Random random = new Random();

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setCallBackList(CallBack_List callBackList) {
        this.callBackList = callBackList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initViews();
        return view;
    }


    private void initViews() {
        for (int i = 0; i < NUM_OF_ROWS; i++) {
            list_ROW_rows[i].setOnClickListener(setOnMap(i));
        }
    }

    private View.OnClickListener setOnMap(int i) {
        return view -> {
            String detailes = getDetailes(i);
            callBackList.rowSelected(detailes);
        };
    }

    private String getDetailes(int i) {
        String userName = (String) list_TXT_userNames[i].getText();
        double lat = 32 + 0.5 * random.nextDouble();//31.534043;
        double lan = 34 + 0.5 * random.nextDouble();//34.604132;
        int score = Integer.parseInt((String) list_TXT_scores[i].getText());
        return String.format("%s,%f,%f,%d", userName, lat, lan, score);
    }

    private void findViews(View view) {
        list_TXT_userNames = new TextView[]{
                view.findViewById(R.id.list_TXT_userName0),
                view.findViewById(R.id.list_TXT_userName1),
                view.findViewById(R.id.list_TXT_userName2),
                view.findViewById(R.id.list_TXT_userName3),
                view.findViewById(R.id.list_TXT_userName4),
                view.findViewById(R.id.list_TXT_userName5),
                view.findViewById(R.id.list_TXT_userName6),
                view.findViewById(R.id.list_TXT_userName7),
                view.findViewById(R.id.list_TXT_userName8),
                view.findViewById(R.id.list_TXT_userName9),
        };
        list_TXT_scores = new TextView[]{
                view.findViewById(R.id.list_TXT_score0),
                view.findViewById(R.id.list_TXT_score1),
                view.findViewById(R.id.list_TXT_score2),
                view.findViewById(R.id.list_TXT_score3),
                view.findViewById(R.id.list_TXT_score4),
                view.findViewById(R.id.list_TXT_score5),
                view.findViewById(R.id.list_TXT_score6),
                view.findViewById(R.id.list_TXT_score7),
                view.findViewById(R.id.list_TXT_score8),
                view.findViewById(R.id.list_TXT_score9),
        };
        list_ROW_rows = new LinearLayout[]{
                view.findViewById(R.id.list_ROW_row0),
                view.findViewById(R.id.list_ROW_row1),
                view.findViewById(R.id.list_ROW_row2),
                view.findViewById(R.id.list_ROW_row3),
                view.findViewById(R.id.list_ROW_row4),
                view.findViewById(R.id.list_ROW_row5),
                view.findViewById(R.id.list_ROW_row6),
                view.findViewById(R.id.list_ROW_row7),
                view.findViewById(R.id.list_ROW_row8),
                view.findViewById(R.id.list_ROW_row9),
        };
    }
}