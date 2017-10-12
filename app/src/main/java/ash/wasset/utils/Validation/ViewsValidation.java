package ash.wasset.utils.Validation;

import android.content.Context;

import java.util.ArrayList;

import ash.wasset.utils.Dialog;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by amr on 11/22/16.
 */
public class ViewsValidation {

    Context context;

    private static ViewsValidation viewsValidation = new ViewsValidation( );

    public static ViewsValidation getInstance( ) {
        return viewsValidation;
    }

    public boolean validate(Context context, ArrayList<ValidationObject> list)
    {
        this.context = context;
        for(int i=0;i<list.size();i++)
        {
            if (list.get(i).ckeckLenthOnly){
                if (!checkLenth(list, i))
                    return false;
            }
            else if (list.get(i).email){
                if (!isValidEmail(list.get(i).editText.getText().toString(), list, i))
                    return false;
            }
            else if (list.get(i).password){
                if (!checkMatchPassword(list.get(i).editText.getText().toString(), list.get(i).editText2.getText().toString(), list, i))
                    return false;
            }
        }

        return true;

    }

    private boolean checkLenth(ArrayList<ValidationObject> list, int i){
        if(list.get(i).editText.getText().toString().trim().length()<list.get(i).minLenght)
        {
            Dialog.showAletDialog(context, context.getResources().getString(list.get(i).messageId), "", SweetAlertDialog.WARNING_TYPE);
            return false;
        }
        return true;
    }

    private boolean isValidEmail(CharSequence target, ArrayList<ValidationObject> list, int i) {
        if (target == null) {
            Dialog.showAletDialog(context, context.getResources().getString(list.get(i).messageId), "", SweetAlertDialog.WARNING_TYPE);
            return false;
        } else {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches())
                Dialog.showAletDialog(context, context.getResources().getString(list.get(i).messageId), "", SweetAlertDialog.WARNING_TYPE);
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private boolean checkMatchPassword(String firstPass, String secondPass, ArrayList<ValidationObject> list, int i){
        if (!firstPass.equals(secondPass)) {
            Dialog.showAletDialog(context, context.getResources().getString(list.get(i).messageId), "", SweetAlertDialog.WARNING_TYPE);
            return false;
        } else {
            return true;
        }
    }
}
