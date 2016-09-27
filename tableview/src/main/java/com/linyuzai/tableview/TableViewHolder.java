package com.linyuzai.tableview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2016/9/26 0026.
 */

public class TableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    OnTableItemClickListener listener;

    int row;
    int col;

    public TableViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.onTableItemClick(v, row, col);
    }
}
