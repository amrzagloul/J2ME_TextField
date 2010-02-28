
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * @author Orr Matarasso
 */
public class Textfield {

    private static final int CHARACTER_LIMIT = 256;
    private static final int CLEAR_KEY = -8;
    private static final int MULTIPLE_CLICK_SPEED = 700;
    public static final int INPUT_MODE_NORMAL = 0, INPUT_MODE_NUMERAL = 1, INPUT_MODE_PASSWORD = 2;
    private TextFieldListener listener;
    private Dimension dimension;
    private StringBuffer input; //text currently inside the textfield
    private Font font = Font.getDefaultFont(); //font used
    private int fontHeight = font.getHeight();
    private int arrayIndex;
    private int stringIndex; //curser
    private int lastAppendedKey = -1;
    private int inputMode;
    private long lastAppendTime;
    private static char[][] chars;
    public boolean isFocused = true;

    static {
        chars = new char[12][];
        chars[0] = new char[]{' ', '0'};
        chars[1] = new char[]{'-', '.', '1'};
        chars[2] = new char[]{'a', 'b', 'c', '2'};
        chars[3] = new char[]{'d', 'e', 'f', '3'};
        chars[4] = new char[]{'g', 'h', 'i', '4'};
        chars[5] = new char[]{'j', 'k', 'l', '5'};
        chars[6] = new char[]{'m', 'n', 'o', '6'};
        chars[6] = new char[]{'m', 'n', 'o', '6'};
        chars[7] = new char[]{'p', 'q', 'r', 's', '7'};
        chars[8] = new char[]{'t', 'u', 'v', '8'};
        chars[9] = new char[]{'w', 'x', 'y', 'z', '9'};
        chars[10] = new char[]{'*'};
        chars[11] = new char[]{'#'};
    }

    /**
     * Constructor
     *
     * @param Dimension a specifyed drawing area that this control is allowed
     * to occupy.
     */
    public Textfield(Dimension dimension) {
        this.dimension = dimension;
        input = new StringBuffer();
        input.append("The quick brown fox jumps over the lazy dog");
    }

    /**
     * Constructor
     *
     * @param Dimension a specifyed drawing area that this control is allowed
     * to occupy.
     * @param font the font to be used for rendering the text.
     */
    public Textfield(Dimension dimension, Font font) {
        this(dimension);
        this.font = font;
        fontHeight = font.getHeight();
    }

    /**
     * @param toLowerCase when true is passed the returened String will be
     * lowercased.
     * @return the text that is currently contained inside the Textfield.
     */
    public String getInput(boolean toLowerCase) {
        return toLowerCase ? input.toString() : input.toString().toLowerCase();
    }

    /**
     * Clears the Textfield, removing all characters that were previously
     * appeneded to it.
     */
    private void clearAll() {
        if (input.length() > 0) {
            stringIndex = 0;
            input.delete(0, input.length());
        }
    }

    /**
     * Clears the last character that was appended to the Textfield.
     */
    private void clearLastChar() {
        if (input.length() > 0) {
            input.deleteCharAt(input.length() - 1);
            if (stringIndex > 0) {
                stringIndex--;
            }
        }
    }

    public boolean handleKeyPress(int keyCode) { // has the field consumed a keyPress ?

        if (keyCode < 0) {
            if (keyCode != CLEAR_KEY) {
                return false;
            } else {
                // continue
            }
        }

        if (keyCode == CLEAR_KEY) {
            if (input.length() > 0) {
                clearLastChar();
                if (listener != null) {
                    listener.textFieldChanged(this);
                }
            }
            return true; //no need to continue.
        }
        if (input.length() == CHARACTER_LIMIT) {
            return false; //limit the textbox to CHARACTER_LIMIT characters
        }

        //Get an array of chars for this button number
        //(this may return null if the key is not assigned!)
        int buttonNum = keyCodeToButtonNum(keyCode);
        if (inputMode == INPUT_MODE_NUMERAL) {
            if (buttonNum >= 0 && buttonNum < 10) {
                input.append(buttonNum);
                return true;
            }
            return false;
        }
        char[] ch = chars[buttonNum];
        if (ch == null) {
            return false; //The key is not assigned in the CharacterMap so there's no need to go on
        }
        if (buttonNum == lastAppendedKey) { //If the same key is pressed twice in a row
            if (System.currentTimeMillis() - lastAppendTime < MULTIPLE_CLICK_SPEED) { //If the last time a char was appeneded was less than MULTIPLE_CLICK_SPEED ago
                //Remove the last appended char (we'll replace it with the next char in the same char array)
                clearLastChar();
                arrayIndex++;
                if (arrayIndex == ch.length) { //If the last char in the array has been reached, reset the arrayIndex position
                    arrayIndex = 0;
                }
            } else { //Same key pressed twice but not fast enough
                arrayIndex = 0;
            }
        } else { //Not the same key as the last that was pressed
            arrayIndex = 0;
        }
        //Add the new char to the string buffer
        if (input.length() == 0 || input.charAt(input.length() - 1) == ' ') {
            //Capiltilize when needed
            // ** disabled **
            //input.append(Character.toUpperCase(ch[arrayIndex]));
            input.append(ch[arrayIndex]);
        } else {
            input.append(ch[arrayIndex]);
        }
        //Update the last time a char was appended and the last button number that was pressed
        lastAppendTime = System.currentTimeMillis();
        lastAppendedKey = buttonNum;
        if (listener != null) {
            listener.textFieldChanged(this);
        }
        return true;
    }

    public void handleKeyRepeat(int keyCode) {
        if (keyCode == CLEAR_KEY) {
            if (input.length() > 0) {
                clearAll();
                if (listener != null) {
                    listener.textFieldChanged(this);
                }
            }
        } else {
            int buttonNum = keyCodeToButtonNum(keyCode);
            if (buttonNum < 0 || buttonNum > 9) {
                return;
            }
            clearLastChar();
            input.append(buttonNum);
            lastAppendTime = System.currentTimeMillis();
            lastAppendedKey = buttonNum;
            if (listener != null) {
                listener.textFieldChanged(this);
            }
        }
    }

    /**
     * Renders the Textfield on top of a Graphics object.
     * @param g the Graphics object on which the Textfield will draw itself.
     */
    public void draw(Graphics g) {
        int x = dimension.getX();
        int y = dimension.getY();
        int w = dimension.getWidth();
        int h = dimension.getHeight();
        g.setFont(font);
        //Draw box (black rect)
        g.setColor(Color.BLACK);
        g.drawRect(x, y, w, h);
        //Fill the inside of the box with white
        //(because it might clear junk left from a previous state).
        g.setColor(Color.WHITE);
        g.fillRect(x + 1, y + 1, w - 1, h - 1);
        //Draw text inside the box
        String currentlyDisplayedString = null;
        //If we can't fit the input we already have plus this new character inside the box
        currentlyDisplayedString = input.toString().substring(stringIndex, input.toString().length());
        while (font.stringWidth(currentlyDisplayedString) + 4 > w) {
            currentlyDisplayedString = input.toString().substring(stringIndex, input.toString().length());
            stringIndex++;
        }
        g.setColor(Color.BLACK);
        g.drawString(currentlyDisplayedString, x + 2, y + h / 2 + fontHeight / 2, Graphics.BOTTOM | Graphics.LEFT);
        if (isFocused) {
            //Draw carret
            int carretPosition = x + font.stringWidth(currentlyDisplayedString) + 2;
            g.drawLine(carretPosition, y + 2, carretPosition, y + h - 2);
        }
    }

    public boolean isEmpty() {
        return input.length() == 0;
    }

    public void setListener(TextFieldListener listener) {
        this.listener = listener;
    }

    private int keyCodeToButtonNum(int keyCode) {
        if (keyCode > 0) {
            if ((char) keyCode == '#') {
                return 11;
            } else if ((char) keyCode == '*') {
                return 10;
            } else {
                return Integer.parseInt("" + (char) keyCode); //This must be a very stupid way to do this, forgive me.
            }
        } else {
            return -1;
        }
    }

    public void setInputMode(int inputMode) {
        if (inputMode != INPUT_MODE_NORMAL && inputMode != INPUT_MODE_NUMERAL && inputMode != INPUT_MODE_PASSWORD) {
            throw new IllegalArgumentException("Invalid input mode");
        } else {
            this.inputMode = inputMode;
        }
    }
}
