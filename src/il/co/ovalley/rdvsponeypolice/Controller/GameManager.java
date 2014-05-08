package il.co.ovalley.rdvsponeypolice.Controller;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import il.co.ovalley.rdvsponeypolice.Common;
import il.co.ovalley.rdvsponeypolice.Model.*;
import il.co.ovalley.rdvsponeypolice.View.DropView;
import il.co.ovalley.rdvsponeypolice.View.GameLayoutView;
import il.co.ovalley.rdvsponeypolice.View.GameView;

import java.util.ArrayList;

/**
 * Created by yuval on 30/04/2014.
 */
public class GameManager {
    private Context m_Context;
    public ArrayList<GameController> m_Controllers;
    public GameLayoutView m_Layout;
    private int m_LoopsCounter;
    private GameFactory m_Factory;
    private int m_OnScreenCopsCounter;
    private int m_CopsLimit;
    private volatile int m_CopsSpawnTime;
    private volatile ArrayList<CopController> m_Cops;
    private ArrayList<ShotController> m_Shots;
    private ArrayList<DropController> m_Drops;
    private RainbowDashController m_RainbowDashController;


    public GameManager(Context context,GameLayoutView gameLayoutView){
        m_Layout= gameLayoutView;
        m_Context =context;
        init();
    }

    private void init() {
        m_LoopsCounter=1;//only god can divide by zero
        m_Factory=new GameFactory(m_Layout);
        m_OnScreenCopsCounter=0;
        m_Controllers =new ArrayList<GameController>();
        m_Cops=new ArrayList<CopController>();
        m_Shots=new ArrayList<ShotController>();
        m_CopsLimit=40;
    //    m_Controllers.add(m_RainbowDashController);
        m_CopsSpawnTime=500;
        m_RainbowDashController=GameFactory.createRainbowDashController(m_Layout);
      ;
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
    public void action(){
        int i=0;
        while (i< m_Cops.size()) {
            CopController controller = m_Cops.get(i);
            i++;
            if (controller != null)
                updateController(controller);
        }
        i=0;

            spawnCops();

        m_LoopsCounter++;

    }
        private void updateController(GameController controller) {
        if(m_LoopsCounter%controller.getModel().getWaitTime()==0)
        controller.update();

    }

    private void spawnCops() {
        if(m_LoopsCounter%m_CopsSpawnTime==0 &&m_OnScreenCopsCounter<m_CopsLimit) {
            CopController copC=null;
            if(m_LoopsCounter%2==0)
                copC= m_Factory.createCopController(CopType.NINJA,m_Layout);
            else copC= m_Factory.createCopController(CopType.SIMPLE,m_Layout);
            m_Cops.add(copC);
            m_OnScreenCopsCounter++;

        }

    }


}
