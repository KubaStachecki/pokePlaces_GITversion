package com.example.kuba10.mypokemonplaces.InfoFragment;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kuba10.mypokemonplaces.Constants;
import com.example.kuba10.mypokemonplaces.Model.AppInfoText;
import com.example.kuba10.mypokemonplaces.R;

import butterknife.BindView;


public class InfoFragment extends DialogFragment {


    @BindView(R.id.app_info_text)
    TextView infoText;

    @BindView(R.id.app_info_title)
    TextView infoTitle;

    private AppInfoText appAppInfoText;


    //TODO separate database for every user - if time allows...


    public static InfoFragment newInstance(Bundle bundle) {
        InfoFragment fragment = new InfoFragment();
        fragment.setArguments(bundle);
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_info, container, false);

        appAppInfoText = getArguments().getParcelable(Constants.INFO);


        infoText.setText(appAppInfoText.getText());
        infoTitle.setText(appAppInfoText.getTitle());


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
