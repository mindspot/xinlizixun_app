package common.router;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.framework.utils.ConvertUtil;
import com.paopao.paopaouser.BuildConfig;
import com.socks.library.KLog;
import com.tencent.bugly.crashreport.CrashReport;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import common.router.command.ErrorCommand;

/**
 * 指令的查找器，并转换为对应的数据类型
 * 保存各个ViewRouter与其处理的跳转path与type的映射关系
 * 1、以后以path为主，type不再添加
 *
 * @author Administrator
 * @date 2018/4/23
 */
public class CommandRegistry {

    /**
     * 保存各个ViewRouter与其处理的跳转path的映射关系
     */
    private static final Map<String, Entry> PATH_COMMANDS = new HashMap<>(100);
    /**
     * 保存各个ViewRouter与其处理的跳转type的映射关系
     */
    @Deprecated
    private static final SparseArray<Entry> TYPE_COMMANDS = new SparseArray<>(100);


    //********************* command register *********************

    static {
        registerCommands(

        );
    }

    /**
     * 使用反射，将指令注册到CommandRegistry中
     *
     * @param commandClassArray 注册的指令class
     */
    @SafeVarargs
    private static void registerCommands(Class<? extends Command>... commandClassArray) {
        for (Class commandClass : commandClassArray) {
            if (commandClass == null) {
                continue;
            }

            try {
                Class.forName(commandClass.getName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    //********************* command saver *********************

    private static final class Entry<C extends Command, D extends CommandEntity> {
        private final Class<C> commandClass;
        private final Class<D> commandDataClass;

        public Entry(Class<C> commandClass, Class<D> commandDataClass) {
            this.commandClass = commandClass;
            this.commandDataClass = commandDataClass;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "commandClass=" + commandClass.getName() +
                    ", commandDataClass=" + commandDataClass.getName() +
                    '}';
        }
    }

    /**
     * 注册路由指令：注入支持类型及指令数据结构
     *
     * @param commandClass     Router的真实处理类
     * @param commandDataClass Router的指令的数据结构class
     * @param supportPaths     commandClass支持的跳转path列表
     */
    public static <T extends Command, Data extends CommandEntity> void register(Class<T> commandClass, Class<Data> commandDataClass, String... supportPaths) {
        KLog.d("class:" + commandClass.getName() + "， dataClass:" + commandDataClass.getName() + ", supportPaths:" + Arrays.toString(supportPaths));
        Entry entry = new Entry<>(commandClass, commandDataClass);
        for (String supportPath : supportPaths) {
            abnormalDetection(entry, supportPath);
            PATH_COMMANDS.put(supportPath, entry);
        }
    }

    /**
     * 异常检测，防止出现多个ViewRouter，都同时支持一个跳转path的问题
     */
    private static void abnormalDetection(Entry commandEntry, String supportPath) {
        if (BuildConfig.IS_RELLEASE) {
            // release环境，不需要检测
            return;
        }

        Entry cacheCommandEntry = PATH_COMMANDS.get(supportPath);
        if (cacheCommandEntry != null) {
            String message = "跳转类型：" + supportPath + ", 支持类：" + cacheCommandEntry.toString() + ", 重复的类型：" + commandEntry.toString();
            KLog.e(message);
            CrashReport.postCatchedException(new Throwable(message));
        }
    }


    /**
     * 注册路由指令：注入支持类型及指令数据结构
     *
     * @param commandClass     Router的真实处理类
     * @param commandDataClass Router的指令的数据结构class
     * @param supportTypes     commandClass支持的跳转type列表
     */
    @Deprecated
    public static <T extends Command, Data extends CommandEntity> void register(Class<T> commandClass, Class<Data> commandDataClass, int... supportTypes) {
        KLog.d("class:" + commandClass.getName() + "， dataClass:" + commandDataClass.getName() + ", supportTypes:" + Arrays.toString(supportTypes));
        Entry entry = new Entry<>(commandClass, commandDataClass);
        for (int supportType : supportTypes) {
            abnormalDetection(entry, supportType);
            TYPE_COMMANDS.put(supportType, entry);
        }
    }

    /**
     * 异常检测，防止出现多个ViewRouter，都同时支持一个跳转type的问题
     */
    private static <T extends Command> void abnormalDetection(Entry commandEntry, int supportType) {
        if (BuildConfig.IS_RELLEASE) {
            // release环境，不需要检测
            return;
        }

        Entry cacheCommandEntry = TYPE_COMMANDS.get(supportType);
        if (cacheCommandEntry != null) {
            String message = "跳转类型：" + supportType + ", 支持类：" + cacheCommandEntry.toString() + ", 重复的类型：" + commandEntry.toString();
            KLog.e(message);
            CrashReport.postCatchedException(new Throwable(message));
        }
    }


    //********************* command data converter *********************

    /**
     * 转换为对应的数据类型
     *
     * @param cmdContent 指令的json格式字符串
     * @return 对应的数据类型
     */
    @NonNull
    static CommandEntity convert(final String cmdContent) {
        if (cmdContent == null || cmdContent.trim().isEmpty()) {
            KLog.e("指令cmdContent is null or empty...");
            return new CommandEntity();
        }

        CommandEntity commonData = ConvertUtil.toObject(cmdContent, CommandEntity.class);
        if (commonData == null) {
            String message = "指令json解析失败, cmdContent:" + cmdContent;
            KLog.e(message);
            CrashReport.postCatchedException(new Throwable(message));
            return new CommandEntity();
        }

        final String path = commonData.getPath();
        final int type = commonData.getType();

        Entry<?, ?> entry = PATH_COMMANDS.get(path);
        if (entry == null) {
            entry = TYPE_COMMANDS.get(type);
        }

        if (entry == null) {
            // 没有找到对应的跳转type
            String message = "没有找到对应的跳转path:" + path + ", type:" + type + ", cmdContent:" + cmdContent;
            KLog.e(message);
            CrashReport.postCatchedException(new Throwable(message));
            return new CommandEntity();
        }

        CommandEntity commandEntity = ConvertUtil.toObject(cmdContent, entry.commandDataClass);
        return commandEntity == null ? new CommandEntity() : commandEntity;
    }


    //********************* command finder *********************

    @NonNull
    static Command findCommand(CommandRequest request) {
        Entry<?, ?> entry;

        final String path = request.getData().getPath();
        entry = PATH_COMMANDS.get(path);
        if (entry != null) {
            Class<? extends CommandEntity> dataClass = entry.commandDataClass;
            if (dataClass != request.getData().getClass()) {
                KLog.w("指令数据类型设置警告：commandData:" + dataClass + ", request.data class:" + request.getData().getClass());
                if (!dataClass.isInstance(request.getData())) {
                    // 设置的指令数据与指令类型，不匹配
                    String message = "设置的指令数据与指令类型，不匹配. path:" + path + ", commandData class:" + dataClass + ", request.data class:" + request.getData().getClass();
                    KLog.e(message);
                    CrashReport.postCatchedException(new Throwable(message));
                    return new ErrorCommand(request, ErrorCommand.EType.CommandTypeIsNotMatchCommandData, message);
                }
            }
            return createCommand(request, entry.commandClass);
        }

        final int type = request.getData().getType();
        entry = TYPE_COMMANDS.get(type);
        if (entry != null) {
            Class<? extends CommandEntity> dataClass = entry.commandDataClass;
            if (dataClass != request.getData().getClass()) {
                KLog.w("指令数据类型设置警告：commandData:" + dataClass + ", request.data class:" + request.getData().getClass());
                if (!dataClass.isInstance(request.getData())) {
                    // 设置的指令数据与指令类型，不匹配
                    String message = "设置的指令数据与指令类型，不匹配. type:" + type + ", commandData class:" + dataClass + ", request.data class:" + request.getData().getClass();
                    KLog.e(message);
                    CrashReport.postCatchedException(new Throwable(message));
                    return new ErrorCommand(request, ErrorCommand.EType.CommandTypeIsNotMatchCommandData, message);
                }
            }
            return createCommand(request, entry.commandClass);
        }

        // 没有找到对应的跳转type和path
        String message = "没有找到对应的跳转type:" + type + ", path:" + path;
        KLog.e(message);
        // 默认错误码，不上报bugly
        if (type != CommandType.TYPE_ERROR) {
            CrashReport.postCatchedException(new Throwable(message));
        }
        return new ErrorCommand(request, ErrorCommand.EType.NotFindTargetCommandType, message);
    }

    @NonNull
    private static Command createCommand(CommandRequest request, Class<? extends Command> commandClass) {
        try {
            Constructor<? extends Command> con = commandClass.getConstructor();
            Command command = con.newInstance();
            command.setRequest(request);
            return command;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        String message = "Command异常:" + commandClass.getName();
        KLog.e(message);
        CrashReport.postCatchedException(new Throwable(message));
        return new ErrorCommand(request, ErrorCommand.EType.CommandActionException, message);
    }


}
