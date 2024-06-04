package com.example.mydrive.service;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;

import com.example.mydrive.FragmentListOfFiles;
import com.example.mydrive.R;
import com.example.mydrive.dialog.AudioDialog;
import com.example.mydrive.dialog.DocumentDialog;
import com.example.mydrive.dialog.ImageDialog;
import com.example.mydrive.dialog.VideoDialog;
import com.example.mydrive.dto.FileDTO;
import com.example.mydrive.dto.UserListDTO;
import com.example.mydrive.format.Format;
import com.example.mydrive.util.UserCallback;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.List;
//import com.bumptech.glide.Glide;

public class FileManager {

    private static StorageReference storageReference;
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference databaseRef;
    private final Object lock = new Object();



    static {
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }



    public void saveFile(String email, String name, String path, Context context, Uri uri) {
            Dialog dialog = new Dialog(context);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.image_dialog, null);
            ImageView loadingGif = dialogView.findViewById(R.id.imageView);
            loadingGif.setImageResource(R.drawable.gto);// Ensure you have a loading_gif.gif in res/drawable
            dialog.setContentView(dialogView);
            dialog.setCancelable(false);
            dialog.show();
            File file = new File(uri.toString());
            String fileName = name;
            StorageReference fileRef = storageReference.child(email + "/" + name);
//        StorageReference fileRef = storageReference.child( email + "/" + fileName);
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                if (inputStream != null) {
                    fileRef.putStream(inputStream)
                            .addOnSuccessListener(taskSnapshot -> {
                                dialog.dismiss();
                                Bundle args = new Bundle();
                                args.putString("option", "all");
                                FragmentListOfFiles fragment = new FragmentListOfFiles(new RegisterAndLogin().getEmail());
                                fragment.setArguments(args);
                                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragmentListOfFile, fragment)
                                        .commit();
                                Log.d("SAVE", "File saved success!!!");
                            })
                            .addOnFailureListener(e -> {
                                Log.e("SAVE", "Could not save file");
                            });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            new FileGet().addFile(email, saveInDatabase(email, getFileNameFromUri(context, uri), getSizeOfFile(context, uri)));
    }

    public FileDTO saveInDatabase(String email, String fileName, int fileSize) {
        List<UserListDTO> sharedToUsers = new ArrayList<>();
        UserListDTO userListDTO = new UserListDTO("empty", "empty");
        sharedToUsers.add(userListDTO);
        return  new FileDTO(email, fileName, new Format().formatFile(new Format().getType(fileName, '.')), new Format().getType(fileName, '.'), fileSize, email + "/" + fileName, new Format().isFile(fileName), false, null, sharedToUsers);
    }

    public void deleteFile(Context context, String filePath) {
        StorageReference fileRef = storageReference.child(filePath);
        fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("FILE DELETE", "File delete successfully");
                Bundle args = new Bundle();
                args.putString("option", "all");
                FragmentListOfFiles fragment = new FragmentListOfFiles(new RegisterAndLogin().getEmail());
                fragment.setArguments(args);
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentListOfFile, fragment)
                        .commit();
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

    public void renameFile(Context context, String originalPath, String newPath) {
        System.out.println("manager " + newPath);
        System.out.println("manager" + originalPath);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference oldFileRef = storageRef.child(originalPath);
        StorageReference newFileRef = storageRef.child(newPath);
        try {
            final File localFile = File.createTempFile("tempFile", ".tmp");
            oldFileRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    UploadTask uploadTask = newFileRef.putFile(Uri.fromFile(localFile));
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            oldFileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Rename", "File renamed successfully.");
                                    Bundle args = new Bundle();
                                    args.putString("option", "all");
                                    FragmentListOfFiles fragment = new FragmentListOfFiles(new RegisterAndLogin().getEmail());
                                    fragment.setArguments(args);
                                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fragmentListOfFile, fragment)
                                            .commit();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle failure in deleting the old file
                                    Log.e("Rename", "Failed to delete the old file.", exception);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.e("Rename", "Failed to upload the new file.", exception);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle failure in downloading the old file's data
                    if (exception instanceof com.google.firebase.storage.StorageException &&
                            ((com.google.firebase.storage.StorageException) exception).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                        Log.e("Rename", "File does not exist at the specified location: " + originalPath);
                    } else {
                        Log.e("Rename", "Failed to download the old file.", exception);
                    }
                }
            });
        } catch (IOException e) {
            // Handle failure in creating the temporary file
            Log.e("Rename", "Failed to create a temporary file.", e);
        }
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

    public String getFileNameFromUriFolder(Context context, Uri uri) {
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

    public static String getFileNameFromUri(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            DocumentFile documentFile = DocumentFile.fromSingleUri(context, uri);
            if (documentFile == null) {
                documentFile = DocumentFile.fromTreeUri(context, uri);
            }
            if (documentFile != null) {
                return documentFile.getName();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            String path = uri.getPath();
            int cut = path.lastIndexOf('/');
            if (cut != -1) {
                return path.substring(cut + 1);
            }
        }
        return null;
    }

    public String getFolderNameFromUri(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            DocumentFile documentFile = DocumentFile.fromTreeUri(context, uri);
            if (documentFile != null && documentFile.isDirectory()) {
                return documentFile.getName();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            String path = uri.getPath();
            int cut = path.lastIndexOf('/');
            if (cut != -1) {
                return path.substring(cut + 1);
            }
        }
        return null;
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

    public void showVideo(Context context, String path, String type) {
        Log.d("SHOW VIDEO", "alo method");
        StorageReference show = FirebaseStorage.getInstance().getReference().child(path);
//        File local = null;
//        try {
//            local = File.createTempFile("video", type);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        File finalFile = local;
        show.getDownloadUrl()
                .addOnSuccessListener( uri -> {
                        new VideoDialog(context, uri);
                        Log.d("FILE SHOW ", "show file");
                    }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("FILE SHOW ", "error to show file");
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

    public void showAudio(Context context, String path) {
        StorageReference show = FirebaseStorage.getInstance().getReference().child(path);
        show.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String temp = uri.toString();
                AudioDialog audio = new AudioDialog(context, temp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void showTxt(Context context, String path) {
        StorageReference show = FirebaseStorage.getInstance().getReference().child(path);
        show.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                String text = new String(bytes);
                new DocumentDialog(context, text);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}
