package im.chatlist;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.paopao.paopaouser.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import common.events.TabEvent;
import de.greenrobot.event.EventBus;
import im.IMHolder;
import im.chat.ChatActivity;
import module.main.MainActivity;
import ui.CustomClickListener;
import ui.title.ToolBarTitleView;
import util.TimeUtils;

/**
 * @version V 1.0 xxxxxxxx
 * @Title: pao_user
 * @Package im.chatlist
 * @Description: $todo$
 * @author: L-BackPacker
 * @date: 2020.03.10 下午 2:51
 * @verdescript 版本号 修改时间  修改人 修改的概要说明
 * @Copyright: 2020
 */
public class MyEaseConversationFragment extends Fragment {
    private final static int MSG_REFRESH = 2;
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    protected EaseConversationList conversationListView;
    protected FrameLayout errorItemContainer;
    protected boolean isConflict;
    private ToolBarTitleView toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_ease_fragment_conversation_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
        initView();
        setUpView();
    }

    protected void initView() {
        conversationListView = (EaseConversationList) getView().findViewById(R.id.list);
        errorItemContainer = (FrameLayout) getView().findViewById(R.id.fl_error_item);
        toolbar = (ToolBarTitleView) getView().findViewById(R.id.toolbar);

        toolbar.setRightClickListener(new CustomClickListener() {
            @Override
            protected void onClick() {
                IMHolder.getInstance().gotoChat(getContext(), "kf1", "用户客服", "");
            }
        });
    }

    protected void setUpView() {
        conversationList.addAll(loadConversationList());
        conversationListView.init(conversationList);

        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isDoubleClick()) {
                    return;
                }
                EMConversation conversation = conversationListView.getItem(position);
                String userid = conversation.conversationId();
                if (userid.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // start chat acitivity
                    if (conversation.isGroup()) {
                    } else {
                        ChatActivity.newIntent(getContext(), userid);
                    }
                }
            }
        });

        EMClient.getInstance().addConnectionListener(connectionListener);

        registerForContextMenu(conversationListView);

        EMClient.getInstance().chatManager().addMessageListener(listener);
    }

    private EMMessageListener listener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> list) {
            //收到消息
            onRefreshView();
            IMHolder.getInstance().disposeMessage(list);
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {
            //收到透传消息
            onRefreshView();
        }

        @Override
        public void onMessageRead(List<EMMessage> list) {
            //收到已读回执
            onRefreshView();
        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {
            //收到已送达回执
            onRefreshView();
        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {
            //消息被撤回
            onRefreshView();
        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {
            //消息状态变动
            onRefreshView();
        }
    };

    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE || error == EMError.SERVER_SERVICE_RESTRICTED
                    || error == EMError.USER_KICKED_BY_CHANGE_PASSWORD || error == EMError.USER_KICKED_BY_OTHER_DEVICE) {
                isConflict = true;
            } else {
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };

    protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    onConnectionDisconnected();
                    break;
                case 1:
                    onConnectionConnected();
                    break;

                case MSG_REFRESH: {
                    conversationList.clear();
                    conversationList.addAll(loadConversationList());
                    conversationListView.refresh();
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * connected to server
     */
    protected void onConnectionConnected() {
        errorItemContainer.setVisibility(View.GONE);
    }

    /**
     * disconnected with server
     */
    protected void onConnectionDisconnected() {
        errorItemContainer.setVisibility(View.VISIBLE);
    }


    /**
     * refresh ui
     */
    public void refresh() {
        if (!handler.hasMessages(MSG_REFRESH)) {
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

    /**
     * load conversation list
     *
     * @return +
     */
    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && !isConflict) {
            onRefreshView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefreshView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(connectionListener);
        EMClient.getInstance().chatManager().removeMessageListener(listener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isConflict) {
            outState.putBoolean("isConflict", true);
        }
    }

    private int lastMsgNum = 0;

    public void onRefreshView() {
        refresh();
        if (lastMsgNum == EMClient.getInstance().chatManager().getUnreadMessageCount()) {
            return;
        }
        lastMsgNum = EMClient.getInstance().chatManager().getUnreadMessageCount();
        EventBus.getDefault().post(new TabEvent(TabEvent.TYPE_UPLOAD_TAB_MSG_NUM, MainActivity.TAB_INDEX_MSG));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onRefreshView();
        }
    }

    private long lastClickTime;

    public boolean isDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime > TimeUtils.DOUBLE_CLICK_TIME) {
            lastClickTime = time;
            return false;
        }
        return true;
    }
}
