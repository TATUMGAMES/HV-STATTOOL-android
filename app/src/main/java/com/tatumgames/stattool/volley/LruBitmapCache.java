package com.tatumgames.stattool.volley;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by leonard on 5/2/2018.
 */
public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    /**
     * Constructor
     */
    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }

    /**
     * Constructor
     *
     * @param sizeInKiloBytes Returns the maximum number of bytes the heap can expand to
     */
    public LruBitmapCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    /**
     * Retrieve the default Lru cache size
     *
     * @return Returns the maximum number of bytes the heap can expand to
     */
    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // use 1/8th of the available memory for this memory cache
        final int cacheSize = maxMemory / 8;
        return cacheSize;
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}
