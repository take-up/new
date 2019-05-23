package www.baidutest.com.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PictureUtil {

    /**
     * 获取网络图片Bitmap
     *
     * @param url
     * @return
     */
    public static Bitmap getBitmapForUrl(String url) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            URL imgUrl = new URL(url);
            conn = (HttpURLConnection) imgUrl.openConnection();
            conn.setDoOutput(true);
            conn.connect();
            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bitmap;
        }

    }

    /**
     * 获取本地图片的bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapForLocation(String filePath) {
        Bitmap bitmap = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeStream(fis), 100, 100);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (filePath != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bitmap;
        }
    }
}
