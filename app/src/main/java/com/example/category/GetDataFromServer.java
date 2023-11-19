package com.example.category;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetDataFromServer extends AsyncTask<Void, Void, String> {
    private Context context;
    private databases.DataCallback dataCallback;

    public GetDataFromServer(Context context, databases.DataCallback dataCallback) {
        this.context = context;
        this.dataCallback = (databases.DataCallback) context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String get_data_url = "http://192.168.1.5/get_data.php";
        try {
            URL url = new URL(get_data_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            StringBuilder result = new StringBuilder();

            // Baca respon dari server
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                result.append(new String(buffer, 0, bytesRead));
            }

            inputStream.close();
            httpURLConnection.disconnect();

            return result.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            // Panggil callback untuk memberitahu MainActivity bahwa data sudah diterima
            dataCallback.onDataReceived(result);
        } else {
            Toast.makeText(context, "Failed to retrieve data from server", Toast.LENGTH_SHORT).show();
        }
    }
}

