package interview.task.model;

import java.util.List;

public class SlugDetails {
    public SlugDetails(List<interview.task.model.products> products) {
        this.products = products;
    }

    private List<products> products;

    public List<interview.task.model.products> getProducts() {
        return products;
    }

    public void setProducts(List<interview.task.model.products> products) {
        this.products = products;
    }
}
