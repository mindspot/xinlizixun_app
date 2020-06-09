package common.router;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.framework.page.Page;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * view router 回调处理助手
 *
 * @author Administrator
 * @date 2017/7/17
 */
public class CommandCallbackHandler {

    private static final HashMap<Page, LinkedList<Command>> CALLBACK_CACHES = new HashMap<>(10);


    static void addCommand(Page page, Command command) {
        if (page == null || command == null) {
            return;
        }

        if (CALLBACK_CACHES.containsKey(page)) {
            CALLBACK_CACHES.get(page).addFirst(command);
        } else {
            LinkedList<Command> builders = new LinkedList<>();
            builders.addFirst(command);
            CALLBACK_CACHES.put(page, builders);
        }
    }

    private static void removeCommand(@NonNull Command command) {
        Page page = command.request.getPage();
        if (!CALLBACK_CACHES.containsKey(page)) {
            return;
        }
        List<Command> commands = CALLBACK_CACHES.get(page);
        commands.remove(command);
    }

    static void detachedPage(@NonNull Page page) {
        if (CALLBACK_CACHES.containsKey(page)) {
            CALLBACK_CACHES.remove(page);
        }
    }

    static boolean onActivityResult(@NonNull Page page, int requestCode, int resultCode, Intent data) {
        if (!CALLBACK_CACHES.containsKey(page)) {
            return false;
        }

        boolean handled = false;
        for (Command command : CALLBACK_CACHES.get(page)) {
            handled = command.onActivityResult(requestCode, resultCode, data);
            if (handled) {
                // 处理了改指令，所以可以删除了
                removeCommand(command);
                break;
            }
        }
        return handled;
    }

    static boolean onRequestPermissionsResult(@NonNull Page page, int requestCode, String[] permissions, int[] grantResults) {
        if (!CALLBACK_CACHES.containsKey(page)) {
            return false;
        }

        boolean handled = false;
        for (Command command : CALLBACK_CACHES.get(page)) {
            handled = command.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (handled) {
                break;
            }
        }
        return handled;
    }
}
