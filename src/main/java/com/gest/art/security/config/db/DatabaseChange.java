package com.gest.art.security.config.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
// Class permettant de switcher entre les bases de données postgres et oracle
@Service
public class DatabaseChange {
    public void getConnexion(String databaseName) {
        // Selon le nom base de donnée fourni une connexion sera créée sur la base
        switch (databaseName) {
            case ClientDB.POSTGRE:
                DBContextHolder.setCurrentDb(ClientDB.POSTGRE);
                log.info("MA BASE COURANTE: {}", ClientDB.POSTGRE);
                break;
            case ClientDB.ORACLE:
                DBContextHolder.setCurrentDb(ClientDB.ORACLE);
                log.info("MA BASE COURANT: {}", ClientDB.ORACLE);
                break;
            case ClientDB.MYSQL:
                DBContextHolder.setCurrentDb(ClientDB.MYSQL);
                log.info("MA BASE COURANT est : {}", ClientDB.MYSQL);
                break;
        }
    }
}
