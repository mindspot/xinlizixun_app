package com.hyphenate.easeui.sqlite;

import android.util.Log;

import com.framework.utils.StringUtil;
import com.hyphenate.easeui.EaseUI;

import java.util.List;

/**
 * Created by hpzhan on 2019/12/13.
 */

public class UserTabUtil {
    public static void uploadUser(String uid, String nickname, String avatar) {
        uploadUser(uid, nickname, avatar, false);
    }

    public static void uploadUser(String uid, String nickname, String avatar, boolean uploadSelf) {
        if (StringUtil.isBlank(nickname) && StringUtil.isBlank(avatar)) {
            return;
        }
        if (!uploadSelf && uid.equals(EaseUI.getInstance().getLoginUserId())) {
            return;
        }
        Log.d("IM-Upload-UserInfo", "uid:" + uid + " nickname:" + nickname + " avatar:" + avatar);
        UserTab userTab = new UserTab(uid, nickname, avatar);
        List<UserTab> user = UserTab.find(UserTab.class, "uid = ?", uid);
        if (user != null && !user.isEmpty()) {
            userTab = user.get(0);
            userTab.setNickname(nickname);
            userTab.setAvatar(avatar);
        }
        userTab.save();
    }

    public static UserTab getUser(String uid) {
        List<UserTab> user = UserTab.find(UserTab.class, "uid = ?", uid);
        if (user != null && !user.isEmpty()) {
            return user.get(0);
        }
        return null;
    }

    public static String getUserNickname(String uid) {
        List<UserTab> user = UserTab.find(UserTab.class, "uid = ?", uid);
        if (user != null && !user.isEmpty()) {
            return user.get(0).getNickname();
        }
        return "";
    }

    public static String getUserImage(String uid) {
        List<UserTab> user = UserTab.find(UserTab.class, "uid = ?", uid);
        if (user != null && !user.isEmpty()) {
            return user.get(0).getAvatar();
        }
        return "";
    }
}
