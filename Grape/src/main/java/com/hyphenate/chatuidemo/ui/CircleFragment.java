package com.hyphenate.chatuidemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.R;

/**
 * Created by cheng on 16-12-1.
 */
public class CircleFragment extends Fragment implements View.OnClickListener {

    private LinearLayout marketLayout;
    private LinearLayout otherLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.em_fragment_circle, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;

        marketLayout = (LinearLayout)getView().findViewById(R.id.ll_market);
        otherLayout = (LinearLayout)getView().findViewById(R.id.ll_other);
        marketLayout.setOnClickListener(this);
        otherLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_market:
                startActivity(new Intent(getActivity(), MarketActivity.class));
                break;
            case R.id.ll_other:
                break;
            default:
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(((MainActivity)getActivity()).isConflict){
            outState.putBoolean("isConflict", true);
        }else if(((MainActivity)getActivity()).getCurrentAccountRemoved()){
            outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
        }
    }
}
