package com.csform.android.uiapptemplate.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jun on 15. 5. 2..
 */
public class HttpTask extends AsyncTask<String, Void, byte[]> {

    OnHttpReceive m_httpReceive;
    public HttpTask(OnHttpReceive httpReceive)
    {
        m_httpReceive = httpReceive;
    }

    protected byte[] doInBackground(String... params) {

//            InputStream is = getInputStreamFromUrl(params[0]);
//            String result = convertStreamToString(is);

        String response = null;

        if(params[0].equals("http://somabell01.cloudapp.net:8080/store/store/get?token=6001b1ce82bc090f7b29964b888903c318f6d518&sid=1"))
        {
            response = null;
        }

        try {
            //response = getRemoteData(params[0]);
            byte data[] = remoteSyncHttp(params[0], null);
            if(data == null)
            {
                Thread.currentThread().sleep(1000);
                data = remoteSyncHttp(params[0], null);
            }
            //if(data == null)
            //    return null;
            //response = new String(data);
            return data;
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }



    protected void onPostExecute(byte data[]) {
        if(m_httpReceive == null)
            return;

        m_httpReceive.onReceive(data);
//            Log.d("Server_result", result);
        //processReceive(result);
    }

    public static byte[] remoteSyncHttp(String requestUrl, String arrParam[][])
    {
        PrintWriter pw = null;
        HttpURLConnection conn = null;
        byte resultData[] = null;
        try
        {
            URL url = new URL(requestUrl);
            conn = (HttpURLConnection)url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);
                conn.setUseCaches(false);

                if(arrParam != null)
                {
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    StringBuilder param = new StringBuilder();
                    for (int i=0; i<arrParam.length; i++)
                    {
                        param.append(arrParam[i][0]);
                        param.append("=");
                        param.append(arrParam[i][1]);
                        if ( i != (arrParam.length-1) )
                            param.append("&");
                    }
                    String paramStr = param.toString();
                    conn.setRequestProperty("Content-Length", "" + Integer.toString(paramStr.getBytes().length));

                    pw = new PrintWriter(conn.getOutputStream());
                    pw.print(paramStr);
                    pw.flush();
                    pw.close();
                }
                int responseCode = conn.getResponseCode();
                if ( responseCode == HttpURLConnection.HTTP_OK )
                {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    InputStream is = conn.getInputStream();
                    int nRead;
                    byte data[] = new byte[10240];
                    while( (nRead = is.read(data)) != -1 )
                    {
                        baos.write(data, 0, nRead);
                    };
                    resultData = baos.toByteArray();
                    baos.close();
                    is.close();

                }
                else
                {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    InputStream is = conn.getErrorStream();
                    int nRead;
                    byte data[] = new byte[10240];
                    while( (nRead = is.read(data)) != -1 )
                    {
                        baos.write(data, 0, nRead);
                    };
                    resultData = baos.toByteArray();
                    baos.close();
                    is.close();
                }
            }
        }
        catch (Exception e)
        {
            Log.e("HttpError", requestUrl);
//            e.printStackTrace();
        }
        finally
        {
            if(conn != null)
            {
                try {
                    conn.disconnect();
                } catch(Exception e){}
            }
        }
        return resultData;
    }

}
