package edu.school21.server.repositories;

import edu.school21.server.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component
public class UsersRepositoryImpl implements UsersRepository<User>{
    JdbcTemplate jdbcTemplate;

    @Autowired
    public UsersRepositoryImpl(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM users WHERE identifier = " + id,
                (resultSet, i) -> new User(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3))));
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users",
                (resultSet, i) -> new User(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3)));
    }

    @Override
    public void save(User entity) {
        entity.setIdentifier(jdbcTemplate.queryForObject
                ("INSERT INTO users(username, password) VALUES ('" + entity.getUsername() + "', '" + entity.getPassword() + "') RETURNING identifier"
                        , (resultSet, i) -> resultSet.getLong(1)));
    }

    @Override
    public void update(User entity) {
        jdbcTemplate.update("UPDATE users SET username = ?, password = ? WHERE identifier = ?",
                entity.getUsername(), entity.getPassword(), entity.getIdentifier());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE identifier = ?", id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM users WHERE username = '" + username + "'",
                    (resultSet, i) -> new User(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3))));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }
}
