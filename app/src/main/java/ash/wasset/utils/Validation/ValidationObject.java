package ash.wasset.utils.Validation;

import android.widget.EditText;

/**
 * Created by amr on 11/22/16.
 */
public class ValidationObject {

    public EditText editText;
    public EditText editText2;
    public int minLenght;
    public int messageId;
    public boolean password;
    public boolean email;
    public boolean ckeckLenthOnly;


    public ValidationObject(EditText editText, int minLenght, boolean ckeckLenthOnly, int messageId)
    {
        this.editText = editText;
        this.minLenght = minLenght;
        this.ckeckLenthOnly = ckeckLenthOnly;
        this.messageId = messageId;
        this.password = false;
        this.email = false;
    }

    public ValidationObject(EditText editText, EditText editText2, boolean password, int messageId) {
        this.editText = editText;
        this.editText2 = editText2;
        this.password = password;
        this.messageId = messageId;
        this.email = false;
        this.ckeckLenthOnly = false;
    }

    public ValidationObject(EditText editText, boolean email, int messageId) {
        this.editText = editText;
        this.email = email;
        this.messageId = messageId;
        this.password = false;
        this.ckeckLenthOnly = false;
    }
}
