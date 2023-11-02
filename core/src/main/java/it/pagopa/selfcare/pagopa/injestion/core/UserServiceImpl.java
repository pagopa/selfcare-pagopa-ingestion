package it.pagopa.selfcare.pagopa.injestion.core;

import feign.FeignException;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.UserConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.ExternalApiConnector;
import it.pagopa.selfcare.pagopa.injestion.constant.WorkStatus;
import it.pagopa.selfcare.pagopa.injestion.mapper.UserMapper;
import it.pagopa.selfcare.pagopa.injestion.model.csv.UserModel;
import it.pagopa.selfcare.pagopa.injestion.model.dto.OnboardingUserRequest;
import it.pagopa.selfcare.pagopa.injestion.model.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    private final ExternalApiConnector externalApiConnector;

    public UserServiceImpl(
            MigrationService migrationService,
            UserConnector userConnector,
            @Value("${app.local.user}") String csvPath,
            @Value("${app.pageSize}") int pageSize,
            ExternalApiConnector externalApiConnector) {
        this.migrationService = migrationService;
        this.userConnector = userConnector;
        this.csvPath = csvPath;
        this.pageSize = pageSize;
        this.externalApiConnector = externalApiConnector;
    }

    @Override
    public void persistUser() {
        migrationService.migrateEntities(UserModel.class, csvPath, userConnector::save, UserMapper::convertModelToDto);
    }

    @Override
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
        map.forEach((institutionId, userList) -> {
            OnboardingUserRequest request = new OnboardingUserRequest(institutionId, userList);
            try {
                externalApiConnector.onboardingUser(request);
                users.forEach(user -> user.setWorkStatus(WorkStatus.DONE));
            } catch (FeignException e) {
                users.forEach(user -> {
                    user.setWorkStatus(WorkStatus.ERROR);
                    user.setOnboardingHttpStatus(e.status());
                });
            }
            userConnector.saveAll(users);
        });
    }


}
