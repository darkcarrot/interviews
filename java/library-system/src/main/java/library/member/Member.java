package library.member;

/**
 * @author Z
 */
public class Member {
    private final int memberId;
    private final String name;
    //TODO: Further information

    public Member(int memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }

    /**
     * @return member id
     */
    public int getMemberId() {
        return memberId;
    }

    /**
     * @return member's name
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        return memberId == member.memberId && (name != null ? name.equals(member.name) : member.name == null);

    }

    @Override
    public int hashCode() {
        int result = memberId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s-%s", memberId, name);
    }
}
