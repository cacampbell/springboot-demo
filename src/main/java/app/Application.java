package app;

import app.db.HcsEntityDao;
import app.domain.Entity;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.annotation.EnableNeo4jAuditing;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;
import java.util.HashSet;


@EnableTransactionManagement
@EnableNeo4jRepositories
@EnableNeo4jAuditing
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.run(args);
    }

    @Bean // Bean Constructors are Autowired
    CommandLineRunner demo(HcsEntityDao dao) {
        return args -> {
            dao.deleteAll();
            Entity entity1 = new Entity();
            Entity entity2 = new Entity();
            Entity entity3 = new Entity();
            Entity entity4 = new Entity();
            Entity entity5 = new Entity();
            entity2.setChildren(new HashSet<>(Arrays.asList(entity1, entity4, entity5)));
            entity3.setChildren(new HashSet<>(Arrays.asList(entity5, entity2)));
            Entity[] entities = new Entity[]{ entity1, entity2, entity3, entity4, entity5 };
            for (Entity e : entities) {
                e.setName(RandomStringUtils.randomAlphabetic(12));
                dao.save(e); 
            }
        };
    }
}
