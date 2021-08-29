package codes.jakob.tstse.example.bytesized;

import java.time.Duration;
import java.time.LocalDateTime;

public class DurationCaution {
    private static final String FORMAT_STRING = "Hours between %s and %s: %dh";

    public static void main(String[] args) {
        LocalDateTime earlierThisYear = LocalDateTime.of(
                2021,
                4,
                5,
                15,
                15
        );
        LocalDateTime nextYear = LocalDateTime.of(
                2022,
                1,
                1,
                20,
                15
        );

        Duration durationA = Duration.between(earlierThisYear, nextYear).abs();
        Duration durationB = Duration.between(nextYear, earlierThisYear).abs();

        System.out.printf(
                (FORMAT_STRING) + "%n",
                earlierThisYear,
                nextYear,
                durationA.toHours()
        );
        System.out.printf(
                (FORMAT_STRING) + "%n",
                nextYear,
                earlierThisYear,
                durationB.toHours()
        );
    }
}
