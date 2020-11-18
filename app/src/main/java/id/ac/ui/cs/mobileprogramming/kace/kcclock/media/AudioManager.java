package id.ac.ui.cs.mobileprogramming.kace.kcclock.media;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static id.ac.ui.cs.mobileprogramming.kace.kcclock.application.App.getAppContext;

public class AudioManager {
    public static List<Audio> getAllAudio() {

        List<Audio> audioList = new ArrayList<Audio>();

        String[] projection = new String[] {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE
        };
        String selectionClause = MediaStore.Audio.Media.DURATION +
                " >= ?";
        String[] selectionArgs = new String[] {
                String.valueOf(TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS))
        };
        String sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";

        Thread t = new Thread(() -> {
            try (Cursor cursor = getAppContext().getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selectionClause,
                    selectionArgs,
                    sortOrder
            )) {
                // Cache column indices.
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                int nameColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                int durationColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
                int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);

                while (cursor.moveToNext()) {
                    // Get values of columns for a given video.
                    long id = cursor.getLong(idColumn);
                    String name = cursor.getString(nameColumn);
                    int duration = cursor.getInt(durationColumn);
                    int size = cursor.getInt(sizeColumn);

                    Uri contentUri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

                    // Stores column values and the contentUri in a local object
                    // that represents the media file.
                    audioList.add(new Audio(contentUri, name, duration, size));
                }
            }
        });
        try {
            t.start();
            t.join();
        } catch (Exception e) {
            Log.d("MediaStore Query", e.toString());
            e.printStackTrace();
        }

        return audioList;
    }
}
