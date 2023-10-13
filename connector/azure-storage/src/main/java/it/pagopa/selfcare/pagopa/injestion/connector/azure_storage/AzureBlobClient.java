package it.pagopa.selfcare.pagopa.injestion.connector.azure_storage;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageCredentials;
import com.microsoft.azure.storage.StorageCredentialsAccountAndKey;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import it.pagopa.selfcare.pagopa.injestion.config.AzureStorageConfig;
import it.pagopa.selfcare.pagopa.injestion.connector.AzureConnector;
import it.pagopa.selfcare.pagopa.injestion.exception.SelfCarePagoPaInjectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;

@Slf4j
@Service
@PropertySource("classpath:config/azure-storage-config.properties")
@Profile("AzureStorage")
public class AzureBlobClient implements AzureConnector {

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
    public String readCsv(String fileName) {
        log.info("START - getFile for path: {}", fileName);
        try {
            final CloudBlobContainer blobContainer = blobClient.getContainerReference(azureStorageConfig.getContainer());
            final CloudBlockBlob blob = blobContainer.getBlockBlobReference(fileName);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blob.download(outputStream);
            String csvContent = outputStream.toString();
            log.info("END - getFile - path {}", fileName);
            return csvContent;
        } catch (StorageException | URISyntaxException e) {
            throw new SelfCarePagoPaInjectionException("ERRORE DURANTE IL DOWNLOAD DEL FILE " +fileName);
        }
    }
}
