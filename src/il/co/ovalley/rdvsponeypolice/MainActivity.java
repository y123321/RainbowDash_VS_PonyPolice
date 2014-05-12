package il.co.ovalley.rdvsponeypolice;

import android.app.Activity;
import android.os.Bundle;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import il.co.ovalley.rdvsponeypolice.Controller.GameFactory;
import il.co.ovalley.rdvsponeypolice.Model.GameModel;
import il.co.ovalley.rdvsponeypolice.Runnables.GameManager;
import il.co.ovalley.rdvsponeypolice.Controller.GameRunnable;
import il.co.ovalley.rdvsponeypolice.View.GameLayoutView;


public class MainActivity extends Activity {
    GameRunnable m_GameRunnable;
    GameLayoutView m_Layout;
    GameManager mGameManager;
    private LruCache mMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    private void init() {
         m_Layout=(GameLayoutView)findViewById(R.id.layout);
        mGameManager = GameFactory.createGameManager(m_Layout);
        new Thread(mGameManager).start();

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_stop:
                mGameManager.pauseGame();
                return true;
            case R.id.action_resume:
                mGameManager.resume();
                return true;

            case  R.id.action_new_game:
                GameModel.isRunning=false;
                try {
                    Thread.sleep(Common.ITERATION_PAUSE_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                recreate();
                return true;
            /*case R.id.action_save_game:
                GameSaverLoader.saveGame(m_GameRunnable,this);
                return true;
            case R.id.action_load_game:
                GameRunnable game=GameSaverLoader.loadGame(m_GameRunnable,this);
                if(game!=null)m_GameRunnable=game;*/
        }
        return super.onOptionsItemSelected(item);
    }

}
