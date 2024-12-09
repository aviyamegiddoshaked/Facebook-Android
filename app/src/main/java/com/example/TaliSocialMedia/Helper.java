package com.example.taliSocialMedia;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helper {
    public static Uri encodeImageIntoBase64(Bitmap photo) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] byteArray = bytes.toByteArray();
        return Uri.parse("data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT));
    }

    public static Bitmap decodeImageFromBase64(Uri uri) {
        String base64Image = uri.toString().replace("data:image/jpeg;base64,", "");
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static Bitmap decodeUri(ContentResolver cr, Uri image) {
        try {
            // Decode the URI into a Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(image));
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Uri parseToUri(String encodedImageUri) {
        try {
            return Uri.parse(encodedImageUri);
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatDateString(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault());

        try {
            Date date = inputFormat.parse(dateString);
            if (date != null) {
                return outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }
}
