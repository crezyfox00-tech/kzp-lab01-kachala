package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class NurseryReportTest {

    private final List<Plant> plants = List.of(
            new Tree("Хвойні", "Сосна звичайна", 250.0, 1200.0, 3, 25.0),
            new Flower("Квітучі", "Тюльпан садовий", 30.0, 50.0, 1, 2),
            new Tree("Листяні", "Дуб звичайний", 300.0, 1500.0, 5, 30.0),
            new Flower("Квітучі", "Роза чайна", 60.0, 150.0, 2, 3)
    );

    @Test
    void filtersFrequentWatering() {
        assertEquals(2, NurseryReport.frequentWateringPlants(plants).size());
    }

    @Test
    void mapsSpecies() {
        assertEquals(List.of("Квітучі", "Квітучі", "Листяні", "Хвойні"), 
                NurseryReport.plantSpecies(plants));
    }

    @Test
    void groupsAndAveragesPrice() {
        assertEquals(100.0, NurseryReport.averagePriceByKind(plants).get(PlantKind.FLOWER));
    }

    @Test
    void calculatesHeightStatistics() {
        assertEquals(4, NurseryReport.heightStatistics(plants).getCount());
        assertEquals(300.0, NurseryReport.heightStatistics(plants).getMax());
    }

    @Test
    void ordersTopPlantsByHeightAndSpecies() {
        List<String> top = NurseryReport.topPlantsByHeight(plants, 2);
        assertEquals(2, top.size());
        assertEquals(List.of(), NurseryReport.topPlantsByHeight(plants, 0));
    }

    @Test
    void reportsMissingSpeciesAsEmptyOptional() {
        Optional<Plant> result = NurseryReport.findBySpecies(plants, "Невідомо");
        assertTrue(result.isEmpty());
    }

    @Test
    void rejectsNegativeLimit() {
        assertThrows(IllegalArgumentException.class,
                () -> NurseryReport.topPlantsByHeight(plants, -1));
    }

    @Test
    void queriesDoNotMutateInput() {
        List<Plant> before = List.copyOf(plants);
        NurseryReport.frequentWateringPlants(plants);
        NurseryReport.plantSpecies(plants);
        NurseryReport.averagePriceByKind(plants);
        NurseryReport.heightStatistics(plants);
        NurseryReport.topPlantsByHeight(plants, 3);
        NurseryReport.findBySpecies(plants, "Хвойні");
        assertEquals(before, plants);
    }

    @Test
    void handlesEmptyInput() {
        assertEquals(0, NurseryReport.frequentWateringPlants(List.of()).size());
        assertEquals(0, NurseryReport.heightStatistics(List.of()).getCount());
        assertTrue(NurseryReport.findBySpecies(List.of(), "Тест").isEmpty());
    }
}