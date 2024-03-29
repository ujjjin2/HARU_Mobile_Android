package com.object.haru.Fcm;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.object.haru.Activity.ApplyDetailActivity;
import com.object.haru.Activity.LoginActivity;
import com.object.haru.Activity.MainActivity;
import com.object.haru.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    /* 토큰이 새로 만들어질때나 refresh 될때  */
    private static final String TAG = "MyFirebaseMessaging";

    @Override
    public void onCreate() {
        super.onCreate();

        // 채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default Channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Default channel for app notifications");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.v("FCM 토큰", token);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String getToken = task.getResult();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token", getToken);
                editor.apply();
            } else {
                // 토큰을 가져오는 데 실패한 경우에 대한 처리
                Log.e("FCM 토큰", "토큰을 가져오는 데 실패했습니다.");
            }
        });
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // 주제 확인
        Log.i("---","---");
        Log.d("//===========//","================================================");
        Log.i("","\n"+"[>> onMessageReceived() :: [전체] 파이어베이스 푸시 알림 수신 확인]");
        Log.i("","\n"+"--------------------------------");
        Log.i("","\n"+"[getFrom() :: "+String.valueOf(remoteMessage.getFrom())+"]");
        Log.i("","\n"+"--------------------------------");
        Log.i("","\n"+"[getData() :: "+String.valueOf(remoteMessage.getData())+"]");
        Log.i("","\n"+"--------------------------------");
        Log.i("","\n"+"[getNotification() :: "+String.valueOf(remoteMessage.getNotification())+"]");
        Log.d("//===========//","================================================");
        Log.i("---","---");

        if (remoteMessage.getData().containsKey("topic") && remoteMessage.getData().get("topic").equals("newApply")) {
            // newApply 주제에 대한 처리
            Intent intent = new Intent(this, LoginActivity.class);
            Log.d("알림 새로운 지원서 번호 :",remoteMessage.getData().get("id"));
            intent.putExtra("newApply", "newApply"); // 알림 데이터 전달
            intent.putExtra("id", remoteMessage.getData().get("id")); // 알림 데이터 전달
          //  intent.putExtra("notification_data", remoteMessage.getData()); // 알림 데이터 전달
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "default")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(new long[]{1, 1000});
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder.setContentIntent(pendingIntent);
            notificationManager.notify(0, mBuilder.build());

        } else if (remoteMessage.getData().containsKey("topic") && remoteMessage.getData().get("topic").equals("newReview")) {
            // newApply 주제에 대한 처리
            Intent intent = new Intent(this, LoginActivity.class);
            //  intent.putExtra("notification_data", remoteMessage.getData()); // 알림 데이터 전달
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "default")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(new long[]{1, 1000});
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder.setContentIntent(pendingIntent);
            notificationManager.notify(0, mBuilder.build());

        } else if (remoteMessage.getData().containsKey("topic") && remoteMessage.getData().get("topic").equals("comfirmation")) {
            // newApply 주제에 대한 처리
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("comfirmation","구인확정"); // 알림 데이터 전달
            intent.putExtra("id", remoteMessage.getData().get("id")); // 알림 데이터 전달
            Log.d("확정된 글 번호 :",remoteMessage.getData().get("id"));

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "default")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(new long[]{1, 1000});
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder.setContentIntent(pendingIntent);
            notificationManager.notify(0, mBuilder.build());

        }else if (remoteMessage.getData().containsKey("topic") && remoteMessage.getData().get("topic").equals("chat")) {
            SharedPreferences auto = getSharedPreferences("checkChat", Activity.MODE_PRIVATE);
            SharedPreferences.Editor autoLoginEdit = auto.edit();
            autoLoginEdit.putInt("chatCount", 1);
            autoLoginEdit.apply();

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MainActivity mainActivity = MainActivity.INSTANCE;
                    if (mainActivity != null) {
                        BottomNavigationView bottomNavigationView = mainActivity.getBottomNavigationView();
                        Menu menu = bottomNavigationView.getMenu();
                        MenuItem chatListMenuItem = menu.findItem(R.id.chatList);
                        Log.d("알림받고 채팅변경 :","성공");
                        chatListMenuItem.setIcon(R.drawable.chat3);
                    }else{
                        Log.d("알림받고 mainActivity :","널");
                    }
                }
            });
            // newApply 주제에 대한 처리
            Intent intent = new Intent(this, LoginActivity.class);
              intent.putExtra("chat", "test"); // 알림 데이터 전달
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "default")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(new long[]{1, 1000});
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder.setContentIntent(pendingIntent);
            notificationManager.notify(0, mBuilder.build());
        }
    }
}


