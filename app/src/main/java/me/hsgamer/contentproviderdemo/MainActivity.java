package me.hsgamer.contentproviderdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import me.hsgamer.contentproviderdemo.provider.StudentContract;

public class MainActivity extends AppCompatActivity {
    private TextView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText nameBox = findViewById(R.id.txtName);
        EditText yearBox = findViewById(R.id.txtYear);
        Button addButton = findViewById(R.id.addButton);
        EditText removeIdBox = findViewById(R.id.txtRemoveId);
        Button clearButton = findViewById(R.id.clearButton);
        listView = findViewById(R.id.listTextView);
        refreshList();

        addButton.setOnClickListener(v -> {
            ContentValues values = new ContentValues();
            values.put(StudentContract.COL_NAME, nameBox.getText().toString());
            values.put(StudentContract.COL_YEAR, yearBox.getText().toString());
            getContentResolver().insert(StudentContract.CONTENT_URI, values);
            nameBox.setText("");
            yearBox.setText("");
            Toast.makeText(getBaseContext(), "New Record Inserted", Toast.LENGTH_SHORT).show();
            refreshList();
        });

        clearButton.setOnClickListener(v -> {
            Uri uri = StudentContract.CONTENT_URI;
            String id = removeIdBox.getText().toString().trim();
            String message = "Cleared all students";
            if (!id.isEmpty()) {
                uri = ContentUris.withAppendedId(uri, Long.parseLong(id));
                message = "Cleared student with id " + id;
            }
            long update = getContentResolver().delete(uri, null, null);
            if (update > 0) {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                refreshList();
            }
        });
    }

    public void refreshList() {
        try (Cursor cursor = getContentResolver().query(StudentContract.CONTENT_URI, null, null, null, null)) {
            int idIndex = cursor.getColumnIndexOrThrow(StudentContract.ID);
            int nameIndex = cursor.getColumnIndexOrThrow(StudentContract.COL_NAME);
            int yearIndex = cursor.getColumnIndexOrThrow(StudentContract.COL_YEAR);
            if (cursor.moveToFirst()) {
                StringBuilder strBuild = new StringBuilder();
                while (!cursor.isAfterLast()) {
                    strBuild.append("\n")
                            .append(cursor.getString(idIndex))
                            .append(" - ")
                            .append(cursor.getString(nameIndex))
                            .append(" - ")
                            .append(cursor.getString(yearIndex));
                    cursor.moveToNext();
                }
                listView.setText(strBuild);
            } else {
                listView.setText("No Records Found");
            }
        }
    }
}