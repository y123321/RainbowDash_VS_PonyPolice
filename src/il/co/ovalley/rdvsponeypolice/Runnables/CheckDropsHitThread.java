package il.co.ovalley.rdvsponeypolice.Runnables;

import android.graphics.Rect;
import android.util.Log;
import il.co.ovalley.rdvsponeypolice.Controller.CopController;
import il.co.ovalley.rdvsponeypolice.Controller.DropController;
import il.co.ovalley.rdvsponeypolice.Controller.GameController;
import il.co.ovalley.rdvsponeypolice.Model.GameModel;

import java.util.ArrayList;

/**
 * Created by yuval on 30/04/2014.
 */
public class CheckDropsHitThread implements Runnable {

    ArrayList<CopController> mCops;
    ArrayList<DropController> mDrops;
    Rect copRect;
    Rect dropRect;

    public CheckDropsHitThread(GameController[] controllers) {
        mCops = new ArrayList<CopController>();
        mDrops = new ArrayList<DropController>();
        for (GameController controller : controllers) {
            if (controller instanceof CopController) mCops.add((CopController) controller);
            if (controller instanceof DropController) mDrops.add((DropController) controller);

        }
        copRect=new Rect();
        dropRect=new Rect();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (GameModel.isRunning) {
            for (CopController cop : mCops) {
                if (cop.getModel().isDead() || cop.isOutOfGame()) continue;
                cop.getView().getHitRect(copRect);
                for (DropController drop : mDrops) {
                    if (drop.getModel().isDead() || drop.isOutOfGame()) continue;
                    drop.getView().getHitRect(dropRect);
                    if (dropRect.intersect(copRect)) {
                        kill(drop, cop);
                    }
                }
            }
            try {
                Thread.sleep(GameModel.ITERATION_PAUSE_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        Log.d("test","drops thread dead");
    }

    private void kill(final DropController drop, final CopController cop) {
     //   Log.d("test","hit cop: "+cop.getModel().getType()+" x: "+cop.getView().getX()+" y: "+cop.getView().getY());
        drop.getModel().setDead(true);
        if(!cop.getModel().isDead()||!cop.getModel().isDying())cop.getModel().setHit();
    }
}
