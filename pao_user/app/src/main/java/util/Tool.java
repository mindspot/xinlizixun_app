package util;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.framework.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Tool {
    /**
     * 是否大于2.3，大于2.3才让adapter用自己的onClickListener
     */
    public final static boolean versionUpGingerbreadMr1;

    static {
        versionUpGingerbreadMr1 = Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1;
    }


    // 小数进位
    public static double carryAigit(float number) {
        return Math.ceil(number);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /***
     * sp转px
     *
     * @param
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    // 判断是否为数字
    public static boolean isNumeric(String str) {
        if (Tool.isBlank(str))
            return false;
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // 名字为汉字
    public static boolean isWord(String str) {
        if (Tool.isBlank(str) || str.trim().length() < 2) {
            return false;
        }
        // int n = 0,count=0;
        //
        // for (int i = 0; i < str.length(); i++) {
        // n = (int) str.charAt(i);
        // if ((19968 <= n && n <= 171941)) {
        // count++;
        // }
        // }
        // if(count==str.length())
        // return true;
        // else {
        // return false;
        // }
        return true;
    }

    // 检查银行卡号
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId
                .substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null
                || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }


    // 微信分享使用
    public static byte[] bmpToByteArray(final Bitmap bmp,
                                        final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // 金额由10000分转为100.00元
    public static String toDeciDouble(String num) {
        if (isBlank(num)) {
            return "0.00";
        }
        if (num.contains(".")) {
            num = num.substring(0, num.indexOf("."));
        }
        int len = num.length();
        if (len == 2 && num.startsWith("-")) {
            return num.substring(0, 1) + "0.0" + num.substring(len - 1);
        } else if (len == 3 && num.startsWith("-")) {
            return num.substring(0, 1) + "0." + num.substring(len - 2);
        } else if (len == 1) {
            return "0.0" + num;
        } else if (len == 2) {
            return "0." + num;
        } else {
            return num.substring(0, len - 2) + "." + num.substring(len - 2);
        }
    }

    /**
     * 转两位小数 单位：元
     *
     * @param moneyInY
     * @return
     */
    public static String toDeciDouble2(double moneyInY) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(moneyInY);
    }


    /**
     * 转两位小数 四舍5入 单位：元
     *
     * @param moneyInY
     * @return
     */
    public static String toDeciDoubleHalf(double moneyInY) {
        DecimalFormat df = new DecimalFormat("#0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(moneyInY);
    }

    /***
     * 转一位小数（留下一位小数，但是不会四舍五入）
     *
     * @param moneyInY
     * @return
     */
    public static float toDeciDouble1(float moneyInY) {
        DecimalFormat df = new DecimalFormat("#0.0");
        float val = 0.0f;
        try {

            val = Float.parseFloat(df.format(moneyInY));

        } catch (Exception e) {
            // TODO: handle exception
        }
        return val;
    }


    // 金额由10000分转为100元
    public static String toIntAccount(String num) {
        if (isBlank(num)) {
            return "0";
        }
        return "" + Long.parseLong(num) / 100;
    }

    // 金额由10000分转为100元
    public static long toIntAccountLong(String num) {
        if (isBlank(num)) {
            return 0;
        }
        return Long.parseLong(num) / 100;
    }

    /**
     * 金额由1000000分转为10,000元
     */
    public static String toDivAccount(String num) {
        if (isBlank(num)) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(Long.parseLong(num) / 100);
    }

    // 金额由10000.00转为10,000.00元
    public static String toDivAccount2(String num) {
        if (isBlank(num)) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#,###.00");
        return df.format(Double.parseDouble(num));
    }

    // 可以适配00的情况
    public static String toDivAccount3(String num) {
        if (isBlank(num)) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(Double.parseDouble(num));
    }

    public static String toFFDouble(double num) {
        if (Double.isNaN(num) || Double.isInfinite(num)) {
            return "0.00";
        }
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        BigDecimal bd = new BigDecimal(num);
        num = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return toDeciDouble(nf.format(num * 100));
    }

    // 将输入的数字转为0.00格式的double
    public static String toForDouble(String num) {
        if (Tool.isBlank(num)) {
            return "0.00";
        }
        return (new DecimalFormat("0.00")).format(Double.parseDouble(num));
    }

    // 将输入的数字转为0.00格式的double
    public static Double toForDouble2(String num) {
        if (Tool.isBlank(num)) {
            return 0.00;
        }
        return Double.parseDouble((new DecimalFormat("0.00")).format(Double.parseDouble(num)));
    }

    public static String trimHeadZero(String num) {
        if (isBlank(num)) {
            return "0";
        } else {
            return num.replaceFirst("^0*", "");
        }
    }

    // 判断字符串对象为null或者""
    public static boolean isBlank(String str) {
        return StringUtil.isBlank(str);
    }

    public static List<String> toList(JSONArray arr) {
        List<String> str_list = new ArrayList<String>();
        for (int i = 0; i < arr.length(); i++) {
            try {
                str_list.add(arr.getString(i));
            } catch (JSONException e) {
                Log.e("Tool", "Failed To List");
                return str_list;
            }
        }
        return str_list;
    }

    /**
     * 得到状态栏的高度
     *
     * @param context 上下文
     * @return 状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     */
    public static int getWindowWith(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int width = windowManager.getDefaultDisplay().getWidth();
        return width;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return yinsujun 2015-8-20 下午5:34:18
     */
    public static int getWindowHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int height = windowManager.getDefaultDisplay().getHeight();
        return height;
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    public static boolean checkDeviceHasNavigationBar2(Context activity) {
        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }

    public static int getNavigationBar(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        Display display = activity.getWindowManager().getDefaultDisplay();
        display.getMetrics(dm);
        int screenHeight = dm.heightPixels;

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(realDisplayMetrics);
        } else {
            Class c;
            try {
                c = Class.forName("android.view.Display");
                Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
                method.invoke(display, realDisplayMetrics);
            } catch (Exception e) {
                e.printStackTrace();
                realDisplayMetrics.setToDefaults();
            }
        }

        int creenRealHeight = realDisplayMetrics.heightPixels;

        Resources rs = activity.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        boolean hasNavBarFun = false;
        if (id > 0) {
            hasNavBarFun = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavBarFun = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavBarFun = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            hasNavBarFun = false;
        }

        if (!hasNavBarFun) {
            return 0;
        }
        return (creenRealHeight - screenHeight) < 0 ? 0 : (creenRealHeight - screenHeight);//screenRealHeight上面方法中有计算
    }

    public static int getVirtualBarHeigh(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }


    /**
     * 判断是否连续点击
     * <p>
     * 对于 startActivity 设置 singletop 无效果
     * 则这样 防止 连续点击跳重复界面
     */
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 时间计数器
     * <p>
     * 最多只能到99小时，如需要更大小时数需要改改方法
     */
    public static String showTimeCount(int time) {
        if (time >= 360000) {
            return "00:00:00";
        }
        int hourc = time / 3600;
        String hour = "0" + hourc;
        hour = hour.substring(hour.length() - 2, hour.length());

        int minuec = (time - hourc * 3600) / (60);
        String minue = "0" + minuec;
        minue = minue.substring(minue.length() - 2, minue.length());

        int secc = (time - hourc * 3600 - minuec * 60);
        String sec = "0" + secc;
        sec = sec.substring(sec.length() - 2, sec.length());

        String timeCount = hour + ":" + minue + ":" + sec;
        return timeCount;
    }

    /**
     * 认证中心的抖动效果
     *
     * @param view
     * @return
     */
    public static ObjectAnimator tada(View view) {
        return tada(view, 1f);
    }

    private static ObjectAnimator tada(View view, float shakeFactor) {

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -3f * shakeFactor),
                Keyframe.ofFloat(.2f, -3f * shakeFactor),
                Keyframe.ofFloat(.3f, 3f * shakeFactor),
                Keyframe.ofFloat(.4f, -3f * shakeFactor),
                Keyframe.ofFloat(.5f, 3f * shakeFactor),
                Keyframe.ofFloat(.6f, -3f * shakeFactor),
                Keyframe.ofFloat(.7f, 3f * shakeFactor),
                Keyframe.ofFloat(.8f, -3f * shakeFactor),
                Keyframe.ofFloat(.9f, 3f * shakeFactor),
                Keyframe.ofFloat(1f, 0)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate).
                setDuration(1000);
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo，因为友盟设置的meta-data是在application标签中
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        //key要与manifest中的配置文件标识一致
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resultData;
    }
}