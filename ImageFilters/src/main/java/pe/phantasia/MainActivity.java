package pe.phantasia;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final String LOGTAG = "ImageFilterLog";

    private int[] mColors;
    protected Bitmap mFilterBitmap;
    Bitmap image;
    ImageView imageView;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");

        image = BitmapFactory.decodeResource(getResources(), R.drawable.image);

        imageView = (ImageView) this.findViewById(R.id.imageView);
        imageView.setImageBitmap(image);

    }


    void applyAmaro() {
        final Amaro filter = new Amaro();
        // show a progress dialogue while processing
        progressDialog.show();

        Thread thread = new Thread() {
            public void run() {

                final Bitmap newImage = filter.transform(image);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        imageView.setImageBitmap(newImage);
                        // hide a progress dialogue when done processing
                        progressDialog.hide();
                    }
                });
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    void applyEarlyBird() {
        final EarlyBird filter = new EarlyBird();
        // show a progress dialogue while processing
        progressDialog.show();

        Thread thread = new Thread() {
            public void run() {

                final Bitmap newImage = filter.transform(image, getResources());

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        imageView.setImageBitmap(newImage);
                        // hide a progress dialogue when done processing
                        progressDialog.hide();
                    }
                });
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    void applyLomoFi() {
        final LomoFi filter = new LomoFi();
        // show a progress dialogue while processing
        progressDialog.show();

        Thread thread = new Thread() {
            public void run() {

                final Bitmap newImage = filter.transform(image);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        imageView.setImageBitmap(newImage);
                        // hide a progress dialogue when done processing
                        progressDialog.hide();
                    }
                });
            }
        };
        thread.setDaemon(true);
        thread.start();
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
            case R.id.action_amaro:
                applyAmaro();
                return true;
            case R.id.action_earlybird:
                applyEarlyBird();
                return true;
            case R.id.action_lomofi:
                applyLomoFi();
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
