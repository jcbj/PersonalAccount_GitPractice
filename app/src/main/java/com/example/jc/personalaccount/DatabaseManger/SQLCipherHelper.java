package com.example.jc.personalaccount.DatabaseManger;

import android.content.Context;

import com.example.jc.personalaccount.GlobalData;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;

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
    private static final String SQLITE_MASTER = "sqlite_master";

    private SQLiteDatabase database = null;

    //implements IDataStoreHelper

    private void execSQL(String sql) {
        try {
            GlobalData.log(ID + ".execSQL",GlobalData.LogType.eMessage,sql);
            this.database.execSQL(sql);
        } catch (Exception ex) {
            GlobalData.log(ID + ".execSQL", GlobalData.LogType.eException, ex.getMessage());
        }
    }

    private void execSQL(String sql, Object[] objs) {
        try {
            GlobalData.log(ID + ".execSQLMulti", GlobalData.LogType.eMessage, sql.replace("?", "%s"));
            this.database.execSQL(sql, objs);
        } catch (Exception ex) {
            GlobalData.log(ID + ".execSQLMulti", GlobalData.LogType.eException, ex.getMessage());
        }
    }

    //selectionArgs: 只针对WHERE字句部分进行占位符替换，其它部分无效
    private Cursor querySQL(String sql, String[] selectionArgs) {
        Cursor cursor = null;
        GlobalData.log(ID + ".querySQL", GlobalData.LogType.eMessage, sql.replace("?","%s"),selectionArgs);

        try {
            cursor = this.database.rawQuery(sql, selectionArgs);
        } catch (Exception ex) {
            GlobalData.log(ID + ".querySQL", GlobalData.LogType.eException, ex.getMessage());
        }

        return cursor;
    }

    private Boolean checkIsExist(String sql, String[] selectionArgs) {
        Cursor cursor = null;
        try {
            GlobalData.log(ID + ".checkIsExist", GlobalData.LogType.eMessage, sql.replace("?","%s"),selectionArgs);
            cursor = this.querySQL(sql, selectionArgs);
            if ((null != cursor) && (cursor.getCount() > 0))
            {
                return true;
            }
        } catch (Exception ex) {
            GlobalData.log(ID + ".checkIsExist", GlobalData.LogType.eException, ex.getMessage());
            return false;
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        return false;
    }

    private void createUserIDTable() {
        try {
            String sql = "SELECT * FROM " + SQLITE_MASTER + " WHERE type = 'table' and name = '" + USERIDTABLENAME + "'";
            if (!this.checkIsExist(sql,null)) {
                sql = "CREATE TABLE " + USERIDTABLENAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, password TEXT, email TEXT)";

                this.execSQL(sql);


                 sql = "SELECT * FROM " + SQLITE_MASTER + " WHERE type = 'table' and name = '" + USERIDTABLENAME + "'";
                if (!this.checkIsExist(sql,null)) {
                    GlobalData.log(ID + ".createUserIDTable", GlobalData.LogType.eMessage,"USERID TABLE not exist.");
                }


            }
        } catch (Exception ex) {
            GlobalData.log(ID + ".createUserIDTable", GlobalData.LogType.eException, ex.getMessage());
        }
    }

    public Boolean initDataStore(Context context) {

        String tag = ID + ".initDatabase";

        Boolean bIsSuccess = false;

        //Init Database
        try {
            SQLiteDatabase.loadLibs(context);              //导入包
            File file = context.getDatabasePath(DATABASENAME);     //获取数据库路径，参数："data.db"即为数据库名

            if (!file.exists()) {
                //如果路径不存在则创建路径
                if (!file.mkdirs()) {
                    GlobalData.log(tag, GlobalData.LogType.eMessage, "Database path is mkdirs() failed.");
                }
                //TODO: 有待验证此处 file.delete() 的作用
                //这个删除是什么意思，如果是删除“data.db”文件，那岂不是每次都把数据库清空了？？？
                //但是如果没加这一句，后面创建数据库时会报错：unable to open database file.
                //删除自动创建的数据库文件，即“data.db”文件，由SQLiteDatabase来创建
                if (!file.delete()) {
                    GlobalData.log(tag, GlobalData.LogType.eMessage, "Database path is delete() failed.");
                }
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

    public void closeDataStore() {
        this.database.close();
    }

    public Boolean login(String name, String password) {
        String sql = "SELECT * FROM " + USERIDTABLENAME + " WHERE name = ? AND password = ?";
        return this.checkIsExist(sql, new String[]{name, password});
    }

    public Boolean register(String name, String password, String email) {

        String sql = "INSERT INTO " + USERIDTABLENAME + "(name,password,email) values('" + name + "','" + password + "','" + email + "')";
        this.execSQL(sql);

        return this.login(name,password);
    }
}
