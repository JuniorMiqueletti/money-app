package com.juniormiqueletti.moneyapp.repository;

import com.juniormiqueletti.moneyapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(final String email);

    public List<User> findByPermissionListDescription(final String descriptionPermission);
}
