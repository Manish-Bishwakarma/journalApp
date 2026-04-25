package net.engineeringdigest.journalApp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement // This tells Spring to find and manage transactions in the application. It allows you to use the @Transactional annotation on methods to indicate that they should be executed within a transaction.
public class JournalApplication {

    public static void main(String[] args) {
        SpringApplication.run(JournalApplication.class, args);
    }

    // We have to define a bean that tells Spring the interface PlatformTransactionManager to use for managing transactions. In this case, we're using MongoTransactionManager because we're working with MongoDB.
    @Bean
    public PlatformTransactionManager transactionManager(MongoDatabaseFactory dbFactory) { // MongoDatabaseFactory helps to create a connection to the MongoDB database and provides access to the database for performing operations. It is used by the MongoTransactionManager to manage transactions in MongoDB.
        return new MongoTransactionManager(dbFactory);
    }
}