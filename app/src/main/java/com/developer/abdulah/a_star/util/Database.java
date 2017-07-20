package com.developer.abdulah.a_star.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.developer.abdulah.a_star.a_star.node.Platform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dev on 7/17/17.
 */

public class Database extends SQLiteOpenHelper {

    private static final String DB_NAME = "inobi_v1.db";

    public static void prepDatabase(Context context) {
        File dbFile = context.getDatabasePath(DB_NAME);

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


    public List<Platform> allPlatforms() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT d.id AS did, av.pos AS pos, p.* FROM directions AS d\n" +
                "    INNER JOIN arrays AS a\n" +
                "        ON a.id = d.platforms\n" +
                "    INNER JOIN array_values AS av\n" +
                "        ON av.id = a.id\n" +
                "    INNER JOIN platforms AS p\n" +
                "        ON av.entry_id = p.id\n" +
                "    ORDER BY did, pos\n", null
        );

        ArrayList<Platform> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int did = cursor.getInt(0);
                int dpos = cursor.getInt(1);
                int pid = cursor.getInt(2);
                int sid = cursor.getInt(3);
                double lat = cursor.getDouble(4);
                double lng = cursor.getDouble(5);

                list.add(new Platform(pid, sid, did, dpos, lat, lng));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
