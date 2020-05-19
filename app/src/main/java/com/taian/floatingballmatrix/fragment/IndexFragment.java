package com.taian.floatingballmatrix.fragment;
/*
 Created by baotaian on 2020/5/19 0019.
*/


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.taian.floatingballmatrix.BR;
import com.taian.floatingballmatrix.IndexActivity;
import com.taian.floatingballmatrix.R;
import com.taian.floatingballmatrix.base.BaseFragment;
import com.taian.floatingballmatrix.constant.Constant;
import com.taian.floatingballmatrix.databinding.FragmentIndexBinding;
import com.taian.floatingballmatrix.viewmodel.IndexViewModel;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

public class IndexFragment extends BaseFragment<FragmentIndexBinding, IndexViewModel> {
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_index;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE) {
            viewModel.setOnItemChanged();
        }
    }

    @Override
    public void initViewObservable() {
        viewModel.settingEvent.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                IndexActivity activity = (IndexActivity) getActivity();
                SettingFragment settingFragment = new SettingFragment();
                activity.openNewFragment(settingFragment);
            }
        });
    }
}
