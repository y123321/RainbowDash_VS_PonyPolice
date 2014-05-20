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

    ArrayList<CopController> m_Cops;
    ArrayList<DropController> m_Drops;

    public CheckDropsHitThread(GameController[] controllers) {
        m_Cops = new ArrayList<CopController>();
        m_Drops = new ArrayList<DropController>();
        for (GameController controller : controllers) {
            if (controller instanceof CopController) m_Cops.add((CopController) controller);
            if (controller instanceof DropController) m_Drops.add((DropController) controller);

        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (GameModel.isRunning) {
            for (CopController cop : m_Cops) {
                if (cop.getModel().isDead() || cop.isOutOfGame()) continue;
                Rect copRect = cop.mHitRect;
                cop.getView().getHitRect(copRect);
                for (DropController drop : m_Drops) {
                    if (drop.getModel().isDead() || drop.isOutOfGame()) continue;
                    Rect dropRect = drop.mHitRect;
                    drop.getView().getHitRect(dropRect);
                    if (dropRect.intersect(copRect)) {
                        Log.d("test","cop rect: "+copRect.toString()+" drop rect: "+dropRect.toString());
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
