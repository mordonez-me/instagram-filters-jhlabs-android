package pe.phantasia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageView;

import com.jabistudio.androidjhlabs.filter.ContrastFilter;
import com.jabistudio.androidjhlabs.filter.Curve;
import com.jabistudio.androidjhlabs.filter.CurvesFilter;
import com.jabistudio.androidjhlabs.filter.GaussianFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Logger;

public class MainActivity extends ActionBarActivity {

    private static final String LOGTAG = "ImageFilterLog";

    private int[] mColors;
    protected Bitmap mFilterBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        Button btnFilterAmaro = (Button) this.findViewById(R.id.btnFilterAmaro);

        Button btnFilterEarlyBird = (Button) this.findViewById(R.id.btnFilterEarlyBird);

        Button btnFilterLomoFi = (Button) this.findViewById(R.id.btnFilterLomoFi);

        final Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.image);

        final ImageView imageView = (ImageView) this.findViewById(R.id.imageView);
        imageView.setImageBitmap(image);

        //final Amaro filter = new Amaro();
        //final EarlyBird filter = new EarlyBird();
        //final LomoFi filter = new LomoFi();

        btnFilterAmaro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Amaro filter = new Amaro();
                imageView.setWillNotDraw(true);

                Thread thread = new Thread(){
                    public void run() {

                        final Bitmap newImage = filter.transform(image);

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                imageView.setImageBitmap(newImage);

                                imageView.setWillNotDraw(false);
                                imageView.postInvalidate();
                            }
                        });
                    }
                };
                thread.setDaemon(true);
                thread.start();
            }
        });

        btnFilterEarlyBird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EarlyBird filter = new EarlyBird();
                imageView.setWillNotDraw(true);

                Thread thread = new Thread(){
                    public void run() {

                        final Bitmap newImage = filter.transform(image, getResources());

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                imageView.setImageBitmap(newImage);

                                imageView.setWillNotDraw(false);
                                imageView.postInvalidate();
                            }
                        });
                    }
                };
                thread.setDaemon(true);
                thread.start();
            }
        });

        btnFilterLomoFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LomoFi filter = new LomoFi();
                imageView.setWillNotDraw(true);

                Thread thread = new Thread(){
                    public void run() {

                        final Bitmap newImage = filter.transform(image);

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                imageView.setImageBitmap(newImage);

                                imageView.setWillNotDraw(false);
                                imageView.postInvalidate();
                            }
                        });
                    }
                };
                thread.setDaemon(true);
                thread.start();
            }
        });
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
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
