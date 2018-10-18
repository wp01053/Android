/*
  Copyright 2016-2017 Kakao Corp.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.kakao.kakaolink.v2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsService;
import android.support.customtabs.CustomTabsServiceConnection;

import com.kakao.kakaolink.R;
import com.kakao.kakaolink.v2.network.KakaoLinkCore;
import com.kakao.kakaolink.v2.network.KakaoLinkImageService;
import com.kakao.message.template.TemplateParams;
import com.kakao.network.ErrorResult;
import com.kakao.network.IRequest;
import com.kakao.network.NetworkService;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.response.ResponseStringConverter;
import com.kakao.network.storage.ImageDeleteResponse;
import com.kakao.network.storage.ImageUploadResponse;
import com.kakao.util.AbstractFuture;
import com.kakao.util.KakaoUtilService;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Class for sending KakaoTalk messages using KakaoLink v2 API.
 *
 * @author kevin.kang
 * Created by kevin.kang on 2016. 11. 25..
 */

public class KakaoLinkService {
    private static KakaoLinkService instance = new KakaoLinkService(KakaoLinkCore.Factory.getInstance(),
            KakaoLinkImageService.Factory.getInstance(), NetworkService.Factory.getInstance());

    private KakaoLinkCore linkCore;
    private KakaoLinkImageService imageService;
    private NetworkService networkService;

    KakaoLinkService(KakaoLinkCore linkCore, KakaoLinkImageService imageService, NetworkService networkService) {
        this.linkCore = linkCore;
        this.imageService = imageService;
        this.networkService = networkService;
    }

    /**
     * Returns a singleton instance for KakaoLink v2 API.
     * @return a singleton instance of KakaoLinkService class
     */
    public static KakaoLinkService getInstance() {
        return instance;
    }

    public boolean isKakaoLinkV2Available(final Context context) {
        return linkCore.isAvailable(context);
    }

    /**
     * Send KakaoLink v2 message with custom templates. Template id and arguments should be provided.
     * @param context Context to start an activity for KakaoLink
     * @param templateId id of the custom template created in developer website
     * @param templateArgs template arguments to fill in the custom template
     * @param callback success/failure callback that will contain detailed warnings or error messages
     * @throws IllegalStateException if kakao app key or android key hash is not set
     */
    public void sendCustom(final Context context, final String templateId, final Map<String, String> templateArgs, final ResponseCallback<KakaoLinkResponse> callback) {
        // App key, key hash, KA header 등을 준비한다.
        // 카카오링크 4.0을 실행시킬 수 있는 카카오톡 버전(6.0) 인지 체크한다.
        Future<IRequest> requestFuture = new AbstractFuture<IRequest>() {
            @Override
            public IRequest get() throws InterruptedException, ExecutionException {
                return linkCore.customTemplateRequest(context, null, templateId, templateArgs);
            }
        };
        Future<Uri> uriFuture = new AbstractFuture<Uri>() {
            @Override
            public Uri get() throws InterruptedException, ExecutionException {
                return linkCore.sharerUri(context, templateId, templateArgs);
            }
        };
        sendKakaoLinkRequest(context, requestFuture, uriFuture, callback);
    }

    /**
     * Send KakaoLink v2 message with default templates.
     * @param context Context to start an activity for KakaoLink
     * @param params TemplateParams object containing template arguments bulit with its builder
     * @param callback success/failure callback that will contain detailed warnings or error messages
     * @throws IllegalStateException if kakao app key or android key hash is not set
     */
    public void sendDefault(final Context context, final TemplateParams params, final ResponseCallback<KakaoLinkResponse> callback) {
        Future<IRequest> requestFuture = new AbstractFuture<IRequest>() {
            @Override
            public IRequest get() throws InterruptedException, ExecutionException {
                return linkCore.defaultTemplateRequest(context, null, params);
            }
        };
        Future<Uri> uriFuture = new AbstractFuture<Uri>() {
            @Override
            public Uri get() throws InterruptedException, ExecutionException {
                return linkCore.sharerUri(context, params);
            }
        };
        sendKakaoLinkRequest(context, requestFuture, uriFuture, callback);
    }

    /**
     * Send a URL scrap message with custom template and template arguments.
     * @param context Context to start an activity for KakaoLink
     * @param url URL to be scrapped
     * @param templateId id of the custom template created in developer website
     * @param templateArgs template arguments to fill in the custom template
     * @param callback success/failure callback that will be passed detailed warnings or error messages
     * @throws IllegalStateException if kakao app key or android key hash is not set
     */
    @SuppressWarnings("WeakerAccess")
    public void sendScrap(final Context context, final String url, final String templateId, final Map<String, String> templateArgs, final ResponseCallback<KakaoLinkResponse> callback) {
        Future<IRequest> requestFuture = new AbstractFuture<IRequest>() {
            @Override
            public IRequest get() throws InterruptedException, ExecutionException {
                return linkCore.scrapTemplateRequest(context, null, url, templateId, templateArgs);
            }
        };
        Future<Uri> uriFuture = new AbstractFuture<Uri>() {
            @Override
            public Uri get() throws InterruptedException, ExecutionException {
                return linkCore.sharerUri(context, url, templateId, templateArgs);
            }
        };
        sendKakaoLinkRequest(context, requestFuture, uriFuture, callback);
    }

    /**
     * Send a URL scrap message with default scrap template.
     * @param context Context to start an activity for KakaoLink
     * @param url URL to be scrapped
     * @param callback success/failure callback that will be passed detailed warnings or error messages
     * @throws IllegalStateException if kakao app key or android key hash is not set
     */
    public void sendScrap(final Context context, final String url, final ResponseCallback<KakaoLinkResponse> callback) {
        sendScrap(context, url, null, null, callback);
    }

    /**
     * Upload image to Kakao storage server to be used in KakaoLink message.
     * @param context Context to start an activity for KakaoLink
     * @param secureResource true if https is needed for image url, false if http is sufficient
     * @param imageFile Image file
     * @param callback success/failure callback that will be passed detailed warnings or error messages
     */
    public void uploadImage(final Context context, final Boolean secureResource, final File imageFile, final ResponseCallback<ImageUploadResponse> callback) {
        Future<IRequest> future = new AbstractFuture<IRequest>() {
            @Override
            public IRequest get() throws InterruptedException, ExecutionException {
                return imageService.imageUploadRequest(context, imageFile, secureResource);
            }
        };
        sendLinkImageRequest(future, ImageUploadResponse.CONVERTER, callback);
    }

    /**
     * Upload image with the given URL to Kakao storage server.
     * @param context Context to start an activity for KakaoLink
     * @param secureResource true if https is needed for image url, false if http is sufficient
     * @param imageUrl URL of image to be scrapped
     * @param callback success/failure callback that will be passed detailed warnings or error messages
     */
    public void scrapImage(final Context context, final Boolean secureResource, final String imageUrl, final ResponseCallback<ImageUploadResponse> callback) {
        Future<IRequest> future = new AbstractFuture<IRequest>() {
            @Override
            public IRequest get() throws InterruptedException, ExecutionException {
                return imageService.imageScrapRequest(context, imageUrl, secureResource);
            }
        };
        sendLinkImageRequest(future, ImageUploadResponse.CONVERTER, callback);
    }

    /**
     * Delete the image with the given URL from Kakao storage server.
     * @param context Context to start an activity for KakaoLink
     * @param imageUrl true if https is needed for image url, false if http is sufficient
     * @param callback success/failure callback that will be passed detailed warnings or error messages
     */
    public void deleteImageWithUrl(final Context context, final String imageUrl, final ResponseCallback<ImageDeleteResponse> callback) {
        Future<IRequest> future = new AbstractFuture<IRequest>() {
            @Override
            public IRequest get() throws InterruptedException, ExecutionException {
                return imageService.imageDeleteRequestWithUrl(context, imageUrl);
            }
        };
        sendLinkImageRequest(future, ImageDeleteResponse.CONVERTER, callback);
    }

    /**
     * Delete the image with the given token from Kakao storage server.
     * @param context Context to start an activity for KakaoLink
     * @param imageToken Token of image to be deleted
     * @param callback success/failure callback that will be passed detailed warnings or error messages
     */
    @SuppressWarnings("unused")
    public void deleteImageWithToken(final Context context, final String imageToken, final ResponseCallback<ImageDeleteResponse> callback) {
        Future<IRequest> future = new AbstractFuture<IRequest>() {
            @Override
            public IRequest get() throws InterruptedException, ExecutionException {
                return imageService.imageDeleteRequestWithToken(context, imageToken);
            }
        };
        sendLinkImageRequest(future, ImageDeleteResponse.CONVERTER, callback);
    }

    private void sendKakaoLinkRequest(final Context context, final Future<IRequest> requestFuture, final Future<Uri> uriFuture, final ResponseCallback<KakaoLinkResponse> callback) {
        if (!isKakaoLinkV2Available(context) && Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            callback.onFailure(getKakaoTalkNotInstalledErrorResult(context));
            return;
        }
        try {
            networkService.request(requestFuture.get(), JSON_OBJECT_CONVERTER, new ResponseCallback<JSONObject>() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    callback.onFailure(errorResult);
                }

                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        if (isKakaoLinkV2Available(context)) {
                            Intent intent = linkCore.kakaoLinkIntent(context, null, result);
                            context.startActivity(intent);
                        } else {
                            openUrlWithCustomTab(context, uriFuture.get());
                        }
                        if (callback != null) {
                            callback.onSuccess(new KakaoLinkResponse(result));
                        }
                    } catch (Exception e) {
                        if (callback != null) {
                            callback.onFailure(new ErrorResult(e));
                        }
                    }
                }

                @Override
                public void onDidStart() {
                    super.onDidStart();
                    callback.onDidStart();
                }

                @Override
                public void onDidEnd() {
                    super.onDidEnd();
                    callback.onDidEnd();
                }
            });
        } catch (Exception e) {
            if (callback != null) {
                callback.onFailure(new ErrorResult(e));
            }
        }
    }

    private <T> void sendLinkImageRequest(final Future<IRequest> requestFuture, final ResponseStringConverter<T> converter, final ResponseCallback<T> callback) {
        try {
            networkService.request(requestFuture.get(), converter, callback);
        } catch (Exception e) {
            if (callback != null) {
                callback.onFailure(new ErrorResult(e));
            }
        }
    }

    private ErrorResult getKakaoTalkNotInstalledErrorResult(final Context context) {
        return new ErrorResult(new KakaoException(KakaoException.ErrorType.KAKAOTALK_NOT_INSTALLED, context.getString(R.string.com_kakao_alert_install_kakaotalk)));
    }

    /**
     * Returns an intent to start KakaoTalk install page of Google play store.
     *
     * @param context Context to get app information (package name, app key, key hash, and KA header) from.
     * @return Intent to start KakaoTalk install page of Google Play Store.
     */
    @SuppressWarnings("unused")
    public Intent getKakaoTalkInstallIntent(final Context context) {
        return linkCore.kakaoTalkMarketIntent(context);
    }

    private static final ResponseStringConverter<JSONObject> JSON_OBJECT_CONVERTER = new ResponseStringConverter<JSONObject>() {
        @Override
        public JSONObject convert(String o) {
            try {
                return new JSONObject(o);
            } catch (JSONException e) {
                Logger.e(e.toString());
                return null;
            }
        }
    };

    void openUrlWithCustomTab(final Context context, final Uri uri) throws KakaoException {
        final String packageName = resolveCustomTabsPackageName(context, uri);
        if (packageName == null) {
            throw new KakaoException(KakaoException.ErrorType.KAKAOTALK_NOT_INSTALLED, context.getString(R.string.com_kakao_alert_install_kakaotalk));
        }
        CustomTabsClient.bindCustomTabsService(context, packageName, new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.enableUrlBarHiding();
                builder.setShowTitle(true);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.intent.setData(uri);
                customTabsIntent.intent.setPackage(packageName);
                context.startActivity(customTabsIntent.intent);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        });
    }

    String resolveCustomTabsPackageName(final Context context, final Uri uri) {
        String packageName = null;
        String availableChrome = null;

        // get ResolveInfo for default browser
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(browserIntent, PackageManager.MATCH_DEFAULT_ONLY);

        // get ResolveInfos for browsers that support custom tabs protocol
        Intent serviceIntent = new Intent();
        serviceIntent.setAction(CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION);
        List<ResolveInfo> serviceInfos = context.getPackageManager().queryIntentServices(serviceIntent, 0);
        for (ResolveInfo info : serviceInfos) {
            // check if chrome is available on this device
            if (availableChrome == null && isPackageNameChrome(info.serviceInfo.packageName)) {
                availableChrome = info.serviceInfo.packageName;
            }
            // check if the browser being looped is the default browser
            if (info.serviceInfo.packageName.equals(resolveInfo.activityInfo.packageName)) {
                packageName = resolveInfo.activityInfo.packageName;
                break;
            }
        }

        // if the default browser does not support custom tabs protocol, use chrome if available.
        if (packageName == null && availableChrome != null) {
            packageName = availableChrome;
        }
        Logger.d("selected browser for kakaolink is %s", packageName);
        return packageName;
    }

    boolean isPackageNameChrome(final String packageName) {
        return chromePackageNames.contains(packageName);
    }

    private List<String> chromePackageNames = Arrays.asList(
            "com.android.chrome",
            "com.chrome.beta",
            "com.chrome.dev");
}
