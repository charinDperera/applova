package com.applova.charin.assessment2.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.applova.charin.assessment2.dto.ProductResponseDTO;
import com.applova.charin.assessment2.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    public interface ProductCallback{
        void onResponse();
        void onError(Throwable error);
    }

    private static final String TAG = OrdersRepository.class.getSimpleName();
    private final List<Product> products = new ArrayList<>();;
    private final MutableLiveData<ProductResponseDTO> productResponse = new MutableLiveData<>();

    private void updateLiveResponse() {
        ProductResponseDTO responseDTO;
        if (products.isEmpty())
            responseDTO = new ProductResponseDTO("Empty List");
        else
            responseDTO = new ProductResponseDTO(products, "Success");
        productResponse.setValue(responseDTO);
    }

    public void addProduct(Product product, ProductCallback callback) {
        this.products.add(product);
        updateLiveResponse();
        callback.onResponse();
    }

    public void removeProduct(Product product, ProductCallback callback) {
        Log.i(TAG, "Attempting to remove product: " + product.getProductName());
        if (this.products.isEmpty()) {
            callback.onError(new Throwable("Array List is empty"));
            return;
        }
        if (!this.products.remove(product)){
            callback.onError(new Throwable("No such item in list"));
            return;
        }
        updateLiveResponse();
        Log.i(TAG, "Removed product: " + product.getProductName());
        callback.onResponse();
    }

    public MutableLiveData<ProductResponseDTO> getProducts(){
        return productResponse;
    }
}
