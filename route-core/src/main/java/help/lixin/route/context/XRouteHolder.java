package help.lixin.route.context;

/**
 * 从Http协议头里,获取到x-route的信息,并Holder,因为还要继续往下一链路传递这个值.
 */
public abstract class XRouteHolder {
    protected static final ThreadLocal<String> XROUNTE_CONTEXT = new InheritableThreadLocal<>();

    /**
     * @param xroute 从HTTP协议头(x-route)获取到的内容
     */
    public static void set(String xroute) {
        XROUNTE_CONTEXT.set(xroute);
    }

    public static boolean isEnabled() {
        String xrouteValue = get();
        return null != xrouteValue && !xrouteValue.isEmpty();
    }

    public static String get() {
        return XROUNTE_CONTEXT.get();
    }

    public static void clean() {
        XROUNTE_CONTEXT.remove();
    }
}
