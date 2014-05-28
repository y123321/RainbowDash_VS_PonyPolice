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

import java.util.ArrayList;

/**
 * Created by yuval on 30/04/2014.
 */
public class GameManager implements Runnable {
    Object mPauseObject;
    public volatile ArrayList<GameController> mControllers;
    public ViewGroup mLayout;
    private Context mContext;
    private GameModel mGameModel;
    private RainbowDashController mRainbowDashController;
    private TextView mScoreView;
    private GetDropAction mGetDropAction;
    private GetShotAction mGetShotAction;
    private SetScoreAction mSetScoreAction;
    private ImageView mGameOver;
    private CheckDropsHitThread mCheckDropsHit;
    private CheckShotsHitThread mCheckShotsHit;

    public GameManager(GameModel gameModel, ViewGroup gameLayoutView, TextView scoreView, ImageView gameOverImage) {
        mLayout = gameLayoutView;
        mScoreView = scoreView;
        mContext = gameLayoutView.getContext();
        mGameModel = gameModel;
        mGameOver = gameOverImage;


    }

    @Override
    public void run() {
        GameModel.isRunning = true;
        init();

        pauseGame();
        pauseIfNeeded();
        ((Activity)mContext).runOnUiThread(new StartRainbowAction());
        //to prevent gc from starting during the game
        System.gc();
        //start game Loop
        while (GameModel.isRunning) {
            //all game related functionalities
            action();
            //sleep for few ml at the end of each loop
            try {
                Thread.sleep(GameModel.ITERATION_PAUSE_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pauseIfNeeded();
        }
        endGameAnimations();
        expandGameOver();
        Log.d("test", "game thread dead");

    }

    private void expandGameOver() {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //       mLayout.addView(mGameOver);
                expand(mGameOver);
            }
        });
    }

    private void adjustDropLocToGameViewBehind(Loc location, GameController controller) {
        if (!controller.getModel().isRight()) {
            location.x += controller.getView().getWidth() - controller.getView().getShotPadding();
        }
    }

    private void adjustShotLocToCopHorn(Loc location, GameController controller) {
        switch (controller.getModel().getDirection()) {
            case RIGHT:
                location.x += controller.getView().getWidth() - controller.getView().getShotPadding();
                break;

        }
        location.y -= 20;
    }

    private void init() {
        mRainbowDashController = GameFactory.createRainbowDashController(mLayout);
        mControllers = new ArrayList<GameController>();
        mControllers.add(mRainbowDashController);
        mPauseObject = new Object();
        addDropsAndShots(mGameModel.getNumberOfDropsAndShots());
        addCops(mGameModel.getNumberOfCopsPerType());
        initGameViewsForCache();
        mGameModel.setOnScreenCopsCounter(0);
        mGameModel.mIsPause = false;
        initUiRunnables();
        initHitsDetection();
        //startThreads();


    }

    private void endGameAnimations() {
        for (final GameController controller : mControllers) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (controller.getView().getDrawable() instanceof AnimationDrawable)
                        ((AnimationDrawable) controller.getView().getDrawable()).stop();
                }
            });
        }
        ;
    }

    private void pauseIfNeeded() {
        if (mGameModel.mIsPause)
            try {
                synchronized (mPauseObject) {
                    mPauseObject.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    public void action() {
        //     Log.d("test", "rainbow dash " + mRainbowDashController.mRainbowDash.goingToY);
        checkHits();
        int amountOfGameLoops = 200;
        decreaseCopsSpaenTimeIfNeeded(amountOfGameLoops);
        releaseDropIfNeeded();
        loseGameIfNeeded();
        updateControllers();
        mGameModel.increaseLoopsCounter();
        spawnCops();

    }

    private void updateControllers() {
        for (GameController controller : mControllers) {
            //remove dead objects if needed
            if (!controller.isOutOfGame()) {
                if (onlyModelIsDead(controller)) {
                    controller.remove();
                    if (isCop(controller)) {
                        KillCopAndUpdateScore(controller);
                    }
                } else {
                    //update objects
                    if (isTimeToUpdate(controller)) {
                        controller.update();
                        if (isCop(controller)) {
                            CopController cop = (CopController) controller;
                            shootIfNeeded(cop);
                        }
                    }
                }
            }
        }
    }

    private void shootIfNeeded(CopController cop) {
        if (cop.getModel().isShooting()) {
            shoot(cop);
        }
    }

    private boolean isTimeToUpdate(GameController controller) {
        return mGameModel.getLoopsCounter() % controller.getModel().getWaitTime() == 0;
    }

    private boolean isCop(GameController controller) {
        return controller.getModel() instanceof Cop;
    }

    private void KillCopAndUpdateScore(GameController controller) {
        mGameModel.decreaseOnScreenCopsCounter();
        mGameModel.addToScore(((Cop) controller.getModel()).getScorePoints());
        ((Activity) mContext).runOnUiThread(mSetScoreAction);
    }

    private boolean onlyModelIsDead(GameController controller) {
        return controller.getModel().isDead() && !controller.getView().isRemoved;
    }

    private void loseGameIfNeeded() {
        if (mRainbowDashController.getModel().isLost()) GameModel.isRunning = false;
    }

    private void releaseDropIfNeeded() {
        if (mRainbowDashController.getModel().isDropping()) releaseDrop();
    }

    private void decreaseCopsSpaenTimeIfNeeded(int amountOfGameLoops) {
        if (mGameModel.getLoopsCounter() % amountOfGameLoops == 0 && mGameModel.getCopsSpawnTime() > 30)
            mGameModel.decreaseCopsSpawnTime(1);
    }

    private void checkHits() {
        mCheckDropsHit.run();
        mCheckShotsHit.run();
    }

    private void initGameViewsForCache() {
        for (int i = 0; i < mControllers.size(); i++) {
            mControllers.get(i).getView().initGameView();
        }
        for (int i = 1; i < mControllers.size(); i++) {
            mControllers.get(i).remove();

        }
    }

    private void addDropsAndShots(int numberOfDropsAndShots) {
        for (int i = 1; i <= numberOfDropsAndShots; i++) {
            mControllers.add(GameFactory.createDropController(mLayout));
            mControllers.add(GameFactory.createShotController(mLayout));
        }
    }

    private void addCops(int numberOfCopsPerType) {
        for (int i = 0; i < numberOfCopsPerType; i++) {
            mControllers.add(GameFactory.createCopController(new NinjaCop(), mLayout));
            mControllers.add(GameFactory.createCopController(new SimpleCop(), mLayout));
            mControllers.add(GameFactory.createCopController(new BruteCop(), mLayout));
            mControllers.add(GameFactory.createCopController(new CamouflageCop(), mLayout));
        }
    }

    private void initHitsDetection() {
        mCheckShotsHit = new CheckShotsHitThread(mControllers, mRainbowDashController);
        mCheckDropsHit = new CheckDropsHitThread(mControllers);
    }

    private void initUiRunnables() {
        mGetDropAction = new GetDropAction();
        mGetShotAction = new GetShotAction();
        mSetScoreAction = new SetScoreAction();
    }

    private void startThreads() {
        Thread thread1 = new Thread(new CheckDropsHitThread(mControllers));
        Thread thread2 = new Thread(new CheckShotsHitThread(mControllers, mRainbowDashController));
        thread1.setDaemon(true);
        thread2.setDaemon(true);
        thread1.start();
        thread2.start();
    }


    private void releaseDrop() {
        mRainbowDashController.getModel().setDropping(false);
        ((Activity) mContext).runOnUiThread(mGetDropAction);

    }

    private void shoot(final CopController cop) {
        cop.getModel().setShooting(false);
        mGetShotAction.cop = cop;
        ((Activity) mContext).runOnUiThread(mGetShotAction);

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

        int res = loopsCounter / mGameModel.getNumberOfLoopsForTypeChange();
        if (res > CopType.values().length - 1) res = CopType.values().length - 1;
        // res%=CopType.values().length;
        if (res != 0) res = Common.random.nextInt() % 2 == 1 ? res : res - 1;
        return res;
    }

    private void getNewCop(CopType type) {
        for (GameController controller : mControllers) {
            if (controller instanceof CopController && controller.isOutOfGame() && !controller.getModel().isDead()) {
                if (((Cop) controller.getModel()).getType() == type) {
                    controller.resurrect();
                    return;
                }
            }
        }
    }

    private void getNewDrop() {
        for (GameController controller : mControllers) {
            if (controller instanceof DropController && controller.isOutOfGame()) {
                controller.resurrect();
                Loc rdLocation = Common.getViewLocation(mRainbowDashController.getView(), mRainbowDashController.getModel().loc);
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
                Loc location = Common.getViewLocation(cop.getView(), cop.getModel().loc);
                adjustShotLocToCopHorn(location, cop);
                Common.setViewLocation(controller.getView(), location);
                return;
            }

        }

    }


    public void resume() {
        if (mPauseObject == null) return;
        mGameModel.mIsPause = false;
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

    public boolean isRunning() {
        return mGameModel.isRunning;
    }

    private class GetDropAction implements Runnable {
        @Override
        public void run() {
            getNewDrop();

        }
    }

    private class StartRainbowAction implements Runnable {

        @Override
        public void run() {
            mRainbowDashController.changeDirection();
            mRainbowDashController.startRDListener();
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
    public void expand(final View v) {
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
        a.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                openMenu();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        a.setDuration(1500);//(int)(targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setStartOffset(0);
        v.startAnimation(a);
        v.setVisibility(View.VISIBLE);

    }

    private void openMenu() {
        final Activity activity=(Activity)mContext;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.openOptionsMenu();
            }
        });
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



