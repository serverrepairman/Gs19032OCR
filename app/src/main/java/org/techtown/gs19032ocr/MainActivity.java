package org.techtown.gs19032ocr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;
    private ImageView imageView;
    private TextView textView;
    private Button button;
    int PICK_IMAGE_MULTIPLE = 1;
    ArrayList<Drawable> images = new ArrayList<Drawable>();
    ArrayList<Uri> imagesUri = new ArrayList<Uri>();
    int imageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageIndex++;
                if(imageIndex >= images.size())
                    imageIndex = 0;
                imageView.setImageDrawable(images.get(imageIndex));
            }
        });

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                if(data.getData()!=null){

                    Uri uri=data.getData();
                    imagesUri.add(uri);
                    images.add(Drawable.createFromStream(
                            getContentResolver().openInputStream(uri),
                            null));
                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ClipData.Item item;
                        Uri uri = null;
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            item = mClipData.getItemAt(i);
                            uri = item.getUri();
                            println(uri.toString());
                            imagesUri.add(uri);
                            images.add(Drawable.createFromStream(
                                    getContentResolver().openInputStream(uri),
                                    null));

                        }
                        println( "Selected Images" + images.size());
                    }
                }
                imageIndex = 0;
                imageView.setImageDrawable(images.get(imageIndex));
            } else {
                println( "You haven't picked Image");
            }
        } catch (Exception e) {
            println("Something went wrong");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void println(String data){textView.append(data);}
}