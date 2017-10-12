package ash.wasset.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by shafeek on 14/04/16.
 */
public class MyTextViewBold extends TextView {

    public MyTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public MyTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyTextViewBold(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
//        Typeface tf;
//        tf = Typeface.createFromAsset(getContext().getAssets(), "Changa-Regular.ttf");
//        setTypeface(tf);
    }

}
