package il.co.ovalley.rdvsponeypolice.Runnables;

import android.view.MotionEvent;
import android.view.View;
import il.co.ovalley.rdvsponeypolice.Common;
import il.co.ovalley.rdvsponeypolice.Controller.DropController;
import il.co.ovalley.rdvsponeypolice.Controller.GameFactory;
import il.co.ovalley.rdvsponeypolice.Controller.RainbowDashController;
import il.co.ovalley.rdvsponeypolice.Model.Direction;
import il.co.ovalley.rdvsponeypolice.Model.GameLayout;
import il.co.ovalley.rdvsponeypolice.Model.Loc;
import il.co.ovalley.rdvsponeypolice.Model.RainbowDash;
import il.co.ovalley.rdvsponeypolice.View.DropView;
import il.co.ovalley.rdvsponeypolice.View.GameView;

import java.util.ArrayList;

/**
 * Created by yuval on 30/04/2014.
 */
public class RainbowDashThread implements Runnable {
    private RainbowDashController m_RainbowDashController;
    private GameLayout m_Layout;
    private int iterationsCounter;
    private volatile ArrayList<DropController> m_Drops;

    public RainbowDashThread(RainbowDashController controller){
        m_RainbowDashController =controller;
        m_Layout=controller.m_Layout;
        m_Drops=new ArrayList<DropController>();
        iterationsCounter=0;
        startRDListener();
    }

    @Override
    public void run() {
        while (!m_RainbowDashController.m_RainbowDash.isLost()) {
            try {
                m_RainbowDashController.update();
                int i=0;
                while (i<m_Drops.size()){
                    if(i<0)i=0;
                    DropController controller=m_Drops.get(i);
                    controller.update();
                    i++;
                    if (controller != null)
                    if(iterationsCounter%controller.getModel().getWaitTime()==0){
                        controller.update();
                    }
                }

                Thread.sleep(Common.ITERATION_PAUSE_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void startRDListener() {
        m_Layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RainbowDash rd = (RainbowDash) m_RainbowDashController.getModel();
                if (rd.isDead() || rd.isCaged() || rd.isLost()) return true;
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        m_RainbowDashController.setGoal(event.getX(), event.getY());
                        m_RainbowDashController.changeDirection();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        m_RainbowDashController.setGoal(event.getX(), event.getY());
                        m_RainbowDashController.changeDirection();
                        break;
                    case MotionEvent.ACTION_UP:
                        releaseDrop();
                        break;
                }
                return true;
            }
        });
        m_RainbowDashController.getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }
    public void stopRDListener() {
        m_Layout.setOnTouchListener(null);
    }

    private void releaseDrop() {
        DropController dc= GameFactory.createDropController(m_Layout);
        DropView drop=(DropView)dc.getView();
        Loc rdLocation= Common.getViewLocation(m_RainbowDashController.getView());
        Common.setViewLocation(m_RainbowDashController.getView(),rdLocation);
        adjustDropLocToGameViewBehind(rdLocation, m_RainbowDashController.getView());
        Common.setViewLocation(drop,rdLocation);
        m_Drops.add(dc);
    }
    private static void adjustDropLocToGameViewBehind(Loc location,GameView view) {
        if(view.getDirection()== Direction.LEFT){
            location.x+=view.getWidth();
        }
    }

}
