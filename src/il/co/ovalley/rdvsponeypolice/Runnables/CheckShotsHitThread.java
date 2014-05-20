package il.co.ovalley.rdvsponeypolice.Runnables;

import android.util.Log;
import il.co.ovalley.rdvsponeypolice.Controller.GameController;
import il.co.ovalley.rdvsponeypolice.Controller.RainbowDashController;
import il.co.ovalley.rdvsponeypolice.Controller.ShotController;
import il.co.ovalley.rdvsponeypolice.Model.GameModel;

import java.util.ArrayList;

/**
 * Created by yuval on 12/05/2014.
 */
public class CheckShotsHitThread implements Runnable {
    private volatile ArrayList<ShotController> m_Shots;
    private volatile RainbowDashController m_RD;

    public CheckShotsHitThread(GameController[] controllers, RainbowDashController rainbowDashController) {
        m_Shots = new ArrayList<ShotController>();
        for (GameController controller : controllers) {
            if (controller instanceof ShotController) m_Shots.add((ShotController) controller);
        }
        m_RD = rainbowDashController;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (GameModel.isRunning) {
            m_RD.getView().getHitRect(m_RD.mHitRect);
            for (ShotController shot : m_Shots) {
                if(shot.isOutOfGame()||shot.getModel().isDead()) continue;
                shot.getView().getHitRect(shot.mHitRect);
                if (shot.mHitRect.intersect(m_RD.mHitRect)) kill(shot, m_RD);
            }
            try {
                Thread.sleep(GameModel.ITERATION_PAUSE_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        Log.d("test", "shots thread dead");

    }

    private void kill(ShotController shot, RainbowDashController rd) {

        shot.getModel().setDead(true);
        rd.getModel().setCaptured(true);
    }
}



