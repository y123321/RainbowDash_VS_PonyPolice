package il.co.ovalley.rdvsponeypolice.Model;

/**
 * Created by yuval on 08/05/2014.
 */
public class GameModel {
    private int numberOfDrops;
    private int m_LoopsCounter;
    private int m_OnScreenCopsCounter;
    private int m_CopsLimit;
    private int m_Score;
    public static int ITERATION_PAUSE_TIME=10;
    private int numberOfCopsPerType;

    public int getScore() {
        return m_Score;
    }

    public void addToScore(int score) {
        this.m_Score += score;
    }
    public void setScore(int m_Score) {
        this.m_Score = m_Score;
    }

    private volatile int m_CopsSpawnTime;
    public static volatile boolean isRunning;

    public int getNumberOfLoopsForTypeChange() {
        return m_NumberOfLoopsForTypeChange;
    }

    public void setNumberOfLoopsForTypeChange(int m_NumberOfLoopsForTypeChange) {
        this.m_NumberOfLoopsForTypeChange = m_NumberOfLoopsForTypeChange;
    }

    private int m_NumberOfLoopsForTypeChange;
    public int get_LoopsCounter() {
        return m_LoopsCounter;
    }

    public void increaseLoopsCounter() {
        this.m_LoopsCounter++;
    }

    public int get_OnScreenCopsCounter() {
        return m_OnScreenCopsCounter;
    }

    public void increaseOnScreenCopsCounter() {
        this.m_OnScreenCopsCounter++;
    }

    public int get_CopsLimit() {
        return m_CopsLimit;
    }

    public void set_CopsLimit(int m_CopsLimit) {
        this.m_CopsLimit = m_CopsLimit;
    }

    public int get_CopsSpawnTime() {
        return m_CopsSpawnTime;
    }

    public void set_CopsSpawnTime(int m_CopsSpawnTime) {
        this.m_CopsSpawnTime = m_CopsSpawnTime;
    }

    public GameModel(){
        m_LoopsCounter=1;//only god can divide by zero
        m_OnScreenCopsCounter=0;
        m_CopsSpawnTime=500;
        m_CopsLimit=40;
        m_NumberOfLoopsForTypeChange=3000;
        m_Score=0;
        numberOfCopsPerType = 30;
        numberOfDrops=50;

    }

    public int getNumberOfCopsPerType() {
        return numberOfCopsPerType;
    }

    public int getNumberOfDrops() {
        return numberOfDrops;
    }
}
