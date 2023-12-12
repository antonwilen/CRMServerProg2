package com.yrgo.dataaccess;

import com.yrgo.domain.Action;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
@Repository
public class ActionDaoJpaImpl implements ActionDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void create(Action newAction) {
        entityManager.persist(newAction);
    }

    @Override
    public List<Action> getIncompleteActions(String userId) {
        return entityManager.createQuery("SELECT a FROM ACTION a WHERE a.owningUser= :userId AND a.complete = false", Action.class).setParameter("userId",userId).getResultList();
    }

    @Override
    public void update(Action actionToUpdate) throws RecordNotFoundException {
        entityManager.merge(actionToUpdate);
    }

    @Override
    public void delete(Action oldAction) throws RecordNotFoundException {
        Action action = entityManager.find(Action.class, oldAction.getActionId());
        entityManager.remove(action);
    }
}
