package il.co.ovalley.rdvsponeypolice.View;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by yuval on 29/05/2014.
 */
public class DynamicImageView extends ImageView {
    int mHeight;
    int mWidth;
    Drawable d;
    public DynamicImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        d = this.getDrawable();

        if (d != null) {
            // ceil not round - avoid thin vertical gaps along the left/right edges
            mHeight =  MeasureSpec.getSize(heightMeasureSpec);
            mWidth =(int) Math.ceil(mHeight * (float) d.getIntrinsicWidth() / d.getIntrinsicHeight());

            this.setMeasuredDimension(mWidth, mHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}