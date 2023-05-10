import java.awt.Graphics;
import java.awt.Color;
import java.io.Serializable;

public class Square implements Serializable {
    Color c;
    int dimension;
    //public String characterName;

    public Square(int d) {
        c = Color.WHITE;
        dimension = d;
    }

    public void drawMe(Graphics g, int x, int y) {
        g.setColor(c);
        g.fillRect(x, y, dimension, dimension);

        g.setColor(Color.BLACK);
        g.drawRect(x, y, dimension, dimension);
    }

    public void fill(Color c) {
        this.c = c;
    }

    public void clear() {
        c = Color.WHITE;
    }


}
