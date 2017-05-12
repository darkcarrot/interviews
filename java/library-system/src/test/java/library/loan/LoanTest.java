package library.loan;

import library.items.UniqueItem;
import library.member.Member;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

/**
 * @author Z
 */
@RunWith(JMockit.class)
public class LoanTest {

    private final static LocalDate BORROW_DATE = LocalDate.of(2016, 1, 1);
    private final static LocalDate DUE_DATE = LocalDate.of(2016, 1, 8);

    private Loan loan;

    @Mocked
    private UniqueItem uniqueItem;
    @Mocked
    private Member member;

    @Before
    public void setUp() throws Exception {
        loan = new Loan(uniqueItem, member, BORROW_DATE);
    }

    @Test
    public void testGetUniqueItem() throws Exception {
        assertEquals(loan.getUniqueItem(), uniqueItem);
    }

    @Test
    public void testGetMember() throws Exception {
        assertEquals(loan.getMember(), member);
    }

    @Test
    public void testGetBorrowDate() throws Exception {
        assertEquals(loan.getBorrowDate(), BORROW_DATE);
    }

    @Test
    public void testGetDueDate() throws Exception {
        assertEquals(loan.getDueDate(), DUE_DATE);
    }
}