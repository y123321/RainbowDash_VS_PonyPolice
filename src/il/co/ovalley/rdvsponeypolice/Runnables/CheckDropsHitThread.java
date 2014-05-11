package il.co.ovalley.rdvsponeypolice.Runnables;

import android.graphics.Rect;
import android.util.Log;
import il.co.ovalley.rdvsponeypolice.Controller.CopController;
import il.co.ovalley.rdvsponeypolice.Controller.DropController;
import il.co.ovalley.rdvsponeypolice.Controller.GameController;

import java.util.ArrayList;

/**
 * Created by yuval on 30/04/2014.
 */
public class CheckDropsHitThread implements Runnable {

    volatile ArrayList<CopController> m_Cops;
    volatile ArrayList<DropController> m_Drops;
    public CheckDropsHitThread(ArrayList<GameController> controllers){
        m_Cops=new ArrayList<CopController>();
        m_Drops=new ArrayList<DropController>();
        for(GameController controller:controllers){
            if(controller instanceof CopController) m_Cops.add((CopController)controller);
            if(controller instanceof DropController) m_Drops.add((DropController)controller);

        }
    }
    @Override
    public void run() {
        while (true){
            for(CopController cop:m_Cops){
                if(cop.isOutOfGame()) continue;
                Rect copRect=new Rect();
                cop.getView().getHitRect(copRect);
                for(DropController drop: m_Drops){
                    if(drop.isOutOfGame()) continue;
                    Rect dropRect=new Rect();
                    drop.getView().getHitRect(dropRect);
                    if(dropRect.intersect(copRect)) kill(drop, cop);
                }
            }

    }
}

    private void kill(final DropController drop, final CopController cop) {
    //    m_Drops.remove(drop);
   //     m_Cops.remove(cop);
        drop.getModel().setDead(true);
        cop.getModel().setDead(true);
        Log.d("test","HIT!");
    }
    }
