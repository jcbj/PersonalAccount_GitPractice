package com.example.jc.personalaccount.DatabaseManger;

import android.content.Context;
import android.nfc.NfcEvent;

/**
 * Created by jc on 16/3/14.
 */
public class RelationalDatabaseManage implements IDataStoreHelper {

    //单例模式
    private RelationalDatabaseManage() {

    }

    private static class LazyRelationalDatabaseManage {
        private static final RelationalDatabaseManage INSTANCE = new RelationalDatabaseManage();
    }

    public static final RelationalDatabaseManage getInstance() {
        return LazyRelationalDatabaseManage.INSTANCE;
    }

    //Filed
    private IRelationalDatabaseHelper relationalDatabaseHelper = SQLCipherHelper.getInstance();

    //IDataStoreHelper

    public Boolean initDataStore(Context context) {
        return this.relationalDatabaseHelper.initDatabase(context);
    }
}
