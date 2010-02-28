
/**
 * @author Orr Matarasso
 */
public interface TextFieldListener {

    /**
     * This method is invoked whenever a Textfield's content has been altered in
     * response to a keyPress/KeyRepeat event.
     * @param textfield The Textfield that invoked this method.
     */
    public void textFieldChanged(Textfield textfield);
}
