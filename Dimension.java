
/**
 * This class describes a rectangular area a UI component may occupy for its
 * drawing.
 * @author Orr Matarasso
 */
public class Dimension {
    private int x, y, w, h;

    /**
     * Constructor
     * @param x the x origin.
     * @param y the y origin.
     * @param w the total width of the area.
     * @param h the total height of the area.
     */
    public Dimension(int x, int y, int w, int h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getWidth(){
        return w;
    }

    public int getHeight(){
        return h;
    }

}
