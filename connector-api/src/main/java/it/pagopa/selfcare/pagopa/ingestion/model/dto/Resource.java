package it.pagopa.selfcare.pagopa.ingestion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Resource {

    private byte[] data;
    private String fileName;
    private String mimetype;
}