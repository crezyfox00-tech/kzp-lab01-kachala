package ua.lpnu.kzp;

/**
 * Незмінне допоміжне значення, що зберігає висоту рослини та інтервал поливу (Варіант 12).
 *
 * @param heightCm     висота рослини в сантиметрах
 * @param wateringDays інтервал поливу в днях
 */
public record HeightWatering(double heightCm, int wateringDays) {

    /**
     * Перевіряє допустимість висоти та інтервалу поливу.
     */
    public HeightWatering {
        if (heightCm <= 0 || !Double.isFinite(heightCm)) {
            throw new IllegalArgumentException("Висота має бути скінченним додатним числом");
        }
        if (wateringDays <= 0) {
            throw new IllegalArgumentException("Інтервал поливу має бути додатним числом");
        }
    }
}