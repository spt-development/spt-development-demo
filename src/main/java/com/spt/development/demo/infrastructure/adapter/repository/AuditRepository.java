package com.spt.development.demo.infrastructure.adapter.repository;

import com.spt.development.audit.spring.AuditEvent;
import com.spt.development.demo.infrastructure.adapter.dao.AuditDao;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import javax.annotation.concurrent.Immutable;

@Immutable
@Repository
@AllArgsConstructor
public class AuditRepository {
    // NOTE. Having a repository that delegates to a DAO for this simple model is clearly overkill, but just serves to prove logging
    // for repositories and DAOs.
    private final AuditDao auditDao;

    public void create(@NonNull AuditEvent auditEvent) {
        auditDao.create(auditEvent);
    }
}
