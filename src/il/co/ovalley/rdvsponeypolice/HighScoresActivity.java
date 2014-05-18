package il.co.ovalley.rdvsponeypolice;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yuval on 17/05/2014.
 */
public class HighScoresActivity extends Activity {
    /**
     * Called when the activity is starting.  This is where most initialization
     * should go: calling {@link #setContentView(int)} to inflate the
     * activity's UI, using {@link #findViewById} to programmatically interact
     * with widgets in the UI, calling
     * {@link #managedQuery(android.net.Uri, String[], String, String[], String)} to retrieve
     * cursors for data being displayed, etc.
     * <p/>
     * <p>You can call {@link #finish} from within this function, in
     * which case onDestroy() will be immediately called without any of the rest
     * of the activity lifecycle ({@link #onStart}, {@link #onResume},
     * {@link #onPause}, etc) executing.
     * <p/>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     * @see #onStart
     * @see #onSaveInstanceState
     * @see #onRestoreInstanceState
     * @see #onPostCreate
     */
    int mScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score);
        final ListView listView=(ListView)findViewById(R.id.listView);
        Button button=(Button)findViewById(R.id.btnSendScore);
        TextView textView=(TextView)findViewById(R.id.textView);
        mScore=getIntent().getExtras().getInt("score");
        textView.setText("Score: "+mScore);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getData(listView);
            }
        }).start();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=((EditText)findViewById(R.id.etName)).getText().toString();
                v.setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        postData(name,mScore,listView);
                        mScore=0;
                       }
                }).start();

            }
        });
        // adapter=new Adapter() {
        }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
        public void getData(final ListView resultListView){
            if (! isNetworkAvailable())
            {



                final LinearLayout ll=(LinearLayout)findViewById(R.id.highScoresLayout);
                ll.removeView(resultListView);
                final TextView tv=new TextView(this);
                tv.setText("Check connection and retry");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HighScoresActivity.this, "No internet connection", Toast.LENGTH_LONG);

                        ll.addView(tv);

                    }
                });
            }
            else {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(getHighScoreURL() + "/index");
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    updateListView(resultListView, decodeJsonArrayResponse(EntityUtils.toString(response.getEntity())));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        public void postData(String name,int score,final ListView resultListView) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(getHighScoreURL()+"/create");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("Name", name));
            nameValuePairs.add(new BasicNameValuePair("Score", score+""));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            String responseText = EntityUtils.toString(response.getEntity());
            Log.d("test","resopnse text: "+responseText);
            ArrayList<HashMap<String, String>> scores = decodeJsonArrayResponse(responseText);
            updateListView(resultListView, scores);

        }
        catch (JSONException e1) {
            e1.printStackTrace();
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    private ArrayList<HashMap<String, String>> decodeJsonArrayResponse(String responseText) throws JSONException {
        JSONArray jArray = new JSONArray(responseText);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < jArray.length(); i++) {
            map.put("#",(i+1)+"");
            map.put("name", jArray.getJSONObject(i).getString("Name"));
            map.put("score",jArray.getJSONObject(i).getString("Score"));
            mylist.add(map);
            map=new HashMap<String, String>();
        }
        return mylist;
    }

    private void updateListView(final ListView resultListView, ArrayList<HashMap<String, String>> mylist) {
        final SimpleAdapter adapter = new SimpleAdapter(this, mylist, R.layout.row,
                new String[]{"#", "name", "score"}, new int[]{R.id.TRAIN_CELL, R.id.FROM_CELL, R.id.TO_CELL});
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultListView.setAdapter(adapter);
                resultListView.refreshDrawableState();
            }
        });
    }

    public String getHighScoreURL() {
        return getResources().getString(R.string.highScoreURL);
    }

    }

