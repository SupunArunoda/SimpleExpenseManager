package lk.ac.mrt.cse.dbs.simpleexpensemanager.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.CustomApplication;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.R;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DB.DBAccess;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.EXPENSE_MANAGER;

/**
 * Created by Supun on 12/5/2015.
 */
public class RemoveAccountFragment extends Fragment {

    private ListView lv;
    private ExpenseManager currentExpenseManager;

    // Listview Adapter
    ArrayAdapter<String> adapter;

    // Search EditText
    EditText inputSearch;
    Button items;


    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;

    public static RemoveAccountFragment newInstance(ExpenseManager expenseManager) {
        RemoveAccountFragment removeAccountFragment = new RemoveAccountFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXPENSE_MANAGER, expenseManager);
        removeAccountFragment.setArguments(args);
        return removeAccountFragment;
    }

    public RemoveAccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_remove_account, container, false);
        TableLayout changeTableLayout = (TableLayout) rootView.findViewById(R.id.view_table);
        TableRow tableRowHeader = (TableRow) rootView.findViewById(R.id.view_table_header);

        currentExpenseManager = (ExpenseManager) getArguments().get(EXPENSE_MANAGER);
        List<Account> numbers=new ArrayList<>();
        if (currentExpenseManager != null) {
            numbers=currentExpenseManager.getAccountsDAO().getAccountsList();
        }
        generateTransactionsTable(rootView,changeTableLayout,numbers);
        return rootView;
    }
    private void generateTransactionsTable(View rootView, TableLayout changeTableLayout,
                                           List<Account> accountList) {
        for (final Account account : accountList) {
            TableRow tr = new TableRow(rootView.getContext());

            TextView accountNoval = new TextView(rootView.getContext());
            accountNoval.setText(account.getAccountNo());
            tr.addView(accountNoval);

            TextView bankName = new TextView(rootView.getContext());
            bankName.setText(account.getBankName());
            tr.addView(bankName);

            TextView holderName = new TextView(rootView.getContext());
            holderName.setText(account.getAccountHolderName());
            tr.addView(holderName);

            Button removeButton=new Button(rootView.getContext());
            removeButton.setMaxHeight(1);
            removeButton.setWidth(2);
            removeButton.setText("remove");
            tr.addView(removeButton);
removeButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        try {
            String accNo = account.getAccountNo();
            currentExpenseManager.getAccountsDAO().removeAccount(account.getAccountNo());
            Toast.makeText(CustomApplication.getCustomAppContext(), "Account with number " + accNo + " has removed",
                    Toast.LENGTH_LONG).show();
        } catch (InvalidAccountException e) {
            e.printStackTrace();
        }
        Log.e("Click", "Clicked Me");
    }
});

            changeTableLayout.addView(tr);
        }
    }

}
