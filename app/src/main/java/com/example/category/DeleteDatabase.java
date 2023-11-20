package com.example.category;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class DeleteDatabase extends AsyncTask<String, Void, String> {
    private Context context;
    private databases.DataCallback dataCallback;

    public DeleteDatabase(Context context, databases.DataCallback dataCallback) {
        this.context = context;
        this.dataCallback = dataCallback;
    }

    @Override
    protected String doInBackground(String... strings) {
        String itemNameToDelete = strings[0];
        String deleteUrl = "http://192.168.1.5/delete.php"; // Replace with your delete URL

        // Implement deletion logic here

        return "Delete operation completed"; // Modify as needed
    }

    @Override
    protected void onPostExecute(String result) {
        if (dataCallback != null) {
            dataCallback.onDataReceived(result);
        } else {
            Toast.makeText(context, "DataCallback is null", Toast.LENGTH_SHORT).show();
        }
    }
}

