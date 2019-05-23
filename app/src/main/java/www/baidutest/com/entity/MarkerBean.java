package www.baidutest.com.entity;

import com.baidu.mapapi.model.LatLng;

/**
 * @param <T>
 */
public class MarkerBean<T> {

    private T bitmap;

    private LatLng latLng;

    public MarkerBean() {

    }

    public MarkerBean(T bitmap, LatLng latLng) {
        this.bitmap = bitmap;
        this.latLng = latLng;
    }

    public T getImgUrl() {
        return bitmap;
    }

    public void setImgUrl(T bitmap) {
        this.bitmap = bitmap;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
