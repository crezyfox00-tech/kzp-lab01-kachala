package ua.lpnu.kzp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Контролер, що керує процесом читання даних, розбору об'єктів Plant,
 * обчисленням статистики та формуванням звіту.
 */
public final class NurseryController {

    /** Створює екземпляр контролера розсадника. */
    public NurseryController() {
    }

    /**
     * Виконує основний робочий процес згідно з переданими параметрами командного рядка.
     *
     * @param options розібрані параметри запуску
     * @return код завершення програми (0 — успіх, 1 — помилка вводу/виводу)
     */
    public int run(CliOptions options) {
        if (options.showHelp()) {
            System.out.printf(
                    "Використання: java -jar lab01.jar [--help] [--version] [--input <файл>] [--output <файл>]%n");
            return 0;
        }
        if (options.showVersion()) {
            System.out.printf("Версія програми: %s%n", CliOptions.readProjectVersion());
            return 0;
        }

        try {
            List<String> lines = readLines(options.inputPath());
            String report = buildReport(lines);
            System.out.print(report);
            writeReport(options.outputPath(), report);
            return 0;
        } catch (IOException e) {
            System.err.printf("Помилка роботи з файлом: %s%n", e.getMessage());
            return 1;
        }
    }

    /**
     * Читає всі рядки файла у кодуванні UTF-8.
     *
     * @param input шлях до вхідного файла
     * @return список рядків
     * @throws IOException якщо файл неможливо прочитати
     */
    public List<String> readLines(Path input) throws IOException {
        return Files.readAllLines(input, StandardCharsets.UTF_8);
    }

    /**
     * Записує звіт у файл у кодуванні UTF-8.
     *
     * @param output шлях до файла звіту
     * @param report текст звіту
     * @throws IOException якщо неможливо створити каталог або записати файл
     */
    public void writeReport(Path output, String report) throws IOException {
        Path parent = output.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(output, report, StandardCharsets.UTF_8);
    }

    /**
     * Обчислює підсумкові показники за списком коректних об'єктів Plant.
     *
     * @param validPlants список перевірених рослин
     * @return незмінний підсумок PlantSummary
     */
    public PlantSummary calculateSummary(List<Plant> validPlants) {
        if (validPlants.isEmpty()) {
            return new PlantSummary(0, 0.0, 0.0, "немає", 0);
        }

        double totalHeight = 0.0;
        double maxPrice = Double.NEGATIVE_INFINITY;
        String mostExpensiveName = "немає";
        int minWateringDays = Integer.MAX_VALUE;

        for (Plant plant : validPlants) {
            HeightWatering hw = plant.getHeightWatering();
            totalHeight += hw.heightCm();
            if (plant.getPrice() > maxPrice) {
                maxPrice = plant.getPrice();
                mostExpensiveName = plant.getName();
            }
            if (hw.wateringDays() < minWateringDays) {
                minWateringDays = hw.wateringDays();
            }
        }

        double averageHeight = totalHeight / validPlants.size();
        return new PlantSummary(validPlants.size(), averageHeight, maxPrice, mostExpensiveName, minWateringDays);
    }

    /**
     * Розбирає CSV-рядки через фабрику Plant.fromCsv, обчислює підсумок і формує текст звіту.
     *
     * @param lines рядки вхідного CSV-файла
     * @return відформатований звіт
     */
    public String buildReport(List<String> lines) {
        List<Plant> validPlants = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (int index = 0; index < lines.size(); index++) {
            int lineNumber = index + 1;
            try {
                validPlants.add(Plant.fromCsv(lines.get(index)));
            } catch (IllegalArgumentException exception) {
                errors.add(String.format(Locale.ROOT, "Рядок %d: %s", lineNumber, exception.getMessage()));
            }
        }

        PlantSummary summary = calculateSummary(validPlants);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.ROOT, "%-30s %s%n", "Показник", "Значення"));
        sb.append(String.format(Locale.ROOT, "%-30s %d%n", "Коректних записів:", summary.validCount()));
        sb.append(String.format(Locale.ROOT, "%-30s %.2f см%n", "Середня висота:", summary.averageHeight()));
        sb.append(String.format(
                Locale.ROOT, "%-30s %.2f грн (%s)%n", "Найдорожча рослина:",
                summary.maxPrice(), summary.mostExpensiveName()));
        sb.append(String.format(
                Locale.ROOT, "%-30s %d дн.%n", "Найменший інтервал поливу:", summary.minWateringDays()));
        sb.append(String.format(Locale.ROOT, "%-30s %d%n", "Помилок:", errors.size()));

        for (String error : errors) {
            sb.append(String.format(Locale.ROOT, "%s%n", error));
        }

        return sb.toString();
    }
}