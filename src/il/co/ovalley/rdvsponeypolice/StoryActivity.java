package il.co.ovalley.rdvsponeypolice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

/**
 * Created by yuval on 22/05/2014.
 */
public class StoryActivity extends Activity {
    private ImageSwitcher imageSwitcher;
    Button btnNext;
    int imageIds[] = {R.drawable.brute_pony_10_left, R.drawable.brute_pony_10_right, R.drawable.brute_pony_3_left, R.drawable.ninja_pony_10_left, R.drawable.ninja_pony_10_right};
    int currentIndex=-1;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story);
        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
        btnNext=(Button) findViewById(R.id.buttonNext);
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

        // Declare the animations and initialize them
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);

        // set the animation type to imageSwitcher
        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);

        nextImage();
        // ClickListener for NEXT button
        // When clicked on Button ImageSwitcher will switch between Images
        // The current Image will go OUT and next Image  will come in with specified animation
        btnNext.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                nextImage();

            }
        });
    }

    private void nextImage() {
        // TODO Auto-generated method stub
        currentIndex++;
        // If index reaches maximum reset it
        if(currentIndex==imageIds.length){
            Intent intent=new Intent(this,GameActivity.class);
            startActivity(intent);                }

        else imageSwitcher.setImageResource(imageIds[currentIndex]);
    }
}