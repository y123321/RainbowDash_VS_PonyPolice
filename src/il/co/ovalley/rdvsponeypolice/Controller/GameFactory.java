package il.co.ovalley.rdvsponeypolice.Controller;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import il.co.ovalley.rdvsponeypolice.Model.*;
import il.co.ovalley.rdvsponeypolice.Runnables.GameManager;
import il.co.ovalley.rdvsponeypolice.View.CopView;
import il.co.ovalley.rdvsponeypolice.View.DropView;
import il.co.ovalley.rdvsponeypolice.View.RainbowDashView;
import il.co.ovalley.rdvsponeypolice.View.ShotView;

/**
 * Created by yuval on 30/04/2014.
 */
public class GameFactory {

    public GameFactory(){

    }
    public static CopController createCopController(Cop cop,ViewGroup layout){
        return new CopController(layout.getContext(),cop,new CopView(layout,cop.getDrawables()),true);

    }
    public static DropController createDropController(ViewGroup layout){
        return new DropController(layout.getContext(),new Drop(),new DropView(layout),true);
        }
    public static ShotController createShotController(ViewGroup layout){
        return new ShotController(layout.getContext(),new Shot(),new ShotView(layout),true);
    }

    public static RainbowDashController createRainbowDashController(ViewGroup m_layout,View background) {
        return new RainbowDashController(m_layout.getContext(),new RainbowDash(),new RainbowDashView(m_layout),background);
    }
    public static GameManager createGameManager(ViewGroup layout,TextView scoreView,ImageView gameOver,View background){
        Log.d("test", "factory");

        return new GameManager(new GameModel(layout.getContext().getResources()),layout, scoreView,gameOver,background);
    }
}
