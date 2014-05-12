package il.co.ovalley.rdvsponeypolice.Model;

/**
 * Created by yuval on 18/04/2014.
 */
public class RainbowDash extends GameObject {
    private boolean isDropping;
    private float m_pullDownSpeedAdvance;
    private boolean isReleased;


    private boolean isCaptured;


    private boolean isCaged;
    private float pulledDownSpeed;
    private int waitTime;
    private float pullDownSpeedAdvance;
    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public boolean isLost() {
        return isLost;
    }

    public boolean isCaged() {
        return isCaged;
    }

    private boolean isLost;
    public float goingToX;
    public float goingToY;
    private int stepCounter;
    private float rotation;
    @Override
    protected void init() {

        goingToX=150;
        goingToY=200;
        pullDownSpeedAdvance=0.3f;
        setWaitTime(1);
        isCaged=false;
        pullDownSpeedAdvance=0.3f;
        pulledDownSpeed=1;
        isDropping=false;
        isCaptured=false;
        for(int i=0;i<60;i++){

        }

    }

    public void advancePullDownSpeed() {
        pulledDownSpeed+=pullDownSpeedAdvance;
    }

    public int getStepCounter() {
        return stepCounter;
    }

    public void setStepCounter(int stepCounter) {
        this.stepCounter = stepCounter;
    }


    public float getPullDownSpeedAdvance() {
        return m_pullDownSpeedAdvance;
    }

    public void setPullDownSpeedAdvance(float m_pullDownSpeedAdvance) {
        this.m_pullDownSpeedAdvance = m_pullDownSpeedAdvance;
    }

    public boolean isDropping() {
        return isDropping;
    }

    public void setDropping(boolean isDropping) {
        this.isDropping = isDropping;
    }

    public boolean isReleased() {
        return isReleased;
    }


    public float getCustomRotation() {
        return this.rotation;
    }

    public void setCustomRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getPulledDownSpeed() {
        return pulledDownSpeed;
    }

    public void setPulledDownSpeed(float pulledDownSpeed) {
        this.pulledDownSpeed = pulledDownSpeed;
    }

    public void setReleased(boolean isReleased) {
        this.isReleased = isReleased;
    }

    public void setCaged(boolean isCaged) {
        this.isCaged = isCaged;
    }

    public void setLost(boolean isLost) {
        this.isLost = isLost;
    }

    public void increasePullDownSpeed() {        pulledDownSpeed += pullDownSpeedAdvance;

    }

    public boolean isCaptured() {
        return isCaptured;
    }

    public void setCaptured(boolean captured) {
        this.isCaptured = captured;
    }
}