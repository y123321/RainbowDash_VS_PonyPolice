package il.co.ovalley.rdvsponeypolice;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yuval on 30/05/2014.
 */
public class CreditsActivity extends Activity {
    TextView tvBackgroundImage;
    TextView tvTimor;
    TextView tvDeveloper;
    TextView tvPonyGen;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);
        tvBackgroundImage=(TextView)findViewById(R.id.tvBackgroundImageLink);
        tvTimor=(TextView)findViewById(R.id.tvTimorLink);
        tvDeveloper=(TextView)findViewById(R.id.tvDeveloperLink);
        tvPonyGen=(TextView)findViewById(R.id.tvPoniesLink);
        tvBackgroundImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getString(R.string.url_background_image);
                browseTo(url);
            }
        });
        tvTimor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseTo(getString(R.string.url_timor));
            }
        });
        tvDeveloper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseTo(getString(R.string.url_developer));
            }
        });
        tvPonyGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseTo(getString(R.string.url_ponies));
            }
        });

    }

    private void browseTo(String url) {
        if(!url.equals("")) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
    }

}