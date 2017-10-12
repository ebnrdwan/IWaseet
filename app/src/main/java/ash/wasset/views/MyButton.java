package ash.wasset.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by shafeek on 11/04/16.
 */
public class MyButton extends Button {

    public MyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyButton(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setAllCaps(false);
//        Typeface tf;
//        tf = Typeface.createFromAsset(getContext().getAssets(), "Changa-Regular.ttf");
//        setTypeface(tf);
    }
}
