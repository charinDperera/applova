package com.applova.charin.assessment2.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.applova.charin.assessment2.R;
import com.applova.charin.assessment2.adapter.OrderRecyclerAdapter;
import com.applova.charin.assessment2.model.Orders;
import com.applova.charin.assessment2.view_model.OrdersViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView orderListView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshOrdersLayout;
    private ConstraintLayout paginationLayout;
    private FloatingActionButton createOrderButton;
    private SearchView searchView;
    private TextView pageNumView;
    private MaterialButton previousPage, nextPage;

    private final List<Orders> orderList = new ArrayList<>();
    private OrdersViewModel ordersViewModel;
    private OrderRecyclerAdapter orderAdapter;

    private int pageNum, totalPages;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }

    private void addListeners() {
        createOrderButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreateOrderActivity.class);
            startActivity(intent);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        previousPage.setOnClickListener(view -> {
            if (pageNum > 0) {
                pageNum--;
                pageChanged();
            } else
                Toast.makeText(this, "End of pages", Toast.LENGTH_SHORT).show();
        });

        nextPage.setOnClickListener(view -> {
            if (pageNum < totalPages - 1) {
                pageNum++;
                pageChanged();
            } else
                Toast.makeText(this, "End of pages", Toast.LENGTH_SHORT).show();
        });

        refreshOrdersLayout.setOnRefreshListener(this);
    }

    private void init() {
        initializeViews();
        addListeners();

        paginationLayout.setVisibility(View.GONE);
        searchView.clearFocus();

        orderListView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        orderListView.setHasFixedSize(true);

        orderAdapter = new OrderRecyclerAdapter(MainActivity.this, orderList);
        orderListView.setAdapter(orderAdapter);

        ordersViewModel = new ViewModelProvider(this).get(OrdersViewModel.class);
        onRefresh();
        setObservers();
    }

    private void setObservers() {
        ordersViewModel.getGetOrderResponse().observe(this, ordersResponse -> {
            if (ordersResponse != null &&
                    ordersResponse.getContent() != null &&
                    !ordersResponse.getContent().isEmpty()) {
                Log.i(TAG, "" + ordersResponse);
                totalPages = ordersResponse.getTotalPages();
                if (totalPages > 1) {
                    String page = pageNum + 1 + "/" + totalPages;
                    pageNumView.setText(page);
                    paginationLayout.setVisibility(View.VISIBLE);
                } else
                    paginationLayout.setVisibility(View.GONE);

                orderList.clear(); // Remember
                orderList.addAll(ordersResponse.getContent());
                orderAdapter.notifyDataSetChanged();
            }
            progressBar.setVisibility(View.GONE);
            refreshOrdersLayout.setRefreshing(false);
            isLoading = false;
        });

        ordersViewModel.getGetOrdersErrorResponse().observe(this, errorResponse -> {
            String errorMessage = "";
            String errorLog = "";
            if (errorResponse.getException() != null && errorResponse.getException().getMessage().contains("failed to connect")) {
                errorLog = "Failed to connect to server: " + errorResponse.getException().getCause();
                errorMessage = "Connection timed out";
            } else {
                if (errorResponse.getCode() > 0) {
                    errorMessage = errorResponse.getMessage() + ": " + errorResponse.getCode();
                    errorLog = errorResponse.getMessage() + ": " + errorResponse.getCode();
                } else {
                    errorMessage = errorResponse.getMessage();
                    errorLog = errorResponse.getMessage();
                }
            }

            // Remove the loading view and refresh view if error
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
            }
            if (refreshOrdersLayout.isRefreshing()) {
                refreshOrdersLayout.setRefreshing(false);
            }

            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();

            // Used log.i() to differentiate from internal errors logs
            Log.i(TAG, errorLog, errorResponse.getException());
        });
    }

    private void initializeViews() {
        progressBar = findViewById(R.id.progressBar);
        orderListView = findViewById(R.id.orderListView);
        refreshOrdersLayout = findViewById(R.id.refreshOrdersLayout);
        createOrderButton = findViewById(R.id.fab);
        searchView = findViewById(R.id.search_view);
        paginationLayout = findViewById(R.id.pagination_button_container);
        pageNumView = findViewById(R.id.page_number);
        previousPage = findViewById(R.id.btn_previous_page);
        nextPage = findViewById(R.id.btn_next_page);
    }

    private void filterList(String newText) {
        List<Orders> filteredList = new ArrayList<>();
        for (Orders order : orderList) {
            if ((String.valueOf(order.getOrderNum()).toLowerCase().contains(newText.toLowerCase())) ||
                    (String.valueOf(order.getWaiter()).toLowerCase().contains(newText.toLowerCase())) ||
                    (String.valueOf(order.getTotal()).toLowerCase().contains(newText.toLowerCase()))
            ) {
                filteredList.add(order);
            }
        }
        if (!filteredList.isEmpty()) {
            orderAdapter.setOrderList(filteredList);
        }
    }

    private void pageChanged() {
        String page = pageNum + 1 + "/" + totalPages;
        pageNumView.setText(page);
        ordersViewModel.requestOrdersPage(pageNum);
    }

    @Override
    public void onRefresh() {
        if(isLoading) return;

        isLoading = true;
        orderList.clear();
        ordersViewModel.requestOrdersPage(pageNum);
    }
}