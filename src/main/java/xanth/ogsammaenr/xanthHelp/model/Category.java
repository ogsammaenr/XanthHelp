package xanth.ogsammaenr.xanthHelp.model;

public class Category {
    private final String id;
    private final CategoryType type;

    public Category(String id, CategoryType type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public CategoryType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", type=" + type +
                '}';
    }
}
