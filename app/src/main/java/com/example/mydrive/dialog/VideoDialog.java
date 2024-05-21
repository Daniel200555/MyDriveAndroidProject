package com.example.mydrive.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.mydrive.R;

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
        VideoView videoView = (VideoView) dialog.findViewById(R.id.videoView);
        MediaController mediaController = new MediaController((Activity) context);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(file);
        videoView.start();
        dialog.show();
    }

}
