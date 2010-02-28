
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.*;

/**
 * @author Orr Matarasso
 */
public class Midlet extends MIDlet implements CommandListener {

    private MyCanvas canvas;

    public void startApp() {
        canvas = new MyCanvas();
        canvas.addCommand(new Command("Exit", Command.EXIT, 0));
        canvas.setCommandListener(this);
        Display.getDisplay(this).setCurrent(canvas);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {
        notifyDestroyed();
    }

    private class MyCanvas extends Canvas implements TextFieldListener {

        private boolean keyReleased = true; //this is a bit of an ugly fix to a nasty bug
        private int w, h;
        private Textfield[] textfields = new Textfield[3];
        private Font[] fonts = new Font[]{
                Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE),
                Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_ITALIC, Font.SIZE_MEDIUM),
                Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL),
        };

        //Constructor
        public MyCanvas() {
            w = getWidth();
            h = getHeight();
            textfields[0] = new Textfield(new Dimension(10, 10, w - (w / 10), fonts[0].getHeight()), fonts[0]);
            textfields[1] = new Textfield(new Dimension(10, 10+fonts[0].getHeight()+2, w - (w / 10), fonts[1].getHeight()), fonts[1]);
            textfields[2] = new Textfield(new Dimension(10, 10+fonts[0].getHeight()+2+fonts[1].getHeight()+2, w - (w / 10), fonts[2].getHeight()), fonts[2]);
            for(int i=0;i<textfields.length;i++){
                textfields[i].setListener(this);
            }
          //  textfields[1].setInputMode(Textfield.INPUT_MODE_NUMERAL);

        }

        //@Override (Canvas)
        protected void paint(Graphics g) {
            //clear screen
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, w, h);
            //draw Textfield
             for(int i=0;i<textfields.length;i++){
                textfields[i].draw(g);
            }
            //new Charmap(new Dimension(0,0,w,h)).draw(g);
        }

        //@Override (Canvas)
        protected void keyPressed(int keyCode) {
            for(int i=0;i<textfields.length;i++){
                textfields[i].handleKeyPress(keyCode);
            }
        }

        //@Override (Canvas)
        protected void keyRepeated(int keyCode) {
            if (!keyReleased) {
                return;
            }
            for(int i=0;i<textfields.length;i++){
                textfields[i].handleKeyRepeat(keyCode);
            }
            keyReleased = false;
        }

        //@Override (Canvas)
        protected void keyReleased(int keyCode) {
            keyReleased = true;
        }

        //@Override (TextFieldListener)
        public void textFieldChanged(Textfield textfield) {
            repaint();
        }
    }
}
