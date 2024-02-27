package it.pagopa.selfcare.pagopa.ingestion.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.pagopa.selfcare.pagopa.ingestion.core.DelegationService;
import it.pagopa.selfcare.pagopa.ingestion.core.ECService;
import it.pagopa.selfcare.pagopa.ingestion.core.PTService;
import it.pagopa.selfcare.pagopa.ingestion.core.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/swagger-resources/ingestion", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "ingestion")
public class PdaIngestionController {

    private final ECService ecService;
    private final PTService ptService;
    private final DelegationService delegationService;
    private final UserService userService;
    private static final String COMPLETE = "Richiesta presa in carico";

    public PdaIngestionController(
            ECService ecService,
            PTService ptService,
            DelegationService delegationService,
            UserService userService) {
        this.ecService = ecService;
        this.ptService = ptService;
        this.delegationService = delegationService;
        this.userService = userService;
    }

    @PostMapping("/persist/relationship")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.injection.api.persistFromCsv}")
    public ResponseEntity<String> persistRelationshipFromCsv(@RequestParam(value = "batchId", required = false) String batchId) {
        delegationService.persistECPTRelationship(batchId);
        return ResponseEntity.ok().body(COMPLETE);
    }

    @PostMapping("/persist/ec")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.injection.api.persistFromCsv}")
    public ResponseEntity<String> persistEcFromCsv(@RequestParam(value = "batchId", required = false) String batchId) {
        ecService.persistEC(batchId);
        return ResponseEntity.ok().body(COMPLETE);
    }

    @PostMapping("/persist/pt")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.injection.api.persistFromCsv}")
    public ResponseEntity<String> persistPtFromCsv() {
        ptService.persistPT();
        return ResponseEntity.ok().body(COMPLETE);
    }

    @PostMapping("/persist/user")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.injection.api.persistFromCsv}")
    public ResponseEntity<String> persistUserFromCsv(@RequestParam(value = "batchId", required = false) String batchId) {
        userService.persistUser(batchId);
        return ResponseEntity.ok().body(COMPLETE);
    }

    @PostMapping("/persist/ec-adesione")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.injection.api.persistFromCsv}")
    public ResponseEntity<String> persistEcAdesioneFromCsv(@RequestParam(value = "batchId", required = false) String batchId) {
        ecService.persistECAdesione(batchId);
        return ResponseEntity.ok().body(COMPLETE);
    }

    @PostMapping("/ec")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.injection.api.ec}")
    public ResponseEntity<String> onboardingEc(@RequestParam("status") String status) {
        ecService.migrateEC(status);
        return ResponseEntity.ok().body(COMPLETE);
    }

    @PostMapping("/pt")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.injection.api.pt}")
    public ResponseEntity<String> onboardingPt(@RequestParam("status") String status) {
        ptService.migratePT(status);
        return ResponseEntity.ok().body(COMPLETE);
    }
    @PostMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.injection.api.pt}")
    public ResponseEntity<String> onboardingUsers(@RequestParam("status") String status) {
        userService.migrateUser(status);
        return ResponseEntity.ok().body(COMPLETE);
    }


    @PostMapping("/delegation")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.injection.api.delegation}")
    public ResponseEntity<String> createDelegation(@RequestParam("status") String status) {
        delegationService.migrateECPTRelationship(status);
        return ResponseEntity.ok().body(COMPLETE);
    }

    @PostMapping("/ec-adesione")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.injection.api.ec}")
    public ResponseEntity<String> onboardingEcAdesione(@RequestParam("status") String status) {
        ecService.migrateECAdesione(status);
        return ResponseEntity.ok().body(COMPLETE);
    }
}
