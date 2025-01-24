package agus.prasetyo.backend.system.config;

import agus.prasetyo.backend.system.filter.LoginRateLimitFilter;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {

    @Autowired
    private LoginRateLimitFilter loginRateLimitFilter;

    @Bean
    public FilterRegistrationBean<Filter> loginRateLimitFilterBean() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(loginRateLimitFilter);
        bean.addUrlPatterns("/login");
        return bean;
    }
}
