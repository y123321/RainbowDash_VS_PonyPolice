package il.co.ovalley.rdvsponeypolice.View;

import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.view.ViewGroup;
import il.co.ovalley.rdvsponeypolice.Common;
import il.co.ovalley.rdvsponeypolice.Model.CopDrawables;
import il.co.ovalley.rdvsponeypolice.Model.Direction;

/**
 * Created by yuval on 30/04/2014.
 */
public class CopView extends GameView {
    private CopDrawables m_Drawables;
    private AnimationDrawable mBaseAnimationLeft;
    private AnimationDrawable mBaseAnimationRight;
    private Point size;

    public CopView(ViewGroup container,CopDrawables drawables) {
        super(container, Common.getStickToBottomParams());
        m_Drawables=drawables;
        setImageResource(drawables.baseDrawableLeft);
        mBaseAnimationLeft=(AnimationDrawable) getDrawable();
        setImageResource(drawables.baseDrawableRight);
        mBaseAnimationRight =(AnimationDrawable) getDrawable();





    }

    @Override
    protected void init() {
        setX(getRandomWidth());
        setPadding(0, 0, 0, getRandomPadding());  // left, top, right, bottom
        setShotPadding(20);
        setAlpha(1f);

    }


    private int getRandomPadding(){
        size=Common.getScreenSize(getContext());
        return Common.random.nextInt(size.y/4);
    }
    private int getRandomWidth() {
        size=Common.getScreenSize(getContext());
        return Math.abs(Common.random.nextInt(size.x-2*SCREEN_BORDERS))+SCREEN_BORDERS;
    }
    public void walkAnimation(Direction direction) {
     //   int resource = direction == Direction.LEFT ? getDrawables().baseDrawableLeft: getDrawables().baseDrawableRight;

        startAnimation(mBaseAnimationLeft,mBaseAnimationRight,direction);
    }

   /* private void startAnimation(int resource) {
        setImageResource(resource);
        AnimationDrawable animation = (AnimationDrawable) getDrawable();
     //   animation.setDither(true);
        setImageDrawable(animation.getFrame(m_DrawableState));
        m_DrawableState = m_DrawableState == animation.getNumberOfFrames()-1 ? 0 : m_DrawableState + 1;
    }*/


    public void shootAnimation(Direction direction) {
      //  int resource = direction == Direction.LEFT ? getDrawables().shootingLeftDrawable: getDrawables().shootingRightDrawable;
     //   startAnimation(resource);
        clearAnimation();
        walkAnimation(direction);

    }

    public CopDrawables getDrawables() {
        return m_Drawables;
    }

    public void setDrawables(CopDrawables drawables) {
        this.m_Drawables = drawables;
    }

    public void dyingAnimation(){
      //  startAnimation(R.drawable.police_pony_shit_left);
    }


}
