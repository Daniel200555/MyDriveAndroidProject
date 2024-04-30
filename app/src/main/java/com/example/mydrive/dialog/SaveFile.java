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

import com.example.mydrive.R;
import com.example.mydrive.service.FileManager;
import com.example.mydrive.service.RegisterAndLogin;

public class SaveFile implements View.OnClickListener {

    private Button buttonSelectFile;
    private Button buttonSaveFile;
    private static EditText editTextFileName;
    private Dialog dialog;
    private static Context context;
    private static Uri selectedFileUri;
    private static final int PICK_FILE_REQUEST_CODE = 100;
    private String path;

    public SaveFile(Context context, String path) {
        this.path = path;
        this.context = context;
        this.dialog = new Dialog(context);
        this.dialog.setContentView(R.layout.add_file_layout);
        this.dialog.setTitle("Login");
        this.dialog.setCancelable(true);
        this.buttonSelectFile = (Button) dialog.findViewById(R.id.buttonSelectFile);
        this.buttonSelectFile.setOnClickListener(this);
        this.buttonSaveFile = (Button) dialog.findViewById(R.id.buttonSaveFile);
        this.buttonSaveFile.setOnClickListener(this);
        this.editTextFileName = (EditText) dialog.findViewById(R.id.editTextFileName);
        this.dialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == this.buttonSelectFile) {
            openFilePicker();
        } if (v == this.buttonSaveFile) {
            Log.d("SAVE FILE", new FileManager().getFileNameFromUri(this.context, this.selectedFileUri));
            if (getSelectedFileUri() != null) {
                new FileManager().saveFile(new RegisterAndLogin().getEmail(), path,context, getSelectedFileUri());
            }
            dialog.dismiss();
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        Intent chooserIntent = Intent.createChooser(intent, "Choose File");
        ((Activity) context).startActivityForResult(chooserIntent, PICK_FILE_REQUEST_CODE);
    }


    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                setSelectedFileUri(data.getData());editTextFileName.setText(new FileManager().getFileNameFromUri(context, getSelectedFileUri()));
            }
        }
    }

    public static Uri getSelectedFileUri() {
        return selectedFileUri;
    }

    public static void setSelectedFileUri(Uri uri) {
        selectedFileUri = uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
