package il.co.ovalley.rdvsponeypolice;

import il.co.ovalley.rdvsponeypolice.Controller.GameFactory;
import il.co.ovalley.rdvsponeypolice.Controller.GameManager;
import il.co.ovalley.rdvsponeypolice.Runnables.RainbowDashThread;

/**
 * Created by yuval on 30/04/2014.
 */
public class Loop implements Runnable {
    volatile GameManager m_GameManager;
    Object m_PauseObject;

    volatile boolean m_isRunning;
    boolean m_isPause;
    RainbowDashThread rdThread;
    Loop(GameManager gameManager){
        m_GameManager=gameManager;
        m_PauseObject=new Object();
    }
    @Override
    public void run() {
        rdThread=new RainbowDashThread(GameFactory.createRainbowDashController(m_GameManager.m_Layout));
        new Thread(rdThread).start();
        Thread thread=new Thread(rdThread);
        m_isRunning = true;
        while (m_isRunning) {
            m_GameManager.action();
            try {
                Thread.sleep(Common.ITERATION_PAUSE_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
        public void resume(){
            m_isPause=false;
            synchronized (m_PauseObject) {
                m_PauseObject.notify();
            }
            rdThread.startRDListener();
        }
    public void pauseGame() {
        rdThread.stopRDListener();
        m_isPause = true;

    }
}
