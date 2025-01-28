package interview.task;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import interview.task.adapter.CategoryAdapter;
import interview.task.databinding.ActivityMainBinding;
import interview.task.viewmodel.CategoryViewModel;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private CategoryViewModel viewModel;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        setupRecyclerView();
        setupObservers();
        loadData();
    }

    private void setupRecyclerView() {
        adapter = new CategoryAdapter(this);

        binding
                .categoryList
                .setLayoutManager(new LinearLayoutManager(this));
        binding
                .categoryList
                .setAdapter(adapter);
    }

    //Observing ViewModel Data
    private void setupObservers() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                binding
                        .progressBar
                        .setVisibility(isLoading ? View.VISIBLE : View.GONE);
                binding.
                        categoryList
                        .setVisibility(isLoading ? View.GONE : View.VISIBLE);
            }
        });

        //first list data setting
        viewModel.getCategories().observe(this, categories -> {
            if (categories != null && !categories.isEmpty()) {
                adapter
                        .submitList(categories);
            }
        });

        viewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast
                        .makeText(this, errorMessage, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    //Initial load
    private void loadData() {
        viewModel
                .loadCategories();
    }
}