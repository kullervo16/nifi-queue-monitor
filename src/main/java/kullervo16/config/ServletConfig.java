package kullervo16.config;

import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import kullervo16.staticweb.StaticPageServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServlet;

@Configuration
public class ServletConfig {
    @Value("${site.path}")
    private String path;

    @Bean
    public ServletRegistrationBean countryServlet() {
        ServletRegistrationBean servRegBean = new ServletRegistrationBean();
        servRegBean.setServlet(new StaticPageServlet(path));
        servRegBean.addUrlMappings("/static/*");
        servRegBean.setLoadOnStartup(1);
        return servRegBean;
    }
}
