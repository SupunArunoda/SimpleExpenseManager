package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DB.DBAccess;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Supun on 12/3/2015.
 */
public class InSQLTransactionsDAO implements TransactionDAO {

    public InSQLTransactionsDAO() {
    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = null;

        try {
            db = DBAccess.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DBAccess.TRANSACTION_ACCOUNT_NO, accountNo);
            values.put(DBAccess.TRANSACTION_DATE, DBAccess.getTimeAsString(date));
            switch (expenseType) {
                case EXPENSE:
                    values.put(DBAccess.TRANSACTION_TYPE, "E");
                    break;
                case INCOME:
                    values.put(DBAccess.TRANSACTION_TYPE, "I");
                    break;
            }
            values.put(DBAccess.TRANSACTION_AMOUNT, amount);

            db.insert(DBAccess.TRANSACTION_TB, null, values);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        ArrayList<Transaction> transactionsList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = DBAccess.getWritableDatabase();

            cursor = db.query(DBAccess.TRANSACTION_TB, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {

                    Transaction transaction = new Transaction(DBAccess.getTimeAsValue(cursor.getString(2)), cursor.getString(1), null, Double.parseDouble(cursor.getString(4)));
                    switch (cursor.getString(3)) {
                        case "E":
                            transaction.setExpenseType(ExpenseType.EXPENSE);
                            break;
                        case "I":
                            transaction.setExpenseType(ExpenseType.INCOME);
                            break;
                    }
                    transactionsList.add(transaction);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return transactionsList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        ArrayList<Transaction> transactionsList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = DBAccess.getWritableDatabase();

            cursor = db.query(DBAccess.TRANSACTION_TB, null, null, null, null, null, DBAccess.TRANSACTION_ID + " DESC", String.valueOf(limit));
            if (cursor.moveToFirst()) {
                do {
                    Transaction transaction = new Transaction(DBAccess.getTimeAsValue(cursor.getString(2)), cursor.getString(1), null, Double.parseDouble(cursor.getString(4)));
                    switch (cursor.getString(3)) {
                        case "E":
                            transaction.setExpenseType(ExpenseType.EXPENSE);
                            break;
                        case "I":
                            transaction.setExpenseType(ExpenseType.INCOME);
                            break;
                    }
                    transactionsList.add(transaction);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return transactionsList;
    }
}
