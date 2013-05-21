package com.bk.restdroid;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: salman
 * Date: 5/9/13
 * Time: 4:11 PM
 * To change this template use File | Settings | File Templates.
 */
class RestService {
    public enum RequestMethod {GET, POST}
    public static final String ENCODER = "UTF - 8";

    private ArrayList<NameValuePair> params;
    private ArrayList<NameValuePair> headers;

    private String url;

    private int responseCode;
    private String message;

    private String response;

    public String getResponse() {
        return response;
    }

    public String getErrorMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public RestService(String url) {
        this.url = url;
        params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();
    }

    public void addParam(String name, String value) {
        params.add(new BasicNameValuePair(name, value));
    }

    public void addHeader(String name, String value) {
        headers.add(new BasicNameValuePair(name, value));
    }

    public void execute(RequestMethod method) throws Exception {
        switch (method) {
            case GET: {
                //add parameters
                String combinedParams = "";
                if (!params.isEmpty()) {
                    combinedParams += "/";
                    for (NameValuePair p : params) {
                        //String paramString = URLEncoder.encode(p.getValue(), ENCODER);
                        String paramString = p.getValue();
                        if (combinedParams.length() > 1) {
                            combinedParams += "/" + paramString;
                        } else {
                            combinedParams += paramString;
                        }
                    }
                    /*combinedParams += "?";
                    for (NameValuePair p : params) {
                        String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), ENCODER);
                        if (combinedParams.length() > 1) {
                            combinedParams += "&" + paramString;
                        } else {
                            combinedParams += paramString;
                        }
                    }*/
                }
                HttpGet request = new HttpGet(url + combinedParams);

                //add headers
                for (NameValuePair h : headers) {
                    request.addHeader(h.getName(), h.getValue());
                }

                executeRequest(request, url);
                break;
            }
            case POST: {
                String combinedParams = "";
                HashMap jsonMap = new HashMap();
                if (!params.isEmpty()) {
                    for (NameValuePair p : params) {
                        jsonMap.put(p.getName(),p.getValue());
                    }
                }
                JSONObject jo = new JSONObject(jsonMap);
                combinedParams += jo.toString();
                HttpPost request = new HttpPost(url);
                //add headers
                for (NameValuePair h : headers) {
                    request.addHeader(h.getName(), h.getValue());
                }

                if (!params.isEmpty()) {
                    StringEntity input = new StringEntity(combinedParams);
                    input.setContentType("application/json");
                    request.setEntity(input);
                }

                executeRequest(request, url);
                break;
            }
        }
    }

    private void executeRequest(HttpUriRequest request, String url) {
        HttpClient client = new DefaultHttpClient();
        //HttpContext localContext = new BasicHttpContext();
        HttpResponse httpResponse;

        try {
            httpResponse = client.execute(request);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();

            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {

                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);

                // Closing the input stream will trigger connection release
                instream.close();
            }

        } catch (ClientProtocolException e) {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        } catch (IOException e) {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        }
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
