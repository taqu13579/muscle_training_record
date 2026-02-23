package com.traintrack.presentation.controller

import com.traintrack.application.usecase.training.*
import com.traintrack.presentation.request.CreateTrainingRecordRequest
import com.traintrack.presentation.request.UpdateTrainingRecordRequest
import com.traintrack.presentation.response.CalendarDayResponse
import com.traintrack.presentation.response.CalendarResponse
import com.traintrack.presentation.response.TrainingRecordResponse
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.YearMonth

@RestController
@RequestMapping("/api/v1/training-records")
class TrainingRecordController(
    private val getTrainingRecordsUseCase: GetTrainingRecordsUseCase,
    private val getTrainingRecordByIdUseCase: GetTrainingRecordByIdUseCase,
    private val getTrainingRecordsByDateUseCase: GetTrainingRecordsByDateUseCase,
    private val registerTrainingRecordUseCase: RegisterTrainingRecordUseCase,
    private val updateTrainingRecordUseCase: UpdateTrainingRecordUseCase,
    private val deleteTrainingRecordUseCase: DeleteTrainingRecordUseCase
) {
    @GetMapping
    fun getAll(
        @PageableDefault(size = 20, sort = ["trainingDate"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ): ResponseEntity<Page<TrainingRecordResponse>> {
        val records = getTrainingRecordsUseCase.execute(pageable)
        return ResponseEntity.ok(records.map { TrainingRecordResponse.from(it) })
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<TrainingRecordResponse> {
        val record = getTrainingRecordByIdUseCase.execute(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(TrainingRecordResponse.from(record))
    }

    @GetMapping("/date/{date}")
    fun getByDate(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate
    ): ResponseEntity<List<TrainingRecordResponse>> {
        val records = getTrainingRecordsByDateUseCase.execute(date)
        return ResponseEntity.ok(records.map { TrainingRecordResponse.from(it) })
    }

    @GetMapping("/calendar")
    fun getCalendar(
        @RequestParam yearMonth: String
    ): ResponseEntity<CalendarResponse> {
        val ym = YearMonth.parse(yearMonth)
        val records = getTrainingRecordsByDateUseCase.executeByMonth(ym)

        val dayGroups = records.groupBy { it.trainingDate }
        val days = dayGroups.map { (date, dayRecords) ->
            val bodyParts = dayRecords
                .mapNotNull { it.exercise?.bodyPart?.name }
                .distinct()
            CalendarDayResponse(
                date = date,
                recordCount = dayRecords.size,
                bodyParts = bodyParts
            )
        }.sortedByDescending { it.date }

        return ResponseEntity.ok(CalendarResponse(
            yearMonth = yearMonth,
            days = days
        ))
    }

    @PostMapping
    fun create(
        @Valid @RequestBody request: CreateTrainingRecordRequest
    ): ResponseEntity<TrainingRecordResponse> {
        val command = RegisterTrainingRecordCommand(
            exerciseId = request.exerciseId,
            weightKg = request.weightKg,
            repCount = request.repCount,
            setCount = request.setCount,
            trainingDate = request.trainingDate,
            memo = request.memo
        )
        val created = registerTrainingRecordUseCase.execute(command)
        return ResponseEntity.status(HttpStatus.CREATED).body(TrainingRecordResponse.from(created))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateTrainingRecordRequest
    ): ResponseEntity<TrainingRecordResponse> {
        val command = UpdateTrainingRecordCommand(
            id = id,
            exerciseId = request.exerciseId,
            weightKg = request.weightKg,
            repCount = request.repCount,
            setCount = request.setCount,
            trainingDate = request.trainingDate,
            memo = request.memo
        )
        val updated = updateTrainingRecordUseCase.execute(command)
        return ResponseEntity.ok(TrainingRecordResponse.from(updated))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        deleteTrainingRecordUseCase.execute(id)
        return ResponseEntity.noContent().build()
    }
}
