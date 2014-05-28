package il.co.ovalley.rdvsponeypolice.Controller;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import il.co.ovalley.rdvsponeypolice.Model.GameObject;
import il.co.ovalley.rdvsponeypolice.View.GameView;

/**
 * Created by yuval on 30/04/2014.
 */
abstract public class GameController {
    Context mContext;
    GameObject mModel;
    GameView mView;

    private volatile boolean isOutOfGame;

    protected GameController(Context context, GameObject gameObject, GameView gameView, boolean isOutOfGame) {
        this.mContext = context;
        this.mModel = gameObject;
        this.mView = gameView;
        this.isOutOfGame = isOutOfGame;
    }
//updates game objects.
    public void update() {
        if(runModelUpdate()) {
            Activity activity = (Activity) getContext();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
               //     try {
                        runViewUpdate();
//                    } catch (Exception e) {
//                        Log.d("test", "GameView of type" + getClass().getName() + " threw exception: " + e.toString()+"\n"+e.getStackTrace().toString() + "\nGameView wasnt updated");
//
//                    }
                }
            });
        }
    }
    /** returns true if view should be updated or false if it shouldn't*/
    abstract protected boolean runModelUpdate();
    /** updates on the view, this method runs on UI thread*/
    abstract protected void runViewUpdate();
    /**change the movement direction on view and model*/
    public abstract void changeDirection();


    public GameObject getModel() {
        return mModel;
    }

    public GameView getView() {
        return mView;
    }

    public boolean isOutOfGame() {
        return isOutOfGame;
    }
    public void remove(){
        getView().isRemoved=true;
        ((Activity)getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getView().getContainer().removeView(getView());
                setOutOfGame(true);
                getModel().setDead(false);
            }
        });
    }
    public boolean resurrect() {
        try {
        getModel().initGameObject();
            setOutOfGame(false);

            getView().initGameView();



            return true;
        }
        catch (Exception e){
            Log.d("test","Object not resurrected\n"+e.toString());
            return false;
        }
    }

    public void setOutOfGame(boolean isOutOfGame) {
        this.isOutOfGame = isOutOfGame;
    }

    public Context getContext() {
        return mContext;
    }







}