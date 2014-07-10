package com.inspiredo.inspiredo;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by erik on 7/10/14.
 */
public abstract class JSONTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        String line = "";
        String response = "";
        String url = params[0];

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);

        try {

            HttpResponse httpResponse = client.execute(get);

            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) { // Ok
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent()));

                while ((line = rd.readLine()) != null) {
                    response += line;
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        JSONObject json = null;
        try {
            json = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        handleJSON(json);

    }

    public abstract void handleJSON(JSONObject json);


}
