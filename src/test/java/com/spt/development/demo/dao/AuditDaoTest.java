package com.spt.development.demo.dao;

import com.spt.development.audit.spring.AuditEvent;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.keyvalue.DefaultMapEntry;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.util.ReflectionTestUtils;

import javax.sql.DataSource;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class AuditDaoTest {
    private static class TestData {
        static final String TYPE = "test-type";
        static final String SUB_TYPE = "TEST_SUB_TYPE";
        static final String CORRELATION_ID = "06696e2a-c587-475f-a6ad-cbe66094f7d5";
        static final String ID = "1";
        static final String DETAILS = "Test details";
        static final String USER_ID = "123";
        static final String USERNAME = "tester";
        static final String ORIGINATING_IP = InetAddress.getLoopbackAddress().getHostAddress();
        static final String SERVICE_ID = "Test service ID";
        static final String SERVICE_VERSION = "v1.0.0";
        static final String SERVICE_HOST_NAME = "localhost";
        static final OffsetDateTime CREATED = OffsetDateTime.of(2022, 10, 22, 14, 26, 12, 0, ZoneOffset.UTC);
    }

    @Test
    void setJdbcTemplate_nullTemplateWhenSimpleJdbcInsertNotSet_shouldThrowException() {
        final AuditDaoArgs args = new AuditDaoArgs();
        args.simpleJdbcInsert = null;

        final AuditDao target = createDao(args);

        assertThatThrownBy(() -> target.setJdbcTemplate(null))
            .isExactlyInstanceOf(IllegalStateException.class)
            .hasMessage("JDBC Template must be set");
    }

    @Test
    void create_validAuditEvent_shouldConvertToInsert() {
        final AuditDaoArgs args = new AuditDaoArgs();

        createDao(args).create(
            AuditEvent.builder()
                      .type(TestData.TYPE)
                      .subType(TestData.SUB_TYPE)
                      .correlationId(TestData.CORRELATION_ID)
                      .id(TestData.ID)
                      .details(TestData.DETAILS)
                      .userId(TestData.USER_ID)
                      .username(TestData.USERNAME)
                      .originatingIP(TestData.ORIGINATING_IP)
                      .serviceId(TestData.SERVICE_ID)
                      .serviceVersion(TestData.SERVICE_VERSION)
                      .serverHostName(TestData.SERVICE_HOST_NAME)
                      .created(TestData.CREATED)
                      .build()
        );

        verify(args.simpleJdbcInsert, times(1)).execute(
            MapUtils.putAll(new HashMap<>(), new Object[]{
                new DefaultMapEntry<>("type", TestData.TYPE),
                new DefaultMapEntry<>("sub_type", TestData.SUB_TYPE),
                new DefaultMapEntry<>("correlation_id", TestData.CORRELATION_ID),
                new DefaultMapEntry<>("id", TestData.ID),
                new DefaultMapEntry<>("details", TestData.DETAILS),
                new DefaultMapEntry<>("user_id", TestData.USER_ID),
                new DefaultMapEntry<>("username", TestData.USERNAME),
                new DefaultMapEntry<>("originating_ip", TestData.ORIGINATING_IP),
                new DefaultMapEntry<>("service_id", TestData.SERVICE_ID),
                new DefaultMapEntry<>("service_version", TestData.SERVICE_VERSION),
                new DefaultMapEntry<>("server_host_name", TestData.SERVICE_HOST_NAME),
                new DefaultMapEntry<>("created", new Timestamp(TestData.CREATED.toInstant().toEpochMilli()))
            })
        );
    }

    private AuditDao createDao(AuditDaoArgs args) {
        final AuditDao dao = new AuditDao(args.dataSource);
        dao.init();

        ReflectionTestUtils.setField(dao, "simpleJdbcInsert", args.simpleJdbcInsert);

        return dao;
    }

    private static class AuditDaoArgs {
        DataSource dataSource = Mockito.mock(DataSource.class);
        SimpleJdbcInsert simpleJdbcInsert = Mockito.mock(SimpleJdbcInsert.class);
    }
}
