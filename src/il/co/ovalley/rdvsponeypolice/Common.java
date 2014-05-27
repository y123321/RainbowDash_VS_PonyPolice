package il.co.ovalley.rdvsponeypolice;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;
import il.co.ovalley.rdvsponeypolice.Model.Loc;
import il.co.ovalley.rdvsponeypolice.Runnables.GameManager;

import java.util.Random;
/**
 * Created by yuval on 17/04/2014.
 */
public class Common {
    public static Random random=new Random();
    public static final int STOPSPLASH = 1;
    public static GameManager gameManager;//instanciated from GameActivity
    private static Point screenSize;
    public static Point getScreenSize(Context context){
        if(screenSize==null) {
            screenSize=new Point();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            display.getSize(screenSize);
        }
        return screenSize;
    }

    public static RelativeLayout.LayoutParams getStickToBottomParams() {
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
     //   params.addRule(RelativeLayout.DRAWING_CACHE_QUALITY_);
        return params;
    }

    public static void setViewLocation(View view, Loc location) {
        view.setX(location.x);
        view.setY(location.y);
    }

    public static Loc getViewLocation(View view,Loc loc) {
        loc.x=view.getX();
        loc.y=view.getY();
        return loc;
    }
    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targtetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targtetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(1500);//(int)(targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setStartOffset(0);
        v.startAnimation(a);
        v.setVisibility(View.VISIBLE);

    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

}
