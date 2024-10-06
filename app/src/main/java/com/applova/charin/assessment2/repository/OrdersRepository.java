package com.applova.charin.assessment2.repository;

import static com.applova.charin.assessment2.constants.AppConstants.PAGE_SIZE;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.applova.charin.assessment2.dto.CreateOrdersRequestDTO;
import com.applova.charin.assessment2.dto.CreateOrdersResponseDTO;
import com.applova.charin.assessment2.dto.ErrorDTO;
import com.applova.charin.assessment2.dto.GetOrdersResponseDTO;
import com.applova.charin.assessment2.model.Orders;
import com.applova.charin.assessment2.network.ApiRequest;
import com.applova.charin.assessment2.network.RetrofitRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersRepository {

    private static final String TAG = OrdersRepository.class.getSimpleName();
    private final ApiRequest apiRequest;
    private final MutableLiveData<GetOrdersResponseDTO> getOrdersResponse = new MutableLiveData<>();
    private final MutableLiveData<CreateOrdersResponseDTO> createOrdersResponse = new MutableLiveData<>();
    private final MutableLiveData<ErrorDTO> getOrdersErrorResponse = new MutableLiveData<>();
    private final MutableLiveData<ErrorDTO> createOrdersErrorResponse = new MutableLiveData<>();

    private int pageNum;

    public OrdersRepository() {
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
    }

    public void getOrdersPage(int pageNum) {
        this.pageNum = pageNum;

        apiRequest.getOrders(pageNum, PAGE_SIZE).enqueue(new Callback<GetOrdersResponseDTO>() {
            @Override
            public void onResponse(Call<GetOrdersResponseDTO> call, Response<GetOrdersResponseDTO> response) {
                if (response.body() != null) {
                    Log.i(TAG + "-get", "API call " + response.body() + "\n Page num " + pageNum);
                    getOrdersResponse.setValue(response.body());
                } else {
                    ErrorDTO error;
                    if (response.code() == 204) {
                        error = new ErrorDTO("No orders made", response.code());
                    } else {
                        error = new ErrorDTO("API returned error", response.code());
                    }
                    getOrdersErrorResponse.setValue(error);
                }
            }

            @Override
            public void onFailure(Call<GetOrdersResponseDTO> call, Throwable throwable) {
                getOrdersErrorResponse.setValue(new ErrorDTO("Api call failed", throwable));
                getOrdersResponse.setValue(null);
            }
        });
    }

    public void addOrder(CreateOrdersRequestDTO ordersRequest) {
        apiRequest.createOrders(ordersRequest).enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {
                if (response.body() != null) {
                    Log.i(TAG + "-add", "API call " + response.body());
                    createOrdersResponse.setValue(new CreateOrdersResponseDTO(response.body(), "Success"));
                    getOrdersPage(pageNum);
                } else {
                    ErrorDTO error = new ErrorDTO("API call failed", response.code());

                    // The API will return a list of all errors in JSON format, the following is to make a map out of it
                    if(response.code() == 400){

                        // Try with clause to get the error response body with no warning according to Android Studio
                        try (ResponseBody errorBody = response.errorBody()){
                            if (errorBody != null){
                                String errorBodyValue = errorBody.string();
                                Gson gson = new Gson();

                                // Map the json response to a response that could be used here
                                Map<String, String> errorMap = gson.fromJson(errorBodyValue, new TypeToken<Map<String, String>>(){}.getType());

                                error = new ErrorDTO(response.code(), errorMap);
                            }
                        } catch (IOException exception) {
                            error = new ErrorDTO("Error parsing data", exception);
                        }
                    }
                    else {
                        error = new ErrorDTO("API returned error", response.code());
                    }
                    Log.e(TAG, error.toString());
                    createOrdersErrorResponse.setValue(error);
                }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable throwable) {
                createOrdersErrorResponse.setValue(new ErrorDTO("Api call failed", throwable));
                createOrdersResponse.setValue(new CreateOrdersResponseDTO(null, "API call failed: " + throwable.getMessage()));
            }
        });
    }

    @NonNull
    public LiveData<ErrorDTO> getGetOrdersErrorResponse() {
        return getOrdersErrorResponse;
    }

    @NonNull
    public LiveData<GetOrdersResponseDTO> getGetOrderResponse() {
        return getOrdersResponse;
    }

    @NonNull
    public LiveData<CreateOrdersResponseDTO> getCreateOrderResponse() {
        return createOrdersResponse;
    }

    @NonNull
    public LiveData<ErrorDTO> getCreateOrdersErrorResponse() {
        return createOrdersErrorResponse;
    }
}
