package com.example.kuba10.mypokemonplaces.InfoFragment;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kuba10.mypokemonplaces.R;


public class InfoFragment extends DialogFragment {

    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        setFullScreenStyle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info, container, false);
        return view;
    }

    private void setFullScreenStyle() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

}
