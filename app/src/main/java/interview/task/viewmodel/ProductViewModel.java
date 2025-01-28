package interview.task.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import interview.task.MyApp;
import interview.task.api.ApiService;
import interview.task.model.SlugDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductViewModel extends ViewModel {
    private final ApiService apiService;
     private final MutableLiveData<SlugDetails> slugDetailsMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final ExecutorService executor;
    private Call<SlugDetails> currentCall;
    public ProductViewModel() {

        apiService = MyApp.getApiService();
        executor = Executors.newSingleThreadExecutor();
    }

    public void loadSlugDetails(String slug) {
        isLoading.postValue(true);

        currentCall = apiService.ProductItemDetails(slug);
        currentCall.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<SlugDetails> call, Response<SlugDetails> response) {
                        if (!executor.isShutdown()){
                            executor
                                    .execute(() ->
                                    {
                                        try {
                                            if (response.isSuccessful() && response.body() != null) {
                                                slugDetailsMutableLiveData
                                                        .postValue(response.body());
                                            } else {
                                                errorLiveData.postValue("Error: " + response.code());
                                            }
                                        }
                                        catch (Exception e) {
                                            isLoading.postValue(false);
                                            errorLiveData.postValue("Unexpected error: " + e.getMessage());
                                        }
                                        finally {
                                            isLoading.postValue(false);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onFailure(Call<SlugDetails> call, Throwable t) {
                        if (!call.isCanceled()) {
                            isLoading.postValue(false);
                            errorLiveData.postValue(t.getMessage());
                        }
                    }
                });
    }

    public LiveData<SlugDetails> getSlugDetails() {
        return slugDetailsMutableLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (currentCall != null) {
            currentCall.cancel();
        }
    }
}
