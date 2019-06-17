package pkgfinal;
/**
 *
 * @author Mihai
 */
public class Saptamana {
    private final int startOfMonth;
    private final int Month;

    public Saptamana(int startOfMonth, int Month) {
        this.startOfMonth = startOfMonth;
        this.Month = Month;
    }

    public int getStartOfMonth() {
        return startOfMonth;
    }

    public int getMonth() {
        return Month;
    }
}
