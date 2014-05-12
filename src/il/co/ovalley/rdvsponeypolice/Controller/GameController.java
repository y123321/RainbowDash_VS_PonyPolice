package il.co.ovalley.rdvsponeypolice.Controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import il.co.ovalley.rdvsponeypolice.Common;
import il.co.ovalley.rdvsponeypolice.Model.Direction;
import il.co.ovalley.rdvsponeypolice.Model.GameObject;
import il.co.ovalley.rdvsponeypolice.View.GameView;

/**
 * Created by yuval on 30/04/2014.
 */
abstract public class GameController {
    Context m_Context;
    GameObject m_Model;
    GameView m_View;
    public Rect hitRect = new Rect();

    private boolean isOutOfGame;

    protected GameController(Context context, GameObject gameObject, GameView gameView, boolean isOutOfGame) {
        this.m_Context = context;
        this.m_Model = gameObject;
        this.m_View = gameView;
        this.isOutOfGame = isOutOfGame;
    }

    public void update() {
        Activity activity = (Activity) getContext();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    runUpdate();
                } catch (Exception e) {
                    Log.d("test", "GameView of type" + getClass().getName() + " threw exception: " + e.toString() + "\nGameView wasnt updated");
                }
            }
        });
    }

    abstract protected void runUpdate();

    protected abstract void changeDirection();


    public GameObject getModel() {
        return m_Model;
    }

    public GameView getView() {
        return m_View;
    }

    public boolean isOutOfGame() {
        return isOutOfGame;
    }

    public boolean resurrect() {
//        try {
        setOutOfGame(false);
        getModel().initGameObject();
        getView().initGameView();
        return true;
//        }
//        catch (Exception e){
//            Log.d("test","Object not resurrected\n"+e.toString());
//            return false;
//        }
    }

    public void setOutOfGame(boolean isOutOfGame) {
        this.isOutOfGame = isOutOfGame;
    }

    public Context getContext() {
        return m_Context;
    }

    protected void setRight() {
        m_View.setDirection(Direction.RIGHT);
        //      isRight=true;
    }

    protected void setUp() {
        m_View.setDirectionVertical(Direction.UP);
    }

    protected void setDown() {
        m_View.setDirectionVertical(Direction.DOWN);
    }

    protected void setLeft() {
        m_View.setDirection(Direction.LEFT);
    }


}