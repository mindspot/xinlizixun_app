package common.router;


/**
 * @author Administrator
 * @date 2017/12/20
 */
public class CommandEntity {

    /**
     * ViewRouter的类型，根据该值，进行数据转换(convert)与指令逻辑
     */
    private int type = CommandType.TYPE_ERROR;
    /**
     * 新版本的指令类型：用于替换type
     * 通过命名即可知道指令的功能
     */
    private String path = null;


    @Deprecated
    public int getType() {
        return type;
    }

    @Deprecated
    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public CommandRequest request() {
        return new CommandRequest<>(this);
    }
}
