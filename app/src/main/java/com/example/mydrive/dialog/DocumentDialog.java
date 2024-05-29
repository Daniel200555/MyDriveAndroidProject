package com.example.mydrive.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mydrive.R;

import java.util.ArrayList;
import java.util.Arrays;

public class DocumentDialog {

    private Dialog dialog;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> itemList;

    @SuppressLint("ResourceType")
    public DocumentDialog(Context context, String text) {
        this.dialog = new Dialog(context);
        this.dialog.setContentView(R.layout.text_dialog);
        this.dialog.setTitle("Document");
        this.dialog.setCancelable(true);

        this.listView = this.dialog.findViewById(R.id.listViewText);
        this.itemList = new ArrayList<>();
        String[] items = text.split("\n");
        this.itemList.clear();
        this.itemList.addAll(Arrays.asList(items));

        // Initialize the adapter
        this.adapter = new ArrayAdapter<>(context, R.layout.show_text, R.id.textViewText, itemList);

        // Set the adapter to the ListView
        this.listView.setAdapter(adapter);

        // Show the dialog
        this.dialog.show();
    }
}
