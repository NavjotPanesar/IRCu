package ircu.navjotpanesar.com.ircu.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Navjot on 5/31/2015.
 */
public class RobotoRegularTextView extends TextView {

    public RobotoRegularTextView(Context context) {
        super(context);
        style(context);
    }

    public RobotoRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context);
    }

    public RobotoRegularTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context);
    }

    private void style(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "Roboto-Regular.ttf");
        setTypeface(tf);
    }

}