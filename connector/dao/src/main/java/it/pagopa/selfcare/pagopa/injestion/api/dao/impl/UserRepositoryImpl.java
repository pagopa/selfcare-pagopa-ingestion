package it.pagopa.selfcare.pagopa.injestion.api.dao.impl;

import it.pagopa.selfcare.pagopa.injestion.api.UserConnector;
import it.pagopa.selfcare.pagopa.injestion.api.dao.mapper.UserMapper;
import it.pagopa.selfcare.pagopa.injestion.api.dao.repo.UserRepository;
import it.pagopa.selfcare.pagopa.injestion.dto.User;
import it.pagopa.selfcare.pagopa.injestion.exception.ResourceNotFoundException;
import it.pagopa.selfcare.pagopa.injestion.api.dao.model.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserRepositoryImpl implements UserConnector {

    private final UserRepository repository;

    public UserRepositoryImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> findAll(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<User> userList = repository.findAll(pageRequest).stream()
                .map(UserMapper::entityToDto)
                .collect(Collectors.toList());
        log.info("Trovati {} elementi User paginati (pagina {}, dimensione {})", userList.size(), page, pageSize);
        return userList;
    }

    @Override
    public List<User> findAll() {
        List<User> userList = repository.findAll().stream()
                .map(UserMapper::entityToDto)
                .collect(Collectors.toList());
        log.info("Trovati {} elementi User", userList.size());
        return userList;
    }

    @Override
    public User findById(String id) {
        return repository.findById(id)
                .map(entity -> {
                    log.info("Trovato User con id: {}", entity.getId());
                    return UserMapper.entityToDto(entity);
                })
                .orElseThrow(() -> {
                    log.error("User non trovato con id: {}", id);
                    return new ResourceNotFoundException("USER NOT FOUND " + id);
                });
    }

    @Override
    public User save(User user) {
        final UserEntity entity = UserMapper.dtoToEntity(user);
        UserEntity savedEntity = repository.save(entity);
        log.info("Salvato User con id: {}", savedEntity.getId());
        return UserMapper.entityToDto(savedEntity);
    }

    @Override
    public List<User> saveAll(List<User> userList) {
        List<User> savedUserList = userList.stream()
                .map(this::save)
                .collect(Collectors.toList());
        log.info("Salvati {} elementi User", savedUserList.size());
        return savedUserList;
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
        log.info("Eliminato User con id: {}", id);
    }

    @Override
    public void delete(User user) {
        UserEntity entity = UserMapper.dtoToEntity(user);
        repository.delete(entity);
        log.info("Eliminato User con id: {}", entity.getId());
    }

}
