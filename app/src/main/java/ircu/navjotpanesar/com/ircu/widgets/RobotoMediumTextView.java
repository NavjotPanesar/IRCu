package ircu.navjotpanesar.com.ircu.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Navjot on 1/2/2015.
 */
public class RobotoMediumTextView extends TextView {

    public RobotoMediumTextView(Context context) {
        super(context);
        style(context);
    }

    public RobotoMediumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context);
    }

    public RobotoMediumTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context);
    }

    private void style(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "roboto-medium.ttf");
        setTypeface(tf);
    }

}