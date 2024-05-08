package com.example.mydrive.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.mydrive.R;

public class VideoDialog {

    private Dialog dialog;
    private Context context;
    private VideoView videoView;
    private MediaController mediaController;

    public VideoDialog(Context context, Uri uri) {
        this.context = context;
        this.dialog = new Dialog(context);
        this.dialog.setContentView(R.layout.image_dialog);
        this.dialog.setTitle("Login");
        this.dialog.setCancelable(true);
        this.videoView = (VideoView) dialog.findViewById(R.id.videoView);
        this.videoView.setVideoURI(uri);
        this.mediaController = new MediaController(this.context);
        this.mediaController.setAnchorView(videoView);
        this.videoView.setMediaController(this.mediaController);
        this.videoView.start();
        this.dialog.show();
    }

}