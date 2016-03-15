package com.example.jc.personalaccount;

import android.util.Log;

import com.example.jc.personalaccount.DatabaseManger.DataStoreFactory;
import com.example.jc.personalaccount.DatabaseManger.IDataStoreHelper;

/**
 * Created by jc on 16/3/13.
 */
public class GlobalData {

    public static DataStoreFactory.DataStoreType StoreType = DataStoreFactory.DataStoreType.SQLCipher;

    public static IDataStoreHelper DataStoreHelper = DataStoreFactory.getDataStoreHelper(StoreType);

    public static enum LogType {
        eMessage,
        eException,
        eError
    }

    public static void log(String tag, LogType logType, String format) {
        log(tag,logType,format,null);
    }

    public static void log(String tag, LogType logType, String format, String[] args) {
        if (!BuildConfig.DEBUG) {
            return;
        }

        if (format.isEmpty()) {
            return;
        }

        //String format
        String log = new String();

        if ((null == args) || (0 == args.length)) {
            log = format;
        } else {
            char[] formatChar = format.toCharArray();
            int j = 0;
            for (int i = 0; i < formatChar.length - 1; i++) {
                if (('%' == formatChar[i]) && ('s' == formatChar[i + 1])) {
                    if (j < args.length) {
                        log += args[j++];
                    }
                } else {
                    log += formatChar[i];
                }
            }
        }

        switch (logType) {
            case eMessage:
                Log.v(tag, log);
                break;
            case eException:
                Log.d(tag, log);
                break;
            case eError:
                Log.e(tag, log);
                break;
            default:
                Log.v(tag, log);
        }
    }
}
