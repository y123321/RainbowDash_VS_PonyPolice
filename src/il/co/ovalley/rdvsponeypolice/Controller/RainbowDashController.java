package il.co.ovalley.rdvsponeypolice.Controller;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import il.co.ovalley.rdvsponeypolice.Common;
import il.co.ovalley.rdvsponeypolice.Model.Direction;
import il.co.ovalley.rdvsponeypolice.Model.Loc;
import il.co.ovalley.rdvsponeypolice.Model.RainbowDash;
import il.co.ovalley.rdvsponeypolice.View.RainbowDashView;

/**
 * Created by yuval on 30/04/2014.
 */
public class RainbowDashController extends GameController {
    private RainbowDash mRainbowDashModel;
    private RainbowDashView mRainbowDashView;
    private View.OnTouchListener mReleaseListener;
    private View.OnTouchListener mMoveListener;
    private View.OnTouchListener mNullListener;
    private View mBackground;
    private float mBackgroundMovement=0.2f;

    public RainbowDashController(Context context, RainbowDash rainbowDash, RainbowDashView rainbowDashView,View background) {
        super(context, rainbowDash, rainbowDashView,false);
        mRainbowDashModel = rainbowDash;
        mRainbowDashView = rainbowDashView;
        mBackground=background;
        init();
    }

    @Override
    public RainbowDash getModel() {
        return mRainbowDashModel;
    }

    @Override
    public RainbowDashView getView() {
        return mRainbowDashView;
    }

    private void init() {
        mReleaseListener =new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mRainbowDashModel.setReleased(true);
                    setGoal(v.getX(), v.getY());
                    getModel().setDirection(Direction.STOP);
                    mRainbowDashView.setOnTouchListener(mNullListener);
                }
                return true;

            }

        };
        mMoveListener= new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RainbowDash rd = getModel();
                if (rd.isDead() || rd.isCaged() || rd.isLost()) return true;
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if(Math.abs(event.getY()-mRainbowDashModel.goingToY)>getYSpeedOrMin(rd.getXSpeed())*2) mRainbowDashModel.goingToY=event.getY();
                        if(Math.abs(event.getX()-mRainbowDashModel.goingToX)>rd.getXSpeed()*2) mRainbowDashModel.goingToX=event.getX();
                        changeDirection();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(Math.abs(event.getY()-mRainbowDashModel.goingToY)>rd.getYSpeed()*2) mRainbowDashModel.goingToY=event.getY();
                        if(Math.abs(event.getX()-mRainbowDashModel.goingToX)>rd.getXSpeed()*2) mRainbowDashModel.goingToX=event.getX();                        changeDirection();
                        break;
                    case MotionEvent.ACTION_UP:
                        (getModel()).setDropping(true);
                        break;
                }
                return true;
            }
        };
        mNullListener = instanciateNullListener();
        getModel().initGameObject();
   //     getView().initGameView();
        getModel().setDirection(Direction.RIGHT);
        changeDirection();

    }

    private View.OnTouchListener instanciateNullListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        };
    }

    /**
     * returns true if view should be updated or false if it shouldn't
     */
    @Override
    protected boolean runModelUpdate() {
   //     Log.d("test", "rd update " + getView().getY());
        return true;
    }

    /**
     * updates on the view, this method runs on UI thread
     */
    @Override
    public void runViewUpdate() {

  //      if(mRainbowDashView.getY()<0) Log.d("test","going to "+ mRainbowDashModel.goingToY + " now at "+mRainbowDashView.getY()+" direction "+mRainbowDashModel.getDirectionVertical().toString());
  //      if(mRainbowDashView.getY()>800) Log.d("test","going to "+ mRainbowDashModel.goingToY + " now at "+mRainbowDashView.getY()+" direction "+mRainbowDashModel.getDirectionVertical().toString());

        if(mRainbowDashModel.isReleased()){
            releaseFromCage();
            getView().releaseAnimation(getModel().getDirection());
            mRainbowDashModel.setReleased(false);
        }
        if (mRainbowDashModel.isCaged()) {
            pullDown();
            return;
        }

        if(mRainbowDashModel.isCaptured()){
            cageRainbowDash();
            return;
        }
        setNextLocation(Common.getViewLocation(mRainbowDashView, mRainbowDashModel.loc));
        stopIfArrived();



    }

    private void cageRainbowDash() {
        mRainbowDashModel.setCaged(true);
        mRainbowDashModel.setCaptured(false);
        mRainbowDashView.lockInCage(mRainbowDashModel.getDirection());
        mRainbowDashModel.increasePullDownSpeed();
        setReleaseListener();

    }
    @Override
    public void changeDirection() {

        Loc loc = Common.getViewLocation(mRainbowDashView, mRainbowDashModel.loc);
        setSpeedAndRotation(loc);
        if (loc.x < mRainbowDashModel.goingToX) {
            mRainbowDashModel.setDirection(Direction.RIGHT);
        } else {
            mRainbowDashModel.setDirection(Direction.LEFT);
            mRainbowDashModel.setCustomRotation(-mRainbowDashModel.getCustomRotation());


        }

        if (loc.y < mRainbowDashModel.goingToY) {
            mRainbowDashModel.setDirectionVertical(Direction.DOWN);
        } else {
            mRainbowDashModel.setCustomRotation(-mRainbowDashModel.getCustomRotation());

            mRainbowDashModel.setDirectionVertical(Direction.UP);
        }
        mRainbowDashView.setRotation(mRainbowDashModel.getCustomRotation());
        getView().baseAnimation(getModel().getDirection());

    }

    private void setSpeedAndRotation(Loc loc) {
        float ySpeed=2;
        if(loc.x !=mRainbowDashModel.goingToX)
            ySpeed=Math.abs((loc.y - mRainbowDashModel.goingToY) / (loc.x - mRainbowDashModel.goingToX));
            mRainbowDashModel.setYSpeed(ySpeed > 2 ? 2 : ySpeed);
        mRainbowDashModel.setCustomRotation((float) Math.toDegrees(Math.atan(mRainbowDashModel.getYSpeed())));
    }

    private boolean checkIfLost() {
        return mRainbowDashModel.isLost();
    }

    private boolean stopIfArrived() {
        float minSpeed = getModel().getXSpeed()*2;
        float xSpeed = getXSpeedOrMin(minSpeed);//getModel().getXSpeed();
        float ySpeed = getYSpeedOrMin(minSpeed);//getModel().getYSpeed();

        if (checkIfArrivedToGoalY(ySpeed)) {
            mRainbowDashModel.setDirectionVertical(Direction.STOP);
        }
        if (checkIfArrivedToGoalX(xSpeed)) {
            mRainbowDashModel.setDirection(Direction.STOP);
            return true;
        }
        return false;
    }

    private void setNextLocation(Loc CurrentLocation) {
        float location=CurrentLocation.x;
        float layoutX=mBackground.getX();
        int xLimit = Common.getScreenSize(getContext()).x;
        if(location> xLimit){
            location=xLimit-1;
            getModel().setDirection(Direction.STOP);
        }
        else if(location<0) {
            getModel().setDirection(Direction.STOP);
            location=1;
        }
        else switch (mRainbowDashModel.getDirection()) {
            case RIGHT:

                location+= mRainbowDashModel.getXSpeed();
                layoutX-= mBackgroundMovement;
                break;

            case LEFT:
                location-=mRainbowDashModel.getXSpeed();
                layoutX+=mBackgroundMovement;
                break;
        }
        getView().setX(location);
        mBackground.setX(layoutX);
        location=CurrentLocation.y;
        int yLimit = Common.getScreenSize(getContext()).y- getView().getHeight()*2;
        if(location> yLimit){
            getModel().setDirectionVertical(Direction.STOP);
            location=yLimit-1;
        }
        else if(location<0) {
            getModel().setDirectionVertical(Direction.STOP);
            location=1;
        }
        else switch (mRainbowDashModel.getDirectionVertical()) {
            case DOWN:
                location+=getYAdvance();
                break;
            case UP:
                location-=getYAdvance();
                break;
        }
        getView().setY(location);
    }

    private float getYAdvance() {
        return mRainbowDashModel.getYSpeed() * mRainbowDashModel.getXSpeed();
    }

    private boolean checkIfArrivedToGoalY(float ySpeed) {
        return Math.abs(mRainbowDashView.getY() - mRainbowDashModel.goingToY) < ySpeed;
    }

    private boolean checkIfArrivedToGoalX(float xSpeed) {
        return Math.abs(mRainbowDashView.getX() - mRainbowDashModel.goingToX) < xSpeed;
    }

    private float getYSpeedOrMin(float min) {
        return mRainbowDashModel.getYSpeed() >min ? mRainbowDashModel.getYSpeed() : min;
    }

    private float getXSpeedOrMin(float min) {
        return mRainbowDashModel.getXSpeed() > min ? mRainbowDashModel.getXSpeed() : min;
    }

    private void pullDown() {
        if (Common.getScreenSize(getContext()).y - mRainbowDashView.getY() < 1) lose();
        mRainbowDashView.setY(mRainbowDashView.getY() + mRainbowDashModel.getPulledDownSpeed());
        return;
    }

    private void lose() {
        mRainbowDashModel.setLost(true);
    }


    public void setGoal(float x, float y) {
        mRainbowDashModel.goingToY = y;
        mRainbowDashModel.goingToX = x;

    }
    private void releaseFromCage() {
        mRainbowDashModel.setCaged(false);
        mRainbowDashModel.setCaptured(false);
        mRainbowDashModel.setDropping(false);
        mRainbowDashModel.increasePullDownSpeed();
    }


    private void setReleaseListener() {
        mRainbowDashView.setOnTouchListener(mReleaseListener);
        }
    public void startRDListener() {
        getView().getContainer().setOnTouchListener(mMoveListener);

        getView().setOnTouchListener(mNullListener);
    }

}