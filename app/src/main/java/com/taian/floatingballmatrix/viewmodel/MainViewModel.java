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
import com.taian.floatingballmatrix.constant.Constant;
import com.taian.floatingballmatrix.entity.ButtonEntity;
import com.taian.floatingballmatrix.utils.GsonUtil;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxkit.RxSPTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<Integer, ButtonEntity> map;

    public MainViewModel(@NonNull Application application) {
        super(application);
        String json = RxSPTool.getString(getApplication(), Constant.BUTTON_SETTING);
        map = convertToMap(GsonUtil.getList(json, ButtonEntity.class));
        for (int i = 0; i < BTN_MAX_NUM; i++) {
            BtnItemViewModel btnItemViewModel = new BtnItemViewModel(this);
            if (map.get(i) !=null)btnItemViewModel.setEntity(map.get(i));
            observableList.add(btnItemViewModel);
        }
    }

    public GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplication(), 3);

    public ItemBinding<BtnItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.btn_item);

    public ButtonAdapter adapter = new ButtonAdapter(itemBinding);

    public ObservableList<BtnItemViewModel> observableList = new ObservableArrayList<>();

    public BindingCommand settinigClicked = new BindingCommand(() -> {
        startActivityForReslut(SettingActivity.class);
    });

    public int getItemPosition(BtnItemViewModel btnItemViewModel) {
        return observableList.indexOf(btnItemViewModel);
    }

    public void setOnItemChanged() {
        if (map.isEmpty()){
            String json = RxSPTool.getString(getApplication(), Constant.BUTTON_SETTING);
            map = convertToMap(GsonUtil.getList(json, ButtonEntity.class));
        }
        for (int i = 0; i < observableList.size(); i++) {
            ButtonEntity buttonEntity = map.get(i);
            if (buttonEntity != null) {
                BtnItemViewModel itemViewModel = observableList.get(i);
                itemViewModel.setEntity(buttonEntity);
                adapter.notifyItemChanged(i);
            }
        }
    }

    private Map<Integer, ButtonEntity> convertToMap(List<ButtonEntity> entities) {
        Map<Integer, ButtonEntity> map = new HashMap<>();
        for (ButtonEntity entity : entities) {
            map.put(entity.index, entity);
        }
        return map;
    }
}
