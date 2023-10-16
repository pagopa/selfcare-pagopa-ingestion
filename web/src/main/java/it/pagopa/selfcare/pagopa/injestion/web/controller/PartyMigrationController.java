package it.pagopa.selfcare.pagopa.injestion.web.controller;

import io.swagger.annotations.Api;
import it.pagopa.selfcare.pagopa.injestion.core.MigrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<String> migrateFiles(
            @RequestParam("ecIntermediarioPT") String ecIntermediarioPTpath,
            @RequestParam("ec") String ecPath,
            @RequestParam("pt") String ptPath,
            @RequestParam("user") String userPath
    ) {
        try {

            migrationService.migrateECIntermediarioPTs(ecIntermediarioPTpath);
            migrationService.migrateECs(ecPath);
            migrationService.migratePTs(ptPath);
            migrationService.migrateUsers(userPath);

            return new ResponseEntity<>("Elaborazione completata con successo", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'elaborazione dei file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
