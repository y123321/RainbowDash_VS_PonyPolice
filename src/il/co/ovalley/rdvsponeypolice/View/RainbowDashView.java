package il.co.ovalley.rdvsponeypolice.View;

import android.graphics.drawable.AnimationDrawable;
import android.view.ViewGroup;
import il.co.ovalley.rdvsponeypolice.Model.Direction;
import il.co.ovalley.rdvsponeypolice.R;

/**
 * Created by yuval on 30/04/2014.
 */
public class RainbowDashView extends GameView {
    AnimationDrawable mLockAnimationLeft;
    AnimationDrawable mLockAnimationRight;
    AnimationDrawable mBaseAnimationLeft;
    AnimationDrawable mBaseAnimationRight;

    public RainbowDashView(ViewGroup container) {
        super(container);
        setImageResource(R.anim.rainbow_dash_caged_left);
        mLockAnimationLeft = (AnimationDrawable) getDrawable();
        setImageResource(R.anim.rainbow_dash_caged_right);
        mLockAnimationRight = (AnimationDrawable) getDrawable();
        setImageResource(R.anim.rainbow_dash_small_left);
        mBaseAnimationLeft = (AnimationDrawable) getDrawable();
        setImageResource(R.anim.rainbow_dash_small_right);
        mBaseAnimationRight = (AnimationDrawable) getDrawable();
    }

    @Override
    protected void init() {
        setX(0);
        setY(0);
        setShotPadding(20);




    }

    public void lockInCage(Direction direction) {
        startAnimation(mLockAnimationLeft,mLockAnimationRight,direction);
    }
    public void releaseAnimation(Direction direction) {
        baseAnimation(direction);
    }
    public void baseAnimation(Direction direction){
        startAnimation(mBaseAnimationLeft, mBaseAnimationRight,direction);
    }






}
