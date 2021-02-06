package amlan.common.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;


@Service
public class MetricPublisher extends HandlerInterceptorAdapter {

    private static final String NAMESPACE = "micro_service";
    private static final String START_TIME_ATTR = "START_TIME";
    private MeterRegistry meterRegistry;

    @Autowired
    public void setMeterRegistry(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            request.setAttribute(START_TIME_ATTR, System.currentTimeMillis());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        if (handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();

            long startTime = (long) request.getAttribute(START_TIME_ATTR);
            long processingTime = System.currentTimeMillis() - startTime;

            String isExpect = getIsExpect(response);
            String status = response.getStatus() + "";

            if (response.getStatus() != HttpStatus.NOT_FOUND.value()) {
                this.incMetric(processingTime, method.getName(), status, isExpect);
            }
        }

    }

    private void incMetric(long processTime, String serviceName, String statusCode, String isExpect) {
        List<Tag> tags = Arrays.asList(Tag.of("service", serviceName), Tag.of("status_code", statusCode), Tag.of("isexpect", isExpect));
        meterRegistry.counter(NAMESPACE + "_transaction_count", tags).increment();
        meterRegistry.gauge(NAMESPACE + "_processing_time", tags, processTime);
    }

    private String getIsExpect(HttpServletResponse response) {
        int status = response.getStatus();
        if (status >= 500) {
            return "1";
        } else if (status >= 400) {
            return "2";
        } else if (status >= 200 && status < 300) {
            return "0";
        }
        return "";
    }

}
