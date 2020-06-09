package common.router.command;

import android.support.annotation.NonNull;

import common.router.Command;
import common.router.CommandEntity;
import common.router.CommandRequest;
import common.router.CommandType;


/**
 * 错误的跳转action
 *
 * @author Administrator
 * @date 2017/7/25
 */
public class ErrorCommand extends Command<CommandEntity> {

    private final EType eType;
    /**
     * 错误提示信息
     */
    private final String message;

    public ErrorCommand(CommandRequest<CommandEntity> request, EType eType, String message) {
        setRequest(request);
        this.eType = eType;
        this.message = message;
    }


    @Override
    public void start() {
        // do nothing...
    }

    @NonNull
    @Override
    protected String typeConvertPath() {
        return CommandType.PATH_UNKNOWN;
    }


    public EType geteType() {
        return eType;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 错误类型
     */
    public enum EType {
        /**
         * 跳转数据为null或空字符串等，不能转换为跳转对象
         */
        CanNotConvertToCommandData,
        /**
         * 没有找到对应的跳转type
         */
        NotFindTargetCommandType,
        /**
         * Command反射异常
         */
        CommandActionException,
        /**
         * 设置的指令数据与指令类型，不匹配
         */
        CommandTypeIsNotMatchCommandData
    }

}
