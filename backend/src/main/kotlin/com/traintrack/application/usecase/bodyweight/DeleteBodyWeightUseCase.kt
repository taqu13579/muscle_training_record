package com.traintrack.application.usecase.bodyweight

import com.traintrack.domain.model.bodyweight.BodyWeightId
import com.traintrack.domain.model.user.UserId
import com.traintrack.domain.repository.BodyWeightRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteBodyWeightUseCase(
    private val bodyWeightRepository: BodyWeightRepository
) {
    @Transactional
    fun execute(userId: Long, id: Long) {
        bodyWeightRepository.deleteByIdAndUserId(BodyWeightId(id), UserId(userId))
    }
}
