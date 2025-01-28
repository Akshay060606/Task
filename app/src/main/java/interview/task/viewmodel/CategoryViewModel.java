package interview.task.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import interview.task.MyApp;
import interview.task.api.ApiService;
import interview.task.db.AppDatabase;
import interview.task.db.CategoryDao;
import interview.task.model.Category;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryViewModel extends ViewModel {
    private final ApiService apiService;
    private final LiveData<List<Category>> categoriesLiveData;
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(); //Loading Spinner
    private final CategoryDao categoryDao;
    private final ExecutorService executor;

    public CategoryViewModel() {
        apiService = MyApp.getApiService();

        AppDatabase database = MyApp.getDatabase();
        categoryDao = database.categoryDao();

        categoriesLiveData = categoryDao.getAllCategories(); //get Data from DB if it has any

        executor = Executors.newSingleThreadExecutor();
    }

    public void loadCategories() {
        isLoading.postValue(true);

        executor.execute(() -> {
            if(categoryDao.getCount() > 0) {
                isLoading.postValue(false);
            }
        });

        apiService.categoryList().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                executor
                        .execute(() ->
                        {
                            try {
                                if (response.isSuccessful() && response.body() != null) {
                                    categoryDao.deleteAll();
                                    categoryDao.insertAll(response.body());
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

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                errorLiveData.postValue(t.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public LiveData<List<Category>> getCategories() {
        return categoriesLiveData;
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
        executor.shutdown();
    }
}
