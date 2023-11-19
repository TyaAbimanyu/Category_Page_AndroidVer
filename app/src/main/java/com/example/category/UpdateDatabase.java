package com.example.category;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class UpdateDatabase extends AsyncTask<String, Void, String> {
    private Context context;

    public UpdateDatabase(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String oldData = strings[0];
        String newData = strings[1];
        String editedType = strings[2];

        String update_url = "http://192.168.1.5/update.php"; // Ganti URL sesuai dengan kebutuhan

        try {
            URL url = new URL(update_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            // Kirim data ke server
            String postData = URLEncoder.encode("old_data", "UTF-8") + "=" + URLEncoder.encode(oldData, "UTF-8");
            postData += "&" + URLEncoder.encode("new_data", "UTF-8") + "=" + URLEncoder.encode(newData, "UTF-8");
            postData += "&" + URLEncoder.encode("edited_type", "UTF-8") + "=" + URLEncoder.encode(editedType, "UTF-8");
            bufferedWriter.write(postData);

            bufferedWriter.flush();
            bufferedWriter.close();

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
        Log.d("UpdateDatabaseTask", "onPostExecute result: " + result);

        if (result != null && result.equals("Update successful")) {
            Toast.makeText(context, "Data updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to update data", Toast.LENGTH_SHORT).show();
        }
    }

}

