package com.kakao.kakaolink.v2.network;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.kakao.kakaolink.R;
import com.kakao.kakaolink.internal.KakaoTalkLinkProtocol;
import com.kakao.message.template.TemplateParams;
import com.kakao.network.IRequest;
import com.kakao.network.ServerProtocol;
import com.kakao.util.IConfiguration;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.CommonProtocol;
import com.kakao.util.helper.log.Logger;
import com.kakao.util.KakaoUtilService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author kevin.kang. Created on 2017. 11. 13..
 */

class DefaultKakaoLinkCore implements KakaoLinkCore {
    private KakaoUtilService utilService;

    DefaultKakaoLinkCore(final KakaoUtilService utilService) {
        this.utilService = utilService;
    }

    @Override
    public IRequest defaultTemplateRequest(final Context context, final String appKey, final TemplateParams params) {
        IConfiguration configuration = utilService.getAppConfiguration(context);
        return new TemplateDefaultRequest(configuration, appKey, params);
    }

    @Override
    public IRequest customTemplateRequest(final Context context, final String appKey, String templateId, Map<String, String> templateArgs) {
        IConfiguration configuration = utilService.getAppConfiguration(context);
        return new TemplateValidateRequest(configuration, appKey, templateId, templateArgs);
    }

    @Override
    public IRequest scrapTemplateRequest(Context context, String appKey, String url) {
        return scrapTemplateRequest(context, appKey, url, null, null);
    }

    @Override
    public IRequest scrapTemplateRequest(final Context context, final String appKey, String url, final String templateId, final Map<String, String> templateArgs) {
        IConfiguration configuration = utilService.getAppConfiguration(context);
        return new TemplateScrapRequest(configuration, appKey, url, templateId, templateArgs);
    }

    @Override
    public boolean isAvailable(Context context) {
        Uri uri = new Uri.Builder().scheme(KakaoTalkLinkProtocol.LINK_SCHEME).authority(KakaoTalkLinkProtocol.LINK_AUTHORITY).build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        return utilService.resolveIntent(context, intent, KakaoTalkLinkProtocol.TALK_MIN_VERSION_SUPPORT_LINK_V2) != null;
    }

    @Override
    public Intent kakaoLinkIntent(final Context context, String appKey, final JSONObject linkResponse) throws KakaoException {
        IConfiguration configuration = utilService.getAppConfiguration(context);

        try {
            int size = getAttachmentSize(configuration, linkResponse);
            Logger.i("KakaoLink intent size is %d bytes.", size);
            if (size > KakaoTalkLinkProtocol.LINK_URI_MAX_SIZE) {
                throw new KakaoException(KakaoException.ErrorType.URI_LENGTH_EXCEEDED, context.getString(R.string.com_kakao_alert_uri_too_long));
            }
        } catch (JSONException e) {
            throw new KakaoException(KakaoException.ErrorType.JSON_PARSING_ERROR, e.toString());
        }

        String templateId = linkResponse.optString(KakaoTalkLinkProtocol.TEMPLATE_ID, null);
        JSONObject templateArgs = linkResponse.optJSONObject(KakaoTalkLinkProtocol.TEMPLATE_ARGS);
        JSONObject templateJson = linkResponse.optJSONObject(KakaoTalkLinkProtocol.TEMPLATE_MSG);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(KakaoTalkLinkProtocol.LINK_SCHEME).authority(KakaoTalkLinkProtocol.LINK_AUTHORITY);
        builder.appendQueryParameter(KakaoTalkLinkProtocol.LINKVER, KakaoTalkLinkProtocol.LINK_VERSION_40);
        if (appKey == null) {
            appKey = configuration.getAppKey();
        }
        builder.appendQueryParameter(KakaoTalkLinkProtocol.APP_KEY, appKey);
        builder.appendQueryParameter(KakaoTalkLinkProtocol.APP_VER, utilService.getAppConfiguration(context).getAppVer());

        if (templateId != null) {
            builder.appendQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_ID, templateId);
        }
        if (templateArgs != null) {
            builder.appendQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_ARGS, templateArgs.toString());
        }
        if (templateJson != null) {
            builder.appendQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_JSON, templateJson.toString());
        }

        if (configuration.getExtras() != null) {
            builder.appendQueryParameter(KakaoTalkLinkProtocol.EXTRAS, configuration.getExtras());
        }
        Uri uri = builder.build();
        Intent intent = new Intent(Intent.ACTION_SEND, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return utilService.resolveIntent(context, intent, TALK_MIN_VERSION_SUPPORT_LINK_40);
    }

    @Override
    public Intent kakaoTalkMarketIntent(Context context) {
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(KakaoTalkLinkProtocol.TALK_MARKET_URL_PREFIX + getReferrer(utilService.getAppConfiguration(context))));
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return marketIntent;
    }

    @Override
    public Uri sharerUri(Context context, TemplateParams params) {
        JSONObject validationParams = new JSONObject();
        try {
            validationParams.put(KakaoTalkLinkProtocol.TEMPLATE_OBJECT, params.toJSONObject());
        } catch (JSONException e) {
            return null;
        }
        return shareUri(context, KakaoTalkLinkProtocol.VALIDATION_DEFAULT, validationParams);    }

    @Override
    public Uri sharerUri(Context context, String templateId, Map<String, String> templateArgs) {
        JSONObject validationParams = new JSONObject();
        try {
            validationParams.put(KakaoTalkLinkProtocol.TEMPLATE_ID, Integer.parseInt(templateId));
            if (templateArgs != null) {
                validationParams.put(KakaoTalkLinkProtocol.TEMPLATE_ARGS, new JSONObject(templateArgs));
            }
        } catch (JSONException e) {
            return null;
        }
        return shareUri(context, KakaoTalkLinkProtocol.VALIDATION_CUSTOM, validationParams);
    }

    @Override
    public Uri sharerUri(Context context, String url) {
        JSONObject validationParams = new JSONObject();
        try {
            validationParams.put(KakaoTalkLinkProtocol.REQUEST_URL, url);
        } catch (JSONException e) {
            return null;
        }
        return shareUri(context, KakaoTalkLinkProtocol.VALIDATION_SCRAP, validationParams);
    }

    @Override
    public Uri sharerUri(Context context, String url, String templateId, Map<String, String> templateArgs) {
        JSONObject validationParams = new JSONObject();
        try {
            validationParams.put(KakaoTalkLinkProtocol.REQUEST_URL, url);
            if (templateId != null) {
                validationParams.put(KakaoTalkLinkProtocol.TEMPLATE_ID, Integer.parseInt(templateId));
            }
            if (templateArgs != null) {
                validationParams.put(KakaoTalkLinkProtocol.TEMPLATE_ARGS, new JSONObject(templateArgs));
            }
        } catch (JSONException e) {
            return null;
        }
        return shareUri(context, KakaoTalkLinkProtocol.VALIDATION_SCRAP, validationParams);
    }

    private Uri shareUri(final Context context, final String validationAction, JSONObject validationParams) {
        IConfiguration configuration = utilService.getAppConfiguration(context);
        try {
            validationParams.put(KakaoTalkLinkProtocol.LINK_VER, KakaoTalkLinkProtocol.LINK_VERSION_40);
        } catch (JSONException e) {
            return null;
        }

        return new Uri.Builder().authority(KakaoTalkLinkProtocol.SHARE_AUTHORITY)
                .scheme(ServerProtocol.SCHEME)
                .path(KakaoTalkLinkProtocol.SHARE_URI)
                .appendQueryParameter(KakaoTalkLinkProtocol.SHARER_APP_KEY, configuration.getAppKey())
                .appendQueryParameter(KakaoTalkLinkProtocol.VALIDATION_ACTION, validationAction)
                .appendQueryParameter(KakaoTalkLinkProtocol.VALIDATION_PARAMS, validationParams.toString())
                .appendQueryParameter(KakaoTalkLinkProtocol.SHARER_KA, configuration.getKAHeader())
                .build();
    }


    @SuppressWarnings("WeakerAccess")
    String getReferrer(final IConfiguration configuration) {
        JSONObject json = new JSONObject();
        try {
            json.put(CommonProtocol.KA_HEADER_KEY, configuration.getKAHeader());
            json.put(KakaoTalkLinkProtocol.APP_KEY, configuration.getAppKey());
            json.put(KakaoTalkLinkProtocol.APP_VER, configuration.getAppVer());
            json.put(KakaoTalkLinkProtocol.APP_PACKAGE, configuration.getPackageName());
        } catch (JSONException e) {
            Logger.w(e);
            return "";
        }
        return json.toString();
    }

    @SuppressWarnings("WeakerAccess")
    int getAttachmentSize(final IConfiguration configuration, final JSONObject response) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KakaoTalkLinkProtocol.lv, KakaoTalkLinkProtocol.LINK_VERSION_40);
        jsonObject.put(KakaoTalkLinkProtocol.av, KakaoTalkLinkProtocol.LINK_VERSION_40);
        jsonObject.put(KakaoTalkLinkProtocol.ak, configuration.getAppKey());

        JSONObject templateJson = response.optJSONObject(KakaoTalkLinkProtocol.TEMPLATE_MSG);
        jsonObject.put(KakaoTalkLinkProtocol.P, templateJson.get(KakaoTalkLinkProtocol.P));
        jsonObject.put(KakaoTalkLinkProtocol.C, templateJson.get(KakaoTalkLinkProtocol.C));
        jsonObject.put(KakaoTalkLinkProtocol.TEMPLATE_ID, response.optString(KakaoTalkLinkProtocol.TEMPLATE_ID, null));
        JSONObject templateArgs = response.optJSONObject(KakaoTalkLinkProtocol.TEMPLATE_ARGS);
        if (templateArgs != null) {
            jsonObject.put(KakaoTalkLinkProtocol.TEMPLATE_ARGS, templateArgs);
        }
        if (configuration.getExtras() != null) {
            jsonObject.put(KakaoTalkLinkProtocol.EXTRAS, configuration.getExtras());
        }
        return jsonObject.toString().getBytes().length;
    }

    private static final int TALK_MIN_VERSION_SUPPORT_LINK_40 = 1400255; // 6.0.0
}
