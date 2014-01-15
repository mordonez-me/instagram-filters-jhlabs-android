package pe.phantasia;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by mordonez on 1/14/14.
 */
public class Filter {

    protected int width;
    protected int height;
    protected int[] mColors;
    protected Bitmap levels;
    protected Resources resources;

    protected Bitmap createRadialGradient() {
        RadialGradient gradient = new RadialGradient(width/2, height/2, width/2, Color.GRAY,
                0xFF000000, android.graphics.Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setShader(gradient);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(0x000000FF);
        canvas.drawRect(new Rect(0,0,width,height), paint);
        //canvas.drawCircle(width/2, height/2, width/2, paint);

        return bitmap;

    }


    public Bitmap setSepiaColorFilter(Bitmap image) {

        float[] sepMat = {
                0.3930000066757202f,
                0.7689999938011169f,
                0.1889999955892563f,
                0,
                0,
                0.3490000069141388f,
                0.6859999895095825f,
                0.1679999977350235f,
                0,
                0,
                0.2720000147819519f,
                0.5339999794960022f,
                0.1309999972581863f,
                0,
                0,
                0,
                0,
                0,
                0.5f,
                0,
                0,
                0,
                0,
                0,
                0.5f};

        ColorMatrix sepiaMatrix = new ColorMatrix();
        sepiaMatrix.set(sepMat);

        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(sepiaMatrix);
        Bitmap result = image.copy(Bitmap.Config.ARGB_8888, true);

        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(result, 0, 0, paint);
        return result;

    }

    protected Bitmap combineGrandientAndImage(Bitmap gradient, Bitmap image, PorterDuff.Mode mode) {
        Bitmap result = image.copy(Bitmap.Config.ARGB_8888, true);


        Paint p = new Paint();
        p.setXfermode(new PorterDuffXfermode(mode));
        BitmapShader gradientShader = new BitmapShader(gradient, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        p.setShader(gradientShader);


        Canvas c = new Canvas();
        c.setBitmap(result);
        c.drawBitmap(image, 0, 0, null);
        c.drawRect(0, 0, image.getWidth(), image.getHeight(), p);

        return result;
    }

   protected Bitmap adjustOpacity(Bitmap image, Integer alpha) {
       Bitmap imageWithAlpha = image.copy(Bitmap.Config.ARGB_8888, true);

       Canvas canvas = new Canvas(imageWithAlpha);
       int colour = (100 & 0xFF) << 24;
       canvas.drawColor(colour, PorterDuff.Mode.DST_IN);

       return imageWithAlpha;
   }
}
