package shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
          .authorizeRequests()
            .antMatchers("/uc/**").authenticated() // user center
            .antMatchers("/**").permitAll()
          
          .and()
          
          .formLogin()
            .loginPage("/login")
          
          .and()
          
          .logout()
            .logoutSuccessUrl("/")
            
          .and()
            
          .rememberMe()
            .tokenValiditySeconds(7 * 24 * 3600)
            .key("test123456") // 避免开发中需要反复登录，上线时注释掉，默认为随机数会导致服务器重启之后重启前的记住我cookie失效
            .userDetailsService(userDetailsService)
            
          .and()
          
          .csrf()
            .ignoringAntMatchers("/async-pay-cb");
    }
}
