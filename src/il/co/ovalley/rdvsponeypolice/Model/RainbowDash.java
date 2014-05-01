package il.co.ovalley.rdvsponeypolice.Model;

/**
 * Created by yuval on 18/04/2014.
 */
public class RainbowDash extends GameObject {
    private float m_pullDownSpeedAdvance;
    private boolean isReleased;

    private boolean isRight;
    public boolean isRight() {
        return isRight;
    }
    public void isRight(boolean isRight) {
        this.isRight=isRight;
    }

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
    public RainbowDash() {

        goingToX=150;
        goingToY=200;
        pullDownSpeedAdvance=0.3f;
        setWaitTime(1);
        isCaged=false;
        pullDownSpeedAdvance=0.3f;
        pulledDownSpeed=1;
    }
    public void addToPulledDownSpeed(float pullDownSpeedAdvance) {

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

    public boolean isReleased() {
        return isReleased;
    }

    public void setRight(boolean isRight) {
        this.isRight = isRight;
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
}