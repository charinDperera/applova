package com.applova.charin.assessment2.network;

import com.applova.charin.assessment2.dto.CreateOrdersRequestDTO;
import com.applova.charin.assessment2.dto.GetOrdersResponseDTO;
import com.applova.charin.assessment2.model.Orders;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiRequest {
    @GET("api/orders")
    Call<GetOrdersResponseDTO> getOrders(@Query("page") int pageNum, @Query("size") int pageSize);

    @POST("api/orders")
    Call<Orders> createOrders(@Body CreateOrdersRequestDTO createOrders);
}
