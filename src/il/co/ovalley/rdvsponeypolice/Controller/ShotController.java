package il.co.ovalley.rdvsponeypolice.Controller;

import android.content.Context;
import android.util.Log;
import il.co.ovalley.rdvsponeypolice.Model.Shot;
import il.co.ovalley.rdvsponeypolice.View.ShotView;

/**
 * Created by yuval on 30/04/2014.
 */
public class ShotController extends GameController {
    protected ShotController(Context context, Shot gameObject, ShotView gameView) {
        super(context, gameObject, gameView);
    }

    @Override
    public void runUpdate() {

        move();
        checkIfDead();
        Log.d("test","suppose to shoot");
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
