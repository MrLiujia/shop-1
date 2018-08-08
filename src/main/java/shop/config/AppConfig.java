package shop.config;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.cache.jcache.JCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.fasterxml.jackson.databind.ObjectMapper;


@Configuration
@ComponentScan("shop")
@EnableWebMvc 
@PropertySource({"classpath:jdbc.properties", "classpath:alipay.properties"})
@MapperScan("shop.mapper")
@EnableTransactionManagement
@EnableScheduling // 开启调度支持
@EnableAsync // 开启异步执行任务支持
@EnableCaching // 开启缓存支持
public class AppConfig extends WebMvcConfigurerAdapter {
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/jsp/", ".jsp");
    }
    
    @Bean
    public DataSource dataSource(Environment env) { 
        DriverManagerDataSource ds = new DriverManagerDataSource(
                env.getProperty("jdbc.url"),
                env.getProperty("jdbc.username"),
                env.getProperty("jdbc.password"));
        ds.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        return ds;
    }   
    
    @Bean 
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean sf = new SqlSessionFactoryBean();
        sf.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        sf.setDataSource(dataSource);
        return sf;
    }
    
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }    
    
    @Bean
    public AlipayClient alipayClient(Environment env) throws IOException {
        return new DefaultAlipayClient(
                "https://openapi.alipay.com/gateway.do",
                env.getProperty("alipay.appId"),
                FileUtils.readFileToString(new File(env.getProperty("alipay.appPrivateKeyFile")), "UTF-8"),
                "json",
                "UTF-8",
                FileUtils.readFileToString(new File(env.getProperty("alipay.appPrivateKeyFile")), "UTF-8"),
                env.getProperty("alipay.signType")
                );
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    
    @Bean // 简化了http请求的发送和响应的处理
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public JCacheCacheManager cacheManager() throws Exception {
        return new JCacheCacheManager(jCacheManager());
    }

    @Bean
    public CacheManager jCacheManager() throws Exception {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager manager = cachingProvider.getCacheManager( 
            getClass().getResource("/ehcache.xml").toURI(), // ehcache配置文件
            getClass().getClassLoader()
            ); 
        return manager;
    }
}