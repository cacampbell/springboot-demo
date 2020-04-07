package app.db;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import app.domain.Entity;
import app.service.HederaConsensusService;

@Repository
@RepositoryRestResource(collectionResourceRel = "entities", path = "entities")
public class HcsEntityDao implements EntityDao {

    @Autowired
    private EntityDao categoryDao;

    @Autowired
    private HederaConsensusService hcs;

    @Override
    public <S extends Entity> S save(S entity) {
        try {
            hcs.postAsync(entity);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        return categoryDao.save(entity);
    }

    @Override
    public <S extends Entity> Iterable<S> saveAll(Iterable<S> entities) {
        return categoryDao.saveAll(entities);
    }

    @Override
    public Optional<Entity> findById(Long id) {
        return categoryDao.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return categoryDao.existsById(id);
    }

    @Override
    public Iterable<Entity> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public Iterable<Entity> findAllById(Iterable<Long> ids) {
        return categoryDao.findAllById(ids);
    }

    @Override
    public long count() {
        return categoryDao.count();
    }

    @Override
    public void deleteById(Long id) {
        categoryDao.deleteById(id);
    }

    @Override
    public void delete(Entity entity) {
        categoryDao.delete(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends Entity> entities) {
        categoryDao.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        categoryDao.deleteAll();
    }
}