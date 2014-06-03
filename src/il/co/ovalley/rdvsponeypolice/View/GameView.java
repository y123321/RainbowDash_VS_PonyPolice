package il.co.ovalley.rdvsponeypolice.View;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import il.co.ovalley.rdvsponeypolice.Model.Direction;

/**
 * Created by yuval on 18/04/2014.
 */
public abstract class GameView extends ImageView {
    public static final int SCREEN_BORDERS = 10;

    private ViewGroup m_container;
    public volatile boolean isRemoved;
    private AnimationDrawable mAnimation;
    private ViewGroup.LayoutParams m_params;
    private AddViewAction mAddViewAction;

    public int getShotPadding() {
        return shotPadding;
    }

    public void setShotPadding(int shotPadding) {
        this.shotPadding = shotPadding;
    }

    private int shotPadding;

    public GameView(ViewGroup container) {
        this(container,null);
    }
    public GameView(ViewGroup container,ViewGroup.LayoutParams params){
        super(container.getContext());
        mAddViewAction=new AddViewAction();
        m_container=container;
    //    if(params==null) params= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        m_params=params;
    }
    protected void startAnimation(AnimationDrawable leftAnimation, AnimationDrawable rightAnimation,Direction direction) {

        clearAnimation();
        try {
            if (direction== Direction.RIGHT) {
                mAnimation = rightAnimation;
            } else {
                mAnimation = leftAnimation;
            }
            if(mAnimation.isRunning()) mAnimation.stop();
            setImageDrawable(mAnimation);
            mAnimation.start();
        }
        catch (Exception e){
            Log.d("test", e.toString());
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);

        }
        catch (Exception e){
            Log.d("test","on draw crashed"+e.getMessage()+"\n"+e.getStackTrace());
        }


    }

    public ViewGroup getContainer() {
        return m_container;
    }

  /*  public void removeView() {
        m_container.removeView(this);
    }*/


    public void setContainer(ViewGroup container) {
        m_container = container;
    }


    @Override
    public void getHitRect(Rect outRect) {
        outRect.top=(int)getY()-getHeight()/2+getPaddingBottom()/2;;
        outRect.bottom=(int)getY()+getHeight()/2-getPaddingBottom()/2;;
        outRect.left=(int)getX();
        outRect.right=(int)getX()+getWidth();

    }
    public void initGameView() {
        isRemoved=false;
        init();
        Activity activity=(Activity)getContext();

        activity.runOnUiThread(mAddViewAction);
    }
    abstract protected void init();
    class AddViewAction implements Runnable{
        @Override
        public void run() {
            if(m_params==null)
                m_container.addView(GameView.this);
            else
                m_container.addView(GameView.this, m_params);
        }

    }
}

