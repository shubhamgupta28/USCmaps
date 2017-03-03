package usc.com.uscmaps.example1.shubham.uscmaps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Shubham on 3/1/17.
 */

public class AboutMe extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_me);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
