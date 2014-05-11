package il.co.ovalley.rdvsponeypolice;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import il.co.ovalley.rdvsponeypolice.Model.Loc;

import java.util.Random;
/**
 * Created by yuval on 17/04/2014.
 */
public class Common {
    public static final int SCREEN_BORDERS = 10;
    public static Random random=new Random();
    public static int ITERATION_PAUSE_TIME=10;


    public static Point getScreenSize(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static RelativeLayout.LayoutParams getStickToBottomParams() {
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        return params;
    }

    public static void setViewLocation(View view, Loc location) {
        view.setX(location.x);
        view.setY(location.y);
    }

    public static Loc getViewLocation(View view) {
        Loc loc=new Loc();
        loc.x=view.getX();
        loc.y=view.getY();
        return loc;
    }

}
