package ua.lpnu.kzp;

import java.util.List;
import java.util.Objects;

/** Описує запис розсадника рослин для експорту в CSV. */
public final class PlantRecord {
    @CsvColumn("вік")
    private final int ageYears;

    @CsvColumn("висота")
    private final double heightCm;

    @CsvColumn("ціна")
    private final double price;

    @CsvColumn("вид")
    private final String species;

    @CsvColumn("полив")
    private final int wateringDays;

    public PlantRecord(String species, int ageYears, double heightCm, double price, int wateringDays) {
        if (species == null || species.isBlank()) {
            throw new IllegalArgumentException("Вид рослини не може бути порожнім");
        }
        if (ageYears < 0 || heightCm <= 0 || price < 0 || wateringDays <= 0) {
            throw new IllegalArgumentException("Некоректні числові дані рослини");
        }
        this.species = species;
        this.ageYears = ageYears;
        this.heightCm = heightCm;
        this.price = price;
        this.wateringDays = wateringDays;
    }

    public static PlantRecord fromCsv(String line) throws DataStorageException {
        List<String> fields = CsvParser.parse(line);
        return fromFields(fields);
    }

    static PlantRecord fromFields(List<String> fields) throws DataStorageException {
        if (fields.size() != 5) {
            throw new DataStorageException("Очікується п'ять полів CSV");
        }
        try {
            // Порядок відповідає алфавітному сортуванню назв полів рефлексією:
            // ageYears -> heightCm -> price -> species -> wateringDays
            return new PlantRecord(
                    fields.get(3),                      // species (String)
                    Integer.parseInt(fields.get(0)),    // ageYears (int)
                    Double.parseDouble(fields.get(1)),  // heightCm (double)
                    Double.parseDouble(fields.get(2)),  // price (double)
                    Integer.parseInt(fields.get(4))     // wateringDays (int)
            );
        } catch (IllegalArgumentException exception) {
            throw new DataStorageException("Некоректний рядок розсадника", exception);
        }
    }

    public String species() {
        return species;
    }

    public int ageYears() {
        return ageYears;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof PlantRecord that)) return false;
        return ageYears == that.ageYears &&
                Double.compare(that.heightCm, heightCm) == 0 &&
                Double.compare(that.price, price) == 0 &&
                wateringDays == that.wateringDays &&
                species.equals(that.species);
    }

    @Override
    public int hashCode() {
        return Objects.hash(species, ageYears, heightCm, price, wateringDays);
    }
}