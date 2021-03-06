package amlan.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    public MetricPublisher metricPublisher;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(metricPublisher);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // turn off all suffix pattern matching
        configurer.setUseSuffixPatternMatch(false);
    }


}
