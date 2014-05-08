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

        move();
        checkIfDead();

    }

    private void checkIfDead() {
        if (m_View.getY() > Common.getScreenSize(getContext()).y)
            m_Model.setDead(true);
    }

    private void move() {
        m_View.setY(m_View.getY() + m_Model.getYSpeed());
    }

    @Override
    protected void changeDirection() {
        m_View.setY(-m_View.getY());
    }
}
