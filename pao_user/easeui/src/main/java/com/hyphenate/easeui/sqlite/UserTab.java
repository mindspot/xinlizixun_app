package com.hyphenate.easeui.sqlite;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by hpzhan on 2019/12/13.
 */

public class UserTab extends SugarRecord {

    @Unique
    String uid;
    String nickname;
    String avatar;

    public UserTab() {
    }

    public UserTab(String uid, String nickname, String avatar) {
        this.uid = uid;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
