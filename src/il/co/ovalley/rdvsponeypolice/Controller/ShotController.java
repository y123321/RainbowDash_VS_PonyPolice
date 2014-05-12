package il.co.ovalley.rdvsponeypolice.Controller;

import android.content.Context;
import il.co.ovalley.rdvsponeypolice.Model.Shot;
import il.co.ovalley.rdvsponeypolice.View.ShotView;

/**
 * Created by yuval on 30/04/2014.
 */
public class ShotController extends GameController {
    protected ShotController(Context context, Shot gameObject, ShotView gameView,boolean isOutOfGame) {
        super(context, gameObject, gameView,isOutOfGame);
    }

    @Override
    public void runUpdate() {

        move();
        checkIfDead();
    }

    private void checkIfDead() {
        if(m_View.getY()<1) m_Model.setDead(true);
    }

    private void move() {
        m_View.setY(m_View.getY() - m_Model.getYSpeed());
    }

    @Override
    protected void changeDirection() {
        m_Model.setYSpeed(-m_Model.getYSpeed());
    }

}
