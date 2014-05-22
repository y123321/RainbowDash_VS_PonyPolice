package il.co.ovalley.rdvsponeypolice.Runnables;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
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
    private GameModel mGameModel;
    private RainbowDashController mRainbowDashController;
    private TextView mScoreView;
    private LruCacheManager cacheManager=new LruCacheManager();
    private GetDropAction mGetDropAction;
    private GetShotAction mGetShotAction;
    private SetScoreAction mSetScoreAction;
    private ImageView mGameOver;
    public GameManager(GameModel gameModel, GameLayoutView gameLayoutView, TextView scoreView,ImageView gameOverImage) {
        mLayout = gameLayoutView;
        mScoreView = scoreView;
        mContext = gameLayoutView.getContext();
        mGameModel = gameModel;
        mGameOver=gameOverImage;

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
            mControllers[i].remove();

        }
        mGameModel.setOnScreenCopsCounter(0);
       // checkHits=new CheckDropsHitThread(mControllers);
        mPauseObject=new Object();
        mGameModel.mIsPause =false;
//        mScoreView.setText(0);
        mGetDropAction=new GetDropAction();
        mGetShotAction=new GetShotAction();
        mSetScoreAction=new SetScoreAction();
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
        if(mGameModel.getLoopsCounter()%200==0 && mGameModel.getCopsSpawnTime()>30)mGameModel.decreaseCopsSpawnTime(1);
        if(mRainbowDashController.getModel().isDropping())releaseDrop();
        if(mRainbowDashController.getModel().isLost()) GameModel.isRunning=false;
        for (GameController controller : mControllers) {
            if (!controller.isOutOfGame()) {
                if (controller.getModel().isDead() && !controller.getView().isRemoved) {
                    controller.getView().isRemoved=true;
                    controller.remove();
                    if(controller.getModel() instanceof Cop){
                        mGameModel.decreaseOnScreenCopsCounter();
                        mGameModel.addToScore(((Cop) controller.getModel()).getScorePoints());
                        ((Activity)mContext).runOnUiThread(mSetScoreAction);
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
        ((Activity)mContext).runOnUiThread(mGetDropAction);

    }
    private void shoot(final CopController cop) {
        cop.getModel().setShooting(false);
        mGetShotAction.cop=cop;
        ((Activity)mContext).runOnUiThread(mGetShotAction);

    }
    private boolean spawnCops() {
        if ((mGameModel.getLoopsCounter() % mGameModel.getCopsSpawnTime() == 0)
               || (mGameModel.getLoopsCounter() > 100 && mGameModel.getOnScreenCopsCounter() < mGameModel.getMinAmountOfCops())) {
              //  ){
                mGameModel.increaseOnScreenCopsCounter();
            int type = getNeededCopTypeInt();
            CopType copType = CopType.values()[type];

            getNewCop(copType);

            return true;

        }
        return false;

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
            if(controller instanceof CopController && controller.isOutOfGame() &&!controller.getModel().isDead()) {
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
        init();
        System.gc();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Thread.sleep(500);
            //        mRainbowDashController.getModel().goingToX=60;
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRainbowDashController.changeDirection();
                            mRainbowDashController.startRDListener();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (GameModel.isRunning) {

            action();
            try {
                Thread.sleep(GameModel.ITERATION_PAUSE_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(mGameModel.mIsPause)
            try {
                synchronized (mPauseObject){
                    mPauseObject.wait();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(final GameController controller:mControllers){
            ((Activity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(controller.getView().getDrawable() instanceof AnimationDrawable)((AnimationDrawable) controller.getView().getDrawable()).stop();
                }
            });
        };
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
         //       mLayout.addView(mGameOver);
                expand(mGameOver);
            }
        });
        Log.d("test", "game thread dead");

    }
    public void resume(){
        mGameModel.mIsPause =false;
        synchronized (mPauseObject) {
            mPauseObject.notify();
        }
    }
    public void pauseGame() {
        mGameModel.mIsPause = true;

    }

    public int getScore() {
        return mGameModel.getScore();
    }
    private class GetDropAction implements Runnable {
        @Override
        public void run() {
            getNewDrop();

        }
    }
    private class GetShotAction implements Runnable {
        public CopController cop;
        @Override
        public void run() {
            getNewShot(cop);
            cop.getModel().setShooting(false);


        }
    }
    private class SetScoreAction implements Runnable{
            @Override
            public void run() {
                mScoreView.setText(mGameModel.getScore()+"");

            }
        }
    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targtetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targtetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(1500);//(int)(targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setStartOffset(0);
        v.startAnimation(a);
        v.setVisibility(View.VISIBLE);

    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

}



