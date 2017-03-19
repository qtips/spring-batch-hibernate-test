package hello.batchconfig;

import hello.Person;
import org.h2.tools.Server;
import org.hibernate.SessionFactory;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
//import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
public class ResourceConfiguration {

    @Autowired
    private DataSource dataSource;

    //@Bean
    public HibernateCursorItemReader<Person> hibernateCursorItemReader(SessionFactory sessionFactory) {
        HibernateCursorItemReader<Person> cursorItemReader = new HibernateCursorItemReader<>();
        cursorItemReader.setSessionFactory(sessionFactory);
        return cursorItemReader;
    }

   // @Bean
//    public LocalSessionFactoryBean sessionFactory() {
//        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
//        sessionFactoryBean.setDataSource(dataSource);
//        Properties p = new Properties();
//        p.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//        sessionFactoryBean.setHibernateProperties(p);
//        return sessionFactoryBean;
//    }

    // Makes it possible to connect to H2 database after batch exits
    @Bean(destroyMethod = "stop")
    @DependsOn("h2WebServer")
    public Server h2Server() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();
    }

    @Bean(destroyMethod = "stop")
    public Server h2WebServer() throws SQLException {
        return Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
    }

}
