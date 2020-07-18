package com.example.databaseapiapp.api;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ClientAsyncTask extends AsyncTask<Void, Void, String> {
    public String request_type = "get";
    public boolean showDialog = false;
    public String api_url = "index.php";
    public String message = "Proses Data";

    private ArrayList<NameValuePair> mParams;
    private OnPostExecuteListener mPostExecuteListener = null;
    private ProgressDialog dialog;
    private Context mContext;
    private String base_api_url = "http://192.168.43.26/api_jadwal/";

    public static interface OnPostExecuteListener {
        void onPostExecute(String result);
    }

    public void setParams(ArrayList<NameValuePair> mParams) {
        this. mParams = mParams;
    }

    public ClientAsyncTask(Context context, OnPostExecuteListener
            postExecuteListener) throws Exception {
        this.mContext = context;
        this.mPostExecuteListener = postExecuteListener;
        if (mPostExecuteListener == null)
            throw new Exception( "Param cannot be null.");
    }

    @Override
    protected void onPreExecute() {
        if (showDialog)
            this.dialog = ProgressDialog.show(mContext, message,"Silakan Menunggu...",true);
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;
        try {
            if (request_type.equals("post"))
                result = post_request();
            else result = get_request();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (mPostExecuteListener != null){
            try {
                //JSONObject json = new JSONObject(result);
                if (showDialog)
                    this.dialog.dismiss();
                mPostExecuteListener.onPostExecute(result);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private String get_request() throws IOException {
        String url = base_api_url + api_url;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        int responCode = response.getStatusLine().getStatusCode();
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    private String post_request() throws IOException {
        String url = base_api_url + api_url;
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.setEntity( new UrlEncodedFormEntity( mParams));
        HttpResponse response = client.execute(post);
        int responCode = response.getStatusLine().getStatusCode();
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

}
