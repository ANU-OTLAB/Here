package com.otlab.here.common;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import java.net.URI;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.otlab.here.R;

public class BackGroundWorker extends Worker {
    private String path;
    private SharedPreferences appData;
    public BackGroundWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    //백그라운드작업//
    @Override
    public Worker.Result doWork(){
        appData = getApplicationContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
        if(path == null) {
            path = appData.getString("alarmPath", "");
        }
        //띄울 알람의 설정//
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default"); //알람생성 getApplicationContext(), 채널id 생성
        builder.setSmallIcon(R.mipmap.ic_launcher_new); // 알람의 아이콘 선언
        builder.setContentTitle("다와가"); // 알람의 제목 선언
        builder.setContentText("근처로 접근중!"); // 알람 제목 밑 텍스트 선언
        //builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setSound(Uri.parse(path));
        builder.setAutoCancel(true); // 옆으로 넘겨서 없애는 기능
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE); // 알림 띄우기위해 notificationmanager 생성
        //이기능은 오레오 이상에서만 기능함
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본채널",NotificationManager.IMPORTANCE_DEFAULT)); // 채널아이디, 이름, 중요도를 파라미터값으로 받음
        }
        notificationManager.notify(1, builder.build()); // 생성
        return Result.success();
    }
}