package il.co.ovalley.rdvsponeypolice.Controller;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import il.co.ovalley.rdvsponeypolice.Common;
import il.co.ovalley.rdvsponeypolice.Model.Direction;
import il.co.ovalley.rdvsponeypolice.View.GameLayoutView;
import il.co.ovalley.rdvsponeypolice.Model.Loc;
import il.co.ovalley.rdvsponeypolice.Model.RainbowDash;
import il.co.ovalley.rdvsponeypolice.R;
import il.co.ovalley.rdvsponeypolice.View.RainbowDashView;

/**
 * Created by yuval on 30/04/2014.
 */
public class RainbowDashController extends GameController {
    public RainbowDash m_RainbowDash;
    public RainbowDashView m_RainbowDashView;
    public GameLayoutView m_Layout;
    public RainbowDashController(Context context, RainbowDash rainbowDash, RainbowDashView rainbowDashView) {
        super(context, rainbowDash, rainbowDashView);
        m_RainbowDash = rainbowDash;
        m_RainbowDashView = rainbowDashView;
        m_Layout=m_RainbowDashView.getContainer();
        init();
    }

    private void init() {
        changeDirection();

    }

    public void runUpdate() {
        if (m_RainbowDash.isReleased()) {
            releaseFromCage();
        }

        if (m_RainbowDash.isCaptured()) {
            cageRainbowDash();
            setReleaseListener();

            return;
        }
        if (m_RainbowDash.isCaged()) {
            if (checkIfLost()) m_RainbowDash.setLost(true);
            pullDown();
            return;
        }

        setRainbowDashXY();

    }

    private void cageRainbowDash() {
        m_RainbowDashView.lockInCage(m_RainbowDashView.getDirection());
        m_RainbowDash.setCaged(true);
        m_RainbowDash.setDead(false);
        m_RainbowDash.increasePullDownSpeed();
    }

    private boolean checkIfLost() {
        return m_RainbowDash.isLost();
    }

    private void setRainbowDashXY() {
        Loc loc = Common.getViewLocation(m_RainbowDashView);
        float xPoint = m_RainbowDash.getXSpeed() > 1 ? m_RainbowDash.getXSpeed() : 1;
        float yPoint = m_RainbowDash.getYSpeed() >1 ? m_RainbowDash.getYSpeed() : 1;
        Log.d("test","move Rainbow");
        if (Math.abs(loc.x - m_RainbowDash.goingToX) < xPoint)
            m_RainbowDashView.setDirection(Direction.STOP);
        if (Math.abs(loc.y - m_RainbowDash.goingToY) < yPoint)
            m_RainbowDashView.setDirectionVertical(Direction.STOP);
        switch (m_RainbowDashView.getDirection()) {
            case RIGHT:
                m_RainbowDashView.setX(loc.x + m_RainbowDash.getXSpeed());
                break;

            case LEFT:
                m_RainbowDashView.setX(loc.x - m_RainbowDash.getXSpeed());
                break;
        }
        switch (m_RainbowDashView.getDirectionVertical()) {
            case DOWN:
                m_RainbowDashView.setY(loc.y + m_RainbowDash.getYSpeed() * m_RainbowDash.getXSpeed());
                break;
            case UP:
                m_RainbowDashView.setY(loc.y - m_RainbowDash.getYSpeed() * m_RainbowDash.getXSpeed());
                break;
        }
    }

    private void pullDown() {

        if (Common.getScreenSize(getContext()).y - m_RainbowDashView.getY() < 1) lose();
        m_RainbowDashView.setY(m_RainbowDashView.getY() + m_RainbowDash.getPulledDownSpeed());
        setReleaseListener();
        return;
    }

    private void lockInCage() {
        if (m_RainbowDash.isRight()) m_RainbowDashView.setImageResource(R.drawable.rainbow_dash_caged_right);
        else m_RainbowDashView.setImageResource(R.drawable.rainbow_dash_caged_left);
        AnimationDrawable RDAnimation = (AnimationDrawable) m_RainbowDashView.getDrawable();
        RDAnimation.start();
        m_RainbowDash.setDead(false);
        m_RainbowDash.setCaged(true);
        m_RainbowDash.setLost(false);
    }

    private void lose() {
        m_RainbowDash.setLost(true);
    }

    @Override
    public void changeDirection() {
        Loc loc = Common.getViewLocation(m_RainbowDashView);
        m_RainbowDash.setYSpeed(Math.abs((loc.y - m_RainbowDash.goingToY) / (loc.x - m_RainbowDash.goingToX)));
        m_RainbowDash.setYSpeed(m_RainbowDash.getYSpeed() > 2 ? 2 : m_RainbowDash.getYSpeed());
        m_RainbowDash.setCustomRotation((float) Math.toDegrees(Math.atan(m_RainbowDash.getYSpeed())));

        if (loc.x < m_RainbowDash.goingToX) {
            setRight();
            m_RainbowDash.isRight(true);

        } else {
            setLeft();
            m_RainbowDash.isRight(false);
            m_RainbowDash.setCustomRotation(-m_RainbowDash.getCustomRotation());


        }

        if (loc.y < m_RainbowDash.goingToY) {
            m_RainbowDashView.setRotation(m_RainbowDash.getCustomRotation());
            setDown();
        } else {
            m_RainbowDashView.setRotation(-m_RainbowDash.getCustomRotation());

            setUp();
        }
        baseAnimation();
    }


    private void baseAnimation() {
        if (m_RainbowDash.isRight()) {
            m_RainbowDashView.setImageResource(R.drawable.rainbow_dash_small_right);
        } else {
            m_RainbowDashView.setImageResource(R.drawable.rainbow_dash_small_left);

        }
        AnimationDrawable RDAnimation = (AnimationDrawable) m_RainbowDashView.getDrawable();
        RDAnimation.start();
    }

    public void setGoal(float x, float y) {
        m_RainbowDash.goingToY = y;
        m_RainbowDash.goingToX = x;

    }
    private void releaseFromCage() {
        m_RainbowDash.setReleased(false);
        m_RainbowDash.setCaged(false);
        setReleaseListener();
        releaseAnimation();
        m_RainbowDash.increasePullDownSpeed();
    }


    private void setReleaseListener() {
        m_RainbowDashView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    m_RainbowDash.setDropping(true);
                    Log.d("test", "release");
                    releaseAnimation();
                    m_RainbowDash.setCaged(false);
                    setGoal(v.getX(), v.getY());
                    m_RainbowDashView.setOnTouchListener(new View.OnTouchListener() {
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
        m_Layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RainbowDash rd = (RainbowDash) getModel();
                if (rd.isDead() || rd.isCaged() || rd.isLost()) return true;
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        setGoal(event.getX(), event.getY());
                        changeDirection();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        setGoal(event.getX(), event.getY());
                        changeDirection();
                        break;
                    case MotionEvent.ACTION_UP:
                        ((RainbowDash) getModel()).setDropping(true);
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
    public void stopRDListener() {
        m_Layout.setOnTouchListener(null);
    }


    private void releaseAnimation() {
        baseAnimation();
    }

}