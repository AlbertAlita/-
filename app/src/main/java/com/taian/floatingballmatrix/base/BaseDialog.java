package com.taian.floatingballmatrix.base;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.taian.floatingballmatrix.R;


/**
 * Created by bta on 2016/11/3.
 */
public abstract class BaseDialog extends Dialog {
    protected Context context;

    public BaseDialog(Context context) {
        this(context, R.style.BoDialog);
        this.context = context;
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    public void create() {
        super.create();
    }

    private void init() {
        View view = setContentView();
        setViewBehavior(view);
    }

    public abstract View setContentView();

    public abstract BaseDialog bindData(Object t);

    @Override
    public void show() {
        super.show();
        init();
    }

    boolean setBackPressedAble() {
        return false;
    }

    @Override
    public void onBackPressed() {
        if (setBackPressedAble()) {
            super.onBackPressed();
        }
    }

    public abstract int generateContentViewId();

    public abstract void setViewBehavior(View content);
}
