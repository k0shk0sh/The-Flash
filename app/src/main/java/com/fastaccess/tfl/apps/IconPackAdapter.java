package com.fastaccess.tfl.apps;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.fastaccess.tfl.R;
import com.fastaccess.tfl.apps.IconPackHelper.IconPackInfo;
import com.fastaccess.tfl.ui.widget.FontTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IconPackAdapter extends BaseAdapter {

    ArrayList<IconPackInfo> mSupportedPackages;
    LayoutInflater mLayoutInflater;
    String mCurrentIconPack;
    int mCurrentIconPackPosition = -1;

    public IconPackAdapter(Context ctx, Map<String, IconPackInfo> supportedPackages) {
        mLayoutInflater = LayoutInflater.from(ctx);
        mSupportedPackages = new ArrayList<IconPackInfo>(supportedPackages.values());
        Collections.sort(mSupportedPackages, new Comparator<IconPackInfo>() {
            @Override
            public int compare(IconPackInfo lhs, IconPackInfo rhs) {
                return lhs.label.toString().compareToIgnoreCase(rhs.label.toString());
            }
        });

        Resources res = ctx.getResources();
        String defaultLabel = "Default Theme";
        Drawable icon = res.getDrawable(R.mipmap.ic_launcher);
        mSupportedPackages.add(0, new IconPackInfo(defaultLabel, icon, ""));
        mCurrentIconPack = "Default";
    }

    @Override
    public int getCount() {
        return mSupportedPackages.size();
    }

    @Override
    public String getItem(int position) {
        return mSupportedPackages.get(position).packageName;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public boolean isCurrentIconPack(int position) {
        return mCurrentIconPackPosition == position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.icon_pack_layout, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        IconPackInfo info = mSupportedPackages.get(position);
        holder.title.setText(info.label);
        holder.icon.setImageDrawable(info.icon);
        boolean isCurrentIconPack = info.packageName.equals(mCurrentIconPack);
        holder.radio.setChecked(isCurrentIconPack);
        if (isCurrentIconPack) {
            mCurrentIconPackPosition = position;
        }
        return view;
    }

    static class ViewHolder {
        @Bind(R.id.icon) ImageView icon;
        @Bind(R.id.title) FontTextView title;
        @Bind(R.id.radio) RadioButton radio;

        ViewHolder(View view) {ButterKnife.bind(this, view);}
    }
}