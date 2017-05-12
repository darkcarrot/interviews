package library.loan;

import com.google.common.base.Objects;
import library.items.UniqueItem;
import library.member.Member;

import java.time.LocalDate;

/**
 * @author Z
 */
public class Loan {

    private final UniqueItem uniqueItem;
    private final Member member;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;

    public Loan(UniqueItem uniqueItem, Member member, LocalDate borrowDate) {
        this.uniqueItem = uniqueItem;
        this.member = member;
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(7L);
    }

    /**
     * @return {@link UniqueItem} associate with this loan
     */
    public UniqueItem getUniqueItem() {
        return uniqueItem;
    }

    /**
     * @return {@link Member} who borrows this book
     */
    public Member getMember() {
        return member;
    }

    /**
     * @return the date the book was borrowed
     */
    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    /**
     * @return due date for the book
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return Objects.equal(uniqueItem, loan.uniqueItem) &&
                Objects.equal(member, loan.member) &&
                Objects.equal(borrowDate, loan.borrowDate) &&
                Objects.equal(dueDate, loan.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uniqueItem, member, borrowDate, dueDate);
    }

    @Override
    public String toString() {
        return "Loan{" +
                "uniqueItem=" + uniqueItem +
                ", member=" + member +
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                '}';
    }
}
