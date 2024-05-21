package com.example.mydrive.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.documentfile.provider.DocumentFile;

import com.example.mydrive.R;
import com.example.mydrive.service.FileManager;

import java.util.ArrayList;
import java.util.List;

public class SaveFolder implements View.OnClickListener {

    private Dialog dialog;
    private static Context context;
    private Button buttonSelectFolder;
    private Button buttonUploadFolder;
    private static EditText editTextNameOfFolder;
    private String path;
    private static final int REQUEST_CODE_OPEN_DIRECTORY = 101;
    private static List<String> filePaths;
    private static Uri selectedUri;

    public SaveFolder(Context context, String path) {
        this.path = path;
        this.context = context;
        this.dialog = new Dialog(context);
        this.dialog.setContentView(R.layout.add_folder);
        this.dialog.setTitle("Login");
        this.dialog.setCancelable(true);
        filePaths = new ArrayList<>();
        this.buttonSelectFolder = (Button) dialog.findViewById(R.id.buttonSelectF);
        this.buttonUploadFolder = (Button) dialog.findViewById(R.id.buttonSaveF);
        this.buttonSelectFolder.setOnClickListener(this);
        this.buttonUploadFolder.setOnClickListener(this);
        this.editTextNameOfFolder = (EditText) dialog.findViewById(R.id.editTextFName);
        this.dialog.show();
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//        intent.setType("*/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        Intent chooserIntent = Intent.createChooser(intent, "Choose Folder");
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_OPEN_DIRECTORY);
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_OPEN_DIRECTORY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri treeUri = data.getData();
                if (treeUri != null) {
                    context.getContentResolver().takePersistableUriPermission(treeUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                    listFiles(treeUri);
                    selectedUri = treeUri;
                    editTextNameOfFolder.setText(new FileManager().getFolderNameFromUri(context, selectedUri));
                }
            }
        }
    }

    public static void listFilesInFolder(Context context, Uri uri) {
        List<String> fileNames = new ArrayList<>();
        List<DocumentFile> folders = new ArrayList<>();
        DocumentFile directory = DocumentFile.fromTreeUri(context, uri);
        if (directory != null && directory.isDirectory()) {
            for (DocumentFile file : directory.listFiles()) {
                if (file.isDirectory())
                    folders.add(file);
                fileNames.add(file.getName());
            }
        }
        if (folders.size() != 0) {

        }
    }

    @Override
    public void onClick(View v) {
        if (v == this.buttonSelectFolder) {
            openFilePicker();
        } if (v == this.buttonUploadFolder) {
//            List<String> fileNames = listFilesInFolder(context, selectedUri);
            System.out.println(new FileManager().getFolderNameFromUri(context, selectedUri));
//            for (String name : fileNames) {
//                System.out.println(name);
//            }
            this.dialog.dismiss();
        }
    }
}
