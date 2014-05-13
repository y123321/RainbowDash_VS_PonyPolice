package il.co.ovalley.rdvsponeypolice.View;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import il.co.ovalley.rdvsponeypolice.Common;
import il.co.ovalley.rdvsponeypolice.Model.CopDrawables;
import il.co.ovalley.rdvsponeypolice.Model.Direction;
import il.co.ovalley.rdvsponeypolice.R;

/**
 * Created by yuval on 30/04/2014.
 */
public class CopView extends GameView {
    private int m_DrawableState;
    private CopDrawables m_Drawables;


    public CopView(GameLayoutView container) {
        super(container, Common.getStickToBottomParams());


    }

    @Override
    protected void init() {
        setX(getRandomWidth());
        setPadding(0, 0, 0, getRandomPadding());  // left, top, right, bottom
        setShotPadding(0);

    }

    @Override
    public void getHitRect(Rect outRect) {
        super.getHitRect(outRect);
        outRect.top+=getPaddingBottom()/2;
        outRect.bottom-=getPaddingBottom()/2;
    }
    private int getRandomPadding(){
        Point size=Common.getScreenSize(getContext());
        return Common.random.nextInt(size.y/4);
    }
    private int getRandomWidth() {
        Point size=Common.getScreenSize(getContext());
        return Common.random.nextInt(size.x-Common.SCREEN_BORDERS)+Common.SCREEN_BORDERS;
    }
    public void walkAnimation(Direction direction) {
        int resource = direction == Direction.LEFT ? getDrawables().baseDrawableLeft: getDrawables().baseDrawableRight;
        startAnimation(resource);

    }

    private void startAnimation(int resource) {
        setImageResource(resource);
        AnimationDrawable animation = (AnimationDrawable) getDrawable();

        setImageDrawable(animation.getFrame(m_DrawableState));
        m_DrawableState = m_DrawableState == animation.getNumberOfFrames()-1 ? 0 : m_DrawableState + 1;
    }

    public void shootAnimation(Direction direction) {
        int resource = direction == Direction.LEFT ? getDrawables().shootingLeftDrawable: getDrawables().shootingRightDrawable;
        startAnimation(resource);

        walkAnimation(direction);

    }

    public CopDrawables getDrawables() {
        return m_Drawables;
    }

    public void setDrawables(CopDrawables drawables) {
        this.m_Drawables = drawables;
    }

    public void dyingAnimation(){
        startAnimation(R.drawable.police_pony_shit_left);
    }
}
