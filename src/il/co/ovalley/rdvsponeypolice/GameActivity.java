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

    int imageIds[] = {R.drawable.brute_pony_10_left, R.drawable.brute_pony_10_right, R.drawable.brute_pony_3_left, R.drawable.ninja_pony_10_left, R.drawable.ninja_pony_10_right};

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
        final Button btnNext=(Button) findViewById(R.id.buttonNext);
        //starts the story board
        initStoryBoard(mImageSwitcher, btnNext);
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

    private void gotoHighScores() {
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
        mGameManager.resume();
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
    private void initStoryBoard(final ImageSwitcher mImageSwitcher, final Button nextButton) {
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


        // ClickListener for NEXT button
        // When clicked on Button ImageSwitcher will switch between Images
        // The current Image will go OUT and next Image  will come in with specified animation
        nextButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                nextImage(mImageSwitcher,nextButton);
            }
        });
        // Declare the animations and initialize them
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);

        // set the animation type to mImageSwitcher
        mImageSwitcher.setInAnimation(in);
        mImageSwitcher.setOutAnimation(out);
        nextImage(mImageSwitcher,nextButton);
    }

    private void nextImage(ImageSwitcher mImageSwitcher,Button nextButton) {
        // TODO Auto-generated method stub
        currentIndex++;
        // If index reaches maximum reset it
        if(currentIndex>=imageIds.length){
            mImageSwitcher.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            mGameManager.resume();
        }
        else mImageSwitcher.setImageResource(imageIds[currentIndex]);
    }
    class BlockTouch implements View.OnTouchListener{

        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    }

}
