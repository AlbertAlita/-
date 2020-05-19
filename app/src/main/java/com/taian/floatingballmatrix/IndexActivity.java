package com.taian.floatingballmatrix;
/*
 Created by baotaian on 2020/5/19 0019.
*/


import android.content.Intent;
import android.os.Bundle;

import com.taian.floatingballmatrix.base.BaseActivity;
import com.taian.floatingballmatrix.base.BaseFragment;
import com.taian.floatingballmatrix.base.BaseViewModel;
import com.taian.floatingballmatrix.constant.Constant;
import com.taian.floatingballmatrix.databinding.ActivityIndexBinding;
import com.taian.floatingballmatrix.fragment.IndexFragment;
import com.taian.floatingballmatrix.fragment.SettingFragment;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

public class IndexActivity extends BaseActivity<ActivityIndexBinding, BaseViewModel> {
    private BaseFragment currentFragment;
    private IndexFragment indexFragment;
    private Map<String, BaseFragment> fragmentStore = new HashMap<>();

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_index;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        indexFragment = new IndexFragment();
        openNewFragment(indexFragment);
    }

    public void openNewFragment(BaseFragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        BaseFragment baseFragment = fragmentStore.get(fragment.getClass().getCanonicalName());
        if (baseFragment != null) fragmentTransaction.show(baseFragment);
        else {
            fragmentTransaction.add(R.id.container, fragment);
            fragmentStore.put(fragment.getClass().getCanonicalName(), fragment);
        }
        if (currentFragment != null) fragmentTransaction.hide(currentFragment);
        currentFragment = fragment;
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (currentFragment != null)
            currentFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (currentFragment instanceof SettingFragment) {
            openNewFragment(indexFragment);
        } else super.onBackPressed();
    }
}
