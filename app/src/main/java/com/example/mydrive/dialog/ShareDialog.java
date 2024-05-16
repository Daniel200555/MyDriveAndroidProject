package com.example.mydrive.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mydrive.R;

public class ShareDialog {

    private Dialog dialog;
    private Context context;
    private EditText shareEmail;
    private Button shareButton;


    public ShareDialog(Context context, String path) {
        Log.d("SHARE", "Open share dialog");
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.share_dialog);
        dialog.setTitle("Share File");
        dialog.setCancelable(true);
        shareEmail = (EditText) dialog.findViewById(R.id.shareEmailEditText);
        shareButton = (Button) dialog.findViewById(R.id.buttonShare);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.show();
    }

}
