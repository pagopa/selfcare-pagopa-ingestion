package it.pagopa.selfcare.pagopa.ingestion.core;

import feign.FeignException;
import it.pagopa.selfcare.pagopa.ingestion.api.mongo.UserConnector;
import it.pagopa.selfcare.pagopa.ingestion.api.rest.InternalApiConnector;
import it.pagopa.selfcare.pagopa.ingestion.constant.WorkStatus;
import it.pagopa.selfcare.pagopa.ingestion.mapper.UserMapper;
import it.pagopa.selfcare.pagopa.ingestion.model.csv.UserModel;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.OnboardingUserRequest;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
@Slf4j
class UserServiceImpl implements UserService {

    private final MigrationService migrationService;
    private final UserConnector userConnector;
    private final String csvPath;
    private final int pageSize;

    private final InternalApiConnector internalApiConnector;

    public UserServiceImpl(
            MigrationService migrationService,
            UserConnector userConnector,
            @Value("${app.local.user}") String csvPath,
            @Value("${app.pageSize}") int pageSize,
            InternalApiConnector internalApiConnector) {
        this.migrationService = migrationService;
        this.userConnector = userConnector;
        this.csvPath = csvPath;
        this.pageSize = pageSize;
        this.internalApiConnector = internalApiConnector;
    }

    @Override
    @Async
    public void persistUser() {
        migrationService.migrateEntities(UserModel.class, csvPath, userConnector::save, UserMapper::convertModelToDto);
    }

    @Override
    @Async
    public void migrateUser(String status) {
        log.info("Starting migration of User");
        int page = 0;
        boolean hasNext = true;
        do {
            List<User> users = userConnector.findAllByStatus(page, pageSize, status);
            if (!CollectionUtils.isEmpty(users)) {
                onboardUser(users);
            } else {
                hasNext = false;
            }
        } while (Boolean.TRUE.equals(hasNext));

        log.info("Completed migration of User");
    }

    private void onboardUser(List<User> users) {
        Map<String, List<User>> map = users.stream().collect(groupingBy(User::getInstitutionTaxCode));
        map.forEach(this::onboardingAndUpdateUser);
    }

    @Async
    private void onboardingAndUpdateUser(String institutionId, List<User> users) {
        OnboardingUserRequest request = new OnboardingUserRequest(institutionId, users);
        try {
            internalApiConnector.onboardingUser(request);
            users.forEach(user -> {
                user.setWorkStatus(WorkStatus.DONE);
                user.setOnboardingHttpStatus(200);
            });
        } catch (FeignException e) {
            users.forEach(user -> {
                user.setWorkStatus(WorkStatus.ERROR);
                user.setOnboardingHttpStatus(e.status());
                user.setOnboardingMessage(e.getMessage());
            });
        }
        userConnector.saveAll(users);
    }

}
