package il.co.ovalley.rdvsponeypolice.Model;

/**
 * Created by yuval on 30/04/2014.
 */
public abstract class GameObject {
    protected boolean isDead;
    protected int waitTime;//default amount of iterations of game loop for object to update time
    protected float xSpeed;
    protected float ySpeed;
    private Direction direction;
    private Direction directionVertical;
    private boolean isRight;
    public Loc loc=new Loc();
    public GameObject(){
    initGameObject();
    }
    abstract protected void init();
    public void initGameObject(){
        waitTime=5;
        isDead=false;
        xSpeed=1;
        ySpeed=1;
        init();
    }
    public void setDirection(Direction direction) {
        switch (direction) {
            case LEFT:
                isRight = false;
                this.direction = direction;
                break;

            case RIGHT:
                isRight = true;
                this.direction = direction;
                break;
            case UP:
                this.directionVertical = direction;
                break;
            case DOWN: this.directionVertical=direction;
                break;
            default:this.direction=direction;
        }
    }
    public boolean isRight(){
        return isRight;
    }

    public Direction getDirection() {
        return direction;
    }

    public Direction getDirectionVertical() {
        return directionVertical;
    }

    public void setDirectionVertical(Direction directionVertical) {
        this.directionVertical = directionVertical;
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
