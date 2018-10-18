package com.kakao.s2;

import com.kakao.network.ServerProtocol;
import com.kakao.test.common.KakaoTestCase;
import com.kakao.util.RequestConfiguration;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.shadows.ShadowLog;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kevin.kang
 * Created by kevin.kang on 2016. 8. 24..
 */

public class EventsLogRequestTest extends KakaoTestCase {
    private Event rootEvent;
    private EventsLogRequest request;
    private RequestConfiguration appConfig = new RequestConfiguration(
            "sample_app_key",
            "client_secret",
            "sample_key_hash",
            "sample_ka_header",
            "sample_app_ver",
            "sample_package_name",
            new JSONObject());

    @Before
    public void setup() {
        ShadowLog.stream = System.out;
        rootEvent = EventsTestHelper.buildNormalEvent(null);
//        rootEvent.setFrom("adid");
//        rootEvent.setAdidEnabled(true);

        List<Event> leafEvents = EventsTestHelper.buildLeafEvents();
        request = new EventsLogRequest(appConfig, rootEvent, leafEvents);
    }

    @Test
    public void testGetParams() {
        Map<String, String> params = request.getParams();
        assertEquals(5, params.size());
        assertEquals("adid", params.get(Event.FROM));
        assertNull(params.get(Event.PROPS));
    }

    @Test
    public void testGetParamsProps() {
        Map<String, Object> props = new HashMap<>();
        props.put("prop1", "value1");
        props.put("prop2", "value2");
        rootEvent = EventsTestHelper.buildNormalEvent(props);

        request = new EventsLogRequest(appConfig, rootEvent, null);
        Map<String, String> params = request.getParams();
        assertEquals("{\"prop2\":\"value2\",\"prop1\":\"value1\"}", params.get(Event.PROPS));
    }

    @Test
    public void testGetParamsWithAdid() {
        Map<String, Object> props = new HashMap<>();
        props.put("prop1", "value1");
        props.put("prop2", "value2");
        rootEvent = new Event.Builder().setTo("url").setFrom("adid").setAdidEnabled(true)
                .setAction("action").setTimestamp(new Date().getTime()).setProps(props).build();

        request = new EventsLogRequest(appConfig, rootEvent, null);
        Map<String, String> params = request.getParams();
        assertEquals("{\"prop2\":\"value2\",\"prop1\":\"value1\",\"adid_enabled\":1}", params.get(Event.PROPS));

    }

    @Test
    public void testGetHeaders() {
        Map<String, String> headers = request.getHeaders();
        assertTrue(headers.containsKey(ServerProtocol.AUTHORIZATION_HEADER_KEY));
        assertEquals(headers.get(ServerProtocol.AUTHORIZATION_HEADER_KEY), ServerProtocol.KAKAO_AK_HEADER_KEY + " " + appConfig.getAppKey());
    }
}
