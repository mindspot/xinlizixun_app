package util;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by hpzhan on 2019/4/4.
 */

public class CountDownUtil {
    public static void startCountDown(final TextView btn) {
        /** 倒计时60秒，一次1秒 */
        CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                btn.setText("重新发送" + millisUntilFinished / 1000 + "秒");
                btn.setEnabled(false);
            }

            @Override
            public void onFinish() {
                btn.setText("获取验证码");
                btn.setEnabled(true);
            }
        }.start();
    }
}
