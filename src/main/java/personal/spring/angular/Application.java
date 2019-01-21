package personal.spring.angular;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import personal.spring.angular.restful.RestfulAPIConfig;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

  @Configuration
  @Order(SecurityProperties.BASIC_AUTH_ORDER)
  protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
        .httpBasic()
        .and()
        .authorizeRequests()
        .antMatchers("/index.html", "/", "/home", "*.js", "/*.js", "/login").permitAll()
        .anyRequest().authenticated()
        .and().csrf()
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
  }

  @Bean
  public ServletRegistrationBean foo() {
    DispatcherServlet dispatcherServlet = new DispatcherServlet();
    AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
    applicationContext.register(RestfulAPIConfig.class);
    dispatcherServlet.setApplicationContext(applicationContext);
    ServletRegistrationBean servletRegistrationBean =
      new ServletRegistrationBean<>(dispatcherServlet, "/path/*");
    servletRegistrationBean.setName("path");
    return servletRegistrationBean;
  }
}



