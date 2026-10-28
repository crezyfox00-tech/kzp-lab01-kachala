package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Модульні тести для класу-сутності Plant, фабричного методу fromCsv та допоміжних record.
 */
class PlantTest {

    @Test
    void constructorCreatesValidPlantAndGettersWork() {
        Plant plant = new Plant("Хвойні", "Туя західна", 120.5, 450.0, 7);

        assertEquals("Хвойні", plant.getSpecies());
        assertEquals("Туя західна", plant.getName());
        assertEquals(120.5, plant.getHeightCm(), 0.0001);
        assertEquals(450.0, plant.getPrice(), 0.0001);
        assertEquals(7, plant.getWateringDays());
    }

    @Test
    void constructorAcceptsZeroPriceAsBoundaryCase() {
        Plant freePlant = new Plant("Кімнатні", "Кактус", 10.0, 0.0, 14);
        assertEquals(0.0, freePlant.getPrice(), 0.0001);
    }

    @Test
    void constructorRejectsNullAndBlankStrings() {
        assertThrows(NullPointerException.class,
                () -> new Plant(null, "Туя", 100.0, 200.0, 5));
        assertThrows(NullPointerException.class,
                () -> new Plant("Хвойні", null, 100.0, 200.0, 5));
        assertThrows(IllegalArgumentException.class,
                () -> new Plant("   ", "Туя", 100.0, 200.0, 5));
        assertThrows(IllegalArgumentException.class,
                () -> new Plant("Хвойні", "", 100.0, 200.0, 5));
    }

    @Test
    void constructorRejectsInvalidNumbers() {
        assertThrows(IllegalArgumentException.class,
                () -> new Plant("Хвойні", "Туя", 0.0, 200.0, 5));
        assertThrows(IllegalArgumentException.class,
                () -> new Plant("Хвойні", "Туя", -10.0, 200.0, 5));
        assertThrows(IllegalArgumentException.class,
                () -> new Plant("Хвойні", "Туя", 100.0, -1.0, 5));
        assertThrows(IllegalArgumentException.class,
                () -> new Plant("Хвойні", "Туя", 100.0, Double.NaN, 5));
        assertThrows(IllegalArgumentException.class,
                () -> new Plant("Хвойні", "Туя", 100.0, 200.0, 0));
    }

    @Test
    void fromCsvCreatesValidPlant() {
        Plant plant = Plant.fromCsv("Листяні;Клен гостролистий;210.0;850.50;5");

        assertEquals("Листяні", plant.getSpecies());
        assertEquals("Клен гостролистий", plant.getName());
        assertEquals(210.0, plant.getHeightCm(), 0.0001);
        assertEquals(850.50, plant.getPrice(), 0.0001);
        assertEquals(5, plant.getWateringDays());
    }

    @Test
    void fromCsvRejectsMalformedLines() {
        assertThrows(IllegalArgumentException.class, () -> Plant.fromCsv(""));
        assertThrows(IllegalArgumentException.class, () -> Plant.fromCsv("Лише;три;поля"));
        assertThrows(IllegalArgumentException.class, () -> Plant.fromCsv("Хвойні;Туя;не_число;400.0;7"));
        assertThrows(IllegalArgumentException.class, () -> Plant.fromCsv("Хвойні;Туя;-50.0;400.0;7"));
    }

    @Test
    void toStringFormatsWithDotDecimalSeparator() {
        Plant plant = new Plant("Хвойні", "Туя", 120.5, 450.0, 7);
        String text = plant.toString();

        assertTrue(text.contains("120.50 см"));
        assertTrue(text.contains("450.00 грн"));
    }

    @Test
    void recordsSupportValueEqualityAndValidation() {
        HeightWatering first = new HeightWatering(120.5, 7);
        HeightWatering second = new HeightWatering(120.5, 7);

        assertEquals(first, second);
        assertEquals(120.5, first.heightCm(), 0.0001);
        assertEquals(7, first.wateringDays());
        assertThrows(IllegalArgumentException.class, () -> new HeightWatering(-5.0, 7));

        PlantSummary summary1 = new PlantSummary(3, 120.0, 800.5, "Клен", 3);
        PlantSummary summary2 = new PlantSummary(3, 120.0, 800.5, "Клен", 3);
        assertEquals(summary1, summary2);
    }

    @Test
    void cliOptionsParsesArgumentsAndReadsPomVersion() {
        CliOptions options = CliOptions.parse(new String[]{"--input", "custom.csv", "--version"});
        assertTrue(options.showVersion());
        assertFalse(options.showHelp());
        assertEquals("custom.csv", options.inputPath().toString());
        assertEquals("2.0.0", CliOptions.readProjectVersion());
    }
}