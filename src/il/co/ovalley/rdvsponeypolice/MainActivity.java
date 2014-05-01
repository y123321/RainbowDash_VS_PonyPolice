package il.co.ovalley.rdvsponeypolice;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import il.co.ovalley.rdvsponeypolice.Controller.GameRunnable;
import il.co.ovalley.rdvsponeypolice.Model.GameLayout;


public class MainActivity extends Activity {
    GameRunnable m_GameRunnable;
    GameLayout m_Layout;
    Loop m_loop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        new Thread(m_loop).start();


    }

    private void init() {
         m_Layout=(GameLayout)findViewById(R.id.layout);
        Common.setGameManager(new GameManager(this,m_Layout));
        try {
            m_loop=new Loop(Common.getGameManager());
        } catch (Exception e) {
            GameManager gameManager=new GameManager(this,m_Layout);
            Common.setGameManager(gameManager);
            m_loop=new Loop(gameManager);
        }

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
                m_loop.pauseGame();
                return true;
            case R.id.action_resume:
                m_loop.resume();
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
