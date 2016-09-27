package com.linyuzai.tableview;

import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/9/26 0026.
 */

public interface ITable<VH extends TableViewHolder> {
    int getRowCount();

    int getColCount();

    int getTableItemViewType(int row, int col);

    VH onCreateTableViewHolder(ViewGroup parent, int viewType);

    void onBindTableViewHolder(VH holder, int row, int col);
}
