package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.CustomApplication;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InSQLAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InSQLTransactionsDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

/**
 * Created by Supun on 12/3/2015.
 */
public class PersistentExpenseManager extends ExpenseManager {
    public PersistentExpenseManager(){
            setup();
    }

    @Override
    public void setup() {


        TransactionDAO insqlransactions = new InSQLTransactionsDAO(CustomApplication.getCustomAppContext());
        setTransactionsDAO(insqlransactions);

        AccountDAO insqlaccount = new InSQLAccountDAO(CustomApplication.getCustomAppContext());
        setAccountsDAO(insqlaccount);

        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);

    }
}
