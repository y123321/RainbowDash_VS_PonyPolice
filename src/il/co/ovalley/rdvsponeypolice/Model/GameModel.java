package il.co.ovalley.rdvsponeypolice.Model;

import android.content.res.Resources;
import android.util.Log;
import il.co.ovalley.rdvsponeypolice.R;

/**
 * Created by yuval on 08/05/2014.
 */
public class GameModel {
    private int numberOfDropsAndShots;
    private int mLoopsCounter;
    private volatile int mOnScreenCopsCounter;
    private int mCopsLimit;
    private int mScore;
    public static int ITERATION_PAUSE_TIME=10;
    private int numberOfCopsPerType;
    public volatile boolean mIsPause;
    private int minAmountOfCops;

    public void setOnScreenCopsCounter(int onScreenCopsCounter) {
        this.mOnScreenCopsCounter = onScreenCopsCounter;
    }
    public int getScore() {
        return mScore;
    }

    public void addToScore(int score) {
        this.mScore += score;
    }
    public void setScore(int mScore) {
        this.mScore = mScore;
    }

    private volatile int mCopsSpawnTime;
    public static volatile boolean isRunning;

    public int getNumberOfLoopsForTypeChange() {
        return mNumberOfLoopsForTypeChange;
    }

    public void setNumberOfLoopsForTypeChange(int mNumberOfLoopsForTypeChange) {
        this.mNumberOfLoopsForTypeChange = mNumberOfLoopsForTypeChange;
    }

    private int mNumberOfLoopsForTypeChange;
    public int getLoopsCounter() {
        return mLoopsCounter;
    }

    public void increaseLoopsCounter() {
        this.mLoopsCounter++;
    }

    public int getOnScreenCopsCounter() {
        return mOnScreenCopsCounter;
    }

    public void increaseOnScreenCopsCounter() {
        synchronized (this) {
            mOnScreenCopsCounter++;
            Log.d("test","no. cops: "+mOnScreenCopsCounter);

            return;
        }

    }

    public int getCopsLimit() {
        return mCopsLimit;
    }

    public void setCopsLimit(int mCopsLimit) {
        this.mCopsLimit = mCopsLimit;
    }

    public int getCopsSpawnTime() {
        return mCopsSpawnTime;
    }

    public void setCopsSpawnTime(int mCopsSpawnTime) {
        this.mCopsSpawnTime = mCopsSpawnTime;
    }

    public GameModel(Resources resources){
        mLoopsCounter=1;//only god can divide by zero
        mOnScreenCopsCounter=0;
        mCopsSpawnTime=600;
        mCopsLimit=40;
        mNumberOfLoopsForTypeChange=3000;
        mScore=0;
        numberOfCopsPerType = 20;
        numberOfDropsAndShots =50;
        ITERATION_PAUSE_TIME=resources.getInteger(R.integer.iteration_time);
        minAmountOfCops=3;

    }

    public int getNumberOfCopsPerType() {
        return numberOfCopsPerType;
    }

    public int getNumberOfDropsAndShots() {
        return numberOfDropsAndShots;
    }

    public void decreaseCopsSpawnTime(int mils) {
        mCopsSpawnTime-=mils;
    }

    public void decreaseOnScreenCopsCounter() {
        synchronized (this) {
            mOnScreenCopsCounter--;
            Log.d("test","no. cops: "+mOnScreenCopsCounter);
            return;
        }
    }

    public int getMinAmountOfCops() {
        return minAmountOfCops;
    }

    public void setMinAmountOfCops(int minAmountOfCops) {
        this.minAmountOfCops = minAmountOfCops;
    }
}
