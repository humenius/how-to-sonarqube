/**
 * <h1>State</h1>
 * <p>Description here...</p>
 *
 * @author kevin
 */
public enum State {
    AVAILABLE("Available"), AWAY("Away"), DND("Do not disturb");

    private final String label;

    State(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
