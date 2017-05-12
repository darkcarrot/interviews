package library.items;

/**
 * @author Z
 */
public class Item {
    /**
     * Book Id
     */
    private final int bookId;
    /**
     * Type
     */
    private final ItemType type;
    /**
     * Title
     */
    private final String title;

    public Item(int bookId, ItemType type, String title) {
        this.bookId = bookId;
        this.type = type;
        this.title = title;
    }

    public int getBookId() {
        return bookId;
    }

    public ItemType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return bookId == item.bookId && type == item.type && title.equals(item.title);
    }

    @Override
    public int hashCode() {
        int result = bookId;
        result = 31 * result + type.hashCode();
        result = 31 * result + title.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Item{" +
                "bookId=" + bookId +
                ", type=" + type +
                ", title='" + title + '\'' +
                '}';
    }
}
