package interview.task;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

import interview.task.databinding.ActivityBarBinding;
import interview.task.model.SlugDetails;
import interview.task.model.products;
import interview.task.viewmodel.ProductViewModel;

public class BarActivity extends AppCompatActivity {

    private ActivityBarBinding binding;
    private ProductViewModel productViewModel;
    BarData barData;
    Description description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String slug = getIntent().getStringExtra("slug");

        binding
                .topAppBar
                .setTitle(slug);
        binding
                .topAppBar
                .setNavigationOnClickListener(v -> finish());

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        description = new Description();
        description.setText("Brand Price Comparison");

        loadData(slug);
        setupObservers();
    }

    private void setupObservers() {

        productViewModel.getSlugDetails().observe(this, slugDetails -> {
            if (slugDetails != null) {
                barData = createStackedBarChart(slugDetails);
                binding
                        .stackedBarChart
                        .setData(
                                barData
                        );
                binding
                        .stackedBarChart
                        .invalidate();
                binding
                        .stackedBarChart
                        .setDescription(description);

            }
        });

        productViewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast
                        .makeText(this, errorMessage, Toast.LENGTH_SHORT)
                        .show();
            }
        });

        productViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                binding
                        .progressBar
                        .setVisibility(isLoading ? View.VISIBLE : View.GONE);
                binding
                        .stackedBarChart
                        .setVisibility(isLoading ? View.GONE : View.VISIBLE);
            }
        });
    }
    private void loadData(String slug) {
        productViewModel
                .loadSlugDetails(slug);
    }

    private BarData createStackedBarChart(SlugDetails slugDetails) {
        List<BarEntry> entries = new ArrayList<>();

        int counter = 0; //looping over to get each item data
        for (products product : slugDetails.getProducts()) {
            float price = (float) product.getPrice();
            float discountPercentage = (float) product.getDiscountPercentage();

            float discountPrice = price * (discountPercentage / 100); //discountPrice Calculation
            price = price - discountPrice; //setting final price after discount

            entries.add(new BarEntry(counter, new float[]{price, discountPrice}));
            counter++;
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Prices");
        barDataSet
                .setColors(
                        ContextCompat.getColor(this, R.color.price),
                        ContextCompat.getColor(this, R.color.discount)
                );
        barDataSet
                .setStackLabels(new String[]{"Price", "Discount"});

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets
                .add(
                        barDataSet
                );

        return new BarData(dataSets);
    }
}