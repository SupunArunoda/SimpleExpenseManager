package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DB;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.CustomApplication;


/**
 * Created by Supun on 12/3/2015.
 */

/**
 * this singleton class is used to get connection to SQLiteDatabase
 */
public class DBAccess {

    // Database Name
    public static final String DATABASE_NAME = "130387D";

    //Database Version (Increase one if want to also upgrade your database)
    public static final int DATABASE_VERSION = 1;// started at 1


    // Valuse For Account
    public static final String ACCOUNT_TB = "account";
    public static final String ACCOUNT_NO = "account_no";
    public static final String BANK_NAME = "bank_name";
    public static final String HOLDER_NAME = "holder_name";
    public static final String BALANCE = "balance";

    //Values For transaction
    public static final String TRANSACTION_TB = "account_transaction";
    public static final String TRANSACTION_ID= "transaction_id";
    public static final String TRANSACTION_ACCOUNT_NO = "account_no";
    public static final String TRANSACTION_DATE = "trans_date";
    public static final String TRANSACTION_TYPE = "trans_type";
    public static final String TRANSACTION_AMOUNT = "amount";


    //Create table syntax
    private static final String ACCOUNT_CREATE =
            "CREATE TABLE account (account_no TEXT NOT NULL,bank_name TEXT NOT NULL,holder_name TEXT NOT NULL,balance DOUBLE NOT NULL CHECK (balance >= 0),PRIMARY KEY (account_no));";
    private static final String TRANSACTION_CREATE =
            "CREATE TABLE account_transaction (transaction_id INTEGER NOT NULL,account_no TEXT NOT NULL,trans_date TEXT NOT NULL,trans_type TEXT NOT NULL CHECK (trans_type =='E' OR trans_type =='I'),amount DOUBLE NULL CHECK (amount > 0),PRIMARY KEY (transaction_id),CONSTRAINT fk_transaction_account1 FOREIGN KEY (account_no) REFERENCES account (account_no) ON DELETE NO ACTION ON UPDATE NO ACTION);";

    private static DBConnect dbConnect = null;

    //Do not instantiate this class
    private DBAccess() {
    }


    public static SQLiteDatabase getWritableDatabase() throws SQLiteException {
        if (dbConnect == null) {
            synchronized (DBAccess.class) {
                dbConnect = new DBConnect();
            }
        }
        return dbConnect.getWritableDatabase();
    }


    public static String getTimeAsString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return simpleDateFormat.format(date);
    }


    public static Date getTimeAsValue(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        try {
            date = dateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }



    private static class DBConnect extends SQLiteOpenHelper {//Inner class for create only one database Ob using singleton
        public DBConnect() {
            super(CustomApplication.getCustomAppContext(), DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                //Create the tables
                db.execSQL(ACCOUNT_CREATE);
                db.execSQL(TRANSACTION_CREATE);

            } catch (Exception exception) {
                Log.i("DatabaseHandler", "Exception onCreate() exception : " + exception.getMessage());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

                db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TB); //On upgrade drop tables
            db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TB);
            onCreate(db);
        }
    }
}
