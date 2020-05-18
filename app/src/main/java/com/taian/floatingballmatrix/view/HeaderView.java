package com.taian.floatingballmatrix.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taian.floatingballmatrix.R;


//header已计算过状态栏高度
public class HeaderView extends LinearLayout {
    private Context context;
    private ViewGroup layout;

    public HeaderView(Context context) {
        super(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        LayoutInflater inflater = LayoutInflater.from(context);
        layout = (ViewGroup) inflater.inflate(R.layout.header_view, this, true);
    }

    public ImageView getLeftBtn() {
        return (ImageView) layout.findViewById(R.id.header_left_btn);
    }

    public TextView getTitleView() {
        return (TextView) layout.findViewById(R.id.header_title);
    }

    public ImageView getRightBtn() {
        return (ImageView) layout.findViewById(R.id.header_right_btn);
    }

    public TextView getRightTxtBtn() {
        return (TextView) layout.findViewById(R.id.header_right_txt_btn);
    }

    public RelativeLayout getSubLayout() {
        return (RelativeLayout) layout.findViewById(R.id.header_sub_layout);
    }

    public HeaderView setText(int layoutId, int resId) {
        ((TextView) layout.findViewById(layoutId)).setText(resId);
        return this;
    }

    public HeaderView setText(int layoutId, String str) {
        ((TextView) layout.findViewById(layoutId)).setText(str);
        return this;
    }

    public HeaderView setImageResource(int layoutId, int resId) {
        ((ImageView) layout.findViewById(layoutId)).setImageResource(resId);
        return this;
    }

    public HeaderView setVisible(int layoutId, int visible) {
        (layout.findViewById(layoutId)).setVisibility(visible);
        return this;
    }

    public HeaderView setOnClickListener(int layoutId, OnClickListener listener) {
        (layout.findViewById(layoutId)).setOnClickListener(listener);
        return this;
    }

}
