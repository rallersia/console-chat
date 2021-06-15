package edu.school21.server.confing;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan("edu.school21.server")
@PropertySource("classpath:db.properties")
public class SocketsApplicationConfig {
    @Value("${db.driver.name}")
    String dbDriverClassName;
    @Value("${db.url}")
    String dbUrl;
    @Value("${db.user}")
    String dbUsername;
    @Value("${db.password}")
    String dbPassword;

    @Bean
    public HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(dbDriverClassName);
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
        return (config);
    }

    @Bean
    @Autowired
    public HikariDataSource hikariDataSource(HikariConfig config) {
        return new HikariDataSource(config);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
