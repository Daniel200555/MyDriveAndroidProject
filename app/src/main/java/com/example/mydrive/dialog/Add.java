package com.example.mydrive.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mydrive.R;

public class Add implements View.OnClickListener {

    private Dialog dialog;
    private Context context;
    private Button buttonAddFile;
    private Button buttonAddDir;
    private String path;

    public Add(Context context, String path) {
        System.out.println("Folder Dialog");
        this.path = path;
        this.context = context;
        this.dialog = new Dialog(context);
        this.dialog.setContentView(R.layout.add_layout);
        this.dialog.setTitle("Login");
        this.dialog.setCancelable(true);
        this.buttonAddFile = (Button) dialog.findViewById(R.id.buttonAddFileDialog);
        this.buttonAddFile.setOnClickListener(this);
//        this.buttonAddDir = (Button) dialog.findViewById(R.id.buttonAddDirDialog);
//        this.buttonAddDir.setOnClickListener(this);
//        this.buttonAddDir.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SaveFolder saveFolder = new SaveFolder(this.context, getPath());
//                this.dialog.dismiss();
//            }
//        });
        this.dialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == this.buttonAddFile) {
            SaveFile saveFile = new SaveFile(this.context, getPath());
            this.dialog.dismiss();
//            SaveFolder saveFolder = new SaveFolder(this.context, getPath());
//            this.dialog.dismiss();
        }
//        if (v == this.buttonAddDir) {
//            SaveFolder saveFolder = new SaveFolder(this.context, getPath());
//            this.dialog.dismiss();
//        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
