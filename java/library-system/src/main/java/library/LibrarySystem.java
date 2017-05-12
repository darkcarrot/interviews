package library;

import com.google.common.annotations.VisibleForTesting;
import library.items.Item;
import library.items.UniqueItem;
import library.loan.Loan;
import library.member.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * @author Z
 */
public class LibrarySystem {

    private final static Logger logger = LoggerFactory.getLogger(LibrarySystem.class);

    private final Map<Integer, Member> members;
    private final Map<Item, List<Integer>> availabilities;
    private final List<Loan> loans;
    private final LocalDate today;

    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock rl = rwl.readLock();
    private final Lock wl = rwl.writeLock();

    /**
     * Initial a instance of {@link LibrarySystem}, with date default to today's date
     *
     * @param availabilities a map of {@link Item} and corresponding unique ids which are in stock
     * @param loans          a list of {@link Loan}
     * @param members        a map of member id and {@link Member}
     */
    public LibrarySystem(Map<Item, List<Integer>> availabilities, List<Loan> loans, Map<Integer, Member> members) {
        this(availabilities, loans, members, LocalDate.now());
    }

    @VisibleForTesting
    protected LibrarySystem(Map<Item, List<Integer>> availabilities, List<Loan> loans, Map<Integer, Member> members, LocalDate today) {
        this.availabilities = availabilities;
        this.loans = loans;
        this.members = members;
        this.today = today;
    }

    /**
     * Assume
     * a) there is search system, return item id given the name of an item
     * b) a member borrow an item by providing a name and the member id
     * c) a unique item with this item will be lend if it is available
     *
     * @param itemId   id of an {@link Item}
     * @param memberId id of a {@link Member}
     * @return true if item is available else false
     */
    public boolean borrowItem(int itemId, int memberId) {
        wl.lock();
        /**Lock is required for the entire process to avoid available items list get update during the process*/
        try {
            //TODO: Possible Improvement here
            final Optional<Map.Entry<Item, List<Integer>>> available = availabilities.entrySet().parallelStream()
                    .filter(entry -> entry.getKey().getBookId() == itemId)
                    .findFirst();

            if (!available.isPresent()) {
                logger.error("no such item {}", itemId);
                return false;
            }

            logger.info("Items {} is a valid item", itemId);

            final Map.Entry<Item, List<Integer>> entry = available.get();
            final List<Integer> availables = entry.getValue();
            if (availables.size() < 1) {
                logger.info("Item not available for item id {}", itemId);
                return false;
            }

            logger.info("Items available for item id {} with unique id {}", itemId, availables);

            final Item item = entry.getKey();

            final Integer uniqueId = availables.remove(0);
            final UniqueItem uniqueItem = new UniqueItem(uniqueId, item);
            final Loan loan = new Loan(uniqueItem, members.get(memberId), today);
            loans.add(loan);
            logger.info("Lend item {} with unique id {} to member {}", itemId, uniqueId, memberId);
            return true;
        } finally {
            wl.unlock();
        }
    }

    /**
     * Assume
     * a) unique Id is known when a item was returned
     *
     * @param uniqueItemId id of a {@link UniqueItem}
     * @return true if return complete else false
     */
    public boolean returnItem(int uniqueItemId) {
        rl.lock();
        final Optional<Loan> itemOptional;
        try {
            itemOptional = loans.stream()
                    .filter(loan -> loan.getUniqueItem().getUniqueId() == uniqueItemId)
                    .findFirst();
        } finally {
            rl.unlock();
        }

        if (itemOptional.isPresent()) {
            final Loan loan = itemOptional.get();
            final UniqueItem uniqueItem = loan.getUniqueItem();
            final Item item = uniqueItem.getItem();
            wl.lock();
            try {
                if (loans.remove(loan)) {
                    availabilities.get(item).add(uniqueItemId);
                    logger.info("Item {} returned", uniqueItemId);
                }
                return true;
            } finally {
                wl.unlock();
            }
        }
        return false;
    }

    /**
     * @return a map of {@link Item} and number of available
     */
    public Map<Item, Integer> checkInventory() {
        rl.lock();
        try {
            logger.info("Checking inventories...");
            return availabilities.entrySet().parallelStream()
                    //TODO: shall we include items with no inventories?
                    //.filter(entry -> entry.getValue().size() > 0)
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size()));
        } finally {
            rl.unlock();
        }
    }

    /**
     * @return list of {@link UniqueItem} which are overdue
     */
    public List<UniqueItem> checkOverdue() {
        rl.lock();
        try {
            logger.info("Checking overdue items...");
            return loans.stream()
                    .filter(loan -> loan.getDueDate().isBefore(today))
                    .map(Loan::getUniqueItem)
                    .collect(Collectors.toList());
        } finally {
            rl.unlock();
        }
    }

    /**
     * @return list of {@link UniqueItem} based on the member id
     */
    public List<UniqueItem> checkMemberItem(int memberId) {
        rl.lock();
        try {
            logger.info("Checking borrow items for member {}...", memberId);
            return loans.stream()
                    .filter(loan -> loan.getMember().getMemberId() == memberId)
                    .map(Loan::getUniqueItem)
                    .collect(Collectors.toList());
        } finally {
            rl.unlock();
        }
    }

}
