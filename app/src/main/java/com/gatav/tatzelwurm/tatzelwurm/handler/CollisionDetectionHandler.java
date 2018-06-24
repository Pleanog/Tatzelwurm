package com.gatav.tatzelwurm.tatzelwurm.handler;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Why going the extra mile, when you get the chance being lazy?
 * credits to Eury Perez
 * https://gist.github.com/euri16/33751df7a24580ec0264
 */

public class CollisionDetectionHandler {

    /**
     * Check if two views are colliding in screen based on rectangular shape
     * [Not pixel perfect (square collision detection)]
     *
     * @param v1 View
     * @param v2 View
     * @return boolean
     */
    public static boolean isCollisionDetected(View v1, View v2) {
        if (v1 == null || v2 == null) {
            throw new IllegalArgumentException("Views mut be not null");
        }
        Rect R1 = new Rect();
        v1.getHitRect(R1);
        Rect R2 = new Rect();
        v2.getHitRect(R2);
        return Rect.intersects(R1, R2);
    }

    /**
     * Check pixel-perfectly if two views are colliding
     *
     * @param view1 View
     * @param x1 first view position in x-axis
     * @param y1 first view position in y-axis
     * @param view2 View
     * @param x2 second view position in x-axis
     * @param y2 second view position in y-axis
     * @return boolean
     */
    public static boolean isCollisionDetected(View view1, int x1, int y1,
                                              View view2, int x2, int y2) {

        WeakReference<Bitmap> bitmap1 = getViewBitmap(view1);
        WeakReference<Bitmap> bitmap2 = getViewBitmap(view2);

        if (bitmap1 == null || bitmap2 == null) {
            throw new IllegalArgumentException("bitmaps cannot be null");
        }

        Rect bounds1 = new Rect(x1, y1, x1 + bitmap1.get().getWidth(), y1 + bitmap1.get().getHeight());
        Rect bounds2 = new Rect(x2, y2, x2 + bitmap2.get().getWidth(), y2 + bitmap2.get().getHeight());

        if (Rect.intersects(bounds1, bounds2)) {
            Rect collisionBounds = getCollisionBounds(bounds1, bounds2);
            for (int i = collisionBounds.left; i < collisionBounds.right; i++) {
                for (int j = collisionBounds.top; j < collisionBounds.bottom; j++) {
                    int bitmap1Pixel = bitmap1.get().getPixel(i - x1, j - y1);
                    int bitmap2Pixel = bitmap2.get().getPixel(i - x2, j - y2);
                    if (isFilled(bitmap1Pixel) && isFilled(bitmap2Pixel)) {
                        bitmap1.get().recycle();
                        bitmap1 = null;
                        bitmap2.get().recycle();
                        bitmap2 = null;
                        System.gc();
                        return true;
                    }
                }
            }
        }
        bitmap1.get().recycle();
        bitmap1 = null;
        bitmap2.get().recycle();
        bitmap2 = null;
        System.gc();
        return false;
    }

    /**
     * Get the collision bounds from two rects
     *
     * @param rect1 Rect
     * @param rect2 Rect
     * @return Rect
     */
    private static Rect getCollisionBounds(Rect rect1, Rect rect2) {
        int left = Math.max(rect1.left, rect2.left);
        int top = Math.max(rect1.top, rect2.top);
        int right = Math.min(rect1.right, rect2.right);
        int bottom = Math.min(rect1.bottom, rect2.bottom);
        return new Rect(left, top, right, bottom);
    }

    /**
     * Check if pixel is not transparent
     *
     * @param pixel int
     * @return boolean
     */
    private static boolean isFilled(int pixel) {
        return pixel != Color.TRANSPARENT;
    }

    /**
     * Get a Bitmap from a specified View
     *
     * @param v View
     * @return Bitmap
     */
    private static WeakReference<Bitmap> getViewBitmap(View v) {
        if (v.getMeasuredHeight() <= 0) {
            int specWidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            v.measure(specWidth, specWidth);
            WeakReference<Bitmap> b = new WeakReference<>(Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height,
                    Bitmap.Config.ARGB_8888));
            Canvas c = new Canvas(b.get());
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
        WeakReference<Bitmap> b = new WeakReference<>(Bitmap.createBitmap(v.getLayoutParams().width,
                v.getLayoutParams().height, Bitmap.Config.ARGB_8888));
        Canvas c = new Canvas(b.get());
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }
}