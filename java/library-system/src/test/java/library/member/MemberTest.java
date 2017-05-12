package library.member;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Z
 */
public class MemberTest {

    private Member member;

    @Before
    public void setUp() throws Exception {
        member = new Member(1, "ZC");
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("ZC", member.getName());
    }

    @Test
    public void testGetMemberId() throws Exception {
        assertEquals(1, member.getMemberId());
    }

    @Test
    public void testEquals() throws Exception {
        assertEquals(new Member(1, "ZC"), member);
        assertNotEquals(new Member(2, "ZC"), member);
    }

}