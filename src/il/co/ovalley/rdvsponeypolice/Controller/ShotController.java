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
        if(m_Model.isDead()) {
            remove();
            Log.d("test","dud");
            return;
        }
        float y = m_View.getY();
        m_View.setY(y - m_Model.getYSpeed());
        if(y<1) m_Model.setDead(true);
        Log.d("test","suppose to shoot");
    }

    @Override
    protected void changeDirection() {
        m_Model.setYSpeed(-m_Model.getYSpeed());
    }

}
