package com.csform.android.managerapp.util;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jun on 15. 5. 2..
 */
public class HttpTask extends AsyncTask<String, Void, String>{

    int m_protocol;
    OnHttpReceive mHttpReceive;
    private int mServerError;
    public static final int MAXNETWORKEXCEPTIONCOUNT = 5;

    String mContentType;

    public HttpTask(int protocol, OnHttpReceive httpReceive)
    {
        m_protocol = protocol;
        mHttpReceive = httpReceive;
    }
    protected String doInBackground(String... params)
    {
        InputStream is = getInputStreamFromUrl(params[0]);

        String result = "Error";
        if(mServerError >= MAXNETWORKEXCEPTIONCOUNT)
            return result;

        String type = mContentType;
        if(type != null && type.indexOf("image") != -1)
            ;
        else
            result = convertStreamToString(is);

        return result;
    }

    protected void onPostExecute(String result)
    {

        if(mHttpReceive == null) {
            Log.v("HttpTask/onPostExecute","mHttpReceive is not null");
            return;
        }
        mHttpReceive.onReceive(m_protocol, result);

    }
    private static String convertStreamToString(InputStream is)
    {
        BufferedReader reader;
        StringBuilder sb = null;

        String line = null;
        try{
            reader = new BufferedReader(new InputStreamReader(is));
            sb = new StringBuilder();
            while ((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

        finally{

            if ( null != is){
                try{
                    is.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();

    }

    public InputStream getInputStreamFromUrl(String url) {

        InputStream content = null;
        int status=0;
        mServerError++;
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(mServerError > MAXNETWORKEXCEPTIONCOUNT)
            return content;
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));

            Header hdr = response.getEntity().getContentType();
            mContentType = hdr.getValue();

            content = response.getEntity().getContent();
            status = response.getStatusLine().getStatusCode();
            Log.v("HtttpTask/InPutStream",content.toString()+" "+status);
        } catch (Exception e) {
            Log.e("[GET REQUEST]", "Network exception", e);
            content = getInputStreamFromUrl(url);
        }
        return content;
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


    /*

    OnHttpReceive mHttpReceive;
    public HttpTask(OnHttpReceive httpReceive)
    {
        mHttpReceive = httpReceive;
    }
    protected byte[] doInBackground(String... params) {

        try {
            byte data[] = remoteSyncHttp(params[0], null);
            if(data == null)
            {
                Thread.currentThread().sleep(1000);
                data = remoteSyncHttp(params[0], null);
            }

            return data;
        }

        catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    protected void onPostExecute(byte data[]) {
        if(mHttpReceive == null)
            return;

        mHttpReceive.onReceive(data);
        Log.v("httpClient",data.toString());
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
*/
