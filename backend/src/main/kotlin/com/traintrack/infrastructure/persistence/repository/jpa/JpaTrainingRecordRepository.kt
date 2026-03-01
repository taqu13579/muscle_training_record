package com.traintrack.infrastructure.persistence.repository.jpa

import com.traintrack.infrastructure.persistence.entity.TrainingRecordEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface JpaTrainingRecordRepository : JpaRepository<TrainingRecordEntity, Long> {
    @Query("SELECT t FROM TrainingRecordEntity t JOIN FETCH t.exercise e JOIN FETCH e.bodyPart WHERE t.userId = :userId")
    fun findAllByUserIdWithExercise(userId: Long, pageable: Pageable): Page<TrainingRecordEntity>

    @Query("SELECT t FROM TrainingRecordEntity t JOIN FETCH t.exercise e JOIN FETCH e.bodyPart WHERE t.id = :id AND t.userId = :userId")
    fun findByIdAndUserIdWithExercise(id: Long, userId: Long): TrainingRecordEntity?

    @Query("SELECT t FROM TrainingRecordEntity t JOIN FETCH t.exercise e JOIN FETCH e.bodyPart WHERE t.userId = :userId AND t.trainingDate = :date ORDER BY t.createdAt DESC")
    fun findByUserIdAndTrainingDateWithExercise(userId: Long, date: LocalDate): List<TrainingRecordEntity>

    @Query("SELECT t FROM TrainingRecordEntity t JOIN FETCH t.exercise e JOIN FETCH e.bodyPart WHERE t.userId = :userId AND t.trainingDate BETWEEN :startDate AND :endDate ORDER BY t.trainingDate DESC, t.createdAt DESC")
    fun findByUserIdAndTrainingDateBetweenWithExercise(userId: Long, startDate: LocalDate, endDate: LocalDate): List<TrainingRecordEntity>

    fun deleteByIdAndUserId(id: Long, userId: Long)
}
