package exerciseCRUD.DAO;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserDao {

    public NamedParameterJdbcTemplate namedJdbcTemplatePostgresSQL;

    public UserDao (@Qualifier("namedParameterJdbcTemplateStudentPostgresSQL")
                       NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplatePostgresSQL = namedJdbcTemplate;
    }
    //------------------------------------------------------------

    public boolean queryDML(SqlDdlEnum sqlEnum, Map<String, String> mapParameters) {
        return Optional.ofNullable(sqlEnum.getQuerySQL())
                .map(sqlRun -> {
                    int countRow = namedJdbcTemplatePostgresSQL.update(sqlEnum.getQuerySQL(), mapParameters);
                    System.out.printf("### Action: %s | records: %s ###%n", sqlEnum.getNote(), countRow);
                    return true;
                })
                .orElse(false);
    }

    public  <T> List<T> getRecords(Class<T> clazz, Map<String, String> mapParameters) {
        List<T> setRows = namedJdbcTemplatePostgresSQL
                .query(SqlDdlEnum.selectUsers.getQuerySQL(),
                        mapParameters, new BeanPropertyRowMapper<>(clazz));
        return Optional.of(setRows)
                .orElse(Collections.emptyList());
    }
}
