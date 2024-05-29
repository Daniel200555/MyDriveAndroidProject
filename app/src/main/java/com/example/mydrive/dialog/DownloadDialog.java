package com.example.mydrive.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mydrive.R;
import com.example.mydrive.service.FileService;

public class DownloadDialog implements View.OnClickListener {

    private Dialog dialog;
    private Context context;
    private TextView textViewInfo;
    private Button buttonInfo;
    private String path;
    private String name;

    public DownloadDialog(Context context, String text, String path, String name) {
        this.name = name;
        this.path = path;
        this.dialog = new Dialog(context);
        this.dialog.setContentView(R.layout.download_dialog);
        this.dialog.setTitle("Register");
        this.dialog.setCancelable(true);
        this.textViewInfo = (TextView) dialog.findViewById(R.id.textViewDownloadInfo);
        this.textViewInfo.setText(text);
        this.buttonInfo = (Button) dialog.findViewById(R.id.buttonDownloadInfo);
        this.buttonInfo.setOnClickListener(this);
        this.dialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == this.buttonInfo) {
            new FileService().downloadFile(getPath(), getName());
        }
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
}
