package com.linyuzai.tableview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/9/26 0026.
 */

public abstract class TableAdapter<VH extends TableViewHolder> extends RecyclerView.Adapter<TableViewHolder> implements ITable<VH>, IDrag {

    OnTableItemClickListener listener;

    @Override
    public int getItemViewType(int position) {
        return getTableItemViewType(position / getItemCount(), position % getItemCount());
    }

    @Override
    public int getTableItemViewType(int row, int col) {
        return 0;
    }

    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateTableViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {
        int row = position / getColCount();
        int col = position % getColCount();
        onBindTableViewHolder((VH) holder, row, col);
        holder.row = row;
        holder.col = col;
        holder.listener = listener;
    }

    @Override
    public int getItemCount() {
        return getColCount() * getRowCount();
    }

    @Override
    public boolean isDraggable(int row, int col) {
        return true;
    }
}
