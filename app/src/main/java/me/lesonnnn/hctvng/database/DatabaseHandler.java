package me.lesonnnn.hctvng.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import me.lesonnnn.hctvng.models.ListTv;
import me.lesonnnn.hctvng.models.TuVung;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hoctuvung";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CATEGORY = "categories";
    private static final String TABLE_TUVUNG = "tuvungs";
    private static final String ID = "id";
    private static final String CATEGORY_ID = "category_id";
    private static final String CATEGORY_TITLE = "title";
    private static final String TUVUNG_TU = "tu";
    private static final String TUVUNG_NOIDUNG = "noidung";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Script tạo bảng category.
        String script = "CREATE TABLE "
                + TABLE_CATEGORY
                + "("
                + ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CATEGORY_TITLE
                + " TEXT"
                + ")";
        // Chạy lệnh tạo bảng.
        db.execSQL(script);
        // Script tạo bảng tuvung.
        script = "CREATE TABLE "
                + TABLE_TUVUNG
                + "("
                + ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TUVUNG_TU
                + " TEXT,"
                + TUVUNG_NOIDUNG
                + " TEXT,"
                + CATEGORY_ID
                + " TEXT"
                + ")";
        // Chạy lệnh tạo bảng.
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TUVUNG);

        onCreate(db);
    }

    public int addCategory(ListTv category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORY_TITLE, category.getTitle());

        // Chèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_CATEGORY, null, values);

        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY;
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        ListTv listTv = new ListTv();
        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                listTv.setId(Integer.parseInt(cursor.getString(0)));
                listTv.setTitle(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // return note list
        return listTv.getId();
    }

    public ListTv getListTv(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.query(TABLE_CATEGORY, new String[] {
                ID, CATEGORY_TITLE
        }, ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();

        // return note
        assert cursor != null;
        return new ListTv(cursor.getInt(0), cursor.getString(1));
    }

    public List<ListTv> getAllCategories() {
        List<ListTv> listTvs = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                ListTv listTv = new ListTv();
                listTv.setId(Integer.parseInt(cursor.getString(0)));
                listTv.setTitle(cursor.getString(1));

                // Thêm vào danh sách.
                listTvs.add(listTv);
            } while (cursor.moveToNext());
        }

        // return note list
        return listTvs;
    }

    public void updateCategory(ListTv category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORY_TITLE, category.getTitle());

        // updating row
        db.update(TABLE_CATEGORY, values, ID + " = ?",
                new String[] { String.valueOf(category.getId()) });
    }

    public void deleteCategory(ListTv category) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY, ID + " = ?", new String[] { String.valueOf(category.getId()) });
        db.close();
    }

    public void deleteTuVungs(ListTv category) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TUVUNG, CATEGORY_ID + " = ?",
                new String[] { String.valueOf(category.getId()) });
        db.close();
    }

    public void addTuVung(TuVung tuVung) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TUVUNG_TU, tuVung.getTu());
        values.put(TUVUNG_NOIDUNG, tuVung.getNghia());
        values.put(CATEGORY_ID, tuVung.getIdListTV());

        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_TUVUNG, null, values);

        // Đóng kết nối database.
        db.close();
    }

    public List<TuVung> getAllTuVungs(int category_id) {
        List<TuVung> tuVungs = new ArrayList<>();
        // Select All Query
        String selectQuery =
                "SELECT * FROM " + TABLE_TUVUNG + " WHERE " + CATEGORY_ID + " = " + category_id;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                TuVung tuVung = new TuVung();
                tuVung.setId(Integer.parseInt(cursor.getString(0)));
                tuVung.setTu(cursor.getString(1));
                tuVung.setNghia(cursor.getString(2));
                tuVung.setIdListTV(Integer.parseInt(cursor.getString(3)));

                // Thêm vào danh sách.
                tuVungs.add(tuVung);
            } while (cursor.moveToNext());
        }

        // return note list
        return tuVungs;
    }
}
