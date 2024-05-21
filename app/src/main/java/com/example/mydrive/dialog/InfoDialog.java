package com.example.mydrive.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mydrive.R;

public class InfoDialog implements View.OnClickListener {

    private Dialog dialog;
    private Context context;
    private TextView textViewInfo;
    private Button buttonInfo;

    public InfoDialog(Context context, String info) {
        this.dialog = new Dialog(context);
        this.dialog.setContentView(R.layout.info_dialog);
        this.dialog.setTitle("Register");
        this.dialog.setCancelable(true);
        this.textViewInfo = (TextView) dialog.findViewById(R.id.textViewInfo);
        this.textViewInfo.setText(info);
        this.buttonInfo = (Button) dialog.findViewById(R.id.buttonInfo);
        this.buttonInfo.setOnClickListener(this);
        this.dialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == this.buttonInfo) {
            this.dialog.dismiss();
        }
    }
}
