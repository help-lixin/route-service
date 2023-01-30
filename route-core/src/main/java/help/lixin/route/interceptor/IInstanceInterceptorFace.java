package help.lixin.route.interceptor;

import java.util.List;

public interface IInstanceInterceptorFace<T> {
    void filter(List<T> instances);
}
