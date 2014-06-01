package il.co.ovalley.rdvsponeypolice.Model;

import il.co.ovalley.rdvsponeypolice.Common;

/**
 * Created by yuval on 16/04/2014.
 */
public abstract class Cop extends GameObject {
    private int m_ScorePoints;
    private int m_chanceNotToShoot;
    private float m_OriginalHitPoints;
    private float m_CurrentHitPoints;

    public int getHitsToHandle() {
        return mHitsToHandle;
    }

    private int mHitsToHandle;
    public Cop(CopDrawables copDrawables) {
        super();
        drawables=copDrawables;
        m_chanceNotToShoot=100;
        isDying=false;
        m_OriginalHitPoints=2;
        mHitsToHandle=0;
        m_ScorePoints=10;
        stepsLimit=100;
        stepCounter=stepsLimit;
        Direction randDirection= Common.random.nextBoolean()?Direction.LEFT:Direction.RIGHT;
        setDirection(randDirection);

    }

    @Override
    public void initGameObject() {
        super.initGameObject();
        m_CurrentHitPoints=m_OriginalHitPoints;
        mHitsToHandle=0;
    }

    public float getOriginalHitPoints() {
        return m_OriginalHitPoints;
    }

    public void setOriginalHitPoints(int hitPoints) {
        this.m_OriginalHitPoints = hitPoints;
        this.m_CurrentHitPoints=hitPoints;
    }

    public float getCurrentHitPoints() {
        return m_CurrentHitPoints;
    }

    public void decreaseCurrentHitPoints() {
        this.m_CurrentHitPoints--;
    }



    public abstract CopType getType();

    public boolean isDying() {
        return isDying;
    }

    public void setDying(boolean isDying) {
        this.isDying = isDying;
    }

    private CopType type;
    //random number of steps to go iin the same direction before the variable reinitialize
    private int stepsLimit;
    protected int stepCounter;

    private boolean isLoading;
    private boolean isShooting;
    private int loadingTime;
    private int loadingTimeCounter;
    private final CopDrawables drawables;
    private boolean isDying;

    public CopDrawables getDrawables() {
        return drawables;
    }
    public int getStepCounter() {
        return stepCounter;
    }
    public void StepCounter(int stepCounter) {
        this.stepCounter = stepCounter;
    }


    public int getChanceNotToShoot() {
        return m_chanceNotToShoot;
    }

    public void setChanceNotToShoot(int m_chanceNotToShoot) {
        this.m_chanceNotToShoot = m_chanceNotToShoot;
    }

    public boolean isShooting() {
        return isShooting;
    }

    public int getStepsLimit() {
        return stepsLimit;
    }

    public void setStepsLimit(int stepsLimit) {
        this.stepsLimit = stepsLimit;
    }


    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void setShooting(boolean isShooting) {
        this.isShooting = isShooting;
    }

    public int getLoadingTime() {
        return loadingTime;
    }

    public void setLoadingTime(int loadingTime) {
        this.loadingTime = loadingTime;
    }

    public int getLoadingTimeCounter() {
        return loadingTimeCounter;
    }

    public void setLoadingTimeCounter(int loadingTimeCounter) {
        this.loadingTimeCounter = loadingTimeCounter;
    }

    public void decreaseLoadingTimeCounter() {
        loadingTimeCounter--;
    }

    public void makeShot() {
        setLoading(true);
        loadingTimeCounter=loadingTime;
    }

    public boolean isHit() {
        return mHitsToHandle>0;
    }

    public void setHit() {
        mHitsToHandle++;

    }
    public void removeHit(){
        mHitsToHandle--;

    }

    public int getScorePoints() {
        return m_ScorePoints;
    }

    public void setScorePoints(int m_ScorePoints) {
        this.m_ScorePoints = m_ScorePoints;
    }

    public void decreaseStepCounter() {
        stepCounter--;
    }

    public void setStepCounter(int stepCounter) {
        this.stepCounter = stepCounter;
    }
}
