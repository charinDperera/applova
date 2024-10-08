package com.applova.charin.assessment2.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.applova.charin.assessment2.R;
import com.applova.charin.assessment2.dto.ProductErrorDTO;
import com.applova.charin.assessment2.model.Product;
import com.applova.charin.assessment2.view.CreateOrderActivity;
import com.applova.charin.assessment2.view_model.ProductsViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderProductsAdapter extends RecyclerView.Adapter<CreateOrderProductsAdapter.ViewHolder> {

    private final Context context;
    private final LifecycleOwner owner;
    private List<Product> productList = new ArrayList<>();
    private final ProductsViewModel viewModel;

    public CreateOrderProductsAdapter(LifecycleOwner owner, Context context, List<Product> productList, ProductsViewModel viewModel) {
        this.owner = owner;
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

        holder.errorListLiveData.observe(owner, errorResponse -> {
            for (ProductErrorDTO error : errorResponse) {
                if (error.getPosition() == position) {
                    switch (error.getKey()) {
                        case "productName":
                            setError(holder.nameLayout, holder.productName);
                            holder.nameError.setText("*" + error.getMessage());
                            holder.nameError.setVisibility(View.VISIBLE);
                            break;
                        case "price":
                            setError(holder.priceLayout, holder.productPrice);
                            holder.priceError.setText("*" + error.getMessage());
                            holder.priceError.setVisibility(View.VISIBLE);
                            break;
                        case "quantity":
                            setError(holder.quantityLayout, holder.productQuantity);
                            holder.quantityError.setText("*" + error.getMessage());
                            holder.quantityError.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
        });
    }

    private void setError(MaterialCardView layout, TextView textView) {
        layout.setStrokeColor(ContextCompat.getColor(context, R.color.md_theme_error));
        textView.setTextColor(ContextCompat.getColor(context, R.color.md_theme_error));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView productName, productQuantity, productPrice, nameError, quantityError, priceError;
        private final MaterialButton removeProductBtn;
        private final MaterialCardView nameLayout, priceLayout, quantityLayout;

        private final LiveData<List<ProductErrorDTO>> errorListLiveData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            productPrice = itemView.findViewById(R.id.product_price);
            removeProductBtn = itemView.findViewById(R.id.remove_product);
            errorListLiveData = CreateOrderActivity.getProductErrorLiveData();
            nameLayout = itemView.findViewById(R.id.product_name_container);
            priceLayout = itemView.findViewById(R.id.product_price_container);
            quantityLayout = itemView.findViewById(R.id.product_quantity_container);
            nameError = itemView.findViewById(R.id.product_name_error);
            quantityError = itemView.findViewById(R.id.product_quantity_error);
            priceError = itemView.findViewById(R.id.product_price_error);
        }
    }
}