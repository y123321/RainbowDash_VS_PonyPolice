package il.co.ovalley.rdvsponeypolice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import il.co.ovalley.rdvsponeypolice.Controller.GameFactory;
import il.co.ovalley.rdvsponeypolice.Model.GameModel;
import il.co.ovalley.rdvsponeypolice.Runnables.GameManager;
import il.co.ovalley.rdvsponeypolice.View.GameLayoutView;


public class GameActivity extends Activity {
    private GameLayoutView mLayout;
    private GameManager mGameManager;
    private int currentIndex=-1;
    int imageIds[] = {R.drawable.brute_pony_10_left, R.drawable.brute_pony_10_right, R.drawable.brute_pony_3_left, R.drawable.ninja_pony_10_left, R.drawable.ninja_pony_10_right};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        final ImageSwitcher imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
        final Button btnNext=(Button) findViewById(R.id.buttonNext);
        //starts the story board
        initImageSwitcher(imageSwitcher,btnNext);

    }

    private void init() {

        TextView tv=(TextView)findViewById(R.id.tvScore);
        getWindow().setFormat(PixelFormat.RGB_565);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
            tv.setText("0");
         mLayout =(GameLayoutView)findViewById(R.id.layout);
        ImageView gameOver=(ImageView)findViewById(R.id.gameOver);
        mGameManager = GameFactory.createGameManager(mLayout,tv,gameOver);
        new Thread(mGameManager).start();

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    private void gotoHighScores(){
    Intent intent=new Intent(this,HighScoresActivity.class);
    intent.putExtra("score",mGameManager.getScore());
    startActivity(intent);
        
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Prepare the Screen's standard options menu to be displayed.  This is
     * called right before the menu is shown, every time it is shown.  You can
     * use this method to efficiently enable/disable items or otherwise
     * dynamically modify the contents.
     * <p/>
     * <p>The default implementation updates the system menu items based on the
     * activity's state.  Deriving classes should always call through to the
     * base class implementation.
     *
     * @param menu The options menu as last shown or first initialized by
     *             onCreateOptionsMenu().
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mGameManager.pauseGame();
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * This hook is called whenever the options menu is being closed (either by the user canceling
     * the menu with the back/menu button, or when an item is selected).
     *
     * @param menu The options menu as last shown or first initialized by
     *             onCreateOptionsMenu().
     */
    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
        mGameManager.resume();
    }

    @Override
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
    }
    private void initImageSwitcher(final ImageSwitcher imageSwitcher, final Button nextButton) {
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

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
                nextImage(imageSwitcher,nextButton);
            }
        });
        // Declare the animations and initialize them
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);

        // set the animation type to imageSwitcher
        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);
        nextImage(imageSwitcher,nextButton);
    }

    private void nextImage(ImageSwitcher imageSwitcher,Button nextButton) {
        // TODO Auto-generated method stub
        currentIndex++;
        // If index reaches maximum reset it
        if(currentIndex>=imageIds.length){
            imageSwitcher.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            mGameManager.resume();
        }
        else imageSwitcher.setImageResource(imageIds[currentIndex]);
    }

}