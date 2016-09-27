package com.linyuzai.tableview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by Administrator on 2016/9/26 0026.
 */

public class DragTableView extends RelativeLayout {

    private SparseIntArray map = new SparseIntArray();
    private ActionStack actionStack = new ActionStack();

    private TableViewHolder viewHolder;
    private View overView;

    private View fromItem;
    private View toItem;
    private int fromItemPosition;
    private int toItemPosition;

    private float downX;
    private float downY;

    private float top;
    private float left;

    private boolean canDrag = true;
    private boolean isMove;

    private TableView tableView;
    private OnTableItemDragListener dragListener;
    private OnTableItemClickListener clickListener;

    private boolean isDraggable = true;

    public DragTableView(Context context) {
        super(context);
        init(context, null);
    }

    public DragTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DragTableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DragTableView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setClickable(true);
        tableView = new TableView(context);
        addView(tableView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public boolean isDraggable() {
        return isDraggable;
    }

    public void setDraggable(boolean draggable) {
        isDraggable = draggable;
    }

    public TableAdapter getAdapter() {
        return tableView.getAdapter();
    }

    public void setAdapter(TableAdapter adapter) {
        if (clickListener != null)
            adapter.listener = clickListener;
        tableView.setAdapter(adapter);
        resetDataMap();
    }

    public OnTableItemDragListener getOnTableItemDragListener() {
        return dragListener;
    }

    public void setOnTableItemDragListener(OnTableItemDragListener dragListener) {
        this.dragListener = dragListener;
    }

    public OnTableItemClickListener getOnTableItemClickListener() {
        return clickListener;
    }

    public void setOnTableItemClickListener(OnTableItemClickListener clickListener) {
        this.clickListener = clickListener;
        if (getAdapter() != null) {
            getAdapter().listener = clickListener;
            getAdapter().notifyItemRangeChanged(0, getAdapter().getItemCount());
        }
    }

    public TableView getTableView() {
        return tableView;
    }

    public void setItemAnimator(RecyclerView.ItemAnimator itemAnimator) {
        tableView.setItemAnimator(itemAnimator);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        tableView.addItemDecoration(itemDecoration);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index) {
        tableView.addItemDecoration(itemDecoration, index);
    }

    public void moveItem(int fromRow, int fromCol, int toRow, int toCol) {
        moveItem(fromRow * getAdapter().getColCount() + fromCol, toRow * getAdapter().getColCount() + toCol, false, false);
    }

    private void moveItem(int fromPosition, int toPosition, boolean isReset, boolean isTouch) {
        if (getAdapter().getItemViewType(fromPosition) != getAdapter().getItemViewType(toPosition))
            return;
        if (isTouch && (Math.abs(fromItem.getWidth() - toItem.getWidth()) > 1f || Math.abs(fromItem.getHeight() - toItem.getHeight()) > 1f))
            return;
        //Log.e("moveItem", "moveItem");
        if (fromPosition != toPosition) {
            int to = map.get(toPosition);
            int from = map.get(fromPosition);
            map.put(fromPosition, to);
            map.put(toPosition, from);
            if (!isReset)
                actionStack.push(fromPosition, toPosition);
            getAdapter().notifyItemMoved(fromPosition, toPosition);
            if (fromPosition < toPosition)
                getAdapter().notifyItemMoved(toPosition - 1, fromPosition);
            else if (fromPosition > toPosition)
                getAdapter().notifyItemMoved(toPosition + 1, fromPosition);
        }
    }

    private void resetDataMap() {
        for (int position = 0; position < getAdapter().getItemCount(); position++)
            map.put(position, position);
    }

    public void reset(boolean animation) {
        if (animation) {
            while (!actionStack.isEmpty()) {
                ActionStack.Action action = actionStack.pop();
                moveItem(action.toPosition, action.fromPosition, true, false);
            }
        } else {
            getAdapter().notifyItemRangeChanged(0, getAdapter().getItemCount());
            resetDataMap();
            actionStack.clear();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (isDraggable)
            return true;
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!canDrag)
            return super.onTouchEvent(event);
        canDrag = true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.e("ACTION_DOWN", "ACTION_DOWN");
                isMove = false;
                downX = event.getX();
                downY = event.getY();
                fromItem = tableView.findChildViewUnder(downX, downY);
                if (fromItem == null)
                    return super.onTouchEvent(event);
                left = fromItem.getLeft();
                top = fromItem.getTop();
                fromItemPosition = tableView.getChildAdapterPosition(fromItem);
                //Log.e("ACTION_DOWN", "fromItemPosition:" + fromItemPosition);
                toItemPosition = fromItemPosition;
                int row = fromItemPosition / getAdapter().getColCount();
                int col = fromItemPosition % getAdapter().getColCount();
                canDrag = getAdapter().isDraggable(row, col);
                if (canDrag) {
                    if (dragListener != null)
                        dragListener.onFromItemSelected(fromItem, row, col);
                    viewHolder = getAdapter().onCreateTableViewHolder(tableView, getAdapter().getItemViewType(fromItemPosition));
                    overView = viewHolder.itemView;
                    getAdapter().onBindViewHolder(viewHolder, map.get(fromItemPosition));
                    overView.setX(left);// + getPaddingLeft());
                    overView.setY(top);// + getPaddingTop());
                    addView(overView, new ViewGroup.LayoutParams(fromItem.getWidth(), fromItem.getHeight()));
                } else
                    return super.onTouchEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.e("ACTION_MOVE", "ACTION_MOVE");
                if (Math.abs(event.getX() - downX) > 5f || Math.abs(event.getY() - downY) > 5f)
                    isMove = true;
                //fromItem.layout((int) e.getX(), (int) e.getY(), 0, 0);
                toItem = tableView.findChildViewUnder(event.getX(), event.getY());
                toItemPosition = tableView.getChildAdapterPosition(toItem);
                //Log.e("ACTION_MOVE", "from:" + fromItemPosition + ",to:" + toItemPosition);
                fromItem.setVisibility(INVISIBLE);

                row = toItemPosition / getAdapter().getColCount();
                col = toItemPosition % getAdapter().getColCount();
                if (dragListener != null)
                    dragListener.onItemDragged(toItem, row, col);
                if (overView != null) {
                    float offsetX = left + event.getX() - downX + getPaddingLeft();
                    float offsetY = top + event.getY() - downY + getPaddingTop();
                    if (offsetX < getPaddingLeft())
                        offsetX = getPaddingLeft();
                    if (offsetX > getWidth() - getPaddingRight() - overView.getWidth())
                        offsetX = getWidth() - getPaddingRight() - overView.getWidth();
                    if (offsetY < getPaddingTop())
                        offsetY = getPaddingTop();
                    if (offsetY > getHeight() - getPaddingBottom() - overView.getHeight())
                        offsetY = getHeight() - getPaddingBottom() - overView.getHeight();
                    overView.setX(offsetX);
                    overView.setY(offsetY);
                }
                break;
            case MotionEvent.ACTION_UP:
                //Log.e("ACTION_UP", "ACTION_UP");
                if (!isMove && fromItem != null)
                    fromItem.performClick();
                if (overView != null)
                    removeView(overView);
                if (fromItem != null)
                    fromItem.setVisibility(VISIBLE);
                //toItem = tableView.findChildViewUnder(event.getX(), event.getY());
                //toItemPosition = tableView.getChildAdapterPosition(toItem);
                //Log.e("ACTION_UP", "from:" + fromItemPosition + ",to:" + toItemPosition);
                row = toItemPosition / getAdapter().getColCount();
                col = toItemPosition % getAdapter().getColCount();
                if (toItemPosition >= 0 && getAdapter().isDraggable(row, col)) {
                    if (dragListener != null)
                        dragListener.onToItemSelected(toItem, row, col);
                    moveItem(fromItemPosition, toItemPosition, false, true);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                //Log.e("ACTION_CANCEL", "ACTION_CANCEL");
                removeView(overView);
                break;
        }
        if (isMove)
            return true;
        return super.onTouchEvent(event);
    }
}
