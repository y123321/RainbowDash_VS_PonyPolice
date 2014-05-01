package il.co.ovalley.rdvsponeypolice.Controller;

import il.co.ovalley.rdvsponeypolice.Model.*;
import il.co.ovalley.rdvsponeypolice.View.CopView;
import il.co.ovalley.rdvsponeypolice.View.DropView;
import il.co.ovalley.rdvsponeypolice.View.RainbowDashView;
import il.co.ovalley.rdvsponeypolice.View.ShotView;

/**
 * Created by yuval on 30/04/2014.
 */
public class GameFactory {
    //GameLayout m_layout;
    //Context m_context;
    public GameFactory(GameLayout layout){
     //   m_layout=layout;
     //   m_context=layout.getContext();
    }
    public static CopController createCopController(CopType type,GameLayout layout){
        switch (type){
            case
                    NINJA:return new CopController(layout.getContext(),new NinjaCop(),new CopView(layout));

            case
                    SIMPLE:return new CopController(layout.getContext(),new SimpleCop(),new CopView(layout));

            default:return new CopController(layout.getContext(),new SimpleCop(),new CopView(layout));
        }
    }
    public static DropController createDropController(GameLayout layout){
        return new DropController(layout.getContext(),new Drop(),new DropView(layout));
        }
    public static ShotController createShotController(GameLayout layout){
        return new ShotController(layout.getContext(),new Shot(),new ShotView(layout));
    }

    public static RainbowDashController createRainbowDashController(GameLayout m_layout) {
        return new RainbowDashController(m_layout.getContext(),new RainbowDash(),new RainbowDashView(m_layout));
    }
}
