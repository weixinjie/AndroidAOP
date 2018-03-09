package will.github.com.traceplugin;


/**
 * Created by will on 2018/3/7.
 */

public enum TraceType {
    ACTIVITY("统计activity"), FRAGMENT("统计fragment"), ONCLICK("统计点击事件"), NULL("不做任何统计");

    private String name;

    private TraceType(String typeName) {
        this.name = typeName;
    }

    public String getName() {
        return name;
    }
}
