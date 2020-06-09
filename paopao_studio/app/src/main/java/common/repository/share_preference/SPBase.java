package common.repository.share_preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/4/20.
 */

public class SPBase {

    protected SharedPreferences sp;

    public SPBase(Context context, String name) {
        this.sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    protected SharedPreferences.Editor edit() {
        return sp.edit();
    }

    public void clear() {
        sp.edit().clear().apply();
    }
}
