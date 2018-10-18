/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.embeddedlog.LightUpDroid.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.embeddedlog.LightUpDroid.Log;

/**
 * Helper class for opening the database from multiple providers.  Also provides
 * some common functionality.
 */
public class ClockDatabaseHelper extends SQLiteOpenHelper {
    /**
     * Original Clock Database.
     */
    private static final int VERSION_5 = 5;

    /**
     * Introduce:
     * Added alarm_instances table
     * Added selected_cities table
     * Added DELETE_AFTER_USE column to alarms table
     */
    private static final int VERSION_6 = 6;

    /**
     * Added alarm settings to instance table.
     */
    private static final int VERSION_7 = 7;

    /**
     * 1: Added LightUpPi alarm ID to alarms table.
     * 2: Added timestamp to the alarms table.
     */
    private static final int VERSION_LIGHTUPPI_1 = 10;
    private static final int VERSION_LIGHTUPPI_2 = 11;

    // Database and table names
    static final String DATABASE_NAME = "alarms.db";
    static final String OLD_ALARMS_TABLE_NAME = "alarms";
    static final String ALARMS_TABLE_NAME = "alarm_templates";
    static final String INSTANCES_TABLE_NAME = "alarm_instances";
    static final String CITIES_TABLE_NAME = "selected_cities";

    private static void createAlarmsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ALARMS_TABLE_NAME + " (" +
                ClockContract.AlarmsColumns._ID + " INTEGER PRIMARY KEY," +
                ClockContract.AlarmsColumns.HOUR + " INTEGER NOT NULL, " +
                ClockContract.AlarmsColumns.MINUTES + " INTEGER NOT NULL, " +
                ClockContract.AlarmsColumns.DAYS_OF_WEEK + " INTEGER NOT NULL, " +
                ClockContract.AlarmsColumns.ENABLED + " INTEGER NOT NULL, " +
                ClockContract.AlarmsColumns.VIBRATE + " INTEGER NOT NULL, " +
                ClockContract.AlarmsColumns.LABEL + " TEXT NOT NULL, " +
                ClockContract.AlarmsColumns.RINGTONE + " TEXT, " +
                ClockContract.AlarmsColumns.DELETE_AFTER_USE + " INTEGER NOT NULL DEFAULT 0," +
                ClockContract.AlarmsColumns.LIGHTUPPI_ID + " INTEGER NOT NULL," +
                ClockContract.AlarmsColumns.TIMESTAMP + " INTEGER NOT NULL);");
        Log.i("Alarms Table created");
    }

    private static void createInstanceTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + INSTANCES_TABLE_NAME + " (" +
                ClockContract.InstancesColumns._ID + " INTEGER PRIMARY KEY," +
                ClockContract.InstancesColumns.YEAR + " INTEGER NOT NULL, " +
                ClockContract.InstancesColumns.MONTH + " INTEGER NOT NULL, " +
                ClockContract.InstancesColumns.DAY + " INTEGER NOT NULL, " +
                ClockContract.InstancesColumns.HOUR + " INTEGER NOT NULL, " +
                ClockContract.InstancesColumns.MINUTES + " INTEGER NOT NULL, " +
                ClockContract.InstancesColumns.VIBRATE + " INTEGER NOT NULL, " +
                ClockContract.InstancesColumns.LABEL + " TEXT NOT NULL, " +
                ClockContract.InstancesColumns.RINGTONE + " TEXT, " +
                ClockContract.InstancesColumns.ALARM_STATE + " INTEGER NOT NULL, " +
                ClockContract.InstancesColumns.LIGHTUPPI_ID + " INTEGER NOT NULL, " +
                ClockContract.InstancesColumns.TIMESTAMP + " INTEGER NOT NULL, " +
                ClockContract.InstancesColumns.ALARM_ID + " INTEGER REFERENCES " +
                    ALARMS_TABLE_NAME + "(" + ClockContract.AlarmsColumns._ID + ") " +
                    "ON UPDATE CASCADE ON DELETE CASCADE" +
                ");");
        Log.i("Instance table created");
    }

    private static void createCitiesTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CITIES_TABLE_NAME + " (" +
                ClockContract.CitiesColumns.CITY_ID + " TEXT PRIMARY KEY," +
                ClockContract.CitiesColumns.CITY_NAME + " TEXT NOT NULL, " +
                ClockContract.CitiesColumns.TIMEZONE_NAME + " TEXT NOT NULL, " +
                ClockContract.CitiesColumns.TIMEZONE_OFFSET + " INTEGER NOT NULL);");
        Log.i("Cities table created");
    }

    private static void insertDefaultAlarms(SQLiteDatabase db) {
        Log.i("Inserting default alarms");
        String cs = ", "; //comma and space
        String insertMe = "INSERT INTO " + ALARMS_TABLE_NAME + " (" +
                ClockContract.AlarmsColumns.HOUR + cs +
                ClockContract.AlarmsColumns.MINUTES + cs +
                ClockContract.AlarmsColumns.DAYS_OF_WEEK + cs +
                ClockContract.AlarmsColumns.ENABLED + cs +
                ClockContract.AlarmsColumns.VIBRATE + cs +
                ClockContract.AlarmsColumns.LABEL + cs +
                ClockContract.AlarmsColumns.RINGTONE + cs +
                ClockContract.AlarmsColumns.DELETE_AFTER_USE + cs +
                ClockContract.AlarmsColumns.LIGHTUPPI_ID + cs +
                ClockContract.AlarmsColumns.TIMESTAMP + ") VALUES ";

        long timestamp = (long)(System.currentTimeMillis()/1000);

        // This creates a default alarm at 8:30 for every Mon,Tue,Wed,Thu,Fri
        final String DEFAULT_ALARM_1 = "(8, 30, 31, 0, 0, '', NULL, 0, -1, " + timestamp + ");";
        // This creates a default alarm at 10:15 for every Sat,Sun
        final String DEFAULT_ALARM_2 = "(10, 15, 96, 0, 0, '', NULL, 0, -1, " + timestamp + ");";
        db.execSQL(insertMe + DEFAULT_ALARM_1);
        db.execSQL(insertMe + DEFAULT_ALARM_2);
    }

    /**
     * Drops the alarms table from the database and recreates it with the two default alarms.
     */
    public void resetAlarmTables() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + ALARMS_TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + INSTANCES_TABLE_NAME + ";");
        createInstanceTable(db);
        createAlarmsTable(db);
        insertDefaultAlarms(db);
    }

    private Context mContext;

    public ClockDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_LIGHTUPPI_2);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createAlarmsTable(db);
        createInstanceTable(db);
        createCitiesTable(db);
        insertDefaultAlarms(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int currentVersion) {
        if (Log.LOGV) {
            Log.v("Upgrading alarms database from version " + oldVersion + " to " + currentVersion);
        }

        if (oldVersion < VERSION_LIGHTUPPI_2) {
            // This is a minor use case and thanks to the LightUp server sync there is little point
            // to upgrade the database properly, so the tables are dropped and recreated instead.
            db.execSQL("DROP TABLE IF EXISTS " + INSTANCES_TABLE_NAME + ";");
            db.execSQL("DROP TABLE IF EXISTS " + CITIES_TABLE_NAME + ";");
            db.execSQL("DROP TABLE IF EXISTS " + OLD_ALARMS_TABLE_NAME + ";");
            db.execSQL("DROP TABLE IF EXISTS " + ALARMS_TABLE_NAME + ";");

            // Create tables and set default data
            createAlarmsTable(db);
            createInstanceTable(db);
            createCitiesTable(db);
            insertDefaultAlarms(db);
        }
    }

    long fixAlarmInsert(ContentValues values) {
        // Why are we doing this? Is this not a programming bug if we try to
        // insert an already used id?
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        long rowId = -1;
        try {
            // Check if we are trying to re-use an existing id.
            Object value = values.get(ClockContract.AlarmsColumns._ID);
            if (value != null) {
                long id = (Long) value;
                if (id > -1) {
                    final Cursor cursor = db.query(ALARMS_TABLE_NAME,
                            new String[]{ClockContract.AlarmsColumns._ID},
                            ClockContract.AlarmsColumns._ID + " = ?",
                            new String[]{id + ""}, null, null, null);
                    if (cursor.moveToFirst()) {
                        // Record exists. Remove the id so sqlite can generate a new one.
                        values.putNull(ClockContract.AlarmsColumns._ID);
                    }
                }
            }

            rowId = db.insert(ALARMS_TABLE_NAME, ClockContract.AlarmsColumns.RINGTONE, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        if (rowId < 0) {
            throw new SQLException("Failed to insert row");
        }
        if (Log.LOGV) Log.v("Added alarm rowId = " + rowId);

        return rowId;
    }
}
