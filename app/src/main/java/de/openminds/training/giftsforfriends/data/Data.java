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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import de.openminds.training.giftsforfriends.domain.ContactInformation;

public class Data {

    private GiftsSQLiteOpenHelper dbHelper;

    public Data(Context ctx) {
        dbHelper = new GiftsSQLiteOpenHelper(ctx);
    }

    // I hate this ArrayList here. Would very much prefer a simple List.
    // But we need it for using a list of ContactInformation
    // in Bundles or Intents :-(
    public ArrayList<ContactInformation> getAllContactData() {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            String[] projection = new String[]{"_id", "name", "count"};
            Cursor c = db.query("gift_contacts",
                    projection,
                    null,        // selection
                    null,        // selectionArguments
                    null,        // groupBy
                    null,        // having
                    null,        // orderBy
                    null         // limit
            );

            // right now the lookup key is not used; you _could_ use this key to
            // connect your contact information to a contact on the device
            // see: http://developer.android.com/reference/android/provider/ContactsContract.ContactsColumns.html#LOOKUP_KEY
            ArrayList<ContactInformation> contacts = new ArrayList<>();
            if (c.moveToFirst()) {
                do {
                    ContactInformation contact =
                            new ContactInformation(c.getLong(0), c.getString(1), c.getInt(2));
                    contacts.add(contact);
                } while (c.moveToNext());
            }
            return contacts;
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }

    // just to show you how you would add data:
    public long addContact(ContactInformation info) {
        ContentValues values = new ContentValues();
        values.put("name", info.name);
        values.put("count", info.noOfGifts);
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            // you can safely ignore the nullColumnHack:
            long id = db.insert("gift_contacts", null, values);
            return id;
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public ContactInformation getDetail(long id) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            String[] projection = new String[]{"_id", "name", "count"};
            Cursor c = db.query("gift_contacts",
                    projection,
                    "_id = ?",                        // selection
                    new String[]{Long.toString(id)},  // selectionArguments
                    null,                             // groupBy
                    null,                             // having
                    null,                             // orderBy
                    null                              // limit
            );
            if (c.moveToFirst()) {
                ContactInformation contact =
                        new ContactInformation(c.getLong(0), c.getString(1), c.getInt(2));
                return contact;
            }
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
        return null;
    }
}
