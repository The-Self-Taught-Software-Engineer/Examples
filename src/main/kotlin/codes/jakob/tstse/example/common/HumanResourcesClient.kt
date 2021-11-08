package codes.jakob.tstse.example.common

import java.math.BigDecimal
import java.time.LocalDate

interface HumanResourcesClient {
    /**
     * Returns the hourly rate of the given [developer].
     *
     * @throws UnknownException If the [developer] is not known by the external HR system.
     * @throws TimeoutException If the request to the external HR system timed out.
     */
    fun getHourlyRate(developer: Developer): BigDecimal

    /**
     * Returns the hourly worked by the given [developer] within the given [period].
     *
     * @throws UnknownException If the [developer] is not known by the external HR system.
     * @throws UnavailableException If the [developer] did not work within the given [period].
     * @throws TimeoutException If the request to the external HR system timed out.
     */
    fun getHoursWorked(developer: Developer, period: ClosedRange<LocalDate>): BigDecimal

    open class HumanResourcesException(
        override val message: String?,
        override val cause: Throwable?,
    ) : RuntimeException()

    class UnknownException(
        val developer: Developer,
        cause: Throwable? = null,
    ) : HumanResourcesException("The developer '$developer' is unknown to the HR system", cause)

    class UnavailableException(
        val developer: Developer,
        message: String?,
        cause: Throwable?,
    ) : HumanResourcesException(message, cause)

    class TimeoutException(
        val developer: Developer,
        message: String?,
        cause: Throwable?,
    ) : HumanResourcesException(message, cause)
}
