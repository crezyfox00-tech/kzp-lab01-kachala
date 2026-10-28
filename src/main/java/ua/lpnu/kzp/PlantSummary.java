package ua.lpnu.kzp;

import java.util.Objects;

/**
 * Незмінний підсумок обчислених показників розсадника рослин.
 *
 * @param validCount        кількість коректних записів
 * @param averageHeight     середня висота рослин у см
 * @param maxPrice          ціна найдорожчої рослини
 * @param mostExpensiveName назва найдорожчої рослини
 * @param minWateringDays   найменший інтервал поливу в днях
 */
public record PlantSummary(
        int validCount,
        double averageHeight,
        double maxPrice,
        String mostExpensiveName,
        int minWateringDays
) {
    /**
     * Перевіряє інваріанти підсумкових показників.
     */
    public PlantSummary {
        Objects.requireNonNull(mostExpensiveName, "Назва не може бути null");
        if (validCount < 0 || averageHeight < 0 || maxPrice < 0 || minWateringDays < 0
                || !Double.isFinite(averageHeight) || !Double.isFinite(maxPrice)) {
            throw new IllegalArgumentException("Підсумкові показники не можуть бути від'ємними");
        }
    }
}