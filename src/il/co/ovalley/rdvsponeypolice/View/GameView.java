package il.co.ovalley.rdvsponeypolice.View;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import il.co.ovalley.rdvsponeypolice.Model.Direction;
import il.co.ovalley.rdvsponeypolice.Model.GameLayout;

/**
 * Created by yuval on 18/04/2014.
 */
public abstract class GameView extends ImageView {

    private GameLayout m_container;
    private Direction direction;
    private Direction directionVertical;
    private float xSpeed;
    private float ySpeed;
    public GameView(GameLayout container) {
        super(container.getContext());
        m_container=container;
        container.addView(this);
    }
    public GameView(final GameLayout container,final RelativeLayout.LayoutParams params){
        super(container.getContext());
        m_container=container;
        final View view=this;
        Activity activity=(Activity)getContext();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_container.addView(view, params);
            }
        });
    }




    public Direction getDirection() {
        return direction;
    }

    public GameLayout getContainer() {
        return m_container;
    }

    public void removeView() {
        m_container.removeView(this);
    }


    public void setContainer(GameLayout container) {
        m_container = container;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }


    public Direction getDirectionVertical() {
        return directionVertical;
    }

    public void setDirectionVertical(Direction directionVertical) {
        this.directionVertical = directionVertical;
    }
}

