package com.applova.charin.assessment2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applova.charin.assessment2.R;
import com.applova.charin.assessment2.model.Orders;
import com.google.android.material.card.MaterialCardView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder> {

    private final Context context;
    private List<Orders> orderList = new ArrayList<>();
    private ProductRecyclerAdapter productAdapter;

    public OrderRecyclerAdapter(Context context, List<Orders> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    public void setOrderList(List<Orders> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Orders item = orderList.get(position);

        holder.normalView.setVisibility(View.VISIBLE);
        holder.expandedView.setVisibility(View.GONE);

        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        String total = decimalFormat.format(item.getTotal());
        String subTotal = decimalFormat.format(item.getSubTotal());

        holder.orderNum.setText(String.valueOf(item.getOrderNum()));
        holder.waiter.setText(item.getWaiter());
        holder.price.setText(total);

        holder.ex_orderNum.setText(String.valueOf(item.getOrderNum()));
        holder.ex_waiter.setText(item.getWaiter());
        holder.ex_sub_total.setText(subTotal);
        holder.ex_total.setText(total);

        holder.parent.setOnClickListener(view -> {
            if (holder.normalView.getVisibility() == View.VISIBLE) {
                holder.normalView.setVisibility(View.GONE);
                holder.expandedView.setVisibility(View.VISIBLE);
            } else {
                holder.normalView.setVisibility(View.VISIBLE);
                holder.expandedView.setVisibility(View.GONE);
            }
        });

        holder.productList.setLayoutManager(new LinearLayoutManager(context));
        productAdapter = new ProductRecyclerAdapter(context, item.getProducts());
        holder.productList.setAdapter(productAdapter);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView orderNum, waiter, price;
        private final TextView ex_orderNum, ex_waiter, ex_sub_total, ex_total;
        private final RecyclerView productList;
        private final MaterialCardView parent;
        private final RelativeLayout normalView, expandedView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNum = itemView.findViewById(R.id.order_num);
            waiter = itemView.findViewById(R.id.waiter);
            price = itemView.findViewById(R.id.price);
            parent = itemView.findViewById(R.id.card_parent);
            normalView = itemView.findViewById(R.id.normal_item_view);
            expandedView = itemView.findViewById(R.id.expanded_item_view);
            ex_orderNum = itemView.findViewById(R.id.ex_order_num);
            ex_waiter = itemView.findViewById(R.id.ex_waiter);
            ex_sub_total = itemView.findViewById(R.id.ex_subtotal);
            ex_total = itemView.findViewById(R.id.ex_total);
            productList = itemView.findViewById(R.id.product_list);
        }
    }
}
