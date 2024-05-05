package com.spt.development.demo.infrastructure.adapter.dao;

import com.spt.development.audit.spring.AuditEvent;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.keyvalue.DefaultMapEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuditDao  extends JdbcDaoSupport {
    private static final String SCHEMA = "audit";
    private static final String TABLE = "event";

    private final DataSource dataSource;

    private SimpleJdbcInsert simpleJdbcInsert = null;

    public AuditDao(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void init() {
        setDataSource(dataSource);
    }

    @Override
    protected void initTemplateConfig() {
        if (simpleJdbcInsert == null) {
            final JdbcTemplate jdbcTemplate = getJdbcTemplate();

            if (jdbcTemplate == null) {
                throw new IllegalStateException("JDBC Template must be set");
            }

            simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName(SCHEMA)
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("event_id");
        }
    }

    public void create(@NonNull AuditEvent auditEvent) {
        final Map<String, Object> args = MapUtils.putAll(new HashMap<>(), new Object[]{
            new DefaultMapEntry<>("type", auditEvent.getType()),
            new DefaultMapEntry<>("sub_type", auditEvent.getSubType()),
            new DefaultMapEntry<>("correlation_id", auditEvent.getCorrelationId()),
            new DefaultMapEntry<>("service_id", auditEvent.getServiceId()),
            new DefaultMapEntry<>("service_version", auditEvent.getServiceVersion()),
            new DefaultMapEntry<>("server_host_name", auditEvent.getServerHostName()),
            new DefaultMapEntry<>("created", new Timestamp(auditEvent.getCreated().toInstant().toEpochMilli()))
        });

        if (auditEvent.getId() != null) {
            args.put("id", auditEvent.getId());
        }

        if (auditEvent.getDetails() != null) {
            args.put("details", auditEvent.getDetails());
        }

        if (auditEvent.getUserId() != null) {
            args.put("user_id", auditEvent.getUserId());
        }

        if (auditEvent.getUsername() != null) {
            args.put("username", auditEvent.getUsername());
        }

        if (auditEvent.getOriginatingIP() != null) {
            args.put("originating_ip", auditEvent.getOriginatingIP());
        }
        simpleJdbcInsert.execute(Collections.unmodifiableMap(args));
    }
}
