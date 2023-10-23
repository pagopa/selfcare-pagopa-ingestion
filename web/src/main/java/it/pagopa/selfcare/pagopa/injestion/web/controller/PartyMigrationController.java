package it.pagopa.selfcare.pagopa.injestion.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.pagopa.selfcare.pagopa.injestion.core.DelegationService;
import it.pagopa.selfcare.pagopa.injestion.core.ECService;
import it.pagopa.selfcare.pagopa.injestion.core.PTService;
import it.pagopa.selfcare.pagopa.injestion.core.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/injestion", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "injestion")
public class PartyMigrationController {

    private final ECService ecService;
    private final PTService ptService;
    private final DelegationService delegationService;
    private final UserService userService;
    private static final String COMPLETE = "Elaborazione completata con successo";

    public PartyMigrationController(
            ECService ecService,
            PTService ptService,
            DelegationService delegationService,
            UserService userService) {
        this.ecService = ecService;
        this.ptService = ptService;
        this.delegationService = delegationService;
        this.userService = userService;
    }

    @PostMapping("/persist")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.injestion.api.persistFromCsv}")
    public ResponseEntity<String> persistFromCsv() {
        ecService.persistEC();
        ptService.persistPT();
        delegationService.persistECPTRelationship();
        userService.persistUser();
        return ResponseEntity.ok().body(COMPLETE);
    }

    @PostMapping("/ec")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.injestion.api.ec}")
    public ResponseEntity<String> migrationEC(@RequestParam("status") String status) {
        ecService.migrateEC(status);
        return ResponseEntity.ok().body(COMPLETE);
    }

    @PostMapping("/pt")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.injestion.api.pt}")
    public ResponseEntity<String> migrationPT(@RequestParam("status") String status) {
        ptService.migratePT(status);
        return ResponseEntity.ok().body(COMPLETE);
    }

    @PostMapping("/delegation")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.injestion.api.delegation}")
    public ResponseEntity<String> createDelegation() {
        delegationService.migrateECPTRelationship();
        return ResponseEntity.ok().body(COMPLETE);
    }


}
