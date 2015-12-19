package com.fastaccess.tfl.ui.main.dock;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.fastaccess.tfl.R;
import com.fastaccess.tfl.apps.AppsLoader;
import com.fastaccess.tfl.apps.AppsModel;
import com.fastaccess.tfl.core.BaseFragment;
import com.fastaccess.tfl.ui.widget.DynamicRecyclerView;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import icepick.State;

/**
 * Created by Kosh on 19/12/15 10:38 AM
 */
public class ChooseAppFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<List<AppsModel>>, SelectAppAdapter.OnAppClicked {
    @Bind(R.id.recyclerView) DynamicRecyclerView recyclerView;
    @Bind(R.id.progress) CircularFillableLoaders progress;
    private SelectAppAdapter adapter;
    private MainDockModel mainDockModel;
    @State boolean isFolder;

    public static ChooseAppFragment getInstance(boolean isFolder) {
        ChooseAppFragment fragment = new ChooseAppFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFolder", isFolder);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof MainDockModel)) {
            throw new IllegalArgumentException("Activity must implement MainDockModel");
        }
        mainDockModel = (MainDockModel) context;
    }

    @Override public void onDetach() {
        super.onDetach();
        mainDockModel = null;
    }

    @Override protected int layoutResId() {
        return R.layout.choose_fragment_layout;
    }

    @Override protected void onFragmentCreated(View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            isFolder = getArguments().getBoolean("isFolder");
        }
        adapter = new SelectAppAdapter(new ArrayList<AppsModel>(), this, isFolder);
        recyclerView.setAdapter(adapter);
        getActivity().getLoaderManager().initLoader(isFolder ? 2 : 3, null, this);
    }

    @Override public Loader<List<AppsModel>> onCreateLoader(int id, Bundle args) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            recyclerView.showProgress(progress);
        }
        return new AppsLoader(getContext());
    }

    @Override public void onLoadFinished(Loader<List<AppsModel>> loader, List<AppsModel> data) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            recyclerView.hideProgress(progress);
            adapter.add(data);
        }
    }

    @Override public void onLoaderReset(Loader<List<AppsModel>> loader) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            adapter.reset();
        }
    }

    @Override public void onClick(AppsModel model) {
        if (mainDockModel != null) {
            mainDockModel.onAppSelected(model);
        }
    }
}
