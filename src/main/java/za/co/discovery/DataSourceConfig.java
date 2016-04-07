package za.co.discovery;

import org.apache.derby.jdbc.BasicEmbeddedDataSource40;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        BasicEmbeddedDataSource40 dataSource = new BasicEmbeddedDataSource40();
        dataSource.setConnectionAttributes("create=true");
        dataSource.setDatabaseName("dbDiscoveryAssignment");
        return dataSource;
    }

    @Bean
    @Qualifier("hibernateProperties")
    public Properties psqlHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.DerbyTenSevenDialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "create");

        return properties;
    }

}
