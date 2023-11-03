# selfcare-pagopa-ingestion
A web application having the purpose to migrate party data through REST services.

It has been implemented in order to transfer ([portal-name](repo?) data towards [selfcare-ms-core](https://github.com/pagopa/selfcare-ms-core).<br />
The actions performed on each entity to migrate are:
WIP
1. Read and analyze the CSV file to obtain all the institutions and users to be onboarded in selfcare with membership of the prod-pagopa product
2. Save on Mongo all institutions and users data to onboard.
   1. For each element
      1. ...
      2. ...


## APIs
| Method | Path     | Description                                                                               | Params |
|--------|----------|-------------------------------------------------------------------------------------------|--------|
| POST   | /persist | This API parses csv source file and persists on mongo institutions, users and delegations |        |
| POST   | /ingest  | The API to start the migration of persisted data on selfcare through APIM                 |        |

## Configuration
### Core
The following environment variables allow to configure some core functionalities.

| ENV             | Description                                          | Default |
|-----------------|------------------------------------------------------|---------|
| APP_SERVER_PORT | The port at which the application server will listen | 8080    |

### Logging
The following environment variables allow to configure logging levels.

| ENV                             | Description                                                                                                                                                      | Default |
|---------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| APP_LOG_LEVEL                   | The base log level                                                                                                                                               | DEBUG   |
| REST_CLIENT_LOGGER_LEVEL        | The log level defined for the REST integrations. <br />Available values are: NONE, BASIC, HEADERS, FULL                                                          | FULL    |

### Business logic (WIP)
The following environment variables allow to configure some business logic behavior.


### Integration
The following environment variables allow to configure integrations towards external systems.

| ENV                         | Description                                                           | Default   |
|-----------------------------|-----------------------------------------------------------------------|-----------|
| REST_CLIENT_CONNECT_TIMEOUT | The default connect milliseconds timeout applied to REST integrations | 5000      |
| REST_CLIENT_READ_TIMEOUT    | The default read milliseconds timeout applied to REST integrations    | 5000      |

## Throubleshouting

Due to the use of selfcare-commons security config, it will configure the authentication through k8s. 
If you defined the KUBECONFIG env var, or the default kubectl config file exists, you will be asked to allow the authentication.
If the config file doesn't exists, it will ignore it and the application will be able to start successfully.

If starting the application you see the following exception:
```
com.microsoft.aad.adal4j.AdalClaimsChallengeException: {"error":"interaction_required"}
```
You have to authenticate your kubectl client, or remove the used config file.

## Useful scripts (WIP)
Inside the repository there are some useful scripts which make easy compile and execute the migration application server in the local computer.:

| SCRIPT           | Description                                                                                                                                                                                                  |
|------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| packageAndRun.sh | It will create the fat jar and then will call the run.sh script                                                                                                                                              |
| run.sh           | It will suggest some useful variables to set and then launch the application. Before to execute it, you have to open it and configure the right values for SOURCE_HOST, TARGET_HOST and JWT_TOKEN_PUBLIC_KEY |
