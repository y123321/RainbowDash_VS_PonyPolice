package il.co.ovalley.rdvsponeypolice;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import il.co.ovalley.rdvsponeypolice.Controller.GameFactory;
import il.co.ovalley.rdvsponeypolice.Controller.GameManager;
import il.co.ovalley.rdvsponeypolice.Controller.GameRunnable;
import il.co.ovalley.rdvsponeypolice.View.GameLayoutView;


public class MainActivity extends Activity {
    GameRunnable m_GameRunnable;
    GameLayoutView m_Layout;
    Loop mLoop;
    private LruCache mMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get memory class of this device, exceeding this amount will throw an
        // OutOfMemory exception.
        final int memClass = ((ActivityManager)this.getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = 1024 * 1024 * memClass / 8;

        mMemoryCache = new LruCache(cacheSize) {
            @Override
            protected int sizeOf(Object key, Object value) {
                // The cache size will be measured in bytes rather than number of items.
                Bitmap bitmap=(Bitmap)value;
                return bitmap.getByteCount();
            }
        };
        init();

        new Thread(mLoop).start();


    }

    private void init() {
         m_Layout=(GameLayoutView)findViewById(R.id.layout);
            GameManager gameManager= GameFactory.createGameManager(m_Layout);
        Log.d("test","init");

        mLoop =new Loop(gameManager);
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
                mLoop.pauseGame();
                return true;
            case R.id.action_resume:
                mLoop.resume();
                return true;

            case  R.id.action_new_game:
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
