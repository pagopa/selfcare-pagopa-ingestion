package it.pagopa.selfcare.pagopa.injestion.web.controller;

import io.swagger.annotations.Api;
import it.pagopa.selfcare.pagopa.injestion.core.MigrationECPTRelationshipService;
import it.pagopa.selfcare.pagopa.injestion.core.MigrationECService;
import it.pagopa.selfcare.pagopa.injestion.core.MigrationPTService;
import it.pagopa.selfcare.pagopa.injestion.core.MigrationUserService;
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

    private final MigrationECService migrationECService;
    private final MigrationPTService migrationPTService;
    private final MigrationECPTRelationshipService migrationECPTRelationshipService;
    private final MigrationUserService migrationUserService;
    
    private static final String COMPLETE = "Elaborazione completata con successo";

    public PartyMigrationController(
            MigrationECService migrationECService,
            MigrationPTService migrationPTService,
            MigrationECPTRelationshipService migrationECPTRelationshipService,
            MigrationUserService migrationUserService) {
        this.migrationECService = migrationECService;
        this.migrationPTService = migrationPTService;
        this.migrationECPTRelationshipService = migrationECPTRelationshipService;
        this.migrationUserService = migrationUserService;
    }

    @PostMapping("/")
    public ResponseEntity<String> migration() {
        try {

            migrationECService.persistEC();
            migrationPTService.persistPT();
            migrationECPTRelationshipService.persistECPTRelationship();
            migrationUserService.persistUser();

            return new ResponseEntity<>(COMPLETE, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'elaborazione dei file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/EC")
    public ResponseEntity<String> migrationEC() {
        try {

            migrationECService.migrateEC();

            return new ResponseEntity<>(COMPLETE, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante la migrazione EC", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/EC/continue")
    public ResponseEntity<String> continueMigrationEC() {
        try {

            migrationECService.autoComplete();

            return new ResponseEntity<>(COMPLETE, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante la migrazione EC", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/PT")
    public ResponseEntity<String> migrationPT() {
        try {

            migrationPTService.migratePT();

            return new ResponseEntity<>(COMPLETE, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante la migrazione PT", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/PT/continue")
    public ResponseEntity<String> continueMigrationPT() {
        try {

            migrationPTService.autoComplete();

            return new ResponseEntity<>(COMPLETE, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante la migrazione PT", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
