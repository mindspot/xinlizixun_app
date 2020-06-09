package common.router;

import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * @author Administrator
 * @date 2017/7/13
 */
public abstract class Command<Data extends CommandEntity> {

    @Deprecated
    protected static <T extends Command, Data extends CommandEntity> void register(Class<T> commandClass, Class<Data> commandDataClass, @NonNull int... supportTypes) {
        CommandRegistry.register(commandClass, commandDataClass, supportTypes);
    }

    protected static <T extends Command, Data extends CommandEntity> void register(Class<T> commandClass, Class<Data> commandDataClass, @NonNull String... supportPaths) {
        CommandRegistry.register(commandClass, commandDataClass, supportPaths);
    }


    protected CommandRequest<Data> request;

    public Command() {
    }

    public void setRequest(CommandRequest<Data> request) {
        this.request = request;
    }

    public abstract void start();


    @NonNull
    protected String path() {
        String path = getPath();
        if (!path.isEmpty()) {
            return path;
        }
        return typeConvertPath();
    }

    @NonNull
    protected abstract String typeConvertPath();

    /**
     * 当前的跳转类型
     */
    protected int getType() {
        return request.getData().getType();
    }

    @NonNull
    private String getPath() {
        final String path = request.getData().getPath();
        if (path == null) {
            return CommandType.PATH_UNKNOWN;
        }
        return path;
    }

    /**
     * 处理activity与fragment的onActivityResult
     *
     * @return 是否处理了该回调
     */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }

    /**
     * 处理activity与fragment的onRequestPermissionsResult
     *
     * @return 是否处理了该回调
     */
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        return false;
    }


}
