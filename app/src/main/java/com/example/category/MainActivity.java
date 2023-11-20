package com.example.category;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    Spinner type;
    FloatingActionButton fab;
    ArrayAdapter<CharSequence> adapter;
    LinearLayout addIncome, addOutcome;
    databases dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        type = findViewById(R.id.Type);
        fab = findViewById(R.id.AddCategory);
        addIncome = findViewById(R.id.linearLayoutIncome);
        addOutcome = findViewById(R.id.linearLayoutOutcome);

        adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dbHelper = new databases(this, new databases.DataCallback() {
            @Override
            public void onDataReceived(String result) {
                Toast.makeText(MainActivity.this, "Data received: " + result, Toast.LENGTH_SHORT).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedItemPosition = type.getSelectedItemPosition();

                // Show the corresponding popup based on the selected item
                if (selectedItemPosition == 0) {
                    showPopupInput(R.layout.pop_menu_outcome);
                } else {
                    showPopupInput(R.layout.pop_menu_income);
                }
            }
        });

        type.setAdapter(adapter);

        Button editButton = findViewById(R.id.EditButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });

        // Add an OnItemSelectedListener to the Spinner
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Hide all layouts initially
                addIncome.setVisibility(View.GONE);
                addOutcome.setVisibility(View.GONE);

                // Update the layout based on the selected item
                if (position == 0) { // Assuming the first position is for Income
                    addIncome.setVisibility(View.VISIBLE);
                    addOutcome.setVisibility(View.GONE);
                } else {
                    addIncome.setVisibility(View.GONE);
                    addOutcome.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        Button deleteButton = findViewById(R.id.DeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });
    }

    private void updateLinearLayout(int type, String data) {
        LinearLayout targetLayout;

        // Determine the target LinearLayout based on the selected type
        if (type == 1) {
            targetLayout = findViewById(R.id.linearLayoutIncome);
        } else {
            targetLayout = findViewById(R.id.linearLayoutOutcome);
        }

        // Create a new TextView to display the data
        TextView textView = new TextView(this);
        textView.setText(data);

        // Add the TextView to the target LinearLayout
        targetLayout.addView(textView);
    }

    private void showPopupInput(int layoutResourceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the layout based on the selected item
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(layoutResourceId, null);

        builder.setView(view);

        final EditText inputField = view.findViewById(R.id.inputUser);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String input = inputField.getText().toString().trim();

                if (input.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Input cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                int typeValue = type.getSelectedItemPosition() + 1; // Assuming types are 1 and 2

                new databases(MainActivity.this, new databases.DataCallback() {
                    @Override
                    public void onDataReceived(String result) {
                        // Handle data received
                    }
                }).execute(input, String.valueOf(typeValue));
                updateLinearLayout(typeValue, input);

                // Close the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputField.getWindowToken(), 0);
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Data");

        // Inflate layout for the dialog
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_menu, null);
        builder.setView(view);

        final EditText oldDataField = view.findViewById(R.id.textlama);
        final EditText editField = view.findViewById(R.id.editText);
        final Spinner editType = view.findViewById(R.id.editType);

        // Set up the spinner with the same adapter as the main type spinner
        editType.setAdapter(adapter);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String oldData = oldDataField.getText().toString();
                String editedData = editField.getText().toString();
                int editedType = editType.getSelectedItemPosition() + 1; // Assuming types are 1 and 2

                if (editedData.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Input cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Call AsyncTask to update data
                new UpdateDatabase(MainActivity.this, new databases.DataCallback() {
                    @Override
                    public void onDataReceived(String result) {
                        // Handle data received
                    }
                }, new UpdateDatabase.UpdateCallback() {
                    @Override
                    public void onUpdateComplete() {
                        // Panggil metode untuk membaca ulang data dari server dan memperbarui tampilan UI
                        fetchDataAndUpdateUI();
                    }
                }).execute(oldData, editedData, String.valueOf(editedType));

                // Close the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editField.getWindowToken(), 0);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    // Metode untuk membaca ulang data dari server dan memperbarui tampilan UI
    private void fetchDataAndUpdateUI() {
        // Implementasikan logika untuk membaca ulang data dari server dan memperbarui tampilan UI
        new GetDataFromServer(MainActivity.this, new databases.DataCallback() {
            @Override
            public void onDataReceived(String result) {
                // Handle data received
            }
        }).execute();
        // Tambahkan logika lain yang diperlukan untuk pembaruan UI
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View deleteDialogView = LayoutInflater.from(this).inflate(R.layout.delete_menu, null);

        // Find views in deleteDialogView
        TextView deleteDialogTitle = deleteDialogView.findViewById(R.id.deleteDialogTitle);
        TextView deleteDialogMessage = deleteDialogView.findViewById(R.id.deleteDialogMessage);
        EditText deleteItemNameInput = deleteDialogView.findViewById(R.id.deleteItemNameInput); // New EditText
        Button deleteDialogButton = deleteDialogView.findViewById(R.id.deleteDialogButton);

        deleteDialogTitle.setText("Delete Item");
        deleteDialogMessage.setText("Are you sure you want to delete this item?");

        AlertDialog deleteDialog = builder.setView(deleteDialogView).create();

        deleteDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the item name from the EditText
                String itemNameToDelete = deleteItemNameInput.getText().toString().trim();

                if (itemNameToDelete.isEmpty()) {
                    // Notify the user that the input is empty
                    Toast.makeText(MainActivity.this, "Please enter an item name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Call AsyncTask to delete data
                new DeleteDatabase(MainActivity.this, new databases.DataCallback() {
                    @Override
                    public void onDataReceived(String result) {
                        // Handle data received
                    }
                }).execute(itemNameToDelete);

                // Close the dialog
                deleteDialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        deleteDialog.show();
    }
}
