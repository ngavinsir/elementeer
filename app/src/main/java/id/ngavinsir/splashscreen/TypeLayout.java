package id.ngavinsir.splashscreen;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by GAVIN on 9/20/2017.
 */

public class TypeLayout extends LinearLayout {

    private float scale = 1;

    public TypeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TypeLayout(Context context) {
        super(context);
    }

    public void setScaleBoth(float scale) {
        this.scale = scale;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = this.getWidth();
        int h = this.getHeight();
        canvas.scale(scale, scale, w / 2, h / 2);
        setAlpha(scale);

        super.onDraw(canvas);
    }
}
