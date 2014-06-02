package il.co.ovalley.rdvsponeypolice;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
    private float mMinAlpha=0.5f;
    private int mMaxAlpha=1;
    private ListView mListView;
    private TextView mScoreView;
    private Button mSendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score);
        mListView = (ListView) findViewById(R.id.listView);
        mSendButton = (Button) findViewById(R.id.btnSendScore);
         mScoreView = (TextView) findViewById(R.id.highScored_tvScore);
        mScore = getIntent().getExtras().getInt("score");
        final EditText etName = (EditText) findViewById(R.id.etName);

        hideViewsIfNeeded(mSendButton, etName);
        mScoreView.setText("Score: " + mScore);
        Bitmap bm = getStripedBitmap();
        mSendButton.setBackgroundDrawable(new BitmapDrawable(bm));
        LinearLayout header = (LinearLayout) getLayoutInflater().inflate(R.layout.header, null);
        mListView.setEmptyView(findViewById(R.id.tvLoading));
        mListView.addHeaderView(header);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0 || s.length() >= 30 ||mScore==0) {
                    disableView(mSendButton, true);
                } else if (!mSendButton.isEnabled()) {
                    enableView(mSendButton, true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                getData(mListView);
            }
        }).start();
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etName.getText().toString();
                disableView(v, true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        postData(name, mScore, mListView);
                        mScore = 0;
                    }
                }).start();

            }
        });
        etName.setText("");
    }


    private void hideViewsIfNeeded(View btnSend, View etName) {
        boolean isRunning=getIntent().getExtras().getBoolean("isRunning");
        if(isRunning){
            ViewGroup.LayoutParams params=findViewById(R.id.scroll).getLayoutParams();
            params.height=Common.getScreenSize(this).y;
            mScoreView.setVisibility(View.GONE);
            etName.setVisibility(View.GONE);
//            etName.setY(45);
           btnSend.setVisibility(View.GONE);
            int top=mListView.getPaddingTop();
            int bottom=mListView.getPaddingBottom();
            int left=mListView.getPaddingLeft();
            int right=mListView.getPaddingRight();
            mListView.setPadding(left,top+25,right,bottom+30);

        }
    }

    private void enableView(View v, boolean useAnimation) {
        v.setEnabled(true);
        if (useAnimation) {
            AlphaAnimation animation1 = getAlphaAnimation(250, v.getAlpha(), mMaxAlpha);
            v.startAnimation(animation1);
        } else {
            v.setAlpha(mMaxAlpha);
        }
    }

    private void disableView(View v, boolean useAnimation) {
        v.setEnabled(false);
        if (useAnimation) {
            AlphaAnimation animation1 = getAlphaAnimation(250, v.getAlpha(), mMinAlpha);
            v.startAnimation(animation1);
        } else {
            v.setAlpha(mMinAlpha);
        }
    }

    private AlphaAnimation getAlphaAnimation(int duration,float from,float to) {
        return Common.getAlphaAnimation(duration,from,to);
    }

    private Bitmap getStripedBitmap() {
        int[] colors = new int[]{0xEE4144, 0xf37033, 0xfdf6af,
                0x62bc4d, 0x1e98d3, 0x672f89};
        Bitmap bm = Bitmap.createBitmap(colors, colors.length, 1, Bitmap.Config.RGB_565);

        bm = rotateBitmap(bm, 90);
        return bm;
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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HighScoresActivity.this, "No internet connection", Toast.LENGTH_LONG).show();

                        ((TextView)findViewById(R.id.tvLoading)).setText("Check connection\n and retry");

                    }
                });
            }
            else {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(getHighScoreURL() + "/index");
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    updateListView(resultListView, decodeJsonArrayResponseToArrayList(EntityUtils.toString(response.getEntity())));
                    Log.d("test","get data");
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
            if(response.getStatusLine().getStatusCode()==200) {
                ArrayList<HashMap<String, String>> scores = decodeJsonArrayResponseToArrayList(responseText);
                updateListView(resultListView, scores);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        disableView(mSendButton, true);
                    }
                });
            }


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
                new String[]{"#", "name", "score"}, new int[]{R.id.rank, R.id.name, R.id.score});
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultListView.setAdapter(adapter);
            }
        });
    }

    public String getHighScoreURL() {
        return getResources().getString(R.string.url_high_score_service);
    }

    }

