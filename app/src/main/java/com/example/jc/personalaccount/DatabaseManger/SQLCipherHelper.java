package com.example.jc.personalaccount.DatabaseManger;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;

/**
 * Created by jc on 16/3/14.
 */
public class SQLCipherHelper implements IRelationalDatabaseHelper{

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

    private SQLiteDatabase database = null;

    //implements IRelationalDatabaseHelper

    public Boolean initDatabase(Context context) {

        Boolean bIsSuccess = false;

        //Init Database
        try {
            SQLiteDatabase.loadLibs(context);              //导入包
            File file = context.getDatabasePath(DATABASENAME);     //获取数据库路径，参数："data.db"即为数据库名
            //如果路径不存在则创建路径
            if (!file.mkdirs()) {
                System.out.print("Database path is mkdirs() failed.");
            }
            //TODO: 有待验证此处 file.delete() 的作用
            //这个删除是什么意思，如果是删除“data.db”文件，那岂不是每次都把数据库清空了？？？
            //但是如果没加这一句，后面创建数据库时会报错：unable to open database file.
            //删除自动创建的数据库文件，即“data.db”文件，由SQLiteDatabase来创建
            if (!file.delete()) {
                System.out.print("Database path is mkdirs() failed.");
            }

            //第二个参数："123456"，即为数据库加密所用密码，如果数据库不存在，则创建新的；如果存在则直接打开
            database = SQLiteDatabase.openOrCreateDatabase(file, KEYENCRYPT, null);

            if (null != database) {
                //如果成功，则可以开始使用数据库(database)。操作和系统自带数据库一样，没有任何区别。
                //Test
                database.execSQL("create table t1(a,b)");
                database.execSQL("insert into t1(a,b) values(?,?)",new Object[]{"one for the money","two for the show"});

                bIsSuccess = true;
            }
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }

        return bIsSuccess;
    }
}
