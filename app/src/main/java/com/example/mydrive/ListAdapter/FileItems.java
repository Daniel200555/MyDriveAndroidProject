package com.example.mydrive.ListAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.example.mydrive.Home;
import com.example.mydrive.ListOfFiles;
import com.example.mydrive.R;
import com.example.mydrive.dto.FileDTO;
import com.example.mydrive.service.FileGet;
import com.example.mydrive.service.FileService;
import com.example.mydrive.service.RegisterAndLogin;

import java.util.List;

public class FileItems extends ArrayAdapter<FileDTO> {

    private TextView textViewNameOFFile;
    private AppCompatButton buttonMore;
    private Drawable yellow_drawable;
    private Context context;

    public FileItems(@NonNull Context context, List<FileDTO> items) {
        super(context, 0, items);
        this.context = context;
        yellow_drawable = context.getResources().getDrawable(R.drawable.yellow_color);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.file_item_for_list, parent, false);
        FileDTO file = getItem(position);
        if (file.isStar())
            convertView.setBackground(yellow_drawable);
        Log.d("FILE SHOW ", file.getType());
        textViewNameOFFile = (TextView) convertView.findViewById(R.id.textViewNameOfFile);
        textViewNameOFFile.setText(file.getName());
        buttonMore = (AppCompatButton) convertView.findViewById(R.id.buttonMore);
        buttonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, file);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file.getType().equals("PICTURE")) {

                    FileService.showImage(getContext(), file.getDir(), file.getFormat());
                    Log.d("FILE SHOW ", "show image");
                } else if (file.getFormat().equals("VIDEO")) {
                    FileService.showVideo(getContext(), file.getDir());
                    Log.d("FILE SHOW ", "show video");
                } else if (file.getFormat() == "NULL") {

                }
            }
        });

        return convertView;
    }

    private void showPopupMenu(View view, FileDTO fileDTO) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        MenuItem delete = popupMenu.getMenu().findItem(R.id.action_delete);
        MenuItem star = popupMenu.getMenu().findItem(R.id.action_star);
        MenuItem download = popupMenu.getMenu().findItem(R.id.action_download);
        delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {

                new FileService().deleteFile(new RegisterAndLogin().getEmail(), fileDTO);
                Toast.makeText(getContext(), "Delete clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        star.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                if (!fileDTO.isStar())
                    new FileService().starFile(new RegisterAndLogin().getEmail(), fileDTO.getDir());
                else
                    new FileService().unStarFile(new RegisterAndLogin().getEmail(), fileDTO.getDir());
                Toast.makeText(getContext(), "Star clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        download.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                new FileService().downloadFile(fileDTO.getDir(), fileDTO.getName());
                Toast.makeText(getContext(), "Download clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
    }

    @NonNull
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
