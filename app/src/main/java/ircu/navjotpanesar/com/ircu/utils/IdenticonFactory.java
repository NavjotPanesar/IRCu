package ircu.navjotpanesar.com.ircu.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Navjot on 5/31/2015.
 *   Implementation based on http://writings.orangegnome.com/writes/creating-identicons/
 */
public class IdenticonFactory {
    private int width;
    private int height;
    private int backgroundColor; //white
    private DisplayMetrics displayMetrics;

    public IdenticonFactory(Activity activity, int width, int height, int backgroundColor){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float density = metrics.density;

        this.displayMetrics = metrics;
        this.width = Math.round((float)width * density);
        this.height = Math.round((float)height * density);
        this.backgroundColor = backgroundColor;
    }

    public IdenticonFactory(Activity activity, int width, int height){
        this(activity, width, height, -1);
    }

    public Bitmap getBitmap(String input){
        String hash = getStringHash(input.toLowerCase());
        String colorString = "#" + hash.substring(0, 6);
        int color = Color.parseColor(colorString);
        boolean[] bitMap = getBitMap(hash);
        int[] coloredMap = applyColorToBitMap(bitMap, color);
        Bitmap bmp = Bitmap.createBitmap(displayMetrics, coloredMap, 5, 5, Bitmap.Config.RGB_565);
        return Bitmap.createScaledBitmap(bmp, width, height, false);
    }

    private int[] applyColorToBitMap(boolean[] bitMap, int color){
        int[] coloredMap = new int[5*5];
        for(int i = 0; i < 5*5; ++i){
            coloredMap[i] = bitMap[i] ?  color : backgroundColor;
        }
        return coloredMap;
    }

    private boolean[] getBitMap(String hash){
        boolean[] pixels = new boolean[5*5];
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                int segmentStart = (i * 5) + j + 6;
                int segmentEnd = segmentStart + 1;
                String hashSegment = hash.substring(segmentStart, segmentEnd);
                int decColor = Integer.parseInt(hashSegment, 16);
                pixels[i + j*5] = (decColor%2 == 0);
            }
        }
        return pixels;
    }

    private String getStringHash(String string){
        String result;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(StandardCharsets.UTF_8.encode(string));
            result = String.format("%032x", new BigInteger(1, messageDigest.digest()));
        } catch (NoSuchAlgorithmException e){
            ChatLogger.v("Failed to generate MD5 hash");
            result = "";
        }
        return result;
    }
}
