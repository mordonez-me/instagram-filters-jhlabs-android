package pe.phantasia;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;

import com.jabistudio.androidjhlabs.filter.Curve;
import com.jabistudio.androidjhlabs.filter.CurvesFilter;
import com.jabistudio.androidjhlabs.filter.GrayscaleFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

/**
 * Created by mordonez on 1/14/14.
 */
public class LomoFi extends Filter{
    public Bitmap transform(Bitmap image) {

        width = image.getWidth();
        height = image.getHeight();

        Bitmap curves = changeCurves(image);

        Bitmap desaturatedImage = desaturateImage(image);

        Bitmap imageWithOverlay = combineWithOverlay(desaturatedImage, curves);

        return imageWithOverlay;

    }

    private Bitmap changeCurves(Bitmap image) {

        mColors = AndroidUtils.bitmapToIntArray(image);

        final CurvesFilter curvesFilter = new CurvesFilter();

        Curve[] curves = new Curve[3];
        Curve r = new Curve();
        Curve g = new Curve();
        Curve b = new Curve();

        curves[0] = r;
        curves[1] = g;
        curves[2] = b;

        r.y = new float[] {0f, 47.0f, 206.f, 255f};
        r.x = new float[] {0f, 62f, 189f, 255f};

        for(int i = 0 ;i<r.x.length;i++) {
            r.x[i] = (r.x[i]*100)/255/100;
            r.y[i] = (r.y[i]*100)/255/100;
        }

        g.y = new float[] {0f, 61f, 199f, 255f };
        g.x = new float[] {0f, 75f, 187f, 255f };

        for(int i = 0 ;i<g.x.length;i++) {
            g.x[i] = (g.x[i]*100)/255/100;
            g.y[i] = (g.y[i]*100)/255/100;
        }

        b.y = new float[] {0f, 66f, 191f, 255f};
        b.x = new float[] {0f, 58f, 200f, 255f};

        for(int i = 0 ;i<b.x.length;i++) {
            b.x[i] = (b.x[i]*100)/255/100;
            b.y[i] = (b.y[i]*100)/255/100;
        }

        curvesFilter.setCurves(curves);

        mColors = curvesFilter.filter(mColors, width, height);

        return Bitmap.createBitmap(
                mColors, 0, width, width, height, Bitmap.Config.ARGB_8888);
    }

    private Bitmap desaturateImage(Bitmap image) {
        Bitmap result = image.copy(Bitmap.Config.ARGB_8888, true);

        GrayscaleFilter grayscaleFilter = new GrayscaleFilter();

        mColors = AndroidUtils.bitmapToIntArray(result);
        mColors = grayscaleFilter.filter(mColors, width, height);

        return Bitmap.createBitmap(
                mColors, 0, width, width, height, Bitmap.Config.ARGB_8888);

    }

    protected Bitmap combineWithOverlay(Bitmap desaturated, Bitmap image) {
        Bitmap result = image.copy(Bitmap.Config.ARGB_8888, true);


        Paint p = new Paint();
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
        BitmapShader gradientShader = new BitmapShader(desaturated, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        p.setShader(gradientShader);


        Canvas c = new Canvas();
        c.setBitmap(result);
        c.drawBitmap(image, 0, 0, null);
        c.drawRect(0, 0, image.getWidth(), image.getHeight(), p);

        return result;
    }
}
