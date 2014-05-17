package il.co.ovalley.rdvsponeypolice.Runnables;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import il.co.ovalley.rdvsponeypolice.Common;
import il.co.ovalley.rdvsponeypolice.Controller.*;
import il.co.ovalley.rdvsponeypolice.Model.*;
import il.co.ovalley.rdvsponeypolice.View.GameLayoutView;

/**
 * Created by yuval on 30/04/2014.
 */
public class GameManager implements Runnable{
    Object m_PauseObject;
    public volatile GameController[] m_Controllers;// ArrayList<GameController> m_Controllers;
    public GameLayoutView m_Layout;
    private Context m_Context;
    private GameModel m_GameModel = new GameModel();
    private RainbowDashController m_RainbowDashController;
    private TextView m_ScoreView;
    private LruCacheManager cacheManager=new LruCacheManager();
    private boolean m_isPause;
    private String m_Name;

    public GameManager(GameModel gameModel, GameLayoutView gameLayoutView, TextView scoreView) {
        m_Layout = gameLayoutView;
        m_ScoreView = scoreView;
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

  //      m_Controllers = new ArrayList<GameController>();
        m_RainbowDashController = GameFactory.createRainbowDashController(m_Layout);
    //    m_Controllers.add(m_RainbowDashController);
        m_RainbowDashController.startRDListener();
        m_Controllers=new GameController[m_GameModel.getNumberOfCopsPerType()*CopType.values().length+m_GameModel.getNumberOfDrops()+1];
        m_Controllers[0]=m_RainbowDashController;
        for(int i=1;i<=m_GameModel.getNumberOfDrops();i+=2){
            m_Controllers[i]=GameFactory.createDropController(m_Layout);
            m_Controllers[i+1]=GameFactory.createShotController(m_Layout);
        }
        for(int i=m_GameModel.getNumberOfDrops()+1;i< m_Controllers.length;i+=CopType.values().length){
            m_Controllers[i]=GameFactory.createCopController(new NinjaCop(),m_Layout);
            m_Controllers[i+1]=GameFactory.createCopController(new SimpleCop(), m_Layout);
            m_Controllers[i+2]=GameFactory.createCopController(new BruteCop(),m_Layout);
            m_Controllers[i+3]=(GameFactory.createCopController(new CamouflageCop(),m_Layout));
        }
       // checkHits=new CheckDropsHitThread(m_Controllers);
        m_PauseObject=new Object();
        m_isPause=false;
//        m_ScoreView.setText(0);

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
                if (controller.getModel().isDead()) {
                    remove(controller);
                    if(controller.getModel() instanceof Cop){
                        controller.getModel().setDead(false);
                        m_GameModel.addToScore(((Cop) controller.getModel()).getScorePoints());
                        ((Activity)m_Context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                m_ScoreView.setText(m_GameModel.getScore()+"");

                            }
                        });
                    }
                }


                else {
                    if (m_GameModel.get_LoopsCounter() % controller.getModel().getWaitTime() == 0) {
                        controller.update();
                        if(controller instanceof CopController){
                            CopController cop=(CopController)controller;
                            if (cop.getModel().isShooting()) {
                                shoot(cop);
                            }
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
                cop.getModel().setShooting(false);


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
            int type= getNeededCopTypeInt();
                CopType copType=CopType.values()[type];
            getNewCop(copType);
        }
            m_GameModel.increaseOnScreenCopsCounter();

        }

    private int getNeededCopTypeInt() {

        int loopsCounter = m_GameModel.get_LoopsCounter();

        int res = loopsCounter / m_GameModel.getNumberOfLoopsForTypeChange() ;
        if(res>CopType.values().length-1) res=CopType.values().length-1;
       // res%=CopType.values().length;
        if(res!=0)res=Common.random.nextInt()%2==1?res:res-1;
        return res;
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
                Thread.sleep(GameModel.ITERATION_PAUSE_TIME);
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

    public int getScore() {
        return m_GameModel.getScore();
    }
}



