package com.example.mydrive.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

import com.example.mydrive.FragmentListOfFiles;
import com.example.mydrive.R;
import com.example.mydrive.format.Format;
import com.example.mydrive.service.FileGet;
import com.example.mydrive.service.FileManager;
import com.example.mydrive.service.RegisterAndLogin;

public class SaveFile implements View.OnClickListener {

    private Button buttonSelectFile;
    private Button buttonSaveFile;
    private static EditText editTextFileName;
    private static String type;
    private Dialog dialog;
    private static Context context;
    private static Uri selectedFileUri;
    private static final int PICK_FILE_REQUEST_CODE = 100;
    private String path;
    private final Object lock = new Object();

    public SaveFile(Context context, String path) {
        System.out.println("File Dialog");
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
            if (!editTextFileName.getText().toString().equals("") || !editTextFileName.getText().toString().equals(null) || getType().equals(new Format().getType(editTextFileName.getText().toString(),'.'))) {
                Log.d("SAVE FILE", new FileManager().getFileNameFromUri(this.context, this.selectedFileUri));
                if (getSelectedFileUri() != null) {
                    new FileGet().addFile(new RegisterAndLogin().getEmail(), new FileManager().saveInDatabase(new RegisterAndLogin().getEmail(), editTextFileName.getText().toString(), new FileManager().getSizeOfFile(context, getSelectedFileUri())));
                    new FileManager().saveFile(new RegisterAndLogin().getEmail(), editTextFileName.getText().toString(), path, context, getSelectedFileUri());
                    try {
                        Bundle args = new Bundle();
                        args.putString("option", "all");
                        Thread.sleep(1000);
                        FragmentListOfFiles fragment = new FragmentListOfFiles(new RegisterAndLogin().getEmail());
                        fragment.setArguments(args);
                        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentListOfFile, fragment)
                                .commit();

                        dialog.dismiss();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    new InfoDialog(context,"Please select file");
                }
                dialog.dismiss();
            } else {
                new InfoDialog(context,"File name is could not be empty or without wrong typr");
            }
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
                setSelectedFileUri(data.getData());
                editTextFileName.setText(new FileManager().getFileNameFromUri(context, getSelectedFileUri()));
                setType(new Format().getType(editTextFileName.getText().toString(), '.'));
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

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        SaveFile.type = type;
    }
}
