package com.example.jc.personalaccount.DatabaseManger;

/**
 * Created by jc on 16/3/13.
 */
public class DataStoreFactory {

    public enum DataStoreType {
        SQLCipher
    }

    public static IDataStoreHelper getDataStoreHelper (DataStoreType storeType) {
        IDataStoreHelper helper;

        switch (storeType) {
            case SQLCipher:
                helper = SQLCipherDataStoreHelper.getInstance();
                break;
            default:
                helper = SQLCipherDataStoreHelper.getInstance();
        }

        return helper;
    }
}
