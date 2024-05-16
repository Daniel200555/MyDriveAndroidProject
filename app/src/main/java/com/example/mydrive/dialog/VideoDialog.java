package com.example.mydrive.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.mydrive.R;

import java.io.File;

public class VideoDialog {

    private Dialog dialog;
    private Context context;
    private VideoView videoView;
    private MediaController mediaController;

    public VideoDialog(Context context, Uri file) {
        Log.d("SHOW VIDEO", "alo");
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.video_dialog);
        dialog.setTitle("Video Dialog");
        dialog.setCancelable(true);
        videoView = dialog.findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(context);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(file);
        videoView.start();
        dialog.show();
    }

}
