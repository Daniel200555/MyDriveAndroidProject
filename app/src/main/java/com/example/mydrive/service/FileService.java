package com.example.mydrive.service;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mydrive.dialog.DownloadDialog;
import com.example.mydrive.dto.FileDTO;

public class FileService {

    public void deleteFile(Context context, String email, FileDTO file) {
        new FileGet().deleteFile(email, file.getName());
        new FileManager().deleteFile(context, email + "/" + file.getName());
    }

    public void downloadFile(String path, String name) {
        new FileManager().downloadFile(path, name);
    }

    public void starFile(Context context, String email, String fileDir) {
        new FileGet().starList(context, email, fileDir);
    }

    public void unStarFile(Context context, String email, String fileDir) {
        new FileGet().unStarList(context, email, fileDir);
    }

    public static void showImage(Context context, String path, String type) {
        new FileManager().showImage(context, path, type);
    }

    public static void showVideo(Context context, String path, String type) {
        new FileManager().showVideo(context, path, type);
    }

    public static void showAudio(Context context, String path) {
        new FileManager().showAudio(context, path);
    }

    public static void showDocument(Context context, String path) {
        new FileManager().showTxt(context, path);
    }

    public static void showDownloadInfo(Context context, String info, String path, String name) {
        new DownloadDialog(context, info, path, name);
    }

    public static void shareFile(Context context, String ownerEmail, String shareEmail, FileDTO fileDTO) {
        new FileGet().shareFileToEmail(shareEmail, fileDTO);
        new FileGet().shareFile(context, shareEmail, ownerEmail, fileDTO.getDir());
    }

    public static void deleteFromShare(Context context, String ownerEmail, String shareEmail, String dir) {
        new FileGet().deleteSharedFileFromOwner(shareEmail, ownerEmail, dir);
        new FileGet().deleteFromShareUserShareFile(context, shareEmail, dir);
    }

    public static void renameFile(Context context, String email, String newName, String oldName) {
        System.out.println(oldName);
        new FileGet().renameFile(email, email + "/" + newName, oldName, newName);
        new FileManager().renameFile(context, oldName, email + "/" + newName);
    }

}
