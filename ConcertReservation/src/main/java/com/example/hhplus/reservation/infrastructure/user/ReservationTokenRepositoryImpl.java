package com.example.hhplus.reservation.infrastructure.user;

import com.example.hhplus.reservation.domain.user.ReservationToken;
import com.example.hhplus.reservation.domain.user.ReservationTokenRepository;
import com.example.hhplus.reservation.domain.user.ReservationTokenStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationTokenRepositoryImpl implements ReservationTokenRepository {

    private final ReservationTokenJpaRepository reservationTokenJpaRepository;

    @Override
    public Optional<ReservationToken> findByUserId(long userId) {
        return reservationTokenJpaRepository.findByUserId(userId)
                .map(ReservationTokenEntity::toDomain);
    }

    @Override
    public ReservationToken save(ReservationToken reservationToken) {
        return reservationTokenJpaRepository.save(ReservationTokenEntity.toEntity(reservationToken))
                .toDomain();
    }

    @Override
    public int expireDurationOverToken(ReservationTokenStatus updateStatus, ReservationTokenStatus status, LocalDateTime targetDateTime, LocalDateTime updatedAt) {
        return reservationTokenJpaRepository.expireDurationOverToken(updateStatus, status, targetDateTime, updatedAt);
    }

    @Override
    public int countByStatus(ReservationTokenStatus status) {
        return reservationTokenJpaRepository.countByStatus(status);
    }

    @Override
    public Optional<ReservationToken> findByTokenValueAndStatus(String token, ReservationTokenStatus status) {
        return reservationTokenJpaRepository.findByTokenValueAndStatus(token, status)
                .map(ReservationTokenEntity::toDomain);
    }
}