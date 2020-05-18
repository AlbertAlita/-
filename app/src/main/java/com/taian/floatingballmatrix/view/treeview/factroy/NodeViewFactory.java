package com.taian.floatingballmatrix.view.treeview.factroy;

import android.view.View;

import com.taian.floatingballmatrix.view.treeview.base.BaseNodeViewBinder;
import com.taian.floatingballmatrix.view.treeview.base.BaseNodeViewFactory;
import com.taian.floatingballmatrix.view.treeview.binder.FirstLevelNodeViewBinder;
import com.taian.floatingballmatrix.view.treeview.binder.SecondLevelNodeViewBinder;

public class NodeViewFactory extends BaseNodeViewFactory {

    //可以有无数级，只要继续setlevel等级
    @Override
    public BaseNodeViewBinder getNodeViewBinder(View view, int level) {

        switch (level) {
            case 0:
                FirstLevelNodeViewBinder firstLevelNodeViewBinder = new FirstLevelNodeViewBinder(view);
                return firstLevelNodeViewBinder;
            case 1:
                SecondLevelNodeViewBinder secondLevelNodeViewBinder = new SecondLevelNodeViewBinder(view);
                return secondLevelNodeViewBinder;
            default:
                return null;
        }
    }
}
