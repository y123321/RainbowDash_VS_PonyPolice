package il.co.ovalley.rdvsponeypolice.Controller;

import android.content.Context;
import il.co.ovalley.rdvsponeypolice.Model.*;
import il.co.ovalley.rdvsponeypolice.View.GameLayoutView;
import il.co.ovalley.rdvsponeypolice.View.GameView;

import java.util.ArrayList;

/**
 * Created by yuval on 30/04/2014.
 */
public class GameManager {
    private Context m_Context;
    public ArrayList<GameController> m_Controllers;
    public GameLayoutView m_Layout;
    private GameModel m_GameModel=new GameModel();
    private volatile ArrayList<CopController> m_Cops;
    private ArrayList<ShotController> m_Shots;
    private ArrayList<DropController> m_Drops;
    private RainbowDashController m_RainbowDashController;


    public GameManager(GameModel gameModel,GameLayoutView gameLayoutView){
        m_Layout= gameLayoutView;
        m_Context =gameLayoutView.getContext();
        m_GameModel=gameModel;
        init();
    }



    private void init() {

        m_Controllers =new ArrayList<GameController>();
        m_Cops=new ArrayList<CopController>();
        m_Shots=new ArrayList<ShotController>();
    //    m_Controllers.add(m_RainbowDashController);
        m_RainbowDashController=GameFactory.createRainbowDashController(m_Layout);
        m_Controllers.add(m_RainbowDashController);
        m_RainbowDashController.startRDListener();


    }

    private void releaseDrop() {
     /*   DropController dc= GameFactory.createDropController(m_Layout);
        DropView drop=(DropView)dc.getView();
        Loc rdLocation= Common.getViewLocation(m_RainbowDashController.getView());
        Common.setViewLocation(m_RainbowDashController.getView(),rdLocation);
        adjustDropLocToGameViewBehind(rdLocation, m_RainbowDashController.getView());
        Common.setViewLocation(drop,rdLocation);
        m_Drops.add(dc);*/
    }
    private static void adjustDropLocToGameViewBehind(Loc location,GameView view) {
        if(view.getDirection()== Direction.LEFT){
            location.x+=view.getWidth();
        }
    }
    public void action(){
        for(GameController controller:m_Controllers){
            if(controller.isOutOfGame()) {
                if (controller.getModel().isDead()) remove(controller);
                else if(controller.getModel().getWaitTime()%m_GameModel.get_LoopsCounter()==0)
                    controller.update();
            }
        }
       /* while (i< m_Cops.size()) {
            CopController controller = m_Cops.get(i);
            i++;
            if (controller != null)
                updateController(controller);
        }
*/
        if(((RainbowDash)m_RainbowDashController.getModel()).isDropping()) releaseDrop();
            spawnCops();

        m_GameModel.increaseLoopsCounter();

    }

    private void remove(GameController controller) {
        m_Layout.removeView(controller.getView());
        controller.setOutOfGame(true);

    }

    private void updateController(GameController controller) {
        if(m_GameModel.get_LoopsCounter()%controller.getModel().getWaitTime()==0)
        controller.update();

    }

    private void spawnCops() {
        if(m_GameModel.get_LoopsCounter()%m_GameModel.get_CopsSpawnTime()==0 &&
                m_GameModel.get_OnScreenCopsCounter()<m_GameModel.get_CopsLimit()) {
            CopController copC=null;
            switch (m_GameModel.get_LoopsCounter()%CopType.values().length) {
                case 1:
                    copC = GameFactory.createCopController(CopType.NINJA, m_Layout);
                    break;
                case 0: copC = GameFactory.createCopController(CopType.SIMPLE, m_Layout);
                    break;
                default: copC = GameFactory.createCopController(CopType.SIMPLE, m_Layout);

            }
            m_Cops.add(copC);
            m_GameModel.increaseOnScreenCopsCounter();

        }

    }



}
