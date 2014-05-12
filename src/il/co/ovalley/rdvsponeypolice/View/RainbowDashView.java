package il.co.ovalley.rdvsponeypolice.View;

import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import il.co.ovalley.rdvsponeypolice.Model.Direction;
import il.co.ovalley.rdvsponeypolice.R;

/**
 * Created by yuval on 30/04/2014.
 */
public class RainbowDashView extends GameView {

    public RainbowDashView(GameLayoutView container) {
        super(container);

    }

    @Override
    protected void init() {
        setX(0);
        setY(0);
        setShotPadding(50);
    }


    public void lockInCage(Direction direction) {
        try {
            if (direction==Direction.RIGHT) setImageResource(R.drawable.rainbow_dash_caged_right);
            else setImageResource(R.drawable.rainbow_dash_caged_left);
            AnimationDrawable RDAnimation = (AnimationDrawable) getDrawable();
            RDAnimation.start();
        }
        catch (Exception e){
            Log.d("rd", e.toString());
        }
    }
    public void releaseAnimation() {
        baseAnimation();
    }
    private void baseAnimation(){
        try {
            if (getDirection()==Direction.RIGHT) {
                setImageResource(R.drawable.rainbow_dash_small_right);
            } else {
                setImageResource(R.drawable.rainbow_dash_small_left);

            }
            AnimationDrawable RDAnimation = (AnimationDrawable) getDrawable();
            RDAnimation.start();
        }
        catch (Exception e){
            Log.d("test",e.toString());
        }
    }

}
