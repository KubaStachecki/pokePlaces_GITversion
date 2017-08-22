package com.theedo.kuba.lazydaisies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Kuba-10 on 14.07.2017.
 */

public class Utils {


    private Context context;



    public Utils(Context context) {
        this.context = context;
    }

    public int getScreenWidth() {

        int columnWidth;

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) {
            // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;

        return columnWidth;
    }

    public File saveImageToSDCard(Bitmap bitmap, String imageName) {


        File mediaStorageDir = new File(
                Environment

                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                context.getString(R.string.gallery_name));
        if (!mediaStorageDir.exists()) {

        }
            if (!mediaStorageDir.mkdirs()) {

        }

        mediaStorageDir.mkdirs();

        File file = new File(mediaStorageDir, imageName + ".jpg");

        if (file.exists())
            file.delete();


        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();


        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(context, "Nie udało się zapisać pliku :(", Toast.LENGTH_SHORT).show();

        }

        return file;
    }


    public void setAsWallpaper(Bitmap bitmap, String imagename) {

        Uri uri = Uri.fromFile(saveImageToSDCard(bitmap, imagename));

        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(uri, "image/jpeg");
        intent.putExtra("mimeType", "image/jpeg");
        context.startActivity(Intent.createChooser(intent, "Set as:"));


    }

    public void shareWallpaperUrl(Bitmap bitmap, String imagename){


        Uri uri = Uri.fromFile(saveImageToSDCard(bitmap, imagename));


        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(shareIntent, "Share image using"));



    }







}
