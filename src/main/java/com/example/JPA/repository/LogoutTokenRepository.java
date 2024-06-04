package com.example.JPA.repository;

import com.example.JPA.entity.LogoutToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogoutTokenRepository extends JpaRepository<LogoutToken, String> {
}
