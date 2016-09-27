package com.linyuzai.demo4tableview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.linyuzai.tableview.DragTableView;
import com.linyuzai.tableview.OnTableItemClickListener;
import com.linyuzai.tableview.OnTableItemDragListener;
import com.linyuzai.tableview.TableAdapter;
import com.linyuzai.tableview.TableView;
import com.linyuzai.tableview.TableViewHolder;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    DragTableView dragTableView;
    TableAdapter adapter;

    Button button;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dragTableView = (DragTableView) findViewById(R.id.table_view);
        //dragTableView.setDraggable(false);
        dragTableView.setOnTableItemClickListener(new OnTableItemClickListener() {
            @Override
            public void onTableItemClick(View view, int row, int col) {
                Toast.makeText(getApplicationContext(), "row:" + row + ",col:" + col, Toast.LENGTH_SHORT).show();
            }
        });
        dragTableView.setOnTableItemDragListener(new OnTableItemDragListener() {
            @Override
            public void onFromItemSelected(View view, int row, int col) {
                //Toast.makeText(getApplicationContext(), "from->row:" + row + ",col:" + col, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDragged(View toView, int toViewRow, int toViewCol) {
                if (view == null)
                    view = toView;
                if (view != toView) {
                    view.setBackgroundColor(Color.TRANSPARENT);
                    toView.setBackgroundColor(Color.parseColor("#0066ff"));
                    view = toView;
                }
                //Toast.makeText(getApplicationContext(), "selectTo->row:" + toViewRow + ",col:" + toViewCol, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onToItemSelected(View view, int row, int col) {
                view.setBackgroundColor(Color.TRANSPARENT);
                //Toast.makeText(getApplicationContext(), "to->row:" + row + ",col:" + col, Toast.LENGTH_SHORT).show();
            }
        });
        dragTableView.setAdapter(adapter = new MyAdapter());
        //dragTableView.setItemAnimator(new DefaultItemAnimator());

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dragTableView.reset(true);
            }
        });
    }

    class MyAdapter extends TableAdapter<MyAdapter.MyViewHolder> {

        @Override
        public int getRowCount() {
            return 5;
        }

        @Override
        public int getColCount() {
            return 4;
        }

        @Override
        public MyViewHolder onCreateTableViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            //View view = View.inflate(parent.getContext(), R.layout.item, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindTableViewHolder(MyViewHolder holder, final int row, final int col) {
            /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "row:" + row + ",col:" + col, Toast.LENGTH_SHORT).show();
                }
            });*/
            holder.textView.setText("row:" + row + ",col:" + col);
        }

        class MyViewHolder extends TableViewHolder {

            TextView textView;

            public MyViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.text_view);
            }
        }
    }
}
