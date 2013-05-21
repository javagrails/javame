package com.bk.restdroid;

import android.os.AsyncTask;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: salman
 * Date: 5/9/13
 * Time: 4:11 PM
 * To change this template use File | Settings | File Templates.
 */
class RequestTask extends AsyncTask<String, String, String> {

    protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
        InputStream in = entity.getContent();
        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n > 0) {
            byte[] b = new byte[4096];
            n = in.read(b);
            if (n > 0) out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        //HttpGet httpGet = new HttpGet("http://www.cheesejedi.com/rest_services/get_big_cheese.php?puzzle=1");
        HttpGet httpGet = new HttpGet(uri[0]);
        //HttpGet httpGet = new HttpGet("http://118.179.212.141:8080/surecash-ussd-api/api/ussd-app/router?transactionID=00000138&dialogid=34023&number=8801940387392&text=*376&status=begin");
        httpGet.addHeader("Accept", "Application/json");
        String text = null;
        try {
            HttpResponse response = httpClient.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();
            text = getASCIIContentFromEntity(entity);
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }
        return text;
    }

    protected void onPostExecute(String results) {
        super.onPostExecute(results);
        /*if (results!=null) {
            EditText et = (EditText)findViewById(R.id.my_edit);
            et.setText(results);
        }
        Button b = (Button)findViewById(R.id.my_button);
        b.setClickable(true);*/
    }
}
