package com.example.taliSocialMedia.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.taliSocialMedia.network.Post;

@Database(entities = {Post.class}, version = 1, exportSchema = false)
@TypeConverters(ListConverter.class)
public abstract class MyDatabase extends RoomDatabase {
    public abstract PostDao postDao();

    private static MyDatabase INSTANCE;

    public static synchronized MyDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyDatabase.class, "post_database")
                    .build();
        }
        return INSTANCE;
    }
}
