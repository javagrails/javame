package com.bk.restdroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.StrictMode;
import com.bk.utils.PropertiesReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class MainAct extends Activity {

    public static final String BASE_URL = "http://192.168.1.10:8080/surecash/v1/api";
    public static final String GET_TYPE = BASE_URL+"/get/type";
    private final String CHECK_BALANCE_URL = BASE_URL+"/balance";
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        addListenerOnButton();
    }
    public void addListenerOnButton() {

        Button buttonToPress = (Button) findViewById(R.id.getButton);
        //buttonToPress.setClickable(false);

        buttonToPress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //propertyFileReadExample();
                //testProp();
                String response = "";
                RestService rs = new RestService(CHECK_BALANCE_URL);
                rs.addParam("userWalletNo", "015343059786");
                rs.addParam("pin", "1234");
                rs.addHeader("Accept", "Application/json");
                try {
                    rs.execute(RestService.RequestMethod.POST);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("error = "+e.getMessage());
                }

                response = rs.getResponse();
                EditText et = (EditText) findViewById(R.id.showText);
                et.setText(response);

            }

        });

    }
    // this is ok

    /*public void addListenerOnButton() {

        Button buttonToPress = (Button) findViewById(R.id.getButton);
        //buttonToPress.setClickable(false);

        buttonToPress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String response = "";
                RestService rs = new RestService(GET_TYPE);
                rs.addParam("mobileNo", "016723592867");
                rs.addHeader("Accept", "Application/json");
                try {
                    rs.execute(RestService.RequestMethod.GET);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("error = "+e.getMessage());
                }

                response = rs.getResponse();
                EditText et = (EditText) findViewById(R.id.showText);
                et.setText(response);

            }

        });

    }*/

    /*public void addListenerOnButton() {

        Button buttonToPress = (Button) findViewById(R.id.getButton);
        //buttonToPress.setClickable(false);

        buttonToPress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String messageData = "";
                String calledUrl = "http://192.168.1.10:8080/surecash/v1/api/get/type/016723592867";
                RequestTask task = (RequestTask) new RequestTask().execute(calledUrl);
                try {
                    messageData = task.get();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                EditText et = (EditText) findViewById(R.id.showText);
                et.setText(messageData);

            }

        });

    }*/

    /* have prolem
    @Override
    public void onClick(View arg0) {
        String messageData = "";
        Button b = (Button) findViewById(R.id.getButton);
        b.setClickable(false);
        String calledUrl = "http://192.168.1.10:8080/surecash/v1/api/get/type/016723592867";
        RequestTask task = (RequestTask) new RequestTask().execute(calledUrl);
        try {
            messageData = task.get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        EditText et = (EditText) findViewById(R.id.showText);
        et.setText(messageData);

    }*/
    /*public void testProp(){
        PropertiesReader  pr = new PropertiesReader();
        Properties prop = new Properties();
        try{
            prop = pr.loadProperties();
        } catch (IOException io){
            System.out.println("IOException = "+io.getMessage());
        }
        System.out.println(prop.getProperty("target"));
        System.out.println(prop.getProperty("do.n"));
        System.out.println(prop.getProperty("do.o"));
        System.out.println("Last");
    }*/
    private Properties loadProperties() throws IOException {
        String[] fileList = {"config.properties"};
        Properties prop = new Properties();
        for (int i = fileList.length - 1; i >= 0; i--) {
            String file = fileList[i];
            try {
                InputStream fileStream = getAssets().open(file);
                prop.load(fileStream);
                fileStream.close();
            } catch (FileNotFoundException e) {
                System.out.println("Ignoring missing property file = "+file);
                System.out.println("FileNotFoundException = "+e.getMessage());
            }
        }
        return prop;
        //http://stackoverflow.com/questions/10222010/how-to-modify-my-custom-properties-file-in-android
        /*call like below*/
        /*
        Properties prop = new Properties();
        try {
            prop = loadProperties();
        } catch (IOException e) {
            System.out.println("error = "+e.getMessage());
        }
        System.out.println(prop.getProperty("target"));
        System.out.println(prop.getProperty("do.n"));
        System.out.println(prop.getProperty("do.o"));
        System.out.println("Last");
        */
        /*call like below*/
    }
    public void propertyFileReadExample(){
        String target = PropertiesReader.getProperty("target","android-15");
        String don = PropertiesReader.getProperty("do.n","23");
        String doo = PropertiesReader.getProperty("do.o","23");
        String none = PropertiesReader.getProperty("none","none");

        System.out.println("target = "+target);
        System.out.println("do.n = "+don);
        System.out.println("do.o = "+doo);
        System.out.println("none = "+none);
        System.out.println("Last");
    }
}
