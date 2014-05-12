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

    protected CopController(Context context, Cop gameObject, CopView gameView,boolean isOutOfGame) {
        super(context, gameObject, gameView,isOutOfGame);
        m_CopObject = gameObject;
        m_CopView = gameView;
        m_CopView.setDrawables(gameObject.getDrawables());
        COPS_COUNTER=0;

    }

    @Override
    public Cop getModel() {
        return m_CopObject;
    }

    @Override
    public CopView getView() {
        return m_CopView;
    }

    @Override
    public void runUpdate() {
        setCopDirection();

        if (shootIfShooting()) return;
        if (loadIfLoading()) return;
        move();

        decideIfToShoot();

    }

    private void decideIfToShoot() {
        int rand = Common.random.nextInt(((int) m_CopObject.getChanceNotToShoot()));
        if (rand == 1) {
            shoot();
        }
    }

    private boolean loadIfLoading() {
        if (m_CopObject.isLoading()) {
            if (m_CopObject.getLoadingTimeCounter() == 0) {
                m_CopObject.setShooting(true);
                m_CopObject.setLoading(false);
            } else m_CopObject.decreaseLoadingTimeCounter();
            return true;
        }
        return false;
    }

    private boolean shootIfShooting() {
        if (m_CopObject.isShooting()) {
            m_CopObject.setShooting(false);
            return true;
        }
        return false;
    }

    private void setCopDirection() {
        float x = m_CopView.getX();
        if (x <= Common.SCREEN_BORDERS) m_CopView.setDirection(Direction.RIGHT);
        else if (x >= Common.getScreenSize(m_CopView.getContext()).x - Common.SCREEN_BORDERS)
            m_CopView.setDirection(Direction.LEFT);
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
        }
            chooseDirectionAndGo();
            m_CopObject.setStepCounter(m_CopObject.getStepCounter() -1);
            if (m_CopObject.getStepCounter() == 0) changeDirection();

    }

    private void chooseDirectionAndGo() {
        if (m_CopView.getDirection() == null) {
            Direction direction = Common.random.nextBoolean() ? Direction.LEFT : Direction.RIGHT;

            m_CopView.setDirection(direction);
    }
        float x = m_CopView.getX();
        switch (m_CopView.getDirection()) {
            case LEFT:
                if (x - 1 >= 0) {
                    m_CopView.setX(x - m_CopObject.getXSpeed());
                } else changeDirection();
                break;
            case RIGHT:
                if (x < m_CopView.getContainer().getWidth()) m_CopView.setX(x + m_CopObject.getXSpeed());
                else changeDirection();
                break;
            default:changeDirection();

        }

    }
}




