package library.items;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Z
 */
public class UniqueItemTest {

    private final static Item item1 = new Item(13, ItemType.BOOK, "Hello World");
    private final static Item item2 = new Item(17, ItemType.DVD, "Bye");

    private UniqueItem uniqueItem;

    @Before
    public void setUp() {
        uniqueItem = new UniqueItem(1, item1);
    }

    @Test
    public void testGetItem() throws Exception {
        assertEquals(uniqueItem.getItem(), item1);
    }

    @Test
    public void testGetUniqueId() throws Exception {
        assertEquals(uniqueItem.getUniqueId(), 1);
    }


    @Test
    public void testGetBookId() throws Exception {
        assertEquals(13, uniqueItem.getBookId());
    }

    @Test
    public void testGetType() throws Exception {
        assertEquals(ItemType.BOOK, uniqueItem.getType());
    }

    @Test
    public void testGetTitle() throws Exception {
        assertEquals("Hello World", uniqueItem.getTitle());
    }


    @Test
    public void testEquals() throws Exception {
        final UniqueItem uniqueItem1 = new UniqueItem(1, item1);
        assertEquals(uniqueItem1, uniqueItem);
        final UniqueItem uniqueItem2 = new UniqueItem(2, item1);
        assertNotEquals(uniqueItem2, uniqueItem);
        final UniqueItem uniqueItem3 = new UniqueItem(3, item2);
        assertNotEquals(uniqueItem3, uniqueItem);
    }
}