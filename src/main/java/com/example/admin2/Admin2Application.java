package com.example.admin2;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableAdminServer
public class Admin2Application
{

    public static void main(String[] args) {
        SpringApplication.run(Admin2Application.class, args);
    }

    @Bean
    public WeChatNews news() {
        return new WeChatNews();
    }

    @Bean
    public WeChatNotifier customNotifier(InstanceRepository repository, WeChatNews news) {
        return new WeChatNotifier(repository, news);
    }


    @Configuration
    public static class SecurityConfig extends WebSecurityConfigurerAdapter
    {
        @Override
        protected void configure(HttpSecurity http) throws Exception {

            // Page with login form is served as /login.html and does a POST on /login
            //http.formLogin().loginPage("/login.html").loginProcessingUrl("/login").permitAll();
            //// The UI does a POST on /logout on logout
            //http.logout().logoutUrl("/logout");
            //// The ui currently doesn't support csrf
            //http.csrf().disable();
            //http.csrf()
            //        .ignoringAntMatchers("/**");
            //// Requests for the login page and the static assets are allowed
            //http.authorizeRequests()
            //        .antMatchers("/login.html", "/**/*.css", "/img/**", "/third-party/**")
            //        .permitAll();
            //// ... and any other request needs to be authorized
            //http.authorizeRequests().antMatchers("/**").authenticated();
            //
            //// Enable so that the clients can authenticate via HTTP basic for registering
            //http.httpBasic();
        }
    }

}