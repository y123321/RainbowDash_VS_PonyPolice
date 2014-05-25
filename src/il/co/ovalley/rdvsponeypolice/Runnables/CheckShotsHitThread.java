package il.co.ovalley.rdvsponeypolice.Runnables;

import android.graphics.Rect;
import android.util.Log;
import il.co.ovalley.rdvsponeypolice.Controller.GameController;
import il.co.ovalley.rdvsponeypolice.Controller.RainbowDashController;
import il.co.ovalley.rdvsponeypolice.Controller.ShotController;

import java.util.ArrayList;

/**
 * Created by yuval on 12/05/2014.
 */
public class CheckShotsHitThread implements Runnable {
    private volatile ArrayList<ShotController> mShots;
    private volatile RainbowDashController mRD;
    private Rect shotRect;
    private Rect rdRect;
    public CheckShotsHitThread(GameController[] controllers, RainbowDashController rainbowDashController) {
        mShots = new ArrayList<ShotController>();
        for (GameController controller : controllers) {
            if (controller instanceof ShotController) mShots.add((ShotController) controller);
        }
        mRD = rainbowDashController;
        shotRect=new Rect();
        rdRect=new Rect();
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
            mRD.getView().getHitRect(rdRect);
            for (ShotController shot : mShots) {
                if(shot.isOutOfGame()||shot.getModel().isDead()) continue;
                shot.getView().getHitRect(shotRect);
                if (shotRect.intersect(rdRect)) kill(shot, mRD);
//            }
//            try {
//                Thread.sleep(GameModel.ITERATION_PAUSE_TIME);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
        }
   //     Log.d("test", "shots thread dead");

    }

    private void kill(ShotController shot, RainbowDashController rd) {
        Log.d("test","shot width: "+shot.getView().getWidth()+" RD width:"+rd.getView().getWidth()+" hit rect: "+ rdRect.left+", "+rdRect.right);
        shot.getModel().setDead(true);
        rd.getModel().setCaptured(true);
    }
}



