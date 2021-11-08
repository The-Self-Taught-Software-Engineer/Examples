package codes.jakob.tstse.example.common

import java.math.BigDecimal
import java.time.LocalDate

data class Paycheck(
    val date: LocalDate,
    val period: LocalDateRange,
    val developer: Developer,
    val hourlyRate: BigDecimal,
    val hoursWorked: BigDecimal,
) {
    val salary: BigDecimal
        get() {
            return hoursWorked.multiply(hourlyRate)
        }

    class Builder {
        var date: LocalDate? = null
        var period: LocalDateRange? = null
        var developer: Developer? = null
        var hourlyRate: BigDecimal? = null
        var hoursWorked: BigDecimal? = null

        fun build(): Paycheck = Paycheck(
            date = date ?: error("A date is required"),
            period = period ?: error("A period is required"),
            developer = developer ?: error("A developer is required"),
            hoursWorked = hoursWorked ?: error("The worked hours are required"),
            hourlyRate = hourlyRate ?: error("A hourly rate is required"),
        )
    }
}
