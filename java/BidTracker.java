import javax.annotation.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author Z
 */
public class BidTracker {

    //List<Bid> bids = Collections.synchronizedList(new ArrayList<>());

    final List<Bid> bids = new CopyOnWriteArrayList<>();

    public void recordBid(Bid bid) {
        bids.add(bid);
    }

    public Bid getWinningBid(@NotNull String itemRef) {
        return bids.stream()
                .filter(bid -> itemRef.equals(bid.getItemRef()))
                .max(Comparator.comparingDouble(Bid::getBid)).get();
    }

    public List<Bid> getAllBids(@NotNull String itemRef) {
        return bids.stream()
                .filter(bid -> itemRef.equals(bid.getItemRef()))
                .collect(Collectors.toList());
    }

    public List<String> getAllBidItems(@NotNull String userRef) {
        return bids.stream()
                .filter(bid -> userRef.equals(bid.getUserRef()))
                .map(Bid::getItemRef)
                .collect(Collectors.toList());
    }

    private class Bid {
        private final String userRef;
        private final String itemRef;
        private final double bid;

        public Bid(String userRef, String itemRef, double bid) {
            this.userRef = userRef;
            this.itemRef = itemRef;
            this.bid = bid;
        }

        public String getUserRef() {
            return userRef;
        }

        public String getItemRef() {
            return itemRef;
        }

        public Double getBid() {
            return bid;
        }
    }

}
