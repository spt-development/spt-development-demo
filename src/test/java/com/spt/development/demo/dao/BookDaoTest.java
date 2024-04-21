package com.spt.development.demo.dao;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.util.ReflectionTestUtils;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookDaoTest {

    @Test
    void setJdbcTemplate_nullTemplateWhenSimpleJdbcInsertNotSet_shouldThrowException() {
        final BookDaoArgs args = new BookDaoArgs();
        args.simpleJdbcInsert = null;

        final BookDao target = createDao(args);

        assertThatThrownBy(() -> target.setJdbcTemplate(null))
            .isExactlyInstanceOf(IllegalStateException.class)
            .hasMessage("JDBC Template must be set");
    }

    private BookDao createDao(BookDaoArgs args) {
        final BookDao dao = new BookDao(args.dataSource);
        dao.init();

        ReflectionTestUtils.setField(dao, "simpleJdbcInsert", args.simpleJdbcInsert);

        return dao;
    }

    private static final class BookDaoArgs {
        DataSource dataSource = Mockito.mock(DataSource.class);
        SimpleJdbcInsert simpleJdbcInsert = Mockito.mock(SimpleJdbcInsert.class);
    }
}
