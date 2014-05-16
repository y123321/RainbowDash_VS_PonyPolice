package il.co.ovalley.rdvsponeypolice.Controller;

import android.util.Log;
import il.co.ovalley.rdvsponeypolice.Model.*;
import il.co.ovalley.rdvsponeypolice.Runnables.GameManager;
import il.co.ovalley.rdvsponeypolice.View.*;

/**
 * Created by yuval on 30/04/2014.
 */
public class GameFactory {

    public GameFactory(){

    }
    public static CopController createCopController(Cop cop,GameLayoutView layout){
        return new CopController(layout.getContext(),cop,new CopView(layout,cop.getDrawables()),true);

    }
    public static DropController createDropController(GameLayoutView layout){
        return new DropController(layout.getContext(),new Drop(),new DropView(layout),true);
        }
    public static ShotController createShotController(GameLayoutView layout){
        return new ShotController(layout.getContext(),new Shot(),new ShotView(layout),true);
    }

    public static RainbowDashController createRainbowDashController(GameLayoutView m_layout) {
        return new RainbowDashController(m_layout.getContext(),new RainbowDash(),new RainbowDashView(m_layout));
    }
    public static GameManager createGameManager(GameLayoutView layout){
        Log.d("test", "factory");

        return new GameManager(new GameModel(),layout);
    }
}
