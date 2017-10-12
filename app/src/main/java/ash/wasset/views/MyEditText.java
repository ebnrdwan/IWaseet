package ash.wasset.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by shafeek on 11/04/16.
 */
public class MyEditText extends EditText {

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyEditText(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
//        Typeface tf;
//        tf = Typeface.createFromAsset(getContext().getAssets(), "Changa-Regular.ttf");
//        setTypeface(tf);
    }
}
