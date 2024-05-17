package com.example.mydrive.service;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mydrive.dto.FileDTO;

public class FileService {

    public void deleteFile(String email, FileDTO file) {
        new FileGet().deleteFile(email, file.getName());
        new FileManager().deleteFile(email + "/" + file.getName());
    }

    public void downloadFile(String path, String name) {
        new FileManager().downloadFile(path, name);
    }

    public void starFile(String email, String fileDir) {
        new FileGet().starList(email, fileDir);
    }

    public void unStarFile(String email, String fileDir) {
        new FileGet().unStarList(email, fileDir);
    }

    public static void showImage(Context context, String path, String type) {
        new FileManager().showImage(context, path, type);
    }

    public static void showVideo(Context context, String path, String type) {
        new FileManager().showVideo(context, path, type);
    }

    public static void shareFile(String ownerEmail, String shareEmail, String path) {
        new FileGet().shareFileToEmail(shareEmail, path);
        new FileGet().shareFile(shareEmail, shareEmail, path);
    }

}
