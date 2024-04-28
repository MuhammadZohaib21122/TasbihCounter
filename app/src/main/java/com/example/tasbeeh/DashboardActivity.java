package com.example.tasbeeh;

import static android.app.Service.START_STICKY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class DashboardActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "My Channel";
    private static final int NOTIFICATION_ID = 100;
    private static final String FIRST_TIME_KEY = "isFirstTime";

    TextView tvCounterButton, tvCounterResult, tvLaps, textView,tvWazifa;
    EditText etLimit;
    ImageView ivReset, ivDecreaseButton, ivEditbutton, ivVolumebutton, ivAlarmButton;
    int currentCounterValue = 0;
    private boolean isMuted = false;

    int progressBarMaxValue = 100;
    ProgressBar progressBar;
    private final String PREF_NAME = "MyPref";
    private final String KEY_INT_VALUE = "tvCounterResult";
    private final String KEY_LAPS_VALUE = "tvLapsValue";
    private final String KEY_LIMIT_VALUE = "etLimitValue";
    private final String KEY_PROGRESS_VALUE = "progressBarValue";
    private MediaPlayer mediaPlayer, mediaPlayer1, mediaPlayer2, mediaPlayer3;
    public String wazaifTextTop = "لآ اِلَهَ اِلّا اللّهُ مُحَمَّدٌ رَسُوُل اللّهِ";
    ;
    SharedPreferences sharedPreferences;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvWazifa = findViewById(R.id.tvWazifa);

        String text = getIntent().getStringExtra("TEXT_KEY");
        tvWazifa.setText(text);



        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        mediaPlayer = MediaPlayer.create(this, R.raw.mouse_click_153941);
        mediaPlayer1 = MediaPlayer.create(this, R.raw.button_124476);
        mediaPlayer2 = MediaPlayer.create(this, R.raw.decrease_sound_123);
        mediaPlayer3 = MediaPlayer.create(this, R.raw.edit_sound_0012);

        tvCounterButton = findViewById(R.id.tvCounterButton);

        ivEditbutton = findViewById(R.id.ivEditbutton);
        tvCounterResult = findViewById(R.id.tvCounterResult);
        tvLaps = findViewById(R.id.tvLaps);
        ivReset = findViewById(R.id.ivReset);
        ivDecreaseButton = findViewById(R.id.ivDecreaseButton);
        progressBar = findViewById(R.id.progressBar);
        etLimit = findViewById(R.id.etLimit);
        ivVolumebutton = findViewById(R.id.ivVolumebutton);
        ivAlarmButton = findViewById(R.id.ivAlarmButton);
        progressBarMaxValue = Integer.parseInt(etLimit.getText().toString());

        currentCounterValue = getInt(KEY_INT_VALUE);
        progressBar.setProgress(currentCounterValue);
        tvCounterResult.setText(currentCounterValue + "");

        int savedLapsValue = getInt(KEY_LAPS_VALUE);
        tvLaps.setText("LAP:" + savedLapsValue);

        int savedLimitValue = getInt(KEY_LIMIT_VALUE);
        etLimit.setText(String.valueOf(savedLimitValue));
        progressBarMaxValue = savedLimitValue;
        progressBar.setMax(progressBarMaxValue);

        int savedProgressValue = getInt(KEY_PROGRESS_VALUE);
        progressBar.setProgress(savedProgressValue);


        isMuted = getBoolean("isMuted");
        if (isMuted) {
            ivVolumebutton.setImageResource(R.drawable.mute_icon_001);
        } else {
            ivVolumebutton.setImageResource(R.drawable.icon_volume_0011);
        }

        textView = findViewById(R.id.tvWazifa);

        wazaifTextTop = sharedPreferences.getString("savedText", wazaifTextTop);

        ivAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AlarmActivity.class);
                startActivity(intent);
            }
        });

        textView.setText(wazaifTextTop);

        etLimit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                resetFun();
                if (!s.toString().isEmpty() && !s.toString().equals("0") && !s.toString().startsWith("0")) {
                    int newMaxValue = Integer.parseInt(s.toString());
                    progressBarMaxValue = newMaxValue;
                    progressBar.setMax(progressBarMaxValue);
                    if (currentCounterValue > progressBarMaxValue) {
                        currentCounterValue = progressBarMaxValue;
                        tvCounterResult.setText(String.valueOf(currentCounterValue));
                    }

                    saveInt(KEY_LIMIT_VALUE, newMaxValue);
                } else {
                    Toast.makeText(DashboardActivity.this, "LIMIT 0 is not valid ", Toast.LENGTH_SHORT).show();

                    progressBarMaxValue = 10;
                    progressBar.setMax(progressBarMaxValue);

                    etLimit.setText("" + 10);

                    saveInt(KEY_LIMIT_VALUE, 10);
                }
            }
        });
        ivReset.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                playResetSound();
                etLimit.setText("0");
                tvLaps.setText("LAP: 0");
                progressBar.setProgress(0);
                currentCounterValue = 0;
                saveInt(KEY_INT_VALUE, currentCounterValue);
                saveInt(KEY_LAPS_VALUE, 0);
                saveInt(KEY_PROGRESS_VALUE, 0);
            }
        });
        ivVolumebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMuted) {
                    isMuted = true;
                    saveBoolean("isMuted", isMuted);
                    ivVolumebutton.setImageResource(R.drawable.mute_icon_001);
                } else {
                    isMuted = false;
                    //save in sp
                    saveBoolean("isMuted", isMuted);
                    ivVolumebutton.setImageResource(R.drawable.icon_volume_0011);
                }
            }
        });
        ivDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMuted)
                    playDecreaseSound();
                if (currentCounterValue > 0) {
                    currentCounterValue--;
                    progressBar.setProgress(currentCounterValue % Integer.parseInt(etLimit.getText().toString()));
                    saveInt(KEY_INT_VALUE, currentCounterValue);
                    tvCounterResult.setText(String.valueOf(currentCounterValue));
                }
            }
        });
        ivEditbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMuted)
                    playEditSound();
                Intent intent = new Intent(DashboardActivity.this, WazaifActivity.class);
                startActivity(intent);
            }
        });
        tvCounterButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (!isMuted)
                    playClickSound();
                if (!etLimit.getText().toString().isEmpty()) {
                    int limit = Integer.parseInt(etLimit.getText().toString());
                    if (limit == 0)
                        limit = 1;
                    int laps = currentCounterValue / limit;
                    tvLaps.setText("LAP:" + laps);
                    currentCounterValue++;

                    if (limit < currentCounterValue) {
                        if (currentCounterValue % limit == 0) {
                            progressBar.setProgress(100);
                        } else {
                            progressBar.setProgress(currentCounterValue % limit);
                        }
                    } else {
                        progressBar.setProgress(currentCounterValue);
                    }

                    tvCounterResult.setText(String.valueOf(currentCounterValue));
                    saveInt(KEY_INT_VALUE, currentCounterValue);
                    saveInt(KEY_LAPS_VALUE, laps);
                    saveInt(KEY_PROGRESS_VALUE, progressBar.getProgress());
                } else
                    Toast.makeText(DashboardActivity.this, "Please Enter a Value", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @SuppressLint("SetTextI18n")
    private void resetFun() {
        currentCounterValue = 0;
        progressBar.setProgress(currentCounterValue);
        saveInt(KEY_INT_VALUE, currentCounterValue);
        tvCounterResult.setText(currentCounterValue + "");
        tvLaps.setText("LAP:" + 0);
    }
    public void saveBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public void saveInt(String key, int value) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    public boolean getBoolean(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }
    public int getInt(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }
    private void playClickSound() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        }
    }
    private void playResetSound() {
        if (mediaPlayer1 != null) {
            mediaPlayer1.seekTo(0);
            mediaPlayer1.start();
        }
    }
    private void playDecreaseSound() {
        if (mediaPlayer2 != null) {
            mediaPlayer2.seekTo(0);
            mediaPlayer2.start();
        }
    }
    private void playEditSound() {
        if (mediaPlayer3 != null) {
            mediaPlayer3.seekTo(0);
            mediaPlayer3.start();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        wazaifTextTop = sharedPreferences.getString("savedText", wazaifTextTop);
        textView.setText(wazaifTextTop);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        boolean handled = false;

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (!isMuted)
                playDecreaseSound();
            if (currentCounterValue > 0) {
                currentCounterValue--;
                progressBar.setProgress(currentCounterValue % Integer.parseInt(etLimit.getText().toString()));
                saveInt(KEY_INT_VALUE, currentCounterValue);
                tvCounterResult.setText(String.valueOf(currentCounterValue));
            }
            handled = true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

            if (!isMuted)
                playClickSound();

            if (!etLimit.getText().toString().isEmpty()) {
                int limit = Integer.parseInt(etLimit.getText().toString());
                int laps = currentCounterValue / limit;
                tvLaps.setText("LAP:" + String.valueOf(laps));
                currentCounterValue++;
                if (limit < currentCounterValue) {
                    progressBar.setProgress(currentCounterValue % limit);
                } else
                    progressBar.setProgress(currentCounterValue);
                tvCounterResult.setText(String.valueOf(currentCounterValue));
                saveInt(KEY_INT_VALUE, currentCounterValue);
                saveInt(KEY_LAPS_VALUE, laps);
                saveInt(KEY_PROGRESS_VALUE, progressBar.getProgress());
            }
            handled = true;
        }
        return handled || super.onKeyDown(keyCode, event);
    }
}

//        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_notification, null);
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
//        Bitmap largeIcon = bitmapDrawable.getBitmap();
//
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Notification notification;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            notification = new Notification.Builder(this)
//                    .setLargeIcon(largeIcon)
//                    .setSmallIcon(R.drawable.ic_notification)
//                    .setContentText("Its Wazaif time")
//                    .setSubText("Reminder")
//                    .setChannelId(CHANNEL_ID)
//                    .build();
//            notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "New Channel", NotificationManager.IMPORTANCE_HIGH));
//        } else {
//            notification = new Notification.Builder(this)
//                    .setLargeIcon(largeIcon)
//                    .setSmallIcon(R.drawable.ic_notification)
//                    .setContentText("Wazaif")
//                    .setSubText("Its WAzaif time")
//                    .build();
//
//        }
//
//        notificationManager.notify(NOTIFICATION_ID, notification);
