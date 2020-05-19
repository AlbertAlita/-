package com.taian.floatingballmatrix.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.taian.floatingballmatrix.R;
import com.taian.floatingballmatrix.base.BaseDialog;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxkit.RxImageTool;

public class CheckCodeDialog extends BaseDialog {


    public CheckCodeDialog(Context context) {
        super(context);
    }


    @Override
    public View setContentView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(generateContentViewId(), null);
        setContentView(view);
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.width = RxDeviceTool.getScreenWidth(getContext()) - RxImageTool.dp2px(20);
        wl.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        setCanceledOnTouchOutside(false); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        window.setGravity(Gravity.CENTER);
        onWindowAttributesChanged(wl);
        return view;
    }

    @Override
    public BaseDialog bindData(Object t) {
        return this;
    }

    @Override
    public int generateContentViewId() {
        return R.layout.dialog_check_code;
    }

    @Override
    public void setViewBehavior(View content) {
        EditText etPsd = content.findViewById(R.id.et_psd);

        content.findViewById(R.id.cancel).setOnClickListener(v -> {
            dismiss();
        });
        content.findViewById(R.id.ok).setOnClickListener(v -> {
            String psd = etPsd.getText().toString();
            if (onViewClickListenter != null)
                onViewClickListenter.onViewClicked(psd);
        });
    }

    private OnViewClickListenter onViewClickListenter;

    public void setOnViewClickListenter(OnViewClickListenter onViewClickListenter) {
        this.onViewClickListenter = onViewClickListenter;
    }

    public interface OnViewClickListenter {
        void onViewClicked(String psd);
    }

}
