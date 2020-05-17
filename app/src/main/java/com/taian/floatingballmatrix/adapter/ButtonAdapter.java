package com.taian.floatingballmatrix.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taian.floatingballmatrix.R;
import com.taian.floatingballmatrix.base.BaseApplication;
import com.taian.floatingballmatrix.viewmodel.BtnItemViewModel;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxkit.RxImageTool;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @Description:
 * @Author: baotaian
 * @Date: 2020/5/17
 * @Version:1.0
 */
public class ButtonAdapter extends BindingRecyclerViewAdapter<BtnItemViewModel> {

    public ButtonAdapter(@NonNull ItemBinding<? super BtnItemViewModel> itemBinding) {
        super(itemBinding);
    }

    @NonNull
    @Override
    public ViewDataBinding onCreateBinding(@NonNull LayoutInflater inflater, int layoutId, @NonNull ViewGroup viewGroup) {
        ViewDataBinding binding = super.onCreateBinding(inflater, layoutId, viewGroup);
        View cardView = binding.getRoot().findViewById(R.id.card_view);
        cardView.getLayoutParams().height = getItemHeight();
        return binding;
    }


    public int getItemHeight() {
        return (RxDeviceTool.getScreenWidth(BaseApplication.getInstance()) - RxImageTool.dip2px(28)) / 3;
    }

}
