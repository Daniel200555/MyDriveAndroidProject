package com.example.mydrive.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mydrive.R;

public class ImageDialog {

    private Dialog dialog;
    private Context context;
    private ImageView imageView;

    public ImageDialog(Context context, Bitmap bitmap) {
        this.context = context;
        this.dialog = new Dialog(context);
        this.dialog.setContentView(R.layout.image_dialog);
        this.dialog.setTitle("Login");
        this.dialog.setCancelable(true);
        this.imageView = (ImageView) dialog.findViewById(R.id.imageView);
        this.imageView.setImageBitmap(bitmap);
        this.dialog.show();
    }

}
