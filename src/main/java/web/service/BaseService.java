package web.service;

import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseService<T, ID> {

    protected abstract JpaRepository<T, ID> getRepository();
    protected abstract String getEntityName();

    protected T findOrThrow(ID id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new RuntimeException(getEntityName() + " not found: " + id));
    }
}