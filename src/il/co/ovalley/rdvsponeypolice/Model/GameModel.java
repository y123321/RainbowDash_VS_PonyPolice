package il.co.ovalley.rdvsponeypolice.Model;

/**
 * Created by yuval on 08/05/2014.
 */
public class GameModel {
    private int numberOfDrops;
    private int mLoopsCounter;
    private int mOnScreenCopsCounter;
    private int mCopsLimit;
    private int mScore;
    public static int ITERATION_PAUSE_TIME=10;
    private int numberOfCopsPerType;

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
        this.mOnScreenCopsCounter++;
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

    public GameModel(){
        mLoopsCounter=1;//only god can divide by zero
        mOnScreenCopsCounter=0;
        mCopsSpawnTime=500;
        mCopsLimit=40;
        mNumberOfLoopsForTypeChange=3000;
        mScore=0;
        numberOfCopsPerType = 30;
        numberOfDrops=50;

    }

    public int getNumberOfCopsPerType() {
        return numberOfCopsPerType;
    }

    public int getNumberOfDrops() {
        return numberOfDrops;
    }

    public void decreaseCopsSpawnTime(int mils) {
        mCopsSpawnTime-=mils;
    }
}
