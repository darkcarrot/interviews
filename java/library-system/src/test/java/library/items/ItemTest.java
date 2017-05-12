package library.items;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Z
 */
public class ItemTest {

    private Item item;

    @Before
    public void setUp() {
        item = new Item(13, ItemType.BOOK, "Hello World");
    }

    @Test
    public void testGetBookId() throws Exception {
        assertEquals(13, item.getBookId());
    }

    @Test
    public void testGetType() throws Exception {
        assertEquals(ItemType.BOOK, item.getType());
    }

    @Test
    public void testGetTitle() throws Exception {
        assertEquals("Hello World", item.getTitle());
    }

    @Test
    public void testEquals() throws Exception {
        final Item item2 = new Item(13, ItemType.BOOK, "Hello World");
        assertEquals(item2, item);

        final Item item3 = new Item(17, ItemType.DVD, "Bye");
        assertNotEquals(item3, item);
    }
}