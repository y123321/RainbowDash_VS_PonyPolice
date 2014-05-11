package il.co.ovalley.rdvsponeypolice.Controller;

import android.app.Activity;
import android.content.Context;
import il.co.ovalley.rdvsponeypolice.Common;
import il.co.ovalley.rdvsponeypolice.Model.*;
import il.co.ovalley.rdvsponeypolice.Runnables.CheckDropsHitThread;
import il.co.ovalley.rdvsponeypolice.View.GameLayoutView;

import java.util.ArrayList;

/**
 * Created by yuval on 30/04/2014.
 */
public class GameManager {
    public volatile ArrayList<GameController> m_Controllers;
    public GameLayoutView m_Layout;
    private Context m_Context;
    private GameModel m_GameModel = new GameModel();
    private RainbowDashController m_RainbowDashController;


    public GameManager(GameModel gameModel, GameLayoutView gameLayoutView) {
        m_Layout = gameLayoutView;
        m_Context = gameLayoutView.getContext();
        m_GameModel = gameModel;

        init();
    }

    private void adjustDropLocToGameViewBehind(Loc location, RainbowDashController rainbowDash) {
        if (!rainbowDash.m_RainbowDash.isRight()) {
            location.x += rainbowDash.getView().getWidth()-30;
        }
    }

    private void init() {

        m_Controllers = new ArrayList<GameController>();
        m_RainbowDashController = GameFactory.createRainbowDashController(m_Layout);
        m_Controllers.add(m_RainbowDashController);
        m_RainbowDashController.startRDListener();
        for(int i=0;i<100;i++){
            m_Controllers.add(GameFactory.createDropController(m_Layout));
        }
        for(int i=0;i<50;i++){
            m_Controllers.add(GameFactory.createCopController(CopType.NINJA,m_Layout));
            m_Controllers.add(GameFactory.createCopController(CopType.SIMPLE, m_Layout));
        }
        new Thread(new CheckDropsHitThread(m_Controllers)).start();



    }

    private void releaseDrop() {
        m_RainbowDashController.m_RainbowDash.setDropping(false);
        ((Activity)m_Context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getNewDrop();

            }
        });

    }
    public void action() {
        //     Log.d("test", "rainbow dash " + m_RainbowDashController.m_RainbowDash.goingToY);
        if(m_RainbowDashController.m_RainbowDash.isDropping())releaseDrop();
        for (GameController controller : m_Controllers) {
            if (controller.isOutOfGame()) {
                if (controller.getModel().isDead()) remove(controller);
            }
            else {
                if (m_GameModel.get_LoopsCounter() % controller.getModel().getWaitTime() == 0) {
                    controller.update();
                }
            }


        }
        m_GameModel.increaseLoopsCounter();
        if (((RainbowDash) m_RainbowDashController.getModel()).isDropping()) releaseDrop();
        spawnCops();


    }

    private void remove(final GameController controller) {
        controller.setOutOfGame(true);
        ((Activity) m_Context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_Layout.removeView(controller.getView());

            }
        });

    }

    private void updateController(GameController controller) {
        if (m_GameModel.get_LoopsCounter() % controller.getModel().getWaitTime() == 0)
            controller.update();

    }

    private void spawnCops() {
        if (m_GameModel.get_LoopsCounter() % m_GameModel.get_CopsSpawnTime()==0) {
            CopType copType=m_GameModel.get_LoopsCounter()>1000? CopType.NINJA:CopType.SIMPLE;
            getNewCop(copType);
        }
            m_GameModel.increaseOnScreenCopsCounter();

        }

    private void getNewCop(CopType type) {
        for(GameController controller:m_Controllers){
            if(controller instanceof CopController && controller.isOutOfGame()) {
                if (((Cop) controller.getModel()).getType() == type) {
                    controller.resurrect();
                    return;
                }
            }
        }
    }
    private void getNewDrop() {
        for(GameController controller:m_Controllers){
            if(controller instanceof DropController && controller.isOutOfGame()){
                controller.resurrect();
                Loc rdLocation= Common.getViewLocation(m_RainbowDashController.getView());
                adjustDropLocToGameViewBehind(rdLocation, m_RainbowDashController);
                Common.setViewLocation(controller.getView(), rdLocation);
                return;
            }
        }
    }

}



