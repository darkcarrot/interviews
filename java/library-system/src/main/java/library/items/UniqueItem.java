package library.items;

/**
 * @author Z
 */
public class UniqueItem {

    /**
     * The generic item
     */
    private final Item item;
    /**
     * Unique Id
     */
    private final int uniqueId;

    public UniqueItem(int uniqueId, Item item) {
        this.item = item;
        this.uniqueId = uniqueId;
    }

    /**
     * @return uniqueId
     */
    public int getUniqueId() {
        return uniqueId;
    }

    public Item getItem() {
        return item;
    }

    /**
     * @return book id of of {@link Item}
     */
    public int getBookId() {
        return item.getBookId();
    }

    /**
     * @return type of {@link Item}
     */
    public ItemType getType() {
        return item.getType();
    }

    /**
     * @return title of {@link Item}
     */
    public String getTitle() {
        return item.getTitle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UniqueItem that = (UniqueItem) o;

        return uniqueId == that.uniqueId && (item != null ? item.equals(that.item) : that.item == null);
    }

    @Override
    public int hashCode() {
        int result = item != null ? item.hashCode() : 0;
        result = 31 * result + uniqueId;
        return result;
    }

    @Override
    public String toString() {
        return "UniqueItem{" +
                "uniqueId=" + uniqueId +
                ", item=" + item +
                '}';
    }
}
