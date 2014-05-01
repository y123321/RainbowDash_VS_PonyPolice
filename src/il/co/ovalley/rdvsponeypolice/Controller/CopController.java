package il.co.ovalley.rdvsponeypolice.Controller;

import android.content.Context;
import il.co.ovalley.rdvsponeypolice.Common;
import il.co.ovalley.rdvsponeypolice.Model.Cop;
import il.co.ovalley.rdvsponeypolice.Model.Direction;
import il.co.ovalley.rdvsponeypolice.View.CopView;

/**
 * Created by yuval on 30/04/2014.
 */
public class CopController extends GameController {
    public static int SPAWN_TIME=500;
    public static int COPS_RATIO=2;
    public static int COPS_COUNTER=0;
    private Cop m_CopObject;
    private CopView m_CopView;

    protected CopController(Context context, Cop gameObject, CopView gameView) {
        super(context, gameObject, gameView);
        m_CopObject = gameObject;
        m_CopView = gameView;
        m_CopView.setDrawables(gameObject.getDrawables());
        COPS_COUNTER=0;

    }

    @Override
    public void runUpdate() {
        float x = m_CopView.getX();
        if (x <=Common.SCREEN_BORDERS) m_CopView.setDirection(Direction.RIGHT);
        else if (x >= Common.getScreenSize(m_CopView.getContext()).x - Common.SCREEN_BORDERS)
            m_CopView.setDirection(Direction.LEFT);
        if (m_CopObject.isDead()) {
            remove();
            return;
        }
        if (m_CopObject.isShooting()) {
            m_CopView.shootAnimation();
            m_CopObject.setShooting(false);
            return;
        }
        if (m_CopObject.isLoading()) {
            if (m_CopObject.getLoadingTimeCounter() == 0) {
                m_CopObject.setShooting(true);
                m_CopObject.setLoading(false);
            } else m_CopObject.decreaseLoadingTimeCounter();
            return;
        }
        move();
        m_CopObject.setStepCounter(m_CopObject.getStepCounter() - (int) m_CopObject.getXSpeed());
        if (m_CopObject.getStepCounter() == 0) changeDirection();

        int rand = Common.random.nextInt(((int) m_CopObject.getChanceNotToShoot()));
        if (rand == 1) {
            shoot();
        }

    }

    @Override
    protected void changeDirection() {
        m_CopObject.setStepCounter(Common.random.nextInt(m_CopObject.getStepsLimit()));
        m_CopView.setDirection(Common.random.nextBoolean() ? Direction.LEFT : Direction.RIGHT);
    }

    void shoot() {
        m_CopObject.makeShot();
    }

    private void move() {
        if (m_CopObject.getStepCounter() % 3 == 0) {
            m_CopView.walkAnimation();
            chooseDirectionAndGo();
        }
    }

    private void chooseDirectionAndGo() {
        if (m_CopView.getDirection() == null) {
            Direction direction = Common.random.nextBoolean() ? Direction.LEFT : Direction.RIGHT;

            m_CopView.setDirection(direction);
    }
        float x = m_CopView.getX();
        switch (m_CopView.getDirection()) {
            case LEFT:
                if (x - 1 > 0) {
                    m_CopView.setX(x - m_CopObject.getXSpeed());
                } else changeDirection();
                break;
            case RIGHT:
                if (x < m_CopView.getContainer().getWidth()) m_CopView.setX(x + m_CopObject.getXSpeed());
                else changeDirection();
                break;

        }
    }
}




