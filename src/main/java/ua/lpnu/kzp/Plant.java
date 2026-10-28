package ua.lpnu.kzp;

import java.util.Locale;
import java.util.Objects;

/**
 * Клас-сутність, що описує незмінний запис про рослину в розсаднику (Варіант 12).
 */
public final class Plant {

    private final String species;
    private final String name;
    private final double heightCm;
    private final double price;
    private final int wateringDays;

    /**
     * Створює коректний запис про рослину після перевірки всіх інваріантів.
     *
     * @param species      вид рослини
     * @param name         назва рослини
     * @param heightCm     висота в сантиметрах
     * @param price        ціна в гривнях
     * @param wateringDays інтервал поливу в днях
     * @throws NullPointerException     якщо вид або назва є null
     * @throws IllegalArgumentException якщо порушено інваріанти полів
     */
    public Plant(String species, String name, double heightCm, double price, int wateringDays) {
        Objects.requireNonNull(species, "Вид рослини не може бути null");
        Objects.requireNonNull(name, "Назва рослини не може бути null");

        String trimmedSpecies = species.trim();
        String trimmedName = name.trim();

        if (trimmedSpecies.isEmpty() || trimmedName.isEmpty()) {
            throw new IllegalArgumentException("порожній вид або назва рослини");
        }
        if (heightCm <= 0 || !Double.isFinite(heightCm)
                || price < 0 || !Double.isFinite(price)
                || wateringDays <= 0) {
            throw new IllegalArgumentException("недопустиме від'ємне або нульове числове значення");
        }

        this.species = trimmedSpecies;
        this.name = trimmedName;
        this.heightCm = heightCm;
        this.price = price;
        this.wateringDays = wateringDays;
    }

    /**
     * Статичний фабричний метод для створення об'єкта Plant із CSV-рядка.
     *
     * @param line рядок CSV у форматі species;name;heightCm;price;wateringDays
     * @return створений об'єкт Plant
     * @throws NullPointerException     якщо рядок є null
     * @throws IllegalArgumentException якщо рядок порожній, має неправильну кількість полів або хибні числа
     */
    public static Plant fromCsv(String line) {
        Objects.requireNonNull(line, "Рядок не може бути null");
        if (line.isBlank()) {
            throw new IllegalArgumentException("порожній рядок");
        }

        String[] fields = line.split(";", -1);
        if (fields.length != 5) {
            throw new IllegalArgumentException(
                    String.format(Locale.ROOT, "очікується 5 полів, отримано %d", fields.length));
        }

        String species = fields[0].trim();
        String name = fields[1].trim();
        if (species.isEmpty() || name.isEmpty()) {
            throw new IllegalArgumentException("порожній вид або назва рослини");
        }

        try {
            double heightCm = Double.parseDouble(fields[2].trim());
            double price = Double.parseDouble(fields[3].trim());
            int wateringDays = Integer.parseInt(fields[4].trim());
            return new Plant(species, name, heightCm, price, wateringDays);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("числове поле має помилковий формат", exception);
        }
    }

    /**
     * Повертає вид рослини.
     *
     * @return вид рослини
     */
    public String getSpecies() {
        return species;
    }

    /**
     * Повертає назву рослини.
     *
     * @return назва рослини
     */
    public String getName() {
        return name;
    }

    /**
     * Повертає висоту рослини в сантиметрах.
     *
     * @return висота в см
     */
    public double getHeightCm() {
        return heightCm;
    }

    /**
     * Повертає ціну рослини в гривнях.
     *
     * @return ціна в грн
     */
    public double getPrice() {
        return price;
    }

    /**
     * Повертає інтервал поливу в днях.
     *
     * @return інтервал поливу
     */
    public int getWateringDays() {
        return wateringDays;
    }

    /**
     * Повертає допоміжний record із параметрами висоти та поливу.
     *
     * @return об'єкт HeightWatering
     */
    public HeightWatering getHeightWatering() {
        return new HeightWatering(heightCm, wateringDays);
    }

    /**
     * Повертає форматоване текстове подання рослини з використанням Locale.ROOT.
     *
     * @return текстовий опис рослини
     */
    @Override
    public String toString() {
        return String.format(
                Locale.ROOT,
                "%s (%s): висота %.2f см, ціна %.2f грн, полив кожні %d дн.",
                name, species, heightCm, price, wateringDays);
    }
}