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

    /**
     * returns true if view should be updated or false if it shouldn't
     */
    @Override
    protected boolean runModelUpdate() {
        return true;
    }

    @Override
    public void runViewUpdate() {

        move();
        checkIfDead();
    }

    private void checkIfDead() {
        if(mView.getY()<1) mModel.setDead(true);
    }

    private void move() {
        mView.setY(mView.getY() - mModel.getYSpeed());
    }

    @Override
    public void changeDirection() {
        mModel.setYSpeed(-mModel.getYSpeed());
    }

}
