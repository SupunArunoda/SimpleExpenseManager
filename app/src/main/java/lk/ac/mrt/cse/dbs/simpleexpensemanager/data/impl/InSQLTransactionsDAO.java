package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Supun on 12/3/2015.
 */
public class InSQLTransactionsDAO extends SQLiteOpenHelper implements TransactionDAO {

    private static final String DATABASE_NAME = "130383D";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME="transaction";
    private static final String ACCOUNT_NO="account_no";
    private static final String EXPENSE_TYPE="expense_type";
    private static final String AMOUNT="amount";
    private static final String DATE="initial_date";
    public InSQLTransactionsDAO(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACCOUNT_NO, accountNo);
        values.put(EXPENSE_TYPE, expenseType.toString());
        values.put(DATE, date.toString());
        values.put(AMOUNT, amount);
        db.insert(TABLE_NAME, null, values);
        values.clear();

        db.close(); // Closing database connection
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transList = new ArrayList<>();

        String selectQuery = "SELECT "+"*"+" FROM "+TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Transaction tansact=new Transaction(null,null,null,0);
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                tansact.setAccountNo(cursor.getString(0));
                tansact.setExpenseType(ExpenseType.valueOf(cursor.getString(1)));
                try {
                    tansact.setDate(df.parse(cursor.getString(2)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tansact.setAmount(cursor.getDouble(3));
                transList.add(tansact);
            } while (cursor.moveToNext());
        }
        db.close();
        // return transList list
        return transList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String MYdatabase = "CREATE TABLE " +TABLE_NAME+ " ("
                + ACCOUNT_NO + " TEXT PRIMARY KEY, " +EXPENSE_TYPE+ " TEXT, "
                +DATE+ " DATE," +AMOUNT+" DOUBLE"+");";
        db.execSQL(MYdatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
