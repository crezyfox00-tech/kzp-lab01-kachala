package ua.lpnu.kzp;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Набір чистих потокових запитів для розсадника рослин (Варіант 12).
 */
public final class NurseryReport {

    private NurseryReport() {
        // Заборона створення екземплярів
    }

    /** 1. Фільтр: періодичність поливу не більша за 2 дні. */
    public static List<Plant> frequentWateringPlants(List<Plant> plants) {
        return streamOf(plants)
                .filter(p -> p.getWateringDays() <= 2)
                .toList();
    }

    /** 2. Перетворення: вилучення видів рослин (map). */
    public static List<String> plantSpecies(List<Plant> plants) {
        return streamOf(plants)
                .map(Plant::getSpecies)
                .sorted()
                .toList();
    }

    /** 3. Групування: середня ціна за типом (PlantKind) рослини. */
    public static Map<PlantKind, Double> averagePriceByKind(List<Plant> plants) {
        return streamOf(plants)
                .collect(Collectors.groupingBy(
                        Plant::getKind,
                        () -> new EnumMap<>(PlantKind.class),
                        Collectors.averagingDouble(Plant::getPrice)
                ));
    }

    /** 4. Статистика: зведена статистика за висотою рослин. */
    public static DoubleSummaryStatistics heightStatistics(List<Plant> plants) {
        return streamOf(plants)
                .collect(Collectors.summarizingDouble(Plant::getHeightCm));
    }

    /** 5. Топ-N: за висотою (спадання) та за видом (зростання). */
    public static List<String> topPlantsByHeight(List<Plant> plants, int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Ліміт не може бути від'ємним");
        }
        Comparator<Plant> byHeightAndSpecies = Comparator
                .comparingDouble(Plant::getHeightCm)
                .reversed()
                .thenComparing(Plant::getSpecies);

        return streamOf(plants)
                .sorted(byHeightAndSpecies)
                .limit(limit)
                .map(Plant::getName)
                .toList();
    }

    /** 6. Пошук: знайти першу рослину за її видом з використанням Optional. */
    public static Optional<Plant> findBySpecies(List<Plant> plants, String species) {
        Objects.requireNonNull(species, "Вид не може бути null");
        return streamOf(plants)
                .filter(p -> p.getSpecies().equalsIgnoreCase(species.trim()))
                .findFirst();
    }

    private static Stream<Plant> streamOf(List<Plant> plants) {
        return Objects.requireNonNull(plants, "Список не може бути null").stream();
    }
}