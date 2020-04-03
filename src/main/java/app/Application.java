package app;

import app.db.HcsCategoryDao;
import app.domain.Category;
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

    @Bean
    CommandLineRunner demo(HcsCategoryDao dao) {
        return args -> {
            // Setup Listener for Events on HCS here
            System.out.println("\n\n\n");
            System.out.println("Deleting All Nodes");
            dao.deleteAll();
            System.out.println("All Nodes: " + dao.findAll());
            Category category1 = new Category();
            Category category2 = new Category();
            Category category3 = new Category();
            Category category4 = new Category();
            Category category5 = new Category();
            Category[] entities = new Category[]{ category1, category2, category3, category4, category5 };
            System.out.println("Saving");
            for (Category e : entities) { dao.save(e); }
            System.out.println("All Nodes: " + dao.findAll());
            System.out.println("Assigning Children");
            category2.setChildren(new HashSet<>(Arrays.asList(category1, category4, category5)));
            category3.setChildren(new HashSet<>(Arrays.asList(category5, category2)));
            System.out.println("Saving");
            for (Category e : entities) { dao.save(e); }
            System.out.println("All Nodes: " + dao.findAll());
            System.out.println("\n\n\n");
            // System.exit(0); // Shut down after adding entities
        };
    }
}
