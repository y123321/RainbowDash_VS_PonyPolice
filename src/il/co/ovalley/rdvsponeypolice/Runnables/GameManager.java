package il.co.ovalley.rdvsponeypolice.Runnables;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import il.co.ovalley.rdvsponeypolice.Common;
import il.co.ovalley.rdvsponeypolice.Controller.*;
import il.co.ovalley.rdvsponeypolice.Model.Cop;
import il.co.ovalley.rdvsponeypolice.Model.CopType;
import il.co.ovalley.rdvsponeypolice.Model.GameModel;
import il.co.ovalley.rdvsponeypolice.Model.Loc;
import il.co.ovalley.rdvsponeypolice.View.GameLayoutView;

import java.util.ArrayList;

/**
 * Created by yuval on 30/04/2014.
 */
public class GameManager implements Runnable{
    Object m_PauseObject;
    public volatile ArrayList<GameController> m_Controllers;
    public GameLayoutView m_Layout;
    private Context m_Context;
    private GameModel m_GameModel = new GameModel();
    private RainbowDashController m_RainbowDashController;
    private LruCacheManager cacheManager=new LruCacheManager();
    private boolean m_isPause;

    public GameManager(GameModel gameModel, GameLayoutView gameLayoutView) {
        m_Layout = gameLayoutView;
        m_Context = gameLayoutView.getContext();
        m_GameModel = gameModel;

    }

    private void adjustDropLocToGameViewBehind(Loc location, GameController controller) {
        if (!controller.getModel().isRight()) {
            location.x += controller.getView().getWidth()-controller.getView().getShotPadding();
        }
     //   else location.x+=30;
    }

    private void init() {

        m_Controllers = new ArrayList<GameController>();
        m_RainbowDashController = GameFactory.createRainbowDashController(m_Layout);
        m_Controllers.add(m_RainbowDashController);
        m_RainbowDashController.startRDListener();
        for(int i=0;i<50;i++){
            m_Controllers.add(GameFactory.createDropController(m_Layout));
            m_Controllers.add(GameFactory.createShotController(m_Layout));
        }
        for(int i=0;i<30;i++){
            m_Controllers.add(GameFactory.createCopController(CopType.NINJA,m_Layout));
            m_Controllers.add(GameFactory.createCopController(CopType.SIMPLE, m_Layout));
        }
       // checkHits=new CheckDropsHitThread(m_Controllers);
        m_PauseObject=new Object();
        m_isPause=false;
        startThreads();



    }

    private void startThreads() {
        Thread thread1=new Thread(new CheckDropsHitThread(m_Controllers));
        Thread thread2=new Thread(new CheckShotsHitThread(m_Controllers,m_RainbowDashController));
        thread1.setDaemon(true);
        thread2.setDaemon(true);
        thread1.start();
        thread2.start();
    }


    public void action() {
        //     Log.d("test", "rainbow dash " + m_RainbowDashController.m_RainbowDash.goingToY);
        if(m_RainbowDashController.getModel().isDropping())releaseDrop();
        if(m_RainbowDashController.getModel().isLost()) GameModel.isRunning=false;
        for (GameController controller : m_Controllers) {
            if (!controller.isOutOfGame()) {
                if (controller.getModel().isDead()) remove(controller);


                else {
                    if (m_GameModel.get_LoopsCounter() % controller.getModel().getWaitTime() == 0) {
                        controller.update();
                        if(controller instanceof CopController){
                            CopController cop=(CopController)controller;
                            if (cop.getModel().isShooting()) shoot(cop);
                        }
                    }
                }
            }



        }
        m_GameModel.increaseLoopsCounter();
        spawnCops();


    }
    private void releaseDrop() {
        m_RainbowDashController.getModel().setDropping(false);
        ((Activity)m_Context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getNewDrop();

            }
        });

    }
    private void shoot(final CopController cop) {
        cop.getModel().setShooting(false);
        ((Activity)m_Context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getNewShot(cop);

            }
        });

    }



    private void remove(final GameController controller) {
        ((Activity) m_Context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_Layout.removeView(controller.getView());
                controller.setOutOfGame(true);

            }

        });

    }


    private void spawnCops() {
        if (m_GameModel.get_LoopsCounter() % m_GameModel.get_CopsSpawnTime()==0) {
            CopType copType=m_GameModel.get_LoopsCounter()>3000? CopType.NINJA:CopType.SIMPLE;
            getNewCop(copType);
        }
            m_GameModel.increaseOnScreenCopsCounter();

        }

    private void getNewCop(CopType type) {
        for(GameController controller:m_Controllers){
            if(controller instanceof CopController && controller.isOutOfGame()) {
                if (((Cop) controller.getModel()).getType() == type) {
                    controller.resurrect();
                    return;
                }
            }
        }
    }
    private void getNewDrop() {
        for(GameController controller:m_Controllers){
            if(controller instanceof DropController && controller.isOutOfGame()){
                controller.resurrect();
                Loc rdLocation= Common.getViewLocation(m_RainbowDashController.getView(),m_RainbowDashController.getModel().loc);
                adjustDropLocToGameViewBehind(rdLocation, m_RainbowDashController);
                Common.setViewLocation(controller.getView(), rdLocation);
                return;
            }
        }
    }
    private void getNewShot(CopController cop) {
        for (GameController controller : m_Controllers) {
            if (controller instanceof ShotController && controller.isOutOfGame()) {
                controller.resurrect();
                Loc location= Common.getViewLocation(cop.getView(),cop.getModel().loc);
                adjustDropLocToGameViewBehind(location, cop);
                Common.setViewLocation(controller.getView(), location);
                return;
            }

        }

    }

    @Override
    public void run() {
        GameModel.isRunning=true;
        init();
        while (GameModel.isRunning) {

            action();
            try {
                Thread.sleep(Common.ITERATION_PAUSE_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(m_isPause)
            try {
                synchronized (m_PauseObject){
                    m_PauseObject.wait();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d("test", "game thread dead");

    }
    public void resume(){
        m_isPause=false;
        synchronized (m_PauseObject) {
            m_PauseObject.notify();
        }
    }
    public void pauseGame() {
        m_isPause = true;

    }
}



