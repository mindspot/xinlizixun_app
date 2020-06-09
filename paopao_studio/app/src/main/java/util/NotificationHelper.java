package util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.paopao.paopaostudio.R;

import im.chat.ChatActivity;

/**
 * @version V 1.0 xxxxxxxx
 * @Title: pao_user
 * @Package util
 * @Description: $todo$
 * @author: L-BackPacker
 * @date: 2020.04.22 上午 10:20
 * @verdescript 版本号 修改时间  修改人 修改的概要说明
 * @Copyright: 2020
 */
public class NotificationHelper {
    private static final String CHANNEL_ID = "channel_id";   //通道渠道id
    public static final String CHANEL_NAME = "chanel_name"; //通道渠道名称
    private Context mContext;

    public NotificationHelper(Context context) {
        this.mContext = context;
    }

    //发送通知
    int notifiId = 1;

    @TargetApi(Build.VERSION_CODES.O)
    public void show(String content, String userid) {

        Intent intent_target = new Intent(mContext, ChatActivity.class);
        intent_target.putExtra("pushUserid", userid);
        //解决PendingIntent的extra数据不准确问题
        intent_target.setAction(Long.toString(System.currentTimeMillis()));
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent_target, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建 通知通道  channelid和channelname是必须的（自己命名就好）
            channel = new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);//是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN);//小红点颜色
            channel.setShowBadge(false); //是否在久按桌面图标时显示此渠道的通知
        }
        Notification notification;
        //获取Notification实例   获取Notification实例有很多方法处理    在此我只展示通用的方法（虽然这种方式是属于api16以上，但是已经可以了，毕竟16以下的Android机很少了，如果非要全面兼容可以用）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //向上兼容 用Notification.Builder构造notification对象
            notification = new Notification.Builder(mContext, CHANNEL_ID)
                    .setContentTitle("消息通知")
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.applogo)
                    .setColor(Color.parseColor("#FEDA26"))
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.applogo))
                    .setTicker("泡泡心理")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build();
        } else {
            //向下兼容 用NotificationCompat.Builder构造notification对象
            notification = new NotificationCompat.Builder(mContext)
                    .setContentTitle("消息通知")
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.applogo)
                    .setColor(Color.parseColor("#FEDA26"))
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.applogo))
                    .setTicker("泡泡心理")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build();
        }
        //创建一个通知管理器
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
        notifiId = Integer.parseInt(userid.replace("ps", "").replace("pu", ""));
        notificationManager.notify(notifiId, notification);
    }
}
