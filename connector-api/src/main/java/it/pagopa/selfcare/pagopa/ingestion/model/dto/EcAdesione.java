package it.pagopa.selfcare.pagopa.ingestion.model.dto;

import it.pagopa.selfcare.pagopa.ingestion.constant.WorkStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EcAdesione {
    private String id;
    private String taxCode;
    private String businessName;
    private String dateOfJoining;
    private String institutionId;
    private WorkStatus workStatus;
    private Integer httpStatus;
    private String httpMessage;
    private String batchId;
}
