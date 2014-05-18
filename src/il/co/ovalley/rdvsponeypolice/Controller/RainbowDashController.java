package il.co.ovalley.rdvsponeypolice.Controller;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.MotionEvent;
import android.view.View;
import il.co.ovalley.rdvsponeypolice.Common;
import il.co.ovalley.rdvsponeypolice.Model.Direction;
import il.co.ovalley.rdvsponeypolice.Model.Loc;
import il.co.ovalley.rdvsponeypolice.Model.RainbowDash;
import il.co.ovalley.rdvsponeypolice.R;
import il.co.ovalley.rdvsponeypolice.View.GameLayoutView;
import il.co.ovalley.rdvsponeypolice.View.RainbowDashView;

/**
 * Created by yuval on 30/04/2014.
 */
public class RainbowDashController extends GameController {
    private RainbowDash mRainbowDashModel;
    private RainbowDashView mRainbowDashView;
    private GameLayoutView mLayout;
    public RainbowDashController(Context context, RainbowDash rainbowDash, RainbowDashView rainbowDashView) {
        super(context, rainbowDash, rainbowDashView,false);
        mRainbowDashModel = rainbowDash;
        mRainbowDashView = rainbowDashView;
        mLayout = mRainbowDashView.getContainer();
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
        getModel().initGameObject();
        getView().initGameView();
        changeDirection();

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
            releaseAnimation();
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
    protected void changeDirection() {

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
        baseAnimation();

    }

    private void setSpeedAndRotation(Loc loc) {
        float ySpeed=2;
        if(loc.x - mRainbowDashModel.goingToX!=0)
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
        switch (mRainbowDashModel.getDirection()) {
            case RIGHT:
                mRainbowDashView.setX(CurrentLocation.x + mRainbowDashModel.getXSpeed());
                break;

            case LEFT:
                mRainbowDashView.setX(CurrentLocation.x - mRainbowDashModel.getXSpeed());
                break;
        }
        switch (mRainbowDashModel.getDirectionVertical()) {
            case DOWN:
                mRainbowDashView.setY(CurrentLocation.y + getYAdvance());
                break;
            case UP:
                mRainbowDashView.setY(CurrentLocation.y - getYAdvance());
                break;
        }
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


    private void baseAnimation() {
        if (mRainbowDashModel.isRight()) {
            mRainbowDashView.setImageResource(R.drawable.rainbow_dash_small_right);
        } else {
            mRainbowDashView.setImageResource(R.drawable.rainbow_dash_small_left);

        }
        AnimationDrawable RDAnimation = (AnimationDrawable) mRainbowDashView.getDrawable();
        RDAnimation.start();
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
        mRainbowDashView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mRainbowDashModel.setReleased(true);
                    setGoal(v.getX(), v.getY());
                    mRainbowDashView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                }
                return true;

            }

        });
        }
    public void startRDListener() {
        mLayout.setOnTouchListener(new View.OnTouchListener() {
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
        });
        getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }
    private void releaseAnimation() {
        baseAnimation();
    }

}