package com.taian.floatingballmatrix.viewmodel;

import android.app.Application;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.taian.floatingballmatrix.BR;
import com.taian.floatingballmatrix.R;
import com.taian.floatingballmatrix.SettingActivity;
import com.taian.floatingballmatrix.adapter.ButtonAdapter;
import com.taian.floatingballmatrix.base.BaseViewModel;
import com.taian.floatingballmatrix.binding.command.BindingCommand;
import com.tamsiree.rxkit.RxDeviceTool;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.GridLayoutManager;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @Author: baotaian
 * @Date: 2020/5/17
 * @Version:1.0
 */
public class MainViewModel extends BaseViewModel {

    private int BTN_MAX_NUM = 9;

    public MainViewModel(@NonNull Application application) {
        super(application);
        for (int i = 0; i < BTN_MAX_NUM; i++) {
            observableList.add(new BtnItemViewModel(this));
        }
    }

    public GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplication(), 3);

    public ItemBinding<BtnItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.btn_item);

    public ButtonAdapter  adapter = new ButtonAdapter (itemBinding);

    public ObservableList<BtnItemViewModel> observableList = new ObservableArrayList<>();

    public BindingCommand settinigClicked = new BindingCommand(() -> {
        startActivity(SettingActivity.class);
    });

    public int getItemPosition(BtnItemViewModel btnItemViewModel) {
        return observableList.indexOf(btnItemViewModel);
    }

}
