package common.router;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.socks.library.KLog;
import com.tencent.bugly.crashreport.CrashReport;

import common.router.command.ErrorCommand;
import common.webview.custom.BaiTiaoWebView;

/**
 * ViewRouter功能的参数设置
 * builder模式
 *
 * @author Administrator
 */
public class CommandRequest<Data extends CommandEntity> {
    private final Data data;
    /**
     * 附属于fragment还是activity
     * 全部的fragment都应该是v4下的
     */
    private Page page;


    //*************** 生成操作指令 ***************

    /**
     * 支持：只需要type就可以跳转的功能
     *
     * @param type VRType中的值
     */
    public CommandRequest(int type) {
        CommandEntity data = new CommandEntity();
        data.setType(type);
        this.data = (Data) data;
    }

    public CommandRequest(String cmdContent) {
        this.data = (Data) CommandRegistry.convert(cmdContent);
    }

    public CommandRequest(@NonNull Data data) {
        this.data = data;
    }

    public CommandRequest setPage(@NonNull Page page) {
        this.page = page;
        return this;
    }


    public void router() {
        Log.d(CommandRequest.class.getName(), "router command data:" + ConvertUtil.toJsonString(data));
        if (checkParams()) {
            return;
        }

        Command command = CommandRegistry.findCommand(this);
        // 没有找到对应的Command，所以执行不了该指令，直接return
        if (command instanceof ErrorCommand) {
            return;
        }

        CommandCallbackHandler.addCommand(page, command);

        try {
            command.start();
        } catch (Exception e) {
            e.printStackTrace();
            showToast("数据参数错误！");
            CrashReport.postCatchedException(new Throwable(e));
        }
    }

    private boolean checkParams() {
        if (data == null) {
            String message = "跳转数据为null或空字符串等，不能转换为跳转对象";
            KLog.d(message);
            CrashReport.postCatchedException(new Throwable(message));
            return true;
        }

        return false;
    }


    //*************** 指令内部的数据及通用功能 ***************

    public Data getData() {
        return data;
    }

    public Page getPage() {
        return page;
    }


    public Activity getActivity() {
        return page.activity();
    }

    public void startActivity(Intent intent) {
        page.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        page.startActivityForResult(intent, requestCode);
    }

    public boolean isWebViewPage() {
        return page instanceof BaiTiaoWebView.WebViewPage;
    }

    public BaiTiaoWebView.WebViewPage getWebViewPage() {
        return (BaiTiaoWebView.WebViewPage) page;
    }

    public void showToast(String message) {
        page.showToast(message);
    }


}