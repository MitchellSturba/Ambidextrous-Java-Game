import java.awt.*;

public class Player {

    public Point location = new Point();
    int health = 5;
    int damage = 1;
    boolean dead = false;

    public Player() {
        location.x = 0;
        location.y = 0;
    }
    public Player(int x, int y) {
        this.location.x = x;
        this.location.y = y;
    }

}
