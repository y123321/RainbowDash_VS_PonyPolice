package il.co.ovalley.rdvsponeypolice.Runnables;

import il.co.ovalley.rdvsponeypolice.Common;
import il.co.ovalley.rdvsponeypolice.Controller.GameController;
import il.co.ovalley.rdvsponeypolice.Controller.RainbowDashController;
import il.co.ovalley.rdvsponeypolice.Controller.ShotController;

import java.util.ArrayList;

/**
 * Created by yuval on 12/05/2014.
 */
public class CheckShotsHitThread implements Runnable{
    private volatile ArrayList<ShotController> m_Shots;
    private volatile RainbowDashController m_RD;
    public CheckShotsHitThread(ArrayList<GameController> controllers,RainbowDashController rainbowDashController){
        m_Shots=new ArrayList<ShotController>();
        for(GameController controller:controllers){
            if(controller instanceof ShotController) m_Shots.add((ShotController)controller);
        }
        m_RD=rainbowDashController;
    }
    @Override
    public void run() {
        while (true){
            m_RD.getView().getHitRect(m_RD.hitRect);
            for(ShotController shot:m_Shots){
                shot.getView().getHitRect(shot.hitRect);

                if(shot.hitRect.intersect(m_RD.hitRect))kill(shot,m_RD);
            }
            try {
                Thread.sleep(Common.ITERATION_PAUSE_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void kill(ShotController shot, RainbowDashController rd) {

        shot.getModel().setDead(true);
        rd.getModel().setCaptured(true);
    }
    }


