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
                helper = SQLCipherHelper.getInstance();
                break;
            default:
                helper = SQLCipherHelper.getInstance();
        }

        return helper;
    }
}
