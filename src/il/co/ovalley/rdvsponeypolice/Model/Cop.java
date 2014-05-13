package il.co.ovalley.rdvsponeypolice.Model;

/**
 * Created by yuval on 16/04/2014.
 */
public abstract class Cop extends GameObject {
    private int m_chanceNotToShoot;

    public Cop(CopDrawables copDrawables) {
        super();
        drawables=copDrawables;
        m_chanceNotToShoot=100;
        isDying=false;


    }

    public CopType getType() {
        return type;
    }

    public boolean isDying() {
        return isDying;
    }

    public void setDying(boolean isDying) {
        this.isDying = isDying;
    }

    public void setType(CopType type) {
        this.type = type;
    }
    private CopType type;
    //random number of steps to go iin the same direction before the variable reinitialize
    private int stepsLimit;
    private boolean isLoading;
    private boolean isShooting;
    private int loadingTime;
    private int loadingTimeCounter;
    private final CopDrawables drawables;
    private boolean isDying;
    public CopDrawables getDrawables() {
        return drawables;
    }

    public int getChanceNotToShoot() {
        return m_chanceNotToShoot;
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
}
