package com.taian.floatingballmatrix.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.taian.floatingballmatrix.R;
import com.taian.floatingballmatrix.base.BaseDialog;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxkit.RxImageTool;

public class SelectSocketModeDialog extends BaseDialog {


    public SelectSocketModeDialog(Context context) {
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
        window.setGravity(Gravity.BOTTOM);
        onWindowAttributesChanged(wl);
        return view;
    }

    @Override
    public BaseDialog bindData(Object t) {
        return this;
    }

    @Override
    public int generateContentViewId() {
        return R.layout.dialog_select_socket_mode;
    }

    @Override
    public void setViewBehavior(View content) {
        content.findViewById(R.id.ll_TCP).setOnClickListener(v -> {
            dismiss();
            if (onViewClickListenter!=null) onViewClickListenter.onViewClicked(false);
        });
        content.findViewById(R.id.ll_UDP).setOnClickListener(v -> {
            dismiss();
            if (onViewClickListenter!=null) onViewClickListenter.onViewClicked(true);
        });
    }

    private OnViewClickListenter onViewClickListenter;

    public void setOnViewClickListenter(OnViewClickListenter onViewClickListenter) {
        this.onViewClickListenter = onViewClickListenter;
    }

    public interface OnViewClickListenter{
        void onViewClicked(boolean isUDP);
    }

}
