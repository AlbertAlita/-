package com.taian.floatingballmatrix.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by baotaian on 2016/8/3.
 */
public class RecyclerViewDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private boolean leftSpace;
    private boolean bottomSpace;
    private boolean rightSpace;
    private boolean topSpace;

    public RecyclerViewDecoration(int space) {
        this.space = space;
    }


    public void setLeftSpace(boolean leftSpace) {
        this.leftSpace = leftSpace;
    }

    public void setBottomSpace(boolean bottomSpace) {
        this.bottomSpace = bottomSpace;
    }

    public void setRightSpace(boolean rightSpace) {
        this.rightSpace = rightSpace;
    }

    public void setTopSpace(boolean topSpace) {
        this.topSpace = topSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        outRect.left = space;
//        outRect.right = space;
//        outRect.bottom = space;
//        if (parent.getChildPosition(view) != 0)
//        outRect.right = space;
        if (leftSpace)
            outRect.left = space;
        if (bottomSpace)
            outRect.bottom = space;
        if (rightSpace)
            outRect.right = space;
        if (topSpace)
            outRect.top = space;
    }
}
