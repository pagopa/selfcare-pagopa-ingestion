package it.pagopa.selfcare.pagopa.injestion.web.controller;

import io.swagger.annotations.Api;
import it.pagopa.selfcare.pagopa.injestion.core.MigrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/migration")
@Api(tags = "migration")
public class PartyMigrationController {

    private final MigrationService migrationService;

    public PartyMigrationController(MigrationService migrationService) {
        this.migrationService = migrationService;
    }

    @PostMapping("/migrateFiles")
    public ResponseEntity<String> migrateFiles() {
        try {

            migrationService.migrateECIntermediarioPTs();
            migrationService.migrateECs();
            migrationService.migratePTs();
            migrationService.migrateUsers();

            return new ResponseEntity<>("Elaborazione completata con successo", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'elaborazione dei file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
