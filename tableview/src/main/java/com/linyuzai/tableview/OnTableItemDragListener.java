package com.linyuzai.tableview;

import android.view.View;

/**
 * Created by Administrator on 2016/9/26 0026.
 */

public interface OnTableItemDragListener {
    void onFromItemSelected(View view, int row, int col);

    void onItemDragged(View toView, int toViewRow, int toViewCol);

    void onToItemSelected(View view, int row, int col);
}
