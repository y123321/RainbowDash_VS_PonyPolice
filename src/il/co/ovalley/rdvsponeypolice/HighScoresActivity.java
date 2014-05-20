package il.co.ovalley.rdvsponeypolice;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
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

        int[] colors = new int[] { 0xEE4144,0xf37033,0xfdf6af,
                0x62bc4d,0x1e98d3,0x672f89};
                Bitmap bm = Bitmap.createBitmap(colors, colors.length, 1, Bitmap.Config.RGB_565);

        bm=rotateBitmap(bm,90);
        button.setBackgroundDrawable(new BitmapDrawable(bm));
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
                float i=9;
                AttributeSet attr=getResources().getAnimation(R.anim.alpha_disable_anim);
                AlphaAnimation animation=new AlphaAnimation(HighScoresActivity.this,attr);
                animation.initialize(v.getWidth(),v.getHeight(),v.getWidth(),v.getHeight());

                v.setAnimation(animation);
                animation.start();
                v.animate();
                v.setAlpha(0.5f);
                Log.d("test","WTF???"+ (animation.getInterpolator())+animation.hasStarted()+                animation.isInitialized()

                );
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
    public static Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
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

                Log.d("test","no connection");

                final RelativeLayout rl=(RelativeLayout)findViewById(R.id.highScoresLayout);
                rl.removeView(resultListView);
                final TextView tv=new TextView(this);
                tv.setTextSize(25);
                tv.setTextColor(Color.RED);
                tv.setText("Check connection and retry");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HighScoresActivity.this, "No internet connection", Toast.LENGTH_LONG).show();

                        rl.addView(tv,0);

                    }
                });
            }
            else {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(getHighScoreURL() + "/index");
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    updateListView(resultListView, decodeJsonArrayResponseToArrayList(EntityUtils.toString(response.getEntity())));
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
            ArrayList<HashMap<String, String>> scores = decodeJsonArrayResponseToArrayList(responseText);
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

    private ArrayList<HashMap<String, String>> decodeJsonArrayResponseToArrayList(String responseText) throws JSONException {
        JSONArray jArray = new JSONArray(responseText);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("#","#");
        map.put("name", "Name");
        map.put("score","Score");
        mylist.add(map);
        map=new HashMap<String, String>();
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
            }
        });
    }

    public String getHighScoreURL() {
        return getResources().getString(R.string.highScoreURL);
    }

    }

