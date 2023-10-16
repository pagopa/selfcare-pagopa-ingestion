package it.pagopa.selfcare.pagopa.injestion.api.client;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobProperties;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import it.pagopa.selfcare.pagopa.injestion.api.azure.AzureConnector;
import it.pagopa.selfcare.pagopa.injestion.exception.ResourceNotFoundException;
import it.pagopa.selfcare.pagopa.injestion.exception.SelfCarePagoPaInjectionException;
import it.pagopa.selfcare.pagopa.injestion.model.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

@Slf4j
@Component
@PropertySource("classpath:config/azure-storage-config.properties")
//@Profile("AzureStorage")
public class AzureBlobClient implements AzureConnector {

    private static final String ERROR_DURING_DOWNLOAD_FILE_MESSAGE = "Error during download file %s";
    private static final String ERROR_DURING_DOWNLOAD_FILE_CODE = "0000";
    private final CloudBlobClient blobClient;
    private final String containerReference;

    public AzureBlobClient(@Value("${blobStorage.connectionString}") String connectionString,
                            @Value("{blobStorage.containerReference}") String containerReference
                           ) throws URISyntaxException, InvalidKeyException {
        log.trace("AzureBlobClient.AzureBlobClient");
        final CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionString);
        this.blobClient = storageAccount.createCloudBlobClient();
        this.containerReference = containerReference;
    }

    @Override
    public Resource readCsv(String fileName) {
        log.info("START - getFile for path: {}", fileName);
        Resource resource = new Resource();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            final CloudBlobContainer blobContainer = blobClient.getContainerReference(containerReference);
            final CloudBlockBlob blob = blobContainer.getBlockBlobReference(fileName);
            BlobProperties properties = blob.getProperties();
            blob.download(outputStream);
            log.info("END - getFile - path {}", fileName);
            resource.setData(outputStream.toByteArray());
            resource.setFileName(blob.getName());
            resource.setMimetype(properties.getContentType());
            return resource;
        } catch (StorageException e) {
            if(e.getHttpStatusCode() == 404){
                throw new ResourceNotFoundException(String.format(ERROR_DURING_DOWNLOAD_FILE_MESSAGE,fileName));
            }
            throw new SelfCarePagoPaInjectionException(String.format(ERROR_DURING_DOWNLOAD_FILE_MESSAGE,fileName), ERROR_DURING_DOWNLOAD_FILE_CODE);
        } catch (URISyntaxException | IOException e) {
            throw new SelfCarePagoPaInjectionException(String.format(ERROR_DURING_DOWNLOAD_FILE_MESSAGE,fileName), ERROR_DURING_DOWNLOAD_FILE_CODE);
        }
    }
}
