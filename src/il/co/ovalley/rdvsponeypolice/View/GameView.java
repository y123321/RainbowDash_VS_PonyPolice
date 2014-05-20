package il.co.ovalley.rdvsponeypolice.View;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import il.co.ovalley.rdvsponeypolice.Model.Direction;

/**
 * Created by yuval on 18/04/2014.
 */
public abstract class GameView extends ImageView {
    public static final int SCREEN_BORDERS = 10;

    private GameLayoutView m_container;


    private RelativeLayout.LayoutParams m_params;
    private AddViewAction mAddViewAction;

    public int getShotPadding() {
        return shotPadding;
    }

    public void setShotPadding(int shotPadding) {
        this.shotPadding = shotPadding;
    }

    private int shotPadding;

    public GameView(GameLayoutView container) {
        this(container,null);
    }
    public GameView(GameLayoutView container,RelativeLayout.LayoutParams params){
        super(container.getContext());
        mAddViewAction=new AddViewAction();
        m_container=container;
        m_params=params;
    }
    protected void startAnimation(AnimationDrawable leftAnimation, AnimationDrawable rightAnimation,Direction direction) {
        AnimationDrawable animation;
        clearAnimation();
        try {
            if (direction== Direction.RIGHT) {
                animation= rightAnimation;
            } else {
                animation= leftAnimation;
            }
            if(animation.isRunning()) animation.stop();
            setImageDrawable(animation);
            animation.start();
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

    public GameLayoutView getContainer() {
        return m_container;
    }

  /*  public void removeView() {
        m_container.removeView(this);
    }*/


    public void setContainer(GameLayoutView container) {
        m_container = container;
    }



    public void initGameView() {
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

