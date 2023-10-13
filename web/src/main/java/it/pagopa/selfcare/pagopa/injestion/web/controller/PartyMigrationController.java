package it.pagopa.selfcare.pagopa.injestion.web.controller;

import io.swagger.annotations.Api;
import it.pagopa.selfcare.pagopa.injestion.core.MigrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/ECIntermediarioPTs")
    public void migrateECIntermediarioPTs() {
        migrationService.migrateECIntermediarioPTs();
    }

    @GetMapping("/ECs")
    public void migrateECs() {
        migrationService.migrateECs();
    }

    @GetMapping("/PTs")
    public void migratePTs() {
        migrationService.migratePTs();
    }

    @GetMapping("/Users")
    public void migrateUsers() {
        migrationService.migrateUsers();
    }
}
