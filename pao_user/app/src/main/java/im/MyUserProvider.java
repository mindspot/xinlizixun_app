package im;

import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.sqlite.UserTab;
import com.hyphenate.easeui.sqlite.UserTabUtil;

/**
 * @version V 1.0 xxxxxxxx
 * @Title: pao_user
 * @Package im
 * @Description: $todo$
 * @author: L-BackPacker
 * @date: 2020.03.10 下午 2:46
 * @verdescript 版本号 修改时间  修改人 修改的概要说明
 * @Copyright: 2020
 */
public class MyUserProvider implements EaseUI.EaseUserProfileProvider {
    private static MyUserProvider myUserProvider;

    @Override
    public EaseUser getUser(String username) {
        UserTab userTab = UserTabUtil.getUser(username);
        EaseUser easeUser = new EaseUser(username);
        if (UserTabUtil.getUser(username) != null) {
            easeUser.setNickname(userTab.getNickname());
            easeUser.setAvatar(userTab.getAvatar());
        }
        return easeUser;
    }

    // 按照你喜欢的修改一下初始化函数吧，
    private MyUserProvider() {
        System.out.println("init myTestProfileProvider");
    }

    // 获取单类。。
    public static MyUserProvider getInstance() {
        if (myUserProvider == null) {
            myUserProvider = new MyUserProvider();
        }
        return myUserProvider;
    }
}