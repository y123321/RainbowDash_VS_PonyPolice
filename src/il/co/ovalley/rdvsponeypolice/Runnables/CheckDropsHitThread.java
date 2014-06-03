package il.co.ovalley.rdvsponeypolice.Runnables;

import android.graphics.Rect;
import android.util.Log;
import il.co.ovalley.rdvsponeypolice.Controller.CopController;
import il.co.ovalley.rdvsponeypolice.Controller.DropController;
import il.co.ovalley.rdvsponeypolice.Controller.GameController;

import java.util.ArrayList;

/**
 * Created by yuval on 30/04/2014.
 */
public class CheckDropsHitThread implements Runnable {

    ArrayList<CopController> mCops;
    ArrayList<DropController> mDrops;
    Rect copRect;
    Rect dropRect;
    int i;
    int j;
    public CheckDropsHitThread(ArrayList<GameController> controllers) {
        mCops = new ArrayList<CopController>();
        mDrops = new ArrayList<DropController>();
        for (GameController controller : controllers) {
            if (controller instanceof CopController) mCops.add((CopController) controller);
            if (controller instanceof DropController) mDrops.add((DropController) controller);

        }
        copRect=new Rect();
        dropRect=new Rect();
    }
//using a different thread is alright if i can use dual core, too bad most phones has only one
    @Override
    public void run() {
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        while (GameModel.isRunning) {
            for (i=0;i<mCops.size();i++) {
                if (mCops.get(i).getModel().isDead() || mCops.get(i).isOutOfGame()) continue;
                mCops.get(i).getView().getHitRect(copRect);
                for (j=0;j<mDrops.size();j++) {
                    if (mDrops.get(j).getModel().isDead() || mDrops.get(j).isOutOfGame()) continue;
                    mDrops.get(j).getView().getHitRect(dropRect);
                    if (dropRect.intersect(copRect)) {
                        kill(mDrops.get(j), mCops.get(i));
                    }
                }
//            }
//            try {
//                Thread.sleep(GameModel.ITERATION_PAUSE_TIME);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        }
    //    Log.d("test","drops thread dead");
    }

    private void kill(final DropController drop, final CopController cop) {
            Log.d("test", "hit cop: " + cop.getModel().toString() + " x: " + cop.getView().getX() + " y: " + cop.getView().getY());
            drop.getModel().setDead(true);
            if (cop.getModel().getCurrentHitPoints()-cop.getModel().getHitsToHandle()>0 && !cop.getModel().isDying() && !cop.getModel().isDead()) cop.getModel().setHit();
    }
}
