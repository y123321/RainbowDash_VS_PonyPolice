package il.co.ovalley.rdvsponeypolice.Controller;

import android.content.Context;
import il.co.ovalley.rdvsponeypolice.Common;
import il.co.ovalley.rdvsponeypolice.Model.Drop;
import il.co.ovalley.rdvsponeypolice.View.DropView;

/**
 * Created by yuval on 30/04/2014.
 */
public class DropController extends GameController {
    public DropController(Context context, Drop gameObject, DropView gameView) {
        super(context, gameObject, gameView);
    }

    @Override
    public void runUpdate() {
        if(m_Model.isDead()) {
            remove();
            return;
        }
        float y = m_View.getY();
        m_View.setY(y + m_Model.getYSpeed());
        if (y > Common.getScreenSize(getContext()).y)
            m_Model.setDead(true);
    }

    @Override
    protected void changeDirection() {
        m_View.setY(-m_View.getY());
    }
}
