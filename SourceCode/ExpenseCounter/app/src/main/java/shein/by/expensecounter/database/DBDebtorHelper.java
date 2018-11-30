package shein.by.expensecounter.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBDebtorHelper {
    private static final String DB_NAME = "ExpenseCounterDB";
    private static final int DB_VERSION = 1;
    private final Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DBDebtorHelper(Context mContext){
        context = mContext;
    }

    public void open() {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public void close(){
        if(dbHelper != null) dbHelper.close();
    }

    public Cursor getAllData(){
        return sqLiteDatabase.query(DBHelper.DB_DEBTOR_TABLE, null, null,
                null, null, null, null);
    }

    public Cursor getMoneyDeptor(long id){
        return sqLiteDatabase.query(DBHelper.DB_DEBTOR_TABLE, new String[]{
                        DBHelper.COLUMN_DEBTOR_MONEY, DBHelper.COLUMN_DEBTOR_CHOOSE},
                DBHelper.COLUMN_DEBTOR_ID + " = " + id,
                null, null, null, null);
    }

    public void addRec(String text, double money, String check){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_DEBTOR_NAME, text);
        contentValues.put(DBHelper.COLUMN_DEBTOR_MONEY, money);
        contentValues.put(DBHelper.COLUMN_DEBTOR_CHOOSE, check);
        sqLiteDatabase.insert(DBHelper.DB_DEBTOR_TABLE, null, contentValues);
    }

    public void delRec(long id){
        sqLiteDatabase.delete(DBHelper.DB_DEBTOR_TABLE,
                DBHelper.COLUMN_DEBTOR_ID + " = " + id, null);
    }
}