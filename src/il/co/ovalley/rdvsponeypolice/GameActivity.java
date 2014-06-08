package il.co.ovalley.rdvsponeypolice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import il.co.ovalley.rdvsponeypolice.Controller.GameFactory;
import il.co.ovalley.rdvsponeypolice.Runnables.GameManager;


public class GameActivity extends Activity {
    private ViewGroup mLayout;
    ImageSwitcher mImageSwitcher;
    private GameManager mGameManager;
    private ViewGroup mMenu;
    private View mOverlay;
    private int currentIndex=-1;
    private BlockTouch mBlockTouchListener;
    private Button mNextImageButton;

    int imageIds[] = {R.drawable.story_board1,R.drawable.story_board2,R.drawable.story_board2_5,R.drawable.story_board3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFormat(PixelFormat.RGB_565);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        initGame();
        //image switcher for story board
        mImageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
        //button to change to next image in story board
        mNextImageButton=(Button) findViewById(R.id.buttonNext);
        //starts the story board
        initStoryBoard();
        mBlockTouchListener=new BlockTouch();
    }

    private void initGame() {
        View background=findViewById(R.id.background);
        TextView tvScore=(TextView)findViewById(R.id.tvScore);
            tvScore.setText("0");
         mLayout =(RelativeLayout)findViewById(R.id.layout);
        ImageView gameOver=(ImageView)findViewById(R.id.gameOver);
        //initiate game manager on which the game loop and all game functionalities run.
        mGameManager = GameFactory.createGameManager(mLayout,tvScore,gameOver,background);
        initMenu();


        //the thread on which the game is played, separated from UI thread
        new Thread(mGameManager).start();

    }

    public void gotoHighScores() {
        Intent intent = new Intent(this, HighScoresActivity.class);
        intent.putExtra("score", mGameManager.getScore());
        intent.putExtra("isRunning",mGameManager.isRunning());
        startActivity(intent);
    }
    private void gotoCredits() {
        Intent intent = new Intent(this, CreditsActivity.class);
        startActivity(intent);

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        showMenu();
        return false;
    }

    /**
     * Called when the activity has detected the user's press of the back
     * key.  The default implementation simply finishes the current activity,
     * but you can override this to do whatever you want.
     */
    @Override
    public void onBackPressed() {
        if(mMenu.getVisibility()==View.VISIBLE) hideMenuAndResume();
        else super.onBackPressed();
    }

    private void initMenu(){
        mMenu=(ViewGroup)findViewById(R.id.menu);
        mOverlay = findViewById(R.id.overlay);
        View btnMenu=(Button)findViewById(R.id.btnMenu);
        View btnNewGame=findViewById(R.id.btnNewGame);
        View btnHighScore=findViewById(R.id.btnHighScore);
        View btnResume=findViewById(R.id.btnResume);
        View btnCredits=findViewById(R.id.btnCredits);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu();
            }
        });
        btnHighScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoHighScores();
                hideMenuAndResume();
            }
        });
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
                hideMenuAndResume();
            }
        });
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMenuAndResume();
            }
        });
        btnCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCredits();
            }
        });

    }


    private void hideMenuAndResume() {
        if(mImageSwitcher.getVisibility()!=View.VISIBLE)mGameManager.resume();
        mOverlay.clearAnimation();
        mMenu.clearAnimation();
        mOverlay.setVisibility(View.GONE);
        mOverlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        mMenu.setVisibility(View.GONE);

    }

    public void showMenu(){

        mGameManager.pauseGame();
        mOverlay.bringToFront();
        mOverlay.setVisibility(View.VISIBLE);
        mOverlay.setOnTouchListener(mBlockTouchListener);
        mMenu.bringToFront();
        mMenu.setVisibility(View.VISIBLE);
        for(int i=0;i<mMenu.getChildCount();i++){

        }
        Animation overlayAnim=Common.getAlphaAnimation(1000,0,0.5f);
        Animation menuAnim=Common.getAlphaAnimation(1000,0,1);
        mOverlay.startAnimation(overlayAnim);
        mMenu.startAnimation(menuAnim);
     //   getLayoutInflater().inflate(R.layout.menu, mLayout);
    }
    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
        hideMenuAndResume();
    }

    @Override
    protected void onPause() {
        mGameManager.pauseGame();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(mImageSwitcher.getVisibility()==View.GONE)
        showMenu();
        super.onResume();
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {

            case  R.id.action_new_game:
                GameModel.isRunning=false;
                try {
                    Thread.sleep(GameModel.ITERATION_PAUSE_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                recreate();
                return true;
            case R.id.action_high_scores:
                gotoHighScores();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
    private void initStoryBoard() {
        mImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                // TODO Auto-generated method stub

                // Create a new ImageView set it's properties
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.MATCH_PARENT, ImageSwitcher.LayoutParams.MATCH_PARENT));
                return imageView;
            }

        });

    mImageSwitcher.setOnTouchListener(new OnSwipeTouchListener() {
        @Override
        public void onSwipeRight() {
            // Declare the animations and initialize them
            int slideInResource = android.R.anim.slide_in_left;
            int slideOutResource = android.R.anim.slide_out_right;

            setSwipeAnimation(slideInResource, slideOutResource, mImageSwitcher);
            if(!nextImage(mImageSwitcher, mNextImageButton)) mGameManager.resume();


        }

        @Override
        public void onSwipeLeft() {
            // Declare the animations and initialize them
            setSwipeAnimation(R.anim.slide_in_right, R.anim.slide_out_left, mImageSwitcher);
            previousImage(mImageSwitcher);
        }
    });
        // ClickListener for NEXT button
        // When clicked on Button ImageSwitcher will switch between Images
        // The current Image will go OUT and next Image  will come in with specified animation
        mNextImageButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(!nextImage(mImageSwitcher, mNextImageButton)) mGameManager.resume();            }
        });
        // Declare the animations and initialize them
        setSwipeAnimation(android.R.anim.slide_in_left, android.R.anim.slide_out_right, mImageSwitcher);
        if(!nextImage(mImageSwitcher, mNextImageButton)) mGameManager.resume();    }

    private void setSwipeAnimation(int slideInResource, int slideOutResource, ImageSwitcher mImageSwitcher) {
        Animation in = AnimationUtils.loadAnimation(this, slideInResource);
        Animation out = AnimationUtils.loadAnimation(this, slideOutResource);

        // set the animation type to mImageSwitcher
        mImageSwitcher.setInAnimation(in);
        mImageSwitcher.setOutAnimation(out);
    }

    private boolean nextImage(ImageSwitcher mImageSwitcher,Button nextButton) {
        // TODO Auto-generated method stub
        currentIndex++;
        // If index reaches maximum reset it
        if(currentIndex>=imageIds.length){
            mImageSwitcher.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            return false;
        }
        else mImageSwitcher.setImageResource(imageIds[currentIndex]);
        return true;
    }
    private void previousImage(ImageSwitcher mImageSwitcher) {
        // TODO Auto-generated method stub
        // If index reaches maximum reset it
        if(currentIndex>0){
            currentIndex--;
            mImageSwitcher.setImageResource(imageIds[currentIndex]);
        }
    }

    public void showFinalStoryBoard() {
        imageIds=new int[]{R.drawable.story_board_end,R.drawable.story_board_end2};
        currentIndex=-1;
        if(!nextImage(mImageSwitcher, mNextImageButton)) gotoHighScores();
        mNextImageButton.setVisibility(View.VISIBLE);
        mImageSwitcher.setVisibility(View.VISIBLE);
        mImageSwitcher.bringToFront();
        mNextImageButton.bringToFront();
        setSwipeAnimation(android.R.anim.slide_in_left, android.R.anim.slide_out_right, mImageSwitcher);
        mImageSwitcher.setOnTouchListener(new OnSwipeTouchListener() {
            @Override
            public void onSwipeRight() {
                // Declare the animations and initialize them
                int slideInResource = android.R.anim.slide_in_left;
                int slideOutResource = android.R.anim.slide_out_right;

                setSwipeAnimation(slideInResource, slideOutResource, mImageSwitcher);
                if(!nextImage(mImageSwitcher, mNextImageButton)) gotoHighScores();


            }

            @Override
            public void onSwipeLeft() {
                // Declare the animations and initialize them
                setSwipeAnimation(R.anim.slide_in_right, R.anim.slide_out_left, mImageSwitcher);
                previousImage(mImageSwitcher);
            }
        });
        // ClickListener for NEXT button
        // When clicked on Button ImageSwitcher will switch between Images
        // The current Image will go OUT and next Image  will come in with specified animation
        mNextImageButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(!nextImage(mImageSwitcher, mNextImageButton)) gotoHighScores();
            }
        });
    }

    class BlockTouch implements View.OnTouchListener{

        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    }

}
class OnSwipeTouchListener implements View.OnTouchListener {

    @SuppressWarnings("deprecation")
    private final GestureDetector gestureDetector = new GestureDetector(new GestureListener());

    public boolean onTouch(final View v, final MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            onTouch(e);
            return true;
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {
                    // onTouch(e);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }
    public void onTouch(MotionEvent e) {
    }
    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }
}
