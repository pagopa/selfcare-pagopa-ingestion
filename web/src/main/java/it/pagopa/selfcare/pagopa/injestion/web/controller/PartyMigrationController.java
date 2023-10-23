package it.pagopa.selfcare.pagopa.injestion.web.controller;

import io.swagger.annotations.Api;
import it.pagopa.selfcare.pagopa.injestion.core.MigrationECPTRelationshipService;
import it.pagopa.selfcare.pagopa.injestion.core.MigrationECService;
import it.pagopa.selfcare.pagopa.injestion.core.MigrationPTService;
import it.pagopa.selfcare.pagopa.injestion.core.MigrationUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("swagger-resources/migration")
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

    @PostMapping("/persist")
    public ResponseEntity<String> persistFromCsv() {
        migrationECService.persistEC();
        migrationPTService.persistPT();
        migrationECPTRelationshipService.persistECPTRelationship();
        migrationUserService.persistUser();
        return ResponseEntity.ok().body(COMPLETE);
    }

    @PostMapping("/EC")
    public ResponseEntity<String> migrationEC(@RequestParam("status") String status) {
        migrationECService.migrateEC(status);
        return ResponseEntity.ok().body(COMPLETE);
    }

    @PostMapping("/PT")
    public ResponseEntity<String> migrationPT(@RequestParam("status") String status) {
            migrationPTService.migratePT(status);
        return ResponseEntity.ok().body(COMPLETE);
    }

    @PostMapping("/delegation")
    public ResponseEntity<String> createDelegation() {
        migrationECPTRelationshipService.migrateECPTRelationship();
        return ResponseEntity.ok().body(COMPLETE);
    }


}
