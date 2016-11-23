package com.felix.adjustablelayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by lufan on 2016/11/22.
 */

public class TestRvAdapter extends RecyclerView.Adapter<TestRvAdapter.TestViewHolder> {

    private Context context;

    public TestRvAdapter(Context context){
        this.context = context;
    }

    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tv_test,parent,false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TestViewHolder holder, int position) {
        holder.tv_test.setText("测试数据" + position);
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    class TestViewHolder extends RecyclerView.ViewHolder {

        TextView tv_test;

        TestViewHolder(View itemView) {
            super(itemView);
            tv_test = (TextView) itemView.findViewById(R.id.tv_test);
        }
    }

}
