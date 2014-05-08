package il.co.ovalley.rdvsponeypolice.Controller;

import il.co.ovalley.rdvsponeypolice.Model.*;
import il.co.ovalley.rdvsponeypolice.View.*;

/**
 * Created by yuval on 30/04/2014.
 */
public class GameFactory {

    public GameFactory(){

    }
    public static CopController createCopController(CopType type,GameLayoutView layout){
        switch (type){
            case
                    NINJA:return new CopController(layout.getContext(),new NinjaCop(),new CopView(layout));

            case
                    SIMPLE:return new CopController(layout.getContext(),new SimpleCop(),new CopView(layout));

            default:return new CopController(layout.getContext(),new SimpleCop(),new CopView(layout));
        }
    }
    public static DropController createDropController(GameLayoutView layout){
        return new DropController(layout.getContext(),new Drop(),new DropView(layout));
        }
    public static ShotController createShotController(GameLayoutView layout){
        return new ShotController(layout.getContext(),new Shot(),new ShotView(layout));
    }

    public static RainbowDashController createRainbowDashController(GameLayoutView m_layout) {
        return new RainbowDashController(m_layout.getContext(),new RainbowDash(),new RainbowDashView(m_layout));
    }
}
