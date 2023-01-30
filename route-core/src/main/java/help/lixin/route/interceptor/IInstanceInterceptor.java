package help.lixin.route.interceptor;

import java.util.List;

public interface IInstanceInterceptor<T> {
    void filter(List<T> instances);
}
