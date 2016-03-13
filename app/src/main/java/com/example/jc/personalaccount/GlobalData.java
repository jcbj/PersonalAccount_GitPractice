package com.example.jc.personalaccount;

import com.example.jc.personalaccount.DatabaseManger.DataStoreFactory;
import com.example.jc.personalaccount.DatabaseManger.IDataStoreHelper;

/**
 * Created by jc on 16/3/13.
 */
public class GlobalData {

    public static DataStoreFactory.DataStoreType StoreType = DataStoreFactory.DataStoreType.SQLCipher;

    public static IDataStoreHelper DataStoreHelper = DataStoreFactory.getDataStoreHelper(StoreType);

}
