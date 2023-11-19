package com.example.category;

import android.content.Context;
import android.os.AsyncTask;
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
    private databases.DataCallback dataCallback;
    private UpdateCallback updateCallback; // Tambahkan callback baru

    // Tambahkan konstruktor dengan callback baru
    public UpdateDatabase(Context context, databases.DataCallback dataCallback, UpdateCallback updateCallback) {
        this.context = context.getApplicationContext();
        this.dataCallback = dataCallback;
        this.updateCallback = updateCallback;
    }

    // Callback untuk memberi tahu pemanggil AsyncTask bahwa data telah diperbarui
    public interface UpdateCallback {
        void onUpdateComplete();
    }

    @Override
    protected String doInBackground(String... strings) {
        String oldData = strings[0];
        String newData = strings[1];
        String editedType = strings[2];

        String updateUrl = "http://192.168.1.5/update.php";

        try {
            URL url = new URL(updateUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String postData = URLEncoder.encode("old_data", "UTF-8") + "=" + URLEncoder.encode(oldData, "UTF-8");
            postData += "&" + URLEncoder.encode("new_data", "UTF-8") + "=" + URLEncoder.encode(newData, "UTF-8");
            postData += "&" + URLEncoder.encode("edited_type", "UTF-8") + "=" + URLEncoder.encode(editedType, "UTF-8");
            bufferedWriter.write(postData);

            bufferedWriter.flush();
            bufferedWriter.close();

            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpURLConnection.getInputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                StringBuilder result = new StringBuilder();

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    result.append(new String(buffer, 0, bytesRead));
                }

                inputStream.close();
                httpURLConnection.disconnect();

                return result.toString();
            } else {
                return "Failed to update data. Response Code: " + responseCode;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Malformed URL Exception: " + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null && result.equalsIgnoreCase("Update successful")) {
            Toast.makeText(context, "Data updated successfully", Toast.LENGTH_SHORT).show();
            // Panggil callback update jika berhasil
            updateCallback.onUpdateComplete();
        } else {
            Toast.makeText(context, "Failed to update data. Reason: " + result, Toast.LENGTH_SHORT).show();
        }
    }
}
