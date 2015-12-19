package com.fastaccess.tfl.ui.main.dock;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.fastaccess.tfl.R;
import com.fastaccess.tfl.helper.AnimUtil;
import com.fastaccess.tfl.ui.widget.ViewPagerView;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.ViewGroup.LayoutParams;

/**
 * Created by Kosh on 19/12/15 10:03 AM
 */
public class ChooseAppPopupPager extends DialogFragment {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.reveal) LinearLayout reveal;
    @Bind(R.id.tabs) TabLayout tabs;
    @Bind(R.id.pager) ViewPagerView pager;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_pager_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnimUtil.circularRevealFromBottom(null, reveal, true);
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
            }
        });
        pager.setAdapter(new ChoosePagerAdapter(getContext(), getChildFragmentManager()));
        pager.setOffscreenPageLimit(2);
        tabs.setupWithViewPager(pager);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }
}
