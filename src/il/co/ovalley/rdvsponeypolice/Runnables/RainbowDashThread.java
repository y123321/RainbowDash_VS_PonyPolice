package il.co.ovalley.rdvsponeypolice.Runnables;

import il.co.ovalley.rdvsponeypolice.Controller.DropController;
import il.co.ovalley.rdvsponeypolice.Controller.RainbowDashController;
import il.co.ovalley.rdvsponeypolice.View.GameLayoutView;

import java.util.ArrayList;

/**
 * Created by yuval on 30/04/2014.
 */
public class RainbowDashThread implements Runnable {
    private RainbowDashController m_RainbowDashController;
    private GameLayoutView m_Layout;
    private int iterationsCounter;
    private volatile ArrayList<DropController> m_Drops;

    public RainbowDashThread(RainbowDashController controller){
        m_RainbowDashController =controller;
        m_Layout=controller.m_Layout;
        m_Drops=new ArrayList<DropController>();
        iterationsCounter=0;
    }

    @Override
    public void run() {
        while (!m_RainbowDashController.m_RainbowDash.isLost()) {
            try {
                m_RainbowDashController.update();
               /*int i=0;
                while (i<m_Drops.size()){
                    if(i<0)i=0;
                    DropController controller=m_Drops.get(i);
                    controller.update();
                    i++;
                    if (controller != null)
                    if(iterationsCounter%controller.getModel().getWaitTime()==0){
                        controller.update();
                    }
                }*/

                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }




}
