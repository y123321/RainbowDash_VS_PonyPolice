package il.co.ovalley.rdvsponeypolice.Model;

/**
 * Created by yuval on 30/04/2014.
 */
public abstract class GameObject {
    protected boolean isDead;
    protected int stepCounter;
    protected int waitTime;//default amount of iterations of game loop for object to update time
    protected float xSpeed;
    protected float ySpeed;

    public GameObject(){
        waitTime=5;
        isDead=false;
        xSpeed=1;
        ySpeed=1;


    }

    public int getStepCounter() {
        return stepCounter;
    }

    public void setStepCounter(int stepCounter) {
        this.stepCounter = stepCounter;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public float getXSpeed() {
        return xSpeed;
    }

    public void setXSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public float getYSpeed() {
        return ySpeed;
    }

    public void setYSpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }
}
