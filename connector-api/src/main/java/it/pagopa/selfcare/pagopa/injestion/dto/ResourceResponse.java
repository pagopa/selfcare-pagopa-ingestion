package it.pagopa.selfcare.pagopa.injestion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResourceResponse {

    private byte[] data;
    private String fileName;
    private String mimetype;
}
