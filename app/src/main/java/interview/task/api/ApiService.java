package interview.task.api;

import java.util.List;

import interview.task.model.Category;
import interview.task.model.SlugDetails;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("products/categories")
    Call<List<Category>> categoryList();

    @GET ("products/category/{slug}")
    Call<SlugDetails> ProductItemDetails(@Path("slug") String slug);
}