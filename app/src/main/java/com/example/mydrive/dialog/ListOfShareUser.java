package com.example.mydrive.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.example.mydrive.R;

public class ListOfShareUser {

    private Dialog dialog;
    private Context context;
    private AppCompatButton addUserShareButton;

    public ListOfShareUser(Context context, String path) {
        this.context = context;
        this.dialog = new Dialog(context);
        this.dialog.setContentView(R.layout.list_of_shared_users_dialog);
        this.dialog.setTitle("Share Users");
        this.dialog.setCancelable(true);
        this.addUserShareButton = this.dialog.findViewById(R.id.buttonAddShareUser);
        this.addUserShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareDialog(context, path);
            }
        });
        this.dialog.show();
    }

}
