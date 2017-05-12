package library;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import library.items.Item;
import library.items.ItemType;
import library.items.UniqueItem;
import library.loan.Loan;
import library.member.Member;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author Z
 */
public class LibrarySystemTest {

    private final static ClassLoader CLASSLOADER = Thread.currentThread().getContextClassLoader();
    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final Map<Integer, Item> itemMap = Maps.newHashMap();
    private final Map<Integer, Member> memberMap = Maps.newHashMap();
    private final ExecutorService service = Executors.newFixedThreadPool(3);
    private LibrarySystem system;

    @Before
    public void setUp() throws Exception {
        provideBooks();
        provideMembers();
        system = new LibrarySystem(provideInventory(), provideLoans(), memberMap, LocalDate.of(2016, 2, 25));
    }

    @Test
    public void testBorrowItem() throws Exception {
        assertTrue(system.borrowItem(2, 1));
        assertFalse(system.borrowItem(2, 4));
        assertTrue(system.borrowItem(6, 3));
        assertFalse(system.borrowItem(6, 4));

        assertTrue(system.returnItem(1));
        assertTrue(system.borrowItem(7, 2));
    }

    @Test
    public void testBorrowItem2() throws Exception {
        final Future<Boolean> future1 = service.submit(() -> system.borrowItem(2, 1));
        final Future<Boolean> future2 = service.submit(() -> system.returnItem(1));
        final Future<Boolean> future3 = service.submit(() -> system.borrowItem(6, 3));
        final Future<Boolean> future4 = service.submit(() -> system.borrowItem(7, 2));
        final Future<Boolean> future5 = service.submit(() -> system.borrowItem(7, 3));
        final Future<Boolean> future6 = service.submit(() -> system.borrowItem(7, 1));
        final Future<Boolean> future7 = service.submit(() -> system.borrowItem(7, 4));

        assertTrue(future1.get(3, TimeUnit.SECONDS));
        assertTrue(future2.get(3, TimeUnit.SECONDS));
        assertTrue(future3.get(3, TimeUnit.SECONDS));
        assertTrue(future4.get(3, TimeUnit.SECONDS));
        assertTrue(future5.get(3, TimeUnit.SECONDS));
        assertTrue(future6.get(3, TimeUnit.SECONDS));
        assertFalse(future7.get(3, TimeUnit.SECONDS));
    }

    @Test
    public void testReturnItem() throws Exception {
        assertTrue(system.returnItem(1));
        assertTrue(system.returnItem(4));
        assertFalse(system.returnItem(5));
    }

    @Test
    public void testCheckInventory() throws Exception {
        final Map<Item, Integer> inventory = system.checkInventory();
        assertEquals(inventory.get(itemMap.get(4)).intValue(), 3);
        assertEquals(inventory.get(itemMap.get(7)).intValue(), 2);

        system.borrowItem(7, 2);
        system.borrowItem(7, 3);
        assertEquals(system.checkInventory().get(itemMap.get(7)).intValue(), 0);
    }

    @Test
    public void testCheckOverdue() throws Exception {
        final List<UniqueItem> overdues = system.checkOverdue();
        assertEquals(overdues.size(), 1);
        final UniqueItem overdue = overdues.get(0);
        assertEquals(overdue.getUniqueId(), 1);
        assertEquals(overdue.getItem(), itemMap.get(7));
    }

    @Test
    public void testCheckMemberItem() throws Exception {
        assertEquals(system.checkMemberItem(1).size(), 1);
        assertEquals(system.checkMemberItem(4).size(), 0);

        assertTrue(system.borrowItem(6, 4));
        assertEquals(system.checkMemberItem(4).size(), 1);
        assertTrue(system.returnItem(11));
        assertEquals(system.checkMemberItem(4).size(), 0);
    }

    private void provideBooks() throws Exception {
        final List<String[]> raw = parseCSV("books.csv");
        raw.forEach(book -> {
            final int bookId = Integer.parseInt(book[1]);
            if (!itemMap.containsKey(bookId)) {
                final ItemType type = ItemType.valueOf(book[2].toUpperCase());
                final Item item = new Item(bookId, type, book[3]);
                itemMap.put(bookId, item);
            }
        });
    }

    private Map<Item, List<Integer>> provideInventory() throws Exception {
        final Map<Item, List<Integer>> books = Maps.newHashMap();
        final List<String[]> raw = parseCSV("inventory.csv");
        raw.forEach(book -> {
            final int uniqueId = Integer.parseInt(book[0]);
            final int bookId = Integer.parseInt(book[1]);
            final Item item;
            if (itemMap.containsKey(bookId)) {
                item = itemMap.get(bookId);
                books.putIfAbsent(item, Lists.newArrayList());
                books.get(item).add(uniqueId);
            }
        });
        return books;
    }

    private void provideMembers() throws Exception {
        final List<String[]> raw = parseCSV("members.csv");
        raw.forEach(member -> {
            final int memberId = Integer.parseInt(member[0]);
            final String name = member[1];
            memberMap.put(memberId, new Member(memberId, name));
        });
    }

    private List<Loan> provideLoans() throws Exception {
        final List<Loan> loans = Lists.newArrayList();
        final List<String[]> raw = parseCSV("loans.csv");
        raw.forEach(loan -> {
            final int bookId = Integer.parseInt(loan[1]);
            final int memberId = Integer.parseInt(loan[2]);
            final Item item = itemMap.getOrDefault(bookId, null);
            final Member member = memberMap.getOrDefault(memberId, null);
            if (item != null && member != null) {
                final int uniqueId = Integer.parseInt(loan[0]);
                final UniqueItem uniqueItem = new UniqueItem(uniqueId, item);
                final LocalDate date = LocalDate.parse(loan[3], FORMATTER);
                loans.add(new Loan(uniqueItem, member, date));
            }
        });
        return loans;
    }

    private List<String[]> parseCSV(String fileName) throws Exception {
        final InputStream is = CLASSLOADER.getResourceAsStream(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().skip(1)
                    .map(line -> line.split(","))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Lists.newArrayList();
        }
    }
}