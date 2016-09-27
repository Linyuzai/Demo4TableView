package com.linyuzai.tableview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/9/26 0026.
 */

public class TableView extends RecyclerView {

    public TableView(Context context) {
        super(context);
    }

    public TableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TableView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Deprecated
    @Override
    public void setAdapter(Adapter adapter) {
        throw new RuntimeException("");
    }

    @Override
    public TableAdapter getAdapter() {
        return (TableAdapter) super.getAdapter();
    }

    public void setAdapter(TableAdapter adapter) {
        super.setAdapter(adapter);
        setLayoutManager(new GridLayoutManager(getContext(), adapter.getColCount()));
    }
}
