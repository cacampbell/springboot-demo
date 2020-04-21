package app.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.sdk.TransactionReceipt;

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

    private <S extends Entity> S annotate(S entity) {
        try {
            TransactionReceipt receipt = hcs.postSync(entity);
            entity.setTopic(receipt.getConsensusTopicId());
            entity.setSequenceNumber(receipt.getConsensusTopicSequenceNumber());
            entity.setRunningHash(receipt.getConsensusTopicRunningHash());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        return entity;
    }

    @Override
    public <S extends Entity> S save(S entity) {
        entity = this.annotate(entity);
        return categoryDao.save(entity);
    }

    @Override
    public <S extends Entity> Iterable<S> saveAll(Iterable<S> entities) {
        ArrayList<S> annotatedEntities = new ArrayList<S>();
        for (S entity : entities) {
            annotatedEntities.add(this.annotate(entity));
        }
        
        return categoryDao.saveAll(annotatedEntities);
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