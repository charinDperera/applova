package com.applova.charin.assessment2.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applova.charin.assessment2.R;
import com.applova.charin.assessment2.adapter.CreateOrderProductsAdapter;
import com.applova.charin.assessment2.dto.CreateOrdersRequestDTO;
import com.applova.charin.assessment2.model.Product;
import com.applova.charin.assessment2.view_model.OrdersViewModel;
import com.applova.charin.assessment2.view_model.ProductsViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateOrderActivity extends AppCompatActivity {

    private static final String TAG = CreateOrderActivity.class.getSimpleName();

    private TextInputEditText waiterNameView, productNameView, productPriceView, discountView, serviceView, taxView;
    private TextView quantityView;
    private TextInputLayout waiterLayout, discountLayout, serviceLayout, taxLayout, productNameLayout, productPriceLayout;
    private MaterialButton add, remove, addProduct, createOrder;
    private RecyclerView productListView;
    private ProgressBar progressBar;
    private MaterialCardView createLayout;

    private int quantity;
    private boolean orderValidated;
    private boolean productValidated;

    private final List<Product> productList = new ArrayList<>();
    private ProductsViewModel productsViewModel;
    private CreateOrderProductsAdapter productAdapter;

    private OrdersViewModel ordersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }

    private void init() {
        initializeViews();
        addListeners();

        quantity = 1;
        orderValidated = false;
        productValidated = false;

        InputFilter textFilter = (charSequence, i, i1, spanned, i2, i3) -> {
            for (int start = i; start < i1; start++) {
                if (!Character.isLetter(charSequence.charAt(i)) && charSequence.charAt(i) != ' ') {
                    return "";
                }
            }
            return null;
        };

        InputFilter[] filter = new InputFilter[]{textFilter};
        waiterNameView.setFilters(filter);
        productNameView.setFilters(filter);

        createLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        quantityView.setText(String.valueOf(quantity));
        ordersViewModel = new ViewModelProvider(this).get(OrdersViewModel.class);

        productListView.setLayoutManager(new LinearLayoutManager(CreateOrderActivity.this));
        productListView.setHasFixedSize(true);

        productsViewModel = new ViewModelProvider(this).get(ProductsViewModel.class);

        productAdapter = new CreateOrderProductsAdapter(CreateOrderActivity.this, productList, productsViewModel);
        productListView.setAdapter(productAdapter);

        setObservers();
    }

    private void setObservers() {
        ordersViewModel.getCreateOrderResponse().observe(this, createResponse -> {
            if (createResponse != null &&
                    createResponse.getContent() != null &&
                    createResponse.getContent().getOrderNum() > 0) {
                Intent intent = new Intent(CreateOrderActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        productsViewModel.getProductsResponse().observe(this, productResponse -> {
            if (productResponse != null && productResponse.getContent() != null && !productResponse.getContent().isEmpty()) {
                productListView.setVisibility(View.VISIBLE);
                productList.clear();
                productList.addAll(productResponse.getContent());
                productAdapter.notifyDataSetChanged();
            }
        });

        ordersViewModel.getCreateOrdersErrorResponse().observe(this, errorResponse -> {
            Map<String, String> errorMap;
            String errorMessage = "";
            String errorLog = "";

            if (errorResponse.getException() != null && errorResponse.getException().getMessage().contains("failed to connect")) {
                errorLog = "Failed to connect to server: " + errorResponse.getException().getCause();
                errorMessage = "Connection timed out";
            }else {
                if (errorResponse.getCode() > 0) {
                    // TODO Do the mapping part
                } else {
                    errorMessage = errorResponse.getMessage();
                    errorLog = errorResponse.getMessage();
                }
            }

            // Remove the loading view and come back to create view if error
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
            }
            if(createLayout.getVisibility() == View.INVISIBLE) {
                createLayout.setVisibility(View.VISIBLE);
            }

            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();

            // Used log.i() to differentiate from internal errors logs
            Log.i(TAG, errorLog, errorResponse.getException());
        });
    }

    private void initializeViews() {
        waiterNameView = findViewById(R.id.input_waiter);
        waiterLayout = findViewById(R.id.input_waiter_container);
        productNameView = findViewById(R.id.input_product_name);
        productNameLayout = findViewById(R.id.input_product_name_container);
        productPriceView = findViewById(R.id.input_product_price);
        productPriceLayout = findViewById(R.id.input_product_price_container);
        quantityView = findViewById(R.id.product_quantity);
        add = findViewById(R.id.add_btn);
        remove = findViewById(R.id.remove_btn);
        productListView = findViewById(R.id.create_product_list);
        addProduct = findViewById(R.id.add_product_btn);
        createOrder = findViewById(R.id.create_order_btn);
        discountView = findViewById(R.id.input_discount);
        discountLayout = findViewById(R.id.input_discount_container);
        serviceView = findViewById(R.id.input_service);
        serviceLayout = findViewById(R.id.input_service_container);
        taxView = findViewById(R.id.input_tax);
        taxLayout = findViewById(R.id.input_tax_container);
        progressBar = findViewById(R.id.progressBar);
        createLayout = findViewById(R.id.create_order_container);
    }

    private void setTextChangeListener(TextInputEditText textView, TextInputLayout textLayout) {
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (textLayout.getError() != null) {
                    textLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e(TAG, textView.getHint().toString());
                if (checkIfTextEmpty(textView)) {
                    textLayout.setError(getString(R.string.lbl_required));
                }
                if(productValidated){
                    if (textLayout.getError() != null) {
                        textLayout.setError(null);
                    }
                }
            }
        });
    }

    private void addListeners() {
        setTextChangeListener(waiterNameView, waiterLayout);
        setTextChangeListener(discountView, discountLayout);
        setTextChangeListener(serviceView, serviceLayout);
        setTextChangeListener(taxView, taxLayout);

        setTextChangeListener(productNameView, productNameLayout);
        setTextChangeListener(productPriceView, productPriceLayout);

        add.setOnClickListener(view -> {
            if (quantity < 100) {
                quantity++;
                quantityView.setText(String.valueOf(quantity));
            } else
                Toast.makeText(this, "Cannot add more than 99 items", Toast.LENGTH_SHORT).show();
        });

        remove.setOnClickListener(view -> {
            if (quantity > 1) {
                quantity--;
                quantityView.setText(String.valueOf(quantity));
            } else
                Toast.makeText(this, "Cannot add less than 1 items", Toast.LENGTH_SHORT).show();
        });

        addProduct.setOnClickListener(view -> {
            productValidated = false;
            validateProduct();
            if (productValidated) {
                Product product = new Product(productNameView.getText().toString(), quantity, Double.parseDouble(productPriceView.getText().toString()));
                productsViewModel.addProduct(product);
                resetTextView(productNameView);
                resetTextView(productPriceView);
                quantity = 1;
                quantityView.setText(String.valueOf(quantity));
            }
        });

        createOrder.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            createLayout.setVisibility(View.INVISIBLE);
            orderValidated = false;
            validateOrder();
            if (orderValidated) {
                CreateOrdersRequestDTO orderRequest = new CreateOrdersRequestDTO(productList,
                        waiterNameView.getText().toString(),
                        Integer.parseInt(taxView.getText().toString()),
                        Integer.parseInt(serviceView.getText().toString()),
                        Integer.parseInt(discountView.getText().toString())
                );

                ordersViewModel.addOrder(orderRequest);
            } else {
                progressBar.setVisibility(View.GONE);
                createLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void resetTextView(TextInputEditText textView) {
        textView.setText("");
        textView.clearFocus();
    }

    private void validateProduct() {
        boolean validation = true;

        if (checkIfTextEmpty(productNameView)) {
            validation = false;
            productNameLayout.setError(getString(R.string.lbl_required));
        }
        if (checkIfTextEmpty(productPriceView)) {
            validation = false;
            productPriceLayout.setError(getString(R.string.lbl_required));
        }

        productValidated = validation;
    }

    private boolean checkIfTextEmpty(TextInputEditText textField) {
        String text = textField.getText().toString();
        return text.isEmpty();
    }

    private void validateOrder() {
        boolean validation = true;
        if (checkIfTextEmpty(waiterNameView)) {
            waiterLayout.setError(getString(R.string.lbl_required));
            validation = false;
        }
        if (checkIfTextEmpty(discountView)) {
            discountLayout.setError(getString(R.string.lbl_required));
            validation = false;
        }
        if (checkIfTextEmpty(serviceView)) {
            serviceLayout.setError(getString(R.string.lbl_required));
            validation = false;
        }
        if (checkIfTextEmpty(taxView)) {
            taxLayout.setError(getString(R.string.lbl_required));
            validation = false;
        }

        if (productList.isEmpty()) {
            validation = false;
            Toast.makeText(this, "Need at least 1 product", Toast.LENGTH_SHORT).show();
        }
        orderValidated = validation;
    }
}