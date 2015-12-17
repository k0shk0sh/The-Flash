package com.fastaccess.tfl.ui.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by Kosh on 21/11/15 8:35 PM
 */
public class VHolder extends RecyclerView.ViewHolder {
    public VHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
