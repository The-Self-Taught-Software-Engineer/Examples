package codes.jakob.tstse.example.common

import java.time.LocalDate

class PaycheckRepository {
    fun save(paychecks: List<Paycheck>) {
        TODO("Not yet implemented")
    }

    fun saveFailed(developers: List<Developer>, paycheckPeriod: ClosedRange<LocalDate>) {
        TODO("Not yet implemented")
    }

    fun save(paychecks: Paycheck) {
        TODO("Not yet implemented")
    }

    fun saveFailed(developer: Developer, paycheckPeriod: ClosedRange<LocalDate>) {
        TODO("Not yet implemented")
    }

    fun exists(developer: Developer, paycheckPeriod: ClosedRange<LocalDate>): Boolean {
        TODO("Not yet implemented")
    }
}
