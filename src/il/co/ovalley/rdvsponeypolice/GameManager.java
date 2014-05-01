package il.co.ovalley.rdvsponeypolice;

import android.content.Context;
import il.co.ovalley.rdvsponeypolice.Controller.CopController;
import il.co.ovalley.rdvsponeypolice.Controller.GameController;
import il.co.ovalley.rdvsponeypolice.Controller.GameFactory;
import il.co.ovalley.rdvsponeypolice.Controller.ShotController;
import il.co.ovalley.rdvsponeypolice.Model.CopType;
import il.co.ovalley.rdvsponeypolice.Model.GameLayout;

import java.util.ArrayList;

/**
 * Created by yuval on 30/04/2014.
 */
public class GameManager {
    private Context m_Context;
  //  public ArrayList<GameController> m_Controllers;
    public GameLayout m_Layout;
    private int m_LoopsCounter;
    private GameFactory m_Factory;
    private int m_OnScreenCopsCounter;
    private int m_CopsLimit;
    private volatile int m_CopsSpawnTime;
    private volatile ArrayList<CopController> m_Cops;
    private ArrayList<ShotController> m_Shots;

    public GameManager(Context context,GameLayout gameLayout){
        m_Layout=gameLayout;
        m_Context =context;
        init();
    }

    private void init() {
        m_LoopsCounter=1;//only god can divide by zero
        m_Factory=new GameFactory(m_Layout);
        m_OnScreenCopsCounter=0;
    //    m_Controllers =new ArrayList<GameController>();
        m_Cops=new ArrayList<CopController>();
        m_Shots=new ArrayList<ShotController>();
        m_CopsLimit=40;
    //    m_Controllers.add(m_RainbowDashController);
        m_CopsSpawnTime=500;
      ;
    }

    public void removeGameObject(GameController controller){

    };
    public void action(){
        int i=0;
        while (i< m_Cops.size()) {
            CopController controller = m_Cops.get(i);
            i++;
            if (controller != null)
                updateController(controller);
        }
        i=0;

            spawnCops();

        m_LoopsCounter++;

    }
        private void updateController(GameController controller) {
        if(m_LoopsCounter%controller.getModel().getWaitTime()==0)
        controller.update();

    }

    private void spawnCops() {
        if(m_LoopsCounter%m_CopsSpawnTime==0 &&m_OnScreenCopsCounter<m_CopsLimit) {
            CopController copC=null;
            if(m_LoopsCounter%2==0)
                copC= m_Factory.createCopController(CopType.NINJA,m_Layout);
            else copC= m_Factory.createCopController(CopType.SIMPLE,m_Layout);
            m_Cops.add(copC);
            m_OnScreenCopsCounter++;

        }

    }


}
