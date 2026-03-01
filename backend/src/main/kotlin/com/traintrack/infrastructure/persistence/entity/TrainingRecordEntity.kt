package com.traintrack.infrastructure.persistence.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "training_records")
class TrainingRecordEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    val exercise: ExerciseEntity,

    @Column(name = "weight_kg", nullable = false, precision = 5, scale = 2)
    var weightKg: BigDecimal,

    @Column(name = "rep_count", nullable = false)
    var repCount: Int,

    @Column(name = "set_count", nullable = false)
    var setCount: Int,

    @Column(name = "training_date", nullable = false)
    var trainingDate: LocalDate,

    @Column(columnDefinition = "TEXT")
    var memo: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
