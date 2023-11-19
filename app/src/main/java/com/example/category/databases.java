package com.example.category;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
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

public class databases extends AsyncTask<String, Void, String> {
    Context context;

    public databases(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String name = strings[0];
        String kategori_url = "http://192.168.1.5/kategori.php"; // Ganti URL sesuai dengan kebutuhan

        // Dapatkan kategori dari Spinner yang sudah dipilih sebelumnya
        String type = strings[1];

        try {
            URL url = new URL(kategori_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            // Kirim data ke server
            String postData = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
            postData += "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
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
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            String[] strings = new String[1];
            int typeValue = Integer.parseInt(strings[1]);

            // Assuming you have a TextView with ID "categoryDataIncome" and "categoryDataOutcome" in your layout
            if (typeValue == 1) {
                TextView categoryDataIncomeTextView = ((Activity) context).findViewById(R.id.categoryDataIncome);
                categoryDataIncomeTextView.setText(result);
            } else {
                TextView categoryDataOutcomeTextView = ((Activity) context).findViewById(R.id.categoryDataOutcome);
                categoryDataOutcomeTextView.setText(result);
            }
        } else {
            Toast.makeText(context, "Failed to retrieve data from the server", Toast.LENGTH_SHORT).show(); //Bahwa database tersebut NULL atau kosong saat dimasukan.
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
