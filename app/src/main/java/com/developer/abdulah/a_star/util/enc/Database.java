package com.developer.abdulah.a_star.util.enc;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by dev on 7/17/17.
 */

public class Database extends SQLiteOpenHelper {


    public static final String DB_NAME = "inobi_v1.db";

    public static void prepDatabase(Context context) {
        File dbFile = context.getDatabasePath(DB_NAME);

        dbFile.delete();

        if (!dbFile.exists()) {
            System.out.println("kek");
            try {
                InputStream is = context.getAssets().open(DB_NAME);
                OutputStream os = new FileOutputStream(dbFile);

                byte[] buffer = new byte[1024];
                while (is.read(buffer) > 0) {
                    os.write(buffer);
                }

                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        } else {
            System.out.println(new Date(dbFile.lastModified()));
            System.out.println(dbFile.getAbsolutePath());
        }

    }

    private static Database instance;
    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = new Database(context.getApplicationContext());
        }
        return instance;
    }

    public Database(Context context) {
        super(context, DB_NAME, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
