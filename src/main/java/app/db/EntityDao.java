package app.db;

import app.domain.Entity;
import org.springframework.data.repository.CrudRepository;

public interface EntityDao extends CrudRepository<Entity, Long>{}