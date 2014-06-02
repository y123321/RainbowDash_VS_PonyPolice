package il.co.ovalley.rdvsponeypolice.Controller;

import android.content.Context;
import il.co.ovalley.rdvsponeypolice.Common;
import il.co.ovalley.rdvsponeypolice.Model.Drop;
import il.co.ovalley.rdvsponeypolice.View.DropView;

/**
 * Created by yuval on 30/04/2014.
 */
public class DropController extends GameController {
    public DropController(Context context, Drop gameObject, DropView gameView,boolean isOutOfGame) {
        super(context, gameObject, gameView,isOutOfGame);
    }


    /**
     * returns true if view should be updated or false if it shouldn't
     */
    @Override
    protected boolean runModelUpdate() {
        if(killIfOutOfScreen()) return false;
        return true;
    }

    /**
     * updates on the view, this method runs on UI thread
     */
    @Override
    public void runViewUpdate() {

        move();

    }

    private boolean killIfOutOfScreen() {
        if (checkIfOutOfScreen()) {
            mModel.setDead(true);
            return true;
        }
        return false;

    }

    private boolean checkIfOutOfScreen() {
        return mView.getY() > Common.getScreenSize(getContext()).y;
    }

    private void move() {
        mView.setY(mView.getY() + mModel.getYSpeed());
    }

    @Override
    public void changeDirection() {
        mView.setY(-mView.getY());
    }
}
