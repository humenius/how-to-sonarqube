import java.awt.*;
import java.util.Random;

/**
 * <h1>Account</h1>
 * <p>Description here...</p>
 *
 * @author kevin
 */
public class Account {
    private final Random random = new Random();

    private String name;
    private State state = State.AVAILABLE;
    private Color color = new Color(random.nextInt(201), random.nextInt(201), random.nextInt(201));

    public Account(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
