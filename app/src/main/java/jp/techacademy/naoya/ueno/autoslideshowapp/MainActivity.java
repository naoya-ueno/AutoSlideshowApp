package jp.techacademy.naoya.ueno.autoslideshowapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 100;

    Timer mTimer;

    Handler mHandler = new Handler();

    Button mStartButton;
    Button mPrevButton;
    Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo();
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo();
                }
                break;
            default:
                break;
        }
    }

    private void getContentsInfo() {

        // 画像の情報を取得する
        ContentResolver resolver = getContentResolver();
        final Cursor cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                null, // 項目(null = 全項目)
                null, // フィルタ条件(null = フィルタなし)
                null, // フィルタ用パラメータ
                null // ソート (null ソートなし)
        );

        if (cursor.moveToFirst()) {
            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = cursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
            imageVIew.setImageURI(imageUri);

            mStartButton = (Button) findViewById(R.id.start_button);
            mPrevButton = (Button) findViewById(R.id.prev_button);
            mNextButton = (Button) findViewById(R.id.next_button);

            mStartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTimer == null) {
                        mStartButton.setText("停止");
                        mTimer = new Timer();
                        mTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (cursor.moveToNext()) {
                                            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                                            Long id = cursor.getLong(fieldIndex);
                                            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                                            ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                                            imageVIew.setImageURI(imageUri);
                                        } else {
                                            cursor.moveToFirst();
                                            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                                            Long id = cursor.getLong(fieldIndex);
                                            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                                            ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                                            imageVIew.setImageURI(imageUri);
                                        }
                                    }
                                });
                            }
                        }, 2000, 2000);
                    } else {
                        mStartButton.setText("開始");
                        mTimer.cancel();
                        mTimer = null;
                    }
                }
            });

            mPrevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTimer == null) {
                        if (cursor.moveToPrevious()) {
                            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                            Long id = cursor.getLong(fieldIndex);
                            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                            ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                            imageVIew.setImageURI(imageUri);
                        } else {
                            cursor.moveToLast();
                            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                            Long id = cursor.getLong(fieldIndex);
                            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                            ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                            imageVIew.setImageURI(imageUri);
                        }
                    }
                }
            });

            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTimer == null) {
                        if (cursor.moveToNext()) {
                            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                            Long id = cursor.getLong(fieldIndex);
                            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                            ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                            imageVIew.setImageURI(imageUri);
                        } else {
                            cursor.moveToFirst();
                            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                            Long id = cursor.getLong(fieldIndex);
                            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                            ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                            imageVIew.setImageURI(imageUri);
                        }
                    }
                }
            });

        }

    }

}