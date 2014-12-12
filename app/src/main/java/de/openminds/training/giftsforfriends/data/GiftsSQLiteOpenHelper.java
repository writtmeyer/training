/*
 * Copyright 2014 Wolfram Rittmeyer / OpenMinds UG (haftungsbeschränkt)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.openminds.training.giftsforfriends.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GiftsSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "gifts4friends";

    /* package private */ GiftsSQLiteOpenHelper(Context ctx) {
        super(ctx, DB_NAME, null, VERSION, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE gift_contacts ( _id INTEGER, name TEXT, count INTEGER, PRIMARY KEY (_id) )";
        db.execSQL(sql);
        sql = "INSERT INTO gift_contacts VALUES ( 1, 'Beate', 5 )";
        db.execSQL(sql);
        sql = "INSERT INTO gift_contacts VALUES ( 2, 'Frank', 2 )";
        db.execSQL(sql);
        // normally you would add a table gifts for the gifts and
        // use a "count(*) from" subquery to get the number of gifts
        // this is omitted to keep the sample code simple
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS gift_contacts";
    }
}
