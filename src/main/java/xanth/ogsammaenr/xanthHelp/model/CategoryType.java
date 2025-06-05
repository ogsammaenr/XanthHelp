package xanth.ogsammaenr.xanthHelp.model;

public class CategoryType {
    private final String id;
    private final String permission;

    public CategoryType(String id, String permission) {
        this.id = id;
        this.permission = permission;
    }

    public String getId() {
        return id;
    }

    public String getPermission() {
        return permission;
    }

    @Override
    public String toString() {
        return "CategoryType{" +
                "id='" + id + '\'' +
                ", permission='" + permission + '\'' +
                '}';
    }
}
