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
    Object mPauseObject;
    public volatile GameController[] mControllers;// ArrayList<GameController> mControllers;
    public GameLayoutView mLayout;
    private Context mContext;
    private GameModel mGameModel = new GameModel();
    private RainbowDashController mRainbowDashController;
    private TextView mScoreView;
    private LruCacheManager cacheManager=new LruCacheManager();
    private boolean misPause;
    private String mName;
    public GameManager(GameModel gameModel, GameLayoutView gameLayoutView, TextView scoreView) {
        mLayout = gameLayoutView;
        mScoreView = scoreView;
        mContext = gameLayoutView.getContext();
        mGameModel = gameModel;

    }

    private void adjustDropLocToGameViewBehind(Loc location, GameController controller) {
        if (!controller.getModel().isRight()) {
            location.x += controller.getView().getWidth()-controller.getView().getShotPadding();
        }
    }
    private void adjustShotLocToCopHorn(Loc location, GameController controller) {
        switch (controller.getModel().getDirection()){
            case RIGHT:location.x += controller.getView().getWidth()-controller.getView().getShotPadding();
                break;

        }
        location.y-=20;
    }

    private void init() {

  //      mControllers = new ArrayList<GameController>();
        mRainbowDashController = GameFactory.createRainbowDashController(mLayout);
    //    mControllers.add(mRainbowDashController);
        mRainbowDashController.startRDListener();
        mControllers=new GameController[mGameModel.getNumberOfCopsPerType()*CopType.values().length+mGameModel.getNumberOfDrops()+1];
        mControllers[0]=mRainbowDashController;
        for(int i=1;i<=mGameModel.getNumberOfDrops();i+=2){
            mControllers[i]=GameFactory.createDropController(mLayout);
            mControllers[i+1]=GameFactory.createShotController(mLayout);
        }
        for(int i=mGameModel.getNumberOfDrops()+1;i< mControllers.length;i+=CopType.values().length){
            mControllers[i]=GameFactory.createCopController(new NinjaCop(),mLayout);
            mControllers[i+1]=GameFactory.createCopController(new SimpleCop(), mLayout);
            mControllers[i+2]=GameFactory.createCopController(new BruteCop(),mLayout);
            mControllers[i+3]=(GameFactory.createCopController(new CamouflageCop(),mLayout));
        }
        for(int i=0;i<mControllers.length;i++){
            mControllers[i].getView().initGameView();
        }
        for(int i=1;i<mControllers.length;i++){
            remove(mControllers[i]);

        }
       // checkHits=new CheckDropsHitThread(mControllers);
        mPauseObject=new Object();
        misPause=false;
//        mScoreView.setText(0);

        startThreads();


    }

    private void startThreads() {
        Thread thread1=new Thread(new CheckDropsHitThread(mControllers));
        Thread thread2=new Thread(new CheckShotsHitThread(mControllers,mRainbowDashController));
        thread1.setDaemon(true);
        thread2.setDaemon(true);
        thread1.start();
        thread2.start();
    }


    public void action() {

        //     Log.d("test", "rainbow dash " + mRainbowDashController.mRainbowDash.goingToY);
        if(mGameModel.getLoopsCounter()%10000==0)mGameModel.decreaseCopsSpawnTime(10);
        if(mRainbowDashController.getModel().isDropping())releaseDrop();
        if(mRainbowDashController.getModel().isLost()) GameModel.isRunning=false;
        for (GameController controller : mControllers) {
            if (!controller.isOutOfGame()) {
                if (controller.getModel().isDead()) {
                    remove(controller);
                    if(controller.getModel() instanceof Cop){
                        mGameModel.addToScore(((Cop) controller.getModel()).getScorePoints());
                        ((Activity)mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mScoreView.setText(mGameModel.getScore()+"");

                            }
                        });
                    }
                }


                else {
                    if (mGameModel.getLoopsCounter() % controller.getModel().getWaitTime() == 0) {
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
        mGameModel.increaseLoopsCounter();
        spawnCops();


    }
    private void releaseDrop() {
        mRainbowDashController.getModel().setDropping(false);
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getNewDrop();

            }
        });

    }
    private void shoot(final CopController cop) {
        cop.getModel().setShooting(false);
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getNewShot(cop);
                cop.getModel().setShooting(false);


            }
        });

    }



    private void remove(final GameController controller) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLayout.removeView(controller.getView());
                controller.setOutOfGame(true);

            }

        });

    }


    private void spawnCops() {
        if (mGameModel.getLoopsCounter() % mGameModel.getCopsSpawnTime()==0) {
            int type= getNeededCopTypeInt();
                CopType copType=CopType.values()[type];
            getNewCop(copType);
        }
            mGameModel.increaseOnScreenCopsCounter();

        }

    private int getNeededCopTypeInt() {

        int loopsCounter = mGameModel.getLoopsCounter();

        int res = loopsCounter / mGameModel.getNumberOfLoopsForTypeChange() ;
        if(res>CopType.values().length-1) res=CopType.values().length-1;
       // res%=CopType.values().length;
        if(res!=0)res=Common.random.nextInt()%2==1?res:res-1;
        return res;
    }

    private void getNewCop(CopType type) {
        for(GameController controller:mControllers){
            if(controller instanceof CopController && controller.isOutOfGame()) {
                if (((Cop) controller.getModel()).getType() == type) {
                    controller.resurrect();
                    return;
                }
            }
        }
    }
    private void getNewDrop() {
        for(GameController controller:mControllers){
            if(controller instanceof DropController && controller.isOutOfGame()){
                controller.resurrect();
                Loc rdLocation= Common.getViewLocation(mRainbowDashController.getView(),mRainbowDashController.getModel().loc);
                adjustDropLocToGameViewBehind(rdLocation, mRainbowDashController);
                Common.setViewLocation(controller.getView(), rdLocation);
                return;
            }
        }
    }
    private void getNewShot(CopController cop) {
        for (GameController controller : mControllers) {
            if (controller instanceof ShotController && controller.isOutOfGame()) {
                controller.resurrect();
                Loc location= Common.getViewLocation(cop.getView(),cop.getModel().loc);
                adjustShotLocToCopHorn(location, cop);
                Common.setViewLocation(controller.getView(), location);
                return;
            }

        }

    }

    @Override
    public void run() {
        GameModel.isRunning=true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    mRainbowDashController.getModel().goingToX=60;
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRainbowDashController.changeDirection();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        init();
        while (GameModel.isRunning) {

            action();
            try {
                Thread.sleep(GameModel.ITERATION_PAUSE_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(misPause)
            try {
                synchronized (mPauseObject){
                    mPauseObject.wait();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d("test", "game thread dead");

    }
    public void resume(){
        misPause=false;
        synchronized (mPauseObject) {
            mPauseObject.notify();
        }
    }
    public void pauseGame() {
        misPause = true;

    }

    public int getScore() {
        return mGameModel.getScore();
    }
}



