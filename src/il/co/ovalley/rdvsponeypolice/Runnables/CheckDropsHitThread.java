package il.co.ovalley.rdvsponeypolice.Runnables;

import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import il.co.ovalley.rdvsponeypolice.Controller.CopController;
import il.co.ovalley.rdvsponeypolice.Controller.DropController;

import java.util.ArrayList;

/**
 * Created by yuval on 30/04/2014.
 */
public class CheckDropsHitThread implements Runnable {
    volatile ArrayList<CopController> m_Cops;
    volatile ArrayList<DropController> m_Drops;
    CheckDropsHitThread(ArrayList<DropController> drops,ArrayList<CopController> cops){
        m_Cops=cops;
        m_Drops=drops;
    }
    @Override
    public void run() {
        while (true){
        int i=0;
        while (i<m_Cops.size()){
            try {
                if (i < 0) break;
                CopController cop=m_Cops.get(i);
                Rect copRect=new Rect();
                cop.getView().getHitRect(copRect);
                int j=0;
                while (j<m_Drops.size()){
                    DropController drop=m_Drops.get(j);
                    Rect dropRect=new Rect();
                    drop.getView().getHitRect(dropRect);
                    if(dropRect.intersect(copRect)){
                        Kill(drop, cop);
                        i--;
                        j--;
                        break;
                    }
                }
            }
            catch (Exception e){
                Log.d("test","hit check threw an exception" + e.toString());
                e.printStackTrace();
                break;
            }


        }
    }
}

    private void Kill(final DropController drop, final CopController cop) {
        m_Drops.remove(drop);
        m_Cops.remove(cop);
        drop.getModel().setDead(true);
        cop.getModel().setDead(true);
        Log.d("test","kill!");
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                drop.getView().getContainer().removeView(drop.getView());
                cop.getView().getContainer().removeView(cop.getView());


            }
        });
    }
    }
