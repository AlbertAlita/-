package com.taian.floatingballmatrix.binding.viewadapter.viewgroup;

import androidx.databinding.ViewDataBinding;

/**
 * Created by baotaian on 2017/6/15.
 */


public interface IBindingItemViewModel<V extends ViewDataBinding> {
    void injecDataBinding(V binding);
}
