package ua.lpnu.kzp;

import java.util.Objects;

/**
 * Запечатаний абстрактний базовий клас для елементів розсадника рослин (Варіант 12).
 */
public sealed abstract class Plant permits Tree, Flower {
    private final String species;
    private final String name;
    private final PlantKind kind;
    private final double heightCm;
    private final double price;
    private final int wateringDays;

    protected Plant(String species, String name, PlantKind kind, double heightCm, double price, int wateringDays) {
        if (species == null || species.isBlank()) {
            throw new IllegalArgumentException("порожній вид або назва рослини");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("порожній вид або назва рослини");
        }
        if (heightCm <= 0 || price < 0 || wateringDays <= 0) {
            throw new IllegalArgumentException("недопустиме від'ємне або нульове числове значення");
        }
        this.species = species.trim();
        this.name = name.trim();
        this.kind = Objects.requireNonNull(kind, "Категорія не може бути null");
        this.heightCm = heightCm;
        this.price = price;
        this.wateringDays = wateringDays;
    }

    /**
     * Захист від Finalizer attacks для усунення попередження SpotBugs CT_CONSTRUCTOR_THROW.
     */
    @Override
    @SuppressWarnings("deprecation")
    protected final void finalize() {
        // Порожній final-метод унеможливлює перевизначення фіналізатора в підкласах
    }

    public final String getSpecies() { return species; }
    public final String getName() { return name; }
    public final PlantKind getKind() { return kind; }
    public final double getHeightCm() { return heightCm; }
    public final double getPrice() { return price; }
    public final int getWateringDays() { return wateringDays; }
    
    public final HeightWatering getHeightWatering() { 
        return new HeightWatering(heightCm, wateringDays); 
    }

    /** Поліморфний метод: оцінка потреби в догляді */
    public abstract double careRequirement();

    @Override
    public final boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Plant plant = (Plant) other;
        return name.equals(plant.name);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass(), name);
    }

    public static Plant fromCsv(String line) {
        String[] fields = line.split(";", -1);
        if (fields.length != 5) {
            throw new IllegalArgumentException("Очікується 5 полів");
        }
        String species = fields[0].trim();
        String name = fields[1].trim();
        double height = Double.parseDouble(fields[2].trim());
        double price = Double.parseDouble(fields[3].trim());
        int watering = Integer.parseInt(fields[4].trim());

        if (species.toLowerCase().contains("дерево") || species.toLowerCase().contains("хвойні") || species.toLowerCase().contains("листяні")) {
            return new Tree(species, name, height, price, watering, height * 0.1);
        } else {
            return new Flower(species, name, height, price, watering, 3);
        }
    }
}