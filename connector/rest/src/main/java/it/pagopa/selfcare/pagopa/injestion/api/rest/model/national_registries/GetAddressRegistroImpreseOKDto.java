package it.pagopa.selfcare.pagopa.injestion.api.rest.model.national_registries;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class GetAddressRegistroImpreseOKDto {

    @JsonProperty("dateTimeExtraction")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date dateTimeExtraction;

    @JsonProperty("taxId")
    private String taxId;

    @JsonProperty("professionalAddress")
    private ProfessionalAddressDto professionalAddress;

}