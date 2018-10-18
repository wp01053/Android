/*
  Copyright 2014-2018 Kakao Corp.

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
package com.kakao.kakaotalk.v2;

import com.kakao.auth.common.MessageSendable;
import com.kakao.friends.AppFriendContext;
import com.kakao.friends.FriendContext;
import com.kakao.friends.api.FriendsApi;
import com.kakao.friends.response.AppFriendsResponse;
import com.kakao.friends.response.FriendsResponse;
import com.kakao.kakaotalk.ChatListContext;
import com.kakao.kakaotalk.api.KakaoTalkApi;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.ChatListResponse;
import com.kakao.kakaotalk.response.KakaoTalkProfile;
import com.kakao.message.template.TemplateParams;
import com.kakao.network.tasks.ITaskQueue;
import com.kakao.network.tasks.KakaoResultTask;
import com.kakao.network.tasks.KakaoTaskQueue;
import com.kakao.util.helper.log.Logger;

import java.util.Map;

/**
 * 카카오톡 API 요청을 담당한다.
 * @author leo.shin
 */
public class KakaoTalkService {
    /**
     * 카카오톡 프로필 요청
     * @param callback 요청 결과에 대한 callback
     */
    public void requestProfile(final TalkResponseCallback<KakaoTalkProfile> callback) {
        requestProfile(callback, false);
    }

    /**
     * 카카오톡 프로필 요청
     * @param callback 요청 결과에 대한 callback
     * @param secureResource 이미지 url을 https로 반환할지 여부.
     */
    public void requestProfile(final TalkResponseCallback<KakaoTalkProfile> callback, final boolean secureResource) {
        taskQueue.addTask(new KakaoResultTask<KakaoTalkProfile>(callback) {
            @Override
            public KakaoTalkProfile call() throws Exception {
                return api.requestProfile(secureResource);
            }
        });
    }

    /**
     * 카카오톡 친구 리스트를 요청한다. Friends에 대한 접근권한이 있는 경우에만 얻어올 수 있다.
     * (제휴를 통해 권한이 부여된 특정 앱에서만 호출이 가능합니다.)
     * @param callback 요청 결과에 대한 callback
     * @param context 친구리스트 요청정보를 담고있는 context
     */
    public void requestFriends(final TalkResponseCallback<FriendsResponse> callback, final FriendContext context) {
        taskQueue.addTask(new KakaoResultTask<FriendsResponse>(callback) {
            @Override
            public FriendsResponse call() throws Exception {
                return api.requestFriends(context);
            }
        });
    }

    /**
     * Request for a list of KakaoTalk friends who also:
     *  - Registered to this app (Connect with Kakao)
     *  - Agreed to provide friends info to this app
     *
     *  This API will return an error if this Kakao account user does not user KakaoTalk. Use
     *  {@link TalkResponseCallback#onNotKakaoTalkUser()} to handle the error.
     *
     * @param context Context
     * @param callback Success/Failure callback
     * @since 1.11.1
     */
    public void requestAppFriends(final AppFriendContext context, final TalkResponseCallback<AppFriendsResponse> callback) {
        taskQueue.addTask(new KakaoResultTask<AppFriendsResponse>(callback) {
            @Override
            public AppFriendsResponse call() throws Exception {
                return api.requestAppFriends(context);
            }
        });
    }

    /**
     * 카카오톡 메시지 전송하며, 리치메시지 4.0 spec으로 구성된 template으로 카카오톡 메시지 전송.
     * (제휴를 통해 권한이 부여된 특정 앱에서만 호출이 가능합니다.)
     * @param callback 요청 결과에 대한 callback
     * @param receiverInfo 메세지 전송할 대상에 대한 정보를 가지고 있는 object
     * @param templateId 개발자 사이트를 통해 생성한 메시지 템플릿 id
     * @param args 메시지 템플릿에 정의한 arg key:value. 템플릿에 정의된 모든 arg가 포함되어야 함.
     */
    public void requestSendMessage(final TalkResponseCallback<Boolean> callback,
                                          final MessageSendable receiverInfo,
                                          final String templateId,
                                          final Map<String, String> args) {
        taskQueue.addTask(new KakaoResultTask<Boolean>(callback) {
            @Override
            public Boolean call() throws Exception {
                return api.requestSendV2Message(receiverInfo, templateId, args);
            }
        });
    }

    public void requestSendMessage(final TalkResponseCallback<Boolean> callback,
                                          final MessageSendable receiverInfo,
                                          final TemplateParams templateParams) {
        taskQueue.addTask(new KakaoResultTask<Boolean>(callback) {
            @Override
            public Boolean call() throws Exception {
                return api.requestSendV2Message(receiverInfo, templateParams);
            }
        });
    }

    /**
     * Send KakaoTalk message to self with message template v2.
     * 퍼미션 불필요. 수신자별/발신자별 쿼터 제한 없음.
     * 초대 메시지는 나에게 전송 불가.
     * 카카오톡에 가입이 되어있어야함.
     * @param callback 요청 결과에 대한 callback
     * @param templateId 개발자 사이트를 통해 생성한 메시지 템플릿 id
     * @param args 메시지 템플릿에 정의한 arg key:value. 템플릿에 정의된 모든 arg가 포함되어야 함.
     */
    public void requestSendMemo(final TalkResponseCallback<Boolean> callback,
                                       final String templateId,
                                       final Map<String, String> args) {
        taskQueue.addTask(new KakaoResultTask<Boolean>(callback) {
            @Override
            public Boolean call() throws Exception {
                return api.requestSendV2Memo(templateId, args);
            }
        });
    }

    public void requestSendMemo(final TalkResponseCallback<Boolean> callback,
                                final TemplateParams templateParams) {
        taskQueue.addTask(new KakaoResultTask<Boolean>(callback) {
            @Override
            public Boolean call() throws Exception {
                return api.requestSendV2Memo(templateParams);
            }
        });
    }

    /**
     * 톡의 채팅방 리스트 정보
     * 권한이 있는 방에 대한 정보만 내려받는다. 권한이 없는 {@link com.kakao.kakaotalk.ChatFilterBuilder.ChatFilter} 타입에 대해서는 카카오톡에 채팅방이 존재해도 값이 내려가지 않는다.
     * 기본 정렬은 asc로 최근 대화 순으로 정렬한다. (desc는 반대로 가장 오래된 대화 순으로 정렬한다.)
     * 권한이 필요한 채팅방 정보(regular_direct, regular_multi)
     * regular에 대한 권한은 제휴된 앱에만 부여합니다.
     * @param callback 요청 결과에 대한 callback
     * @param context {@link ChatListContext} 챗방리스트 요청정보를 담고있는 context
     */
    public void requestChatRoomList(final TalkResponseCallback<ChatListResponse> callback, final ChatListContext context) {
        taskQueue.addTask(new KakaoResultTask<ChatListResponse>(callback) {
            @Override
            public ChatListResponse call() throws Exception {
                return api.requestChatRoomList(context);
            }
        });
    }

    private static KakaoTalkService instance = new KakaoTalkService(KakaoTalkApi.getInstance(),
            KakaoTaskQueue.getInstance());

    private KakaoTalkApi api;
    private ITaskQueue taskQueue;

    private KakaoTalkService() {
    }

    public static KakaoTalkService getInstance() {
        return instance;
    }

    KakaoTalkService(final KakaoTalkApi api, final ITaskQueue taskQueue) {
        this.api = api;
        this.taskQueue = taskQueue;
    }
}
