package com.example.category;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class databases extends AsyncTask<String, Void, String> {
    private Context context;
    private DataCallback dataCallback;

    public databases(Context context, DataCallback dataCallback) {
        this.context = context;
        this.dataCallback = dataCallback;
    }

    public interface DataCallback {
        void onDataReceived(String result);
    }

    @Override
    protected String doInBackground(String... strings) {
        String name = strings[0];
        String type = strings[1];
        String kategori_url = "http://192.168.1.5/kategori.php"; // Ganti URL sesuai dengan kebutuhan

        try {
            URL url = new URL(kategori_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            // Kirim data ke server
            String postData = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
            postData += "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
            bufferedWriter.write(postData);

            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Berhasil mengirim data ke server
                return "Data berhasil dikirim ke server";
            } else {
                return "Gagal mengirim data ke server. Response Code: " + responseCode;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Exception occurred";
    }

    @Override
    protected void onPostExecute(String result) {
        // Pastikan dataCallback tidak null sebelum memanggil metodenya
        if (dataCallback != null) {
            // Panggil callback untuk memberitahu MainActivity bahwa data sudah terkirim
            dataCallback.onDataReceived(result);

            // Jika berhasil mengirim data, panggil AsyncTask baru untuk mengambil data dari server
            if (result != null && result.equals("Data berhasil dikirim ke server")) {
                new GetDataFromServer(context, dataCallback).execute();
            }
        } else {
            // Handle kasus di mana dataCallback null
            Toast.makeText(context, "DataCallback is null", Toast.LENGTH_SHORT).show();
        }
    }
}
