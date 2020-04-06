package app.db;

import app.domain.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryDao extends CrudRepository<Category, Long>{}