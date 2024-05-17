package com.example.mydrive.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.mydrive.FragmentListOfFiles;
import com.example.mydrive.R;
import com.example.mydrive.VideoFragment;

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
//        context.getSupportFragmentManager().beginTransaction()
//                .add(R.id.videoViewFragment, new VideoFragment(context, file))
//                .commit();
        dialog.show();
    }

}
