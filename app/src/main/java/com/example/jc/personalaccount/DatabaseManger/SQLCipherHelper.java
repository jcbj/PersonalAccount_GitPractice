package com.example.jc.personalaccount.DatabaseManger;

import android.content.Context;

import com.example.jc.personalaccount.GlobalData;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.util.Objects;

/**
 * Created by jc on 16/3/14.
 */
public class SQLCipherHelper implements IDataStoreHelper {

    private static final String ID = "SQLCipherHelper";

    //单例模式
    private SQLCipherHelper() {

    }

    private static class LazyHelper {
        private static final SQLCipherHelper INSTANCE = new SQLCipherHelper();
    }

    public static final SQLCipherHelper getInstance() {
        return LazyHelper.INSTANCE;
    }

    //Field
    private static final String DATABASENAME = "PersonalAccount.db";
    private static final String KEYENCRYPT = "JCYOYO";
    private static final String USERIDTABLENAME = "UserIDTable";

    private SQLiteDatabase database = null;

    //implements IDataStoreHelper

    private void execSQL(String sql) {
        try {
            this.database.execSQL(sql);
        } catch (Exception ex) {
            GlobalData.log(ID + ".execSQL", GlobalData.LogType.eException, ex.getMessage());
        }
    }

    private void execSQL(String sql, Object[] objs) {
        try {
            this.database.execSQL(sql, objs);
        } catch (Exception ex) {
            GlobalData.log(ID + ".execSQLMulti", GlobalData.LogType.eException, ex.getMessage());
        }
    }

    public Boolean initDataStore(Context context) {

        String tag = ID + ".initDatabase";

        Boolean bIsSuccess = false;

        //Init Database
        try {
            SQLiteDatabase.loadLibs(context);              //导入包
            File file = context.getDatabasePath(DATABASENAME);     //获取数据库路径，参数："data.db"即为数据库名
            //如果路径不存在则创建路径
            if (!file.mkdirs()) {
                GlobalData.log(tag, GlobalData.LogType.eMessage, "Database path is mkdirs() failed.");
            }
            //TODO: 有待验证此处 file.delete() 的作用
            //这个删除是什么意思，如果是删除“data.db”文件，那岂不是每次都把数据库清空了？？？
            //但是如果没加这一句，后面创建数据库时会报错：unable to open database file.
            //删除自动创建的数据库文件，即“data.db”文件，由SQLiteDatabase来创建
            if (!file.delete()) {
                GlobalData.log(tag, GlobalData.LogType.eMessage, "Database path is mkdirs() failed.");
            }

            //第二个参数："123456"，即为数据库加密所用密码，如果数据库不存在，则创建新的；如果存在则直接打开
            database = SQLiteDatabase.openOrCreateDatabase(file, KEYENCRYPT, null);

            if (null != database) {
                //如果成功，则可以开始使用数据库(database)。操作和系统自带数据库一样，没有任何区别。
                this.createUserIDTable();

                bIsSuccess = true;
            } else {
                GlobalData.log(tag, GlobalData.LogType.eMessage, "SQLiteDatabase.openOrCreateDatabase is failed.");
            }
        } catch (Exception ex) {
            GlobalData.log(tag, GlobalData.LogType.eException, ex.getMessage());
        }

        return bIsSuccess;
    }

    private void createUserIDTable() {



    }
}
