package interview.task.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import interview.task.BarActivity;
import interview.task.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> categories = new ArrayList<>();
    private Context context;

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder
                .bind(category);
        holder
                .itemView
                .setOnClickListener(v -> {
                    Log.d("RecyclerView", "Item clicked: " + category.getSlug());

                    Intent intent = new Intent(context, BarActivity.class);
                    intent.putExtra("slug", category.getSlug());
                    context.startActivity(intent);
                });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    //First Call
    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<Category> newCategories) {
        categories = newCategories;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCategoryName;

        ViewHolder(View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(android.R.id.text1);
        }

        void bind(Category category) {
            tvCategoryName.setText(category.getName());
        }
    }
}
