package com.example.tasbeeh;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ContactsDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CONTACT = "contacts";
    private static final String KEY_WAZAIF = "wazaif";

    public MyDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CONTACT + "(" + KEY_WAZAIF + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        onCreate(db);
    }

    public void addContact(String wazaif) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_WAZAIF, wazaif);
        db.insert(TABLE_CONTACT, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public ArrayList<ContactModel> getContact() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACT, null);
        ArrayList<ContactModel> arrContacts = new ArrayList<>();

        while (cursor.moveToNext()) {
            ContactModel model = new ContactModel();
            model.wazaif = cursor.getString(cursor.getColumnIndex(KEY_WAZAIF));
            Log.e("Tester", model.wazaif);
            arrContacts.add(model);
        }

        cursor.close();
        db.close();
        return arrContacts;
    }

    public void updateContact(ContactModel contactModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_WAZAIF, "ALLAHOAKBER");
        db.update(TABLE_CONTACT, cv, KEY_WAZAIF + " = ?", new String[]{contactModel.wazaif});
        db.close();
    }

    public void DeleteContact(String wazaif) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACT, KEY_WAZAIF + " = ?", new String[]{wazaif});
        db.close();
    }


}
