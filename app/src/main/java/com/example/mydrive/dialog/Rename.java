package com.example.mydrive.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mydrive.R;
import com.example.mydrive.format.Format;
import com.example.mydrive.service.FileService;
import com.example.mydrive.service.RegisterAndLogin;

public class Rename {


    private Context context;
    private Dialog dialog;
    private EditText editTextRename;
    private Button buttonRename;
    private String type;

    public Rename(Context context, String oldName) {
        System.out.println(oldName);
        this.context = context;
        this.dialog = new Dialog(context);
        this.dialog.setContentView(R.layout.rename_dia);
        this.dialog.setTitle("Rename File");
        this.dialog.setCancelable(true);
        this.editTextRename = (EditText) this.dialog.findViewById(R.id.edittextRenameFile);
        this.buttonRename = (Button) this.dialog.findViewById(R.id.buttonRename);
        this.buttonRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = new Format().getType(oldName,'.');
                if (!editTextRename.getText().toString().equals("") || !editTextRename.getText().toString().equals(null) || type.equals(new Format().getType(editTextRename.getText().toString(),'.'))) {
                    FileService.renameFile(context, new RegisterAndLogin().getEmail(), editTextRename.getText().toString(), oldName);
                } else {
                    new InfoDialog(context,"Please select file");
                }
                dialog.dismiss();
            }
        });
        this.dialog.show();
    }


}
