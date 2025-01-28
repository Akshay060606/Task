package interview.task.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {

    @PrimaryKey
    @NonNull
    private String slug;
    private String name;
    private String url;

    public Category(@NonNull String slug, String name, String url) {
        this.slug = slug;
        this.name = name;
        this.url = url;
    }

    // Getters and setters
    @NonNull
    public String getSlug() { return slug; }
    public void setSlug(@NonNull String slug) { this.slug = slug; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
