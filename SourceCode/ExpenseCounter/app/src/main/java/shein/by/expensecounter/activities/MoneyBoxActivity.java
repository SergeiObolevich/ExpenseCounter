package shein.by.expensecounter.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import shein.by.expensecounter.ConstainsPreference;
import shein.by.expensecounter.database.DBHelper;
import shein.by.expensecounter.database.DBMoneyBoxHelper;
import shein.by.expensecounter.R;

public class MoneyBoxActivity extends Activity implements View.OnClickListener{
    private static final int CM_DELETE_ID = 1;
    private Button btnAdd;
    private ListView lvMoneyBox;
    private DBMoneyBoxHelper db;
    private SimpleCursorAdapter simpleCursorAdapter;
    private Cursor cursor;
    private Cursor delPreference;

    private SharedPreferences sharedPreferences;
    private double currentBalance;
    private double spentBalance;
    private double moneyBoxBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.money_box_activity);

        db = new DBMoneyBoxHelper(this);
        db.open();

        cursor = db.getAllData();
        cursor.requery();


        String[] from = new String[]{DBHelper.COLUMN_MONEYBOX_MONEY, DBHelper.COLUMN_MONEYBOX_DATE,
                DBHelper.COLUMN_MONEYBOX_NOTE};
        int[] to = new int[] {R.id.tv_money_box_money, R.id.tv_money_box_date, R.id.tv_money_box_note};

        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.money_box_item, cursor,
                from, to);
        lvMoneyBox = (ListView) findViewById(R.id.lv_money_box);
        lvMoneyBox.setAdapter(simpleCursorAdapter);
        registerForContextMenu(lvMoneyBox);
        btnAdd = (Button) findViewById(R.id.money_box_add);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.money_box_add:
                Intent intent = new Intent(this, InputMoneyBoxActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo contextMenuInfo){
        super.onCreateContextMenu(menu, view, contextMenuInfo);
        menu.add(0, CM_DELETE_ID, 0, "Удалить запись");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getItemId() == CM_DELETE_ID){
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo)
                    item.getMenuInfo();
            delPreference = db.getMoney(acmi.id);
            delPreference.moveToFirst();
            double num = delPreference.getDouble(0);
            setPreference(num, 0);
            db.delRec(acmi.id);
            cursor.requery();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(data == null) return;
        double money = data.getDoubleExtra("money", 0);
        String date = data.getStringExtra("date");
        String note = data.getStringExtra("note");
        setPreference(money, 1);
        db.addRec(money, date, note);
        cursor.requery();
    }

    public void setPreference(double money, int check){
        sharedPreferences = getSharedPreferences(ConstainsPreference.APP_PREFERENCE, MODE_PRIVATE);
        String curr = sharedPreferences.getString(ConstainsPreference.CURRENT_BUDGET, "");
        String spent = sharedPreferences.getString(ConstainsPreference.SPENT_BUDGET, "");
        String mbox = sharedPreferences.getString(ConstainsPreference.MONEY_BOX_BUDGET, "");
        try{
            if(check == 1){
                currentBalance = Double.parseDouble(curr);
                spentBalance = Double.parseDouble(spent);
                moneyBoxBalance = Double.parseDouble(mbox);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                spentBalance += money;
                currentBalance -= money;
                moneyBoxBalance += money;
                editor.putString(ConstainsPreference.CURRENT_BUDGET, currentBalance + "");
                editor.putString(ConstainsPreference.SPENT_BUDGET, spentBalance + "");
                editor.putString(ConstainsPreference.MONEY_BOX_BUDGET, moneyBoxBalance + "");
                editor.commit();
            }
            else if(check == 0){
                currentBalance = Double.parseDouble(curr);
                spentBalance = Double.parseDouble(spent);
                moneyBoxBalance = Double.parseDouble(mbox);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                spentBalance -= money;
                currentBalance += money;
                moneyBoxBalance -= money;
                editor.putString(ConstainsPreference.CURRENT_BUDGET, currentBalance + "");
                editor.putString(ConstainsPreference.SPENT_BUDGET, spentBalance + "");
                editor.putString(ConstainsPreference.MONEY_BOX_BUDGET, moneyBoxBalance + "");
                editor.commit();
            }

        }catch (NumberFormatException e){
            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onDestroy(){
        super.onDestroy();
        db.close();
    }
}