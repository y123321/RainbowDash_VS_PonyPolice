package il.co.ovalley.rdvsponeypolice.Controller;

import android.content.Context;
import il.co.ovalley.rdvsponeypolice.Common;
import il.co.ovalley.rdvsponeypolice.Model.Cop;
import il.co.ovalley.rdvsponeypolice.Model.Direction;
import il.co.ovalley.rdvsponeypolice.View.CopView;
import il.co.ovalley.rdvsponeypolice.View.GameView;

/**
 * Created by yuval on 30/04/2014.
 */
public class CopController extends GameController {
    public static int SPAWN_TIME=500;
    public static int COPS_RATIO=2;
    public static int COPS_COUNTER=0;
    private Cop mCopModel;
    private CopView mCopView;
    ClearAnimationAction mClearAnimationAction;
//    public boolean animating;
    protected CopController(Context context, Cop gameObject, CopView gameView,boolean isOutOfGame) {
        super(context, gameObject, gameView,isOutOfGame);
        mCopModel = gameObject;
        mCopView = gameView;
        mClearAnimationAction=new ClearAnimationAction();
        COPS_COUNTER=0;
        changeDirection();
  //      animating=false;

    }


    @Override
    public Cop getModel() {
        return mCopModel;
    }

    @Override
    public CopView getView() {
        return mCopView;
    }
    /**
     * returns true if view should be updated or false if it shouldn't
     */
    @Override
    protected boolean runModelUpdate() {
        dieIfDying();
        return true;
    }
    /**
     * updates on the view, this method runs on UI thread
     */
    @Override
    public void runViewUpdate() {
        changeCopDirectionOnBorders(GameView.SCREEN_BORDERS);
        if(loadIfLoading()){
            getView().clearAnimation();
            return;
        }
        if(getModel().isHit()){
            getModel().decreaseCurrentHitPoints();
            if (getModel().getCurrentHitPoints() <= 0) {
                getModel().setDying(true);
            }
            getModel().removeHit();
                getView().setAlpha(getModel().getCurrentHitPoints() / getModel().getOriginalHitPoints());
                   }
        move();

        decideIfToShoot();

    }
    /**
     * change the movement direction on view and model
     */
    @Override
    protected void changeDirection() {
        mCopModel.setStepCounter(Common.random.nextInt(mCopModel.getStepsLimit()));
        Direction direction = Common.random.nextBoolean() ? Direction.LEFT : Direction.RIGHT;
        mCopModel.setDirection(direction);
        getView().walkAnimation(direction);
    }

    @Override
    public void remove() {

        super.remove();
    }

    @Override
    public boolean resurrect() {
        return super.resurrect();

    }

    private boolean dieIfDying() {
        if(getModel().isDying()){
            getModel().setDead(true);
            getModel().setDying(false);


                }

        return false;
    }

    private void decideIfToShoot() {
        int rand = Common.random.nextInt(mCopModel.getChanceNotToShoot());
        if (rand == 1) {
            shoot();
        }
    }

    private boolean loadIfLoading() {
        if (mCopModel.isLoading()) {
            if (mCopModel.getLoadingTimeCounter() == 0) {
                mCopModel.setShooting(true);
                mCopModel.setLoading(false);
            } else {
                mCopModel.decreaseLoadingTimeCounter();
            }

            return true;
        }
        return false;
    }

    private boolean shootIfShooting() {
        if (mCopModel.isShooting()) {
            mCopModel.setShooting(false);
            return true;
        }
        return false;
    }

    private void changeCopDirectionOnBorders(int screenBorders) {
        float x = mCopView.getX();
        Direction direction=null;
        if (x <= screenBorders)direction=Direction.RIGHT;
        else if (x >= Common.getScreenSize(mCopView.getContext()).x - screenBorders)
            direction=Direction.LEFT;
        if(direction!=null){
            getModel().setDirection(direction);
            getView().walkAnimation(direction);
        }
    }

    void shoot() {
        getView().shootAnimation(getModel().getDirection());
        mCopModel.makeShot();
    }

    private void move() {
            chooseDirectionAndGo();
            mCopModel.decreaseStepCounter();
            if (mCopModel.getStepCounter() == 0) changeDirection();

    }


    private void chooseDirectionAndGo() {
        if (mCopModel.getDirection() == null) {
            Direction direction = Common.random.nextBoolean() ? Direction.LEFT : Direction.RIGHT;

            mCopModel.setDirection(direction);
    }
        float x = mCopView.getX();
        switch (mCopModel.getDirection()) {
            case LEFT:
                if (x - 1 >= 0) {
                    mCopView.setX(x - mCopModel.getXSpeed());
                } else changeDirection();
                break;
            case RIGHT:
                if (x < mCopView.getContainer().getWidth()) mCopView.setX(x + mCopModel.getXSpeed());
                else changeDirection();
                break;
            default:changeDirection();
        }

    }
    private class ClearAnimationAction implements Runnable{

        /**
         * Starts executing the active part of the class' code. This method is
         * called when a thread is started that has been created with a class which
         * implements {@code Runnable}.
         */
        @Override
        public void run() {
            getView().clearAnimation();
        }
    }
}




