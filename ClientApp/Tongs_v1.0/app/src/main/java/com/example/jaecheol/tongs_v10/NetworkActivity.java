package com.example.jaecheol.tongs_v10;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by JaeCheol on 15. 3. 28..
 */
public class NetworkActivity {

    /**
     * 서버에 데이터를 보내는 메소드
     *
     * @param json
     * @return
     */
    static public String sendJsonDataToServer(int major, int minor, String json, String ServerURL) {
        InputStream inputStream = null;
        String result = "";

        if (json == null)
            json = "";

        DefaultHttpClient client = new DefaultHttpClient();
        try {

            // 1. create HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(ServerURL);

            // 4. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 5. set httpPost Entity
            httpPost.setEntity(se);

            // 6. Set some header to inform server about
            SetHttpHeader(major, minor, httpPost);

            // 7. Execute POST request to the given URL
            HttpResponse httpResponse = httpClient.execute(httpPost);

            // 8. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 9. convert inputStream to string
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else {
                result = "Did not work!";
            }

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 10. return result
        return result;
    }



    private static void SetHttpHeader(int major, int minor, HttpPost httpPost)
    {
        if( major == 0 )    {
            if( minor == 0 )    {

                httpPost.setHeader("mobile_number", "application/json");
            }
            else if( minor == 1 )   {

                httpPost.setHeader("mobile_number", "application/json");
                httpPost.setHeader("verification_code", "application/json");
            }
        }
        else if( major == 1 )   {
            if( minor == 0 )    {

                httpPost.setHeader("mobile_number", "application/json");
                httpPost.setHeader("sex", "application/json");
                httpPost.setHeader("birthdate", "application/json");
                httpPost.setHeader("auth_token", "application/json");
            }
        }
    }



    /**
     * InputStream to String
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((inputStream)));
        String line = "";
        String result = "";
        while( (line = bufferedReader.readLine()) != null ) {
            result += line;
        }

        inputStream.close();
        return result;
    }


    /**
     * 받은 JSON 객체를 파싱하는 메소드
     * @param pRecvServerPage
     * @return
     */
    public static String[][] jsonParserList(String pRecvServerPage) {

        Log.i("서버에서 받은 전체 내용 : ", pRecvServerPage);

        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");


            // 받아온 pRecvServerPage를 분석하는 부분
            String[] jsonName = {"msg1", "msg2", "msg3"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                if(json != null) {
                    for(int j = 0; j < jsonName.length; j++) {
                        parseredData[i][j] = json.getString(jsonName[j]);
                    }
                }
            }


            // 분해 된 데이터를 확인하기 위한 부분
            for(int i=0; i<parseredData.length; i++){
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][0]);
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][1]);
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][2]);
            }

            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
