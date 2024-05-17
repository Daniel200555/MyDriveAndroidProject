package com.example.mydrive;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoFragment extends Fragment {

    private Context context;
    private Uri file;

    public VideoFragment(Context context, Uri file) {
        this.context = context;
        this.file = file;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        VideoView videoView = (VideoView) view.findViewById(R.id.videoViewFragment);
        MediaController mediaController = new MediaController(this.context);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(file);
        videoView.start();
        return view;
    }
}