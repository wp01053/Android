package com.study.android.xmljsonex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "lecture";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onBtn1(View v) {
        getJsonData1();
    }

    public void onBtn2(View v) {
        getJsonData2();
    }

    public void onBtn3(View v) {
        getJsonData3();
    }

    public void onBtn4(View v) {
        getXmlData1();
    }

    private void getJsonData1(){
        String json4arr = "{\"number\":[1,2,3,4,5]}";
        try{
            JSONObject jobj = new JSONObject(json4arr);
            JSONArray jarr = jobj.getJSONArray("number");
            for(int i =0; i< jarr.length(); i++){
                int tmp = jarr.getInt(i);
                Log.d(TAG, "Json Data : " + tmp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getJsonData2(){
        String json4obj=   "{\"color\":{\"top\":\"red\",\"bottom\":\"black\",\"left\":\"blue\",\"right\":\"green\"}}";
        try{
            JSONObject jobj = new JSONObject(json4obj);
            JSONObject color = jobj.getJSONObject("color");
            if(color.has("left")){
               String top_color = color.getString("left");
                Log.d(TAG, "left color : " + top_color);
            }
            if(color.has("top")){
                String top_color = color.getString("top");
                Log.d(TAG, "top color : " + top_color);
            }

            if(color.has("bottom")){
                String top_color = color.getString("bottom");
                Log.d(TAG, "bottom color : " + top_color);
            }

            if(color.has("right")){
                String top_color = color.getString("right");
                Log.d(TAG, "right color : " + top_color);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getJsonData3(){
        String jString = "{\"menu\": {\"id\": \"file\", \"value\": \"File\", \"popup\": { \"menuitem\": [ {\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"}, {\"value\": \"Open\", \"onclick\": \"OpenDoc()\"}, {\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}]}}}";
        try{
            JSONObject jobj = new JSONObject(jString);
            JSONObject menuObject = jobj.getJSONObject("menu");

            String attributeId = menuObject.getString("id");
            String attributeValue = menuObject.getString("value");
            JSONObject popupObject = menuObject.getJSONObject("popup");

            JSONArray menuitemArray = popupObject.getJSONArray("menuitem");

            for(int i = 0; i < menuitemArray.length(); i++){
                Log.d(TAG, "value : " + menuitemArray.getJSONObject(i).getString("value"));
                Log.d(TAG, "onclick : " + menuitemArray.getJSONObject(i).getString("onclick"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getXmlData1() {

        try {
            ArrayList<String> aNumber = new ArrayList<String>();
            ArrayList<String> aWord   = new ArrayList<String>();
            ArrayList<String> aMean   = new ArrayList<String>();

            // XML 처리에 사용할 변수 선언
            int event = 0;
            String currentTag = null;
            Stack<String> tagStack = new Stack<String>();

            // 스택 사용 여부! 값을 바꿔서 실행해 보시고
            // <word>aaa<any>any text</any>bbb</word>에서 bbb의 결과를 확인해 보세요.
            boolean useStack = true;

            // XML 파서
            XmlPullParser parser = getResources().getXml(R.xml.test);

            // 파싱 시작
            while((event = parser.next()) != XmlPullParser.END_DOCUMENT) {

                switch(event) {

                    case XmlPullParser.START_TAG:
                        // 시작 태그를 만나면 currentTag에 기록
                        if(useStack && currentTag != null) {
                            tagStack.push(currentTag);
                        }
                        currentTag = parser.getName();
                        break;

                    case XmlPullParser.TEXT:
                        // currentTag가 처리하고자 하는 태그이면...
                        if(currentTag != null) {

                            if (currentTag.equals("number")) {
                                String value = parser.getText();
                                aNumber.add(value);
                            } else if (currentTag.equals("word")) {
                                String value = parser.getText();
                                aWord.add(value);
                            } else if (currentTag.equals("mean")) {
                                String value = parser.getText();
                                aMean.add(value);
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        // 종료 태그를 만나면 currentTag 초기화
                        if(useStack && tagStack.size() > 0) {
                            currentTag = tagStack.pop();
                        }
                        else {
                            currentTag = null;
                        }
                        break;

                    default:
                        break;
                }
            }

            for(int i = 0; i < aNumber.size(); i++)
            {
                Log.d(TAG, "number : " + aNumber.get(i));
                Log.d(TAG, "word   : " + aWord.get(i));
                Log.d(TAG, "mean   : " + aMean.get(i));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }



}
