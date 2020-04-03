package app.db;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import app.domain.Category;
import app.service.HederaConsensusService;

public class HcsCategoryDao implements CategoryDao {

    @Autowired
    @Qualifier("categoryDao")
    private CategoryDao categoryDao;

    @Autowired
    private HederaConsensusService hcs;

    @Override
    public <S extends Category> S save(S entity) {
        hcs.postAsync(entity);
        return categoryDao.save(entity);
    }

    @Override
    public <S extends Category> Iterable<S> saveAll(Iterable<S> entities) {
        return categoryDao.saveAll(entities);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryDao.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return categoryDao.existsById(id);
    }

    @Override
    public Iterable<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public Iterable<Category> findAllById(Iterable<Long> ids) {
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
    public void delete(Category entity) {
        categoryDao.delete(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends Category> entities) {
        categoryDao.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        categoryDao.deleteAll();
    }
}