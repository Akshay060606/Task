package interview.task;

import android.app.Application;

import interview.task.api.ApiService;
import interview.task.api.RetrofitService;
import interview.task.db.AppDatabase;

public class MyApp extends Application {
    private static ApiService apiService;
    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeServices();
    }

    private void initializeServices() {
        RetrofitService retrofitService = new RetrofitService();
        apiService = retrofitService.getApiService();

        database = AppDatabase.getDatabase(this);
    }

    public static ApiService getApiService() {
        return apiService;
    }

    public static AppDatabase getDatabase() {
        return database;
    }
}
