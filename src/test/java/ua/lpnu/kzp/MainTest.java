package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Модульні тести для перевірки обробки записів розсадника рослин (Варіант 12).
 */
class MainTest {

    @Test
    void validRecordsCalculateCorrectMetrics() {
        List<String> lines = List.of(
                "Хвойні;Туя західна;100.0;400.00;7",
                "Листяні;Клен гостролистий;200.0;800.50;5",
                "Кімнатні;Фікус;60.0;300.00;3"
        );

        String report = Main.buildReport(lines);

        assertTrue(report.contains("Коректних записів:             3"));
        assertTrue(report.contains("120.00 см"));
        assertTrue(report.contains("800.50 грн (Клен гостролистий)"));
        assertTrue(report.contains("3 дн."));
        assertTrue(report.contains("Помилок:                       0"));
    }

    @Test
    void invalidRecordsAreReportedAndSkipped() {
        List<String> lines = List.of(
                "Хвойні;Туя;100.0;400.00;7",
                "",
                "Недостатньо;полів;100.0",
                ";Троянда;50.0;200.00;4",
                "Хвойні;Ялина;не_число;500.00;6",
                "Кімнатні;Монстера;-20.0;300.00;5"
        );

        String report = Main.buildReport(lines);

        assertTrue(report.contains("Коректних записів:             1"));
        assertTrue(report.contains("Помилок:                       5"));
        assertTrue(report.contains("Рядок 2: порожній рядок"));
        assertTrue(report.contains("Рядок 3: очікується 5 полів"));
        assertTrue(report.contains("Рядок 4: порожній вид або назва рослини"));
        assertTrue(report.contains("Рядок 5: числове поле має помилковий формат"));
        assertTrue(report.contains("Рядок 6: недопустиме від'ємне або нульове числове значення"));
    }

    @Test
    void fileWithNoValidRecordsDoesNotDivideByZero() {
        List<String> lines = List.of(
                "",
                "Помилковий;рядок"
        );

        String report = Main.buildReport(lines);

        assertTrue(report.contains("Коректних записів:             0"));
        assertTrue(report.contains("0.00 см"));
        assertTrue(report.contains("Помилок:                       2"));
    }
}