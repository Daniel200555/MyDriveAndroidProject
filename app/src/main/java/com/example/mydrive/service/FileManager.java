package com.example.mydrive.service;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.mydrive.R;
import com.example.mydrive.dialog.ImageDialog;
import com.example.mydrive.dialog.VideoDialog;
import com.example.mydrive.dto.FileDTO;
import com.example.mydrive.dto.UserListDTO;
import com.example.mydrive.format.Format;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static StorageReference storageReference;
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference databaseRef;



    static {
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void saveFile(String email, String path, Context context, Uri uri) {
        File file = new File(uri.toString());
        String fileName = file.getName();
        new FileGet().addFile(email, saveInDatabase(email, getFileNameFromUri(context, uri), getSizeOfFile(context, uri)));
        StorageReference fileRef = storageReference.child( email + "/" + getFileNameFromUri(context, uri));
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                fileRef.putStream(inputStream)
                        .addOnSuccessListener(taskSnapshot -> {
                            Log.d("SAVE", "File saved success!!!");
                        })
                        .addOnFailureListener(e -> {
                            Log.e("SAVE", "Could not save file");
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveDir(String email, String path, Context context, String name) {
        String fileName = name;
        new FileGet().addFile(email, saveInDatabase(email, name, 0));
        StorageReference fileRef = storageReference.child(path);
//        try {
////            InputStream inputStream = context.getContentResolver().openInputStream(uri);
//            if (inputStream != null) {
//                fileRef.putStream(inputStream)
//                        .addOnSuccessListener(taskSnapshot -> {
//                            Log.d("SAVE", "File saved success!!!");
//                        })
//                        .addOnFailureListener(e -> {
//                            Log.e("SAVE", "Could not save file");
//                        });
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    public FileDTO saveInDatabase(String email, String fileName, int fileSize) {
        List<UserListDTO> sharedToUsers = new ArrayList<>();
        UserListDTO userListDTO = new UserListDTO("empty");
        sharedToUsers.add(userListDTO);
        return  new FileDTO(email, fileName, new Format().formatFile(new Format().getType(fileName, '.')), new Format().getType(fileName, '.'), fileSize, email + "/" + fileName, new Format().isFile(fileName), false, null, sharedToUsers);
    }

    public void deleteFile(String filePath) {
        StorageReference fileRef = storageReference.child(filePath);
        fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("FILE DELETE", "File delete successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FILE DELETE", "Error delete file");
            }
        });
    }

    public void downloadFile(String filePath, String fileName) {
        StorageReference fileRef = storageReference.child(filePath);
        File localFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        fileRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d("FILE DOWNLOAD", "File download successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FILE DOWNLOAD", "Error download file");
            }
        });
    }

    public void renameFile(Context context, String originalPath, String newPath, String fileFormat) {
        StorageReference originalStorageReference = storageReference.child(originalPath);
        StorageReference newStorageReference = storageReference.child(newPath);
        File tempFile;
        try {
            tempFile = File.createTempFile("temp_file", fileFormat, context.getCacheDir());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        originalStorageReference.getFile(tempFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("RENAME", "File downloaded successfully");
                        StorageReference newFileRef = storageReference.child(newPath);
                        newFileRef.putFile(Uri.fromFile(tempFile))
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Log.d("RENAME", "New file uploaded successfully");
                                        originalStorageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("RENAME", "Original file deleted successfully");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("RENAME", "Error deleting original file", e);
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("RENAME", "Error uploading new file", e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                        Log.e("TAG", "Error downloading file", e);
                    }
                });
    }

    public int getSizeOfFile(Context context, Uri uri) {
        int fileSize = 0;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                fileSize = inputStream.available();
                Log.d("FILE SIZE", "File size 1 " + fileSize);
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileSize;
    }

    public String getFileNameFromUri(Context context, Uri uri) {
        String fileName = null;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        fileName = cursor.getString(displayNameIndex);
                        Log.d("FILE SIZE", "File name 1 " + fileName);
                    }
                }
            } finally {
                cursor.close();
            }
        }
        return fileName;
    }

    private boolean isFile(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        String type = contentResolver.getType(uri);
        if (type != null && type.startsWith("file/")) {
            String fileName = getFileNameFromUri(context, uri);
            return fileName != null;
        }
        return false;
    }

    public void showVideo(Context context, String path) {
        StorageReference show = FirebaseStorage.getInstance().getReference().child(path);
            show.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    new VideoDialog(context, uri);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
    }

    public void showImage(Context context, String path, String type) {
        StorageReference show = FirebaseStorage.getInstance().getReference().child(path);
        try {
            File fileTemp = File.createTempFile("images", type);
            show.getFile(fileTemp).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(fileTemp.getAbsolutePath());
                    new ImageDialog(context, bitmap);
                    Log.d("FILE SHOW ", "show file");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("FILE SHOW ", "error to show file");
                }
            });
        } catch (IOException io) {

        }
    }

}
