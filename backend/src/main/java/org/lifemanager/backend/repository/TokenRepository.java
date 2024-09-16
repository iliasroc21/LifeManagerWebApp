package org.lifemanager.backend.repository;

import org.lifemanager.backend.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query(value = """
    SELECT t.* 
    FROM Token t 
    WHERE t.user_id = :id AND (t.revoked = false OR t.expired = false)
    """ , nativeQuery = true)
    List<Token> findAllValidTokenByUser(@Param("id") Long id);

    Optional<Token> findByToken(String token);



    Optional<Token> findByTokenAndExpiredFalseAndRevokedFalse(String token);
}
