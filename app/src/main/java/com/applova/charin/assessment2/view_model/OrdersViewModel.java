package com.applova.charin.assessment2.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.applova.charin.assessment2.dto.CreateOrdersRequestDTO;
import com.applova.charin.assessment2.dto.CreateOrdersResponseDTO;
import com.applova.charin.assessment2.dto.ErrorDTO;
import com.applova.charin.assessment2.dto.GetOrdersResponseDTO;
import com.applova.charin.assessment2.repository.OrdersRepository;


public class OrdersViewModel extends AndroidViewModel {

    private static final String TAG = OrdersViewModel.class.getSimpleName();

    private final OrdersRepository ordersRepository;
    private final LiveData<GetOrdersResponseDTO> getOrderResponse;
    private final LiveData<CreateOrdersResponseDTO> createOrderResponse;
    private final LiveData<ErrorDTO> getOrdersErrorResponse;
    private final LiveData<ErrorDTO> createOrdersErrorResponse;

    public OrdersViewModel(@NonNull Application application) {
        super(application);
        this.ordersRepository = new OrdersRepository();
        this.getOrderResponse = ordersRepository.getGetOrderResponse();
        this.createOrderResponse = ordersRepository.getCreateOrderResponse();
        this.getOrdersErrorResponse =ordersRepository.getGetOrdersErrorResponse();
        this.createOrdersErrorResponse = ordersRepository.getCreateOrdersErrorResponse();
    }

    public LiveData<GetOrdersResponseDTO> getGetOrderResponse() {
        return getOrderResponse;
    }

    public LiveData<CreateOrdersResponseDTO> getCreateOrderResponse() {
        return createOrderResponse;
    }

    public LiveData<ErrorDTO> getGetOrdersErrorResponse() {
        return getOrdersErrorResponse;
    }

    public LiveData<ErrorDTO> getCreateOrdersErrorResponse() {
        return createOrdersErrorResponse;
    }

    public void requestOrdersPage(int pageNum){
        ordersRepository.getOrdersPage(pageNum);
    }

    public void addOrder(CreateOrdersRequestDTO order) {
        ordersRepository.addOrder(order);
    }
}
