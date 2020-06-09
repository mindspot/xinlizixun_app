package com.framework.page;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.view.View;

public interface ComponentPage {

    Context context();

    Activity activity();

    void startActivity(Intent intent);

    void startActivityForResult(Intent intent, int requestCode);

    Intent getIntent();

    ComponentName startService(Intent service);

    void showToast(String message);

    <T extends View> T $(@IdRes int id);

    View root();

}