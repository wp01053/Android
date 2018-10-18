package com.kakao.util;

import android.content.Context;

import com.kakao.util.helper.CommonProtocol;
import com.kakao.util.helper.SystemInfo;
import com.kakao.util.helper.Utility;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author kevin.kang. Created on 2017. 11. 13..
 */

public interface IConfiguration {
    String getAppKey();
    String getClientSecret();
    String getKeyHash();
    String getKAHeader();
    String getPackageName();
    String getAppVer();
    String getExtras();

    class Factory {
        public static IConfiguration createConfiguration(final Context context) {
            return createConfiguration(context, Utility.getMetadata(context, CommonProtocol.APP_KEY_PROPERTY));
        }

        public static IConfiguration createConfiguration(final Context context, final String appKey) {
            SystemInfo.initialize(context);
            String clientSecret = Utility.getMetadata(context, CommonProtocol.CLIENT_SECRET_PROPERTY);
            String keyHash = Utility.getKeyHash(context);
            String kaHeader = SystemInfo.getKAHeader();
            String appVer = String.valueOf(Utility.getAppVersion(context));
            String packageName = context.getPackageName();
            String extrasString;
            JSONObject extras = new JSONObject();
            try {
                extras.put(CommonProtocol.APP_PACKAGE, context.getPackageName());
                extras.put(CommonProtocol.KA_HEADER_KEY, kaHeader);
                extras.put(CommonProtocol.APP_KEY_HASH, keyHash);
                extrasString = extras.toString();
            } catch (JSONException e) {
                throw new IllegalArgumentException("JSON parsing error. Malformed parameters were provided. Detailed error message: " + e.toString());
            }
            return new RequestConfiguration(appKey, clientSecret, keyHash, kaHeader, appVer, packageName, extrasString);
        }
    }
}
