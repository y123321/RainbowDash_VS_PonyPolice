package il.co.ovalley.rdvsponeypolice.View;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by yuval on 18/04/2014.
 */
public abstract class GameView extends ImageView {
    public static final int SCREEN_BORDERS = 10;

    private GameLayoutView m_container;


    private RelativeLayout.LayoutParams m_params;

    public int getShotPadding() {
        return shotPadding;
    }

    public void setShotPadding(int shotPadding) {
        this.shotPadding = shotPadding;
    }

    private int shotPadding;

    public GameView(GameLayoutView container) {
        super(container.getContext());
        m_container=container;
   //     container.addView(this);
    }
    public GameView(GameLayoutView container,RelativeLayout.LayoutParams params){
        super(container.getContext());
        m_container=container;
        m_params=params;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

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
        final View view=this;
        Activity activity=(Activity)getContext();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(m_params==null)
                    m_container.addView(view);
                else
                m_container.addView(view, m_params);
            }
        });
    }
    abstract protected void init();
}

