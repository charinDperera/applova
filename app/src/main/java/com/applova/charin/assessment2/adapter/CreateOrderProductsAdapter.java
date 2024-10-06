package com.applova.charin.assessment2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.applova.charin.assessment2.R;
import com.applova.charin.assessment2.model.Product;
import com.applova.charin.assessment2.view_model.ProductsViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderProductsAdapter extends RecyclerView.Adapter<CreateOrderProductsAdapter.ViewHolder> {

    private final Context context;
    private List<Product> productList = new ArrayList<>();
    private final ProductsViewModel viewModel;

    public CreateOrderProductsAdapter(Context context, List<Product> productList, ProductsViewModel viewModel) {
        this.context = context;
        this.productList = productList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public CreateOrderProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_create_view, parent, false);
        return new CreateOrderProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateOrderProductsAdapter.ViewHolder holder, int position) {
        final Product product = productList.get(position);
        holder.productName.setText(product.getProductName());
        holder.productQuantity.setText(String.valueOf(product.getQuantity()));
        holder.productPrice.setText(String.valueOf(product.getPrice()));

        holder.removeProductBtn.setOnClickListener(view -> {
            viewModel.removeProduct(product);
            productList.remove(product);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView productName, productQuantity, productPrice;
        private final MaterialButton removeProductBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            productPrice = itemView.findViewById(R.id.product_price);
            removeProductBtn = itemView.findViewById(R.id.remove_product);
        }
    }
}