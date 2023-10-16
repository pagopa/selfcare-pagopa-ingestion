package it.pagopa.selfcare.pagopa.injestion.connector.azure_storage;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageCredentials;
import com.microsoft.azure.storage.StorageCredentialsAccountAndKey;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobProperties;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import it.pagopa.selfcare.pagopa.injestion.config.AzureStorageConfig;
import it.pagopa.selfcare.pagopa.injestion.connector.AzureConnector;
import it.pagopa.selfcare.pagopa.injestion.dto.ResourceResponse;
import it.pagopa.selfcare.pagopa.injestion.exception.ResourceNotFoundException;
import it.pagopa.selfcare.pagopa.injestion.exception.SelfCarePagoPaInjectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@Service
@PropertySource("classpath:config/azure-storage-config.properties")
@Profile("AzureStorage")
public class AzureBlobClient implements AzureConnector {

    private static final String ERROR_DURING_DOWNLOAD_FILE_MESSAGE = "Error during download file %s";
    private static final String ERROR_DURING_DOWNLOAD_FILE_CODE = "0000";
    private final CloudBlobClient blobClient;
    private final AzureStorageConfig azureStorageConfig;

    public AzureBlobClient(AzureStorageConfig azureStorageConfig) throws URISyntaxException {
        log.trace("AzureBlobClient.AzureBlobClient");
        log.debug("storageConnectionString = {}", azureStorageConfig.getConnectionString());
        this.azureStorageConfig = azureStorageConfig;
        final CloudStorageAccount storageAccount = buildStorageAccount();
        this.blobClient = storageAccount.createCloudBlobClient();
    }

    private CloudStorageAccount buildStorageAccount() throws URISyntaxException {
        StorageCredentials storageCredentials = new StorageCredentialsAccountAndKey(azureStorageConfig.getAccountName(), azureStorageConfig.getAccountKey());
        return new CloudStorageAccount(storageCredentials,
                true,
                azureStorageConfig.getEndpointSuffix(),
                azureStorageConfig.getAccountName());
    }

    @Override
    public ResourceResponse readCsv(String fileName) {
        log.info("START - getFile for path: {}", fileName);
        ResourceResponse resourceResponse = new ResourceResponse();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            final CloudBlobContainer blobContainer = blobClient.getContainerReference(azureStorageConfig.getContainer());
            final CloudBlockBlob blob = blobContainer.getBlockBlobReference(fileName);
            BlobProperties properties = blob.getProperties();
            blob.download(outputStream);
            log.info("END - getFile - path {}", fileName);
            resourceResponse.setData(outputStream.toByteArray());
            resourceResponse.setFileName(blob.getName());
            resourceResponse.setMimetype(properties.getContentType());
            return resourceResponse;
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
