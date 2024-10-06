package com.applova.charin.assessment2.view_model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.applova.charin.assessment2.dto.ProductResponseDTO;
import com.applova.charin.assessment2.model.Product;
import com.applova.charin.assessment2.repository.ProductRepository;

public class ProductsViewModel extends AndroidViewModel {
    private static final String TAG = ProductsViewModel.class.getSimpleName();

    private final ProductRepository productRepository;
    private final LiveData<ProductResponseDTO> getProductsResponse;

    public ProductsViewModel(@NonNull Application application) {
        super(application);
        this.productRepository = new ProductRepository();
        this.getProductsResponse = productRepository.getProducts();
    }

    public LiveData<ProductResponseDTO> getProductsResponse() {
        return getProductsResponse;
    }

    public void addProduct(Product product) {
        productRepository.addProduct(product, new ProductRepository.ProductCallback() {
            @Override
            public void onResponse() {
            }

            @Override
            public void onError(Throwable error) {
                Log.e(TAG, "Add product call failed: " + error.getMessage(), error);
            }
        });
    }

    public void removeProduct(Product product){
        productRepository.removeProduct(product, new ProductRepository.ProductCallback() {

            @Override
            public void onResponse() {
            }

            @Override
            public void onError(Throwable error) {
                Log.e(TAG, "Remove product call failed: " + error.getMessage(), error);
            }
        });
    }
}
