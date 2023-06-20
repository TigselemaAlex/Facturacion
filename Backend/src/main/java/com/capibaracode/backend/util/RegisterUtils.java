package com.capibaracode.backend.util;

import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class RegisterUtils {

    private final JdbcTemplate jdbcTemplate;

    public RegisterUtils(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public  String generatePassword(){
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        return IntStream.range(0, 10)
                .map(i -> random.nextInt(chars.length()))
                .mapToObj(randomIndex -> String.valueOf(chars.charAt(randomIndex)))
                .collect(Collectors.joining());
    }

    public String bodyMessage(String password){
        return MessageFormat.format("Tu contraseña es: <strong>{0}</strong>, recuerda cambiarla una vez inicies sesion.", password);
    }

    public void createSchema(String schemaName){
        Flyway flyway = Flyway.configure()
                .baselineOnMigrate(true)
                .dataSource(jdbcTemplate.getDataSource())
                .schemas(schemaName)
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
    }
}
