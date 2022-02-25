package dtos;

public class ChuckDTO {
    String created_at;
    String icon_url;
    String id;
    String updated_at;
    String url;
    String value;

    public ChuckDTO() {
    }

    @Override
    public String toString() {
        return "ChuckDTO{" +
                "created_at='" + created_at + '\'' +
                ", icon_url='" + icon_url + '\'' +
                ", id='" + id + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", url='" + url + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String getValue() {
        return value;
    }
}
