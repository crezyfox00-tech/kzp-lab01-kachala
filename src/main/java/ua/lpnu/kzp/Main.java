package ua.lpnu.kzp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Головний клас консольної програми для обробки даних розсадника рослин (Варіант 12).
 */
public final class Main {

    /** Забороняє створення екземплярів службового класу. */
    private Main() {
    }

    /**
     * Точка входу до програми. Підтримує аргументи --help, --input та --output.
     *
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        Path inputPath = Path.of("data", "input.csv");
        Path outputPath = Path.of("out", "report.txt");

        for (int i = 0; i < args.length; i++) {
            if ("--help".equals(args[i])) {
                System.out.printf("Використання: java -jar lab01.jar [--help] [--input <файл>] [--output <файл>]%n");
                return;
            } else if ("--input".equals(args[i]) && i + 1 < args.length) {
                inputPath = Path.of(args[++i]);
            } else if ("--output".equals(args[i]) && i + 1 < args.length) {
                outputPath = Path.of(args[++i]);
            }
        }

        try {
            List<String> lines = readLines(inputPath);
            String report = buildReport(lines);
            System.out.print(report);
            writeReport(outputPath, report);
        } catch (IOException e) {
            System.err.printf("Помилка роботи з файлом: %s%n", e.getMessage());
        }
    }

    /**
     * Читає всі рядки вхідного файла в кодуванні UTF-8.
     *
     * @param input шлях до вхідного файла
     * @return список рядків файла
     * @throws IOException якщо файл не існує або не може бути прочитаний
     */
    public static List<String> readLines(Path input) throws IOException {
        return Files.readAllLines(input, StandardCharsets.UTF_8);
    }

    /**
     * Записує сформований звіт у файл у кодуванні UTF-8, створюючи батьківські каталоги за потреби.
     *
     * @param output шлях до файла звіту
     * @param report текст звіту
     * @throws IOException якщо неможливо створити каталог або записати файл
     */
    public static void writeReport(Path output, String report) throws IOException {
        Path parent = output.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(output, report, StandardCharsets.UTF_8);
    }

    /**
     * Перевіряє рядки CSV, обчислює чотири показники варіанта 12 та формує текст звіту.
     *
     * @param lines вхідні рядки з CSV-файла
     * @return відформатований текст звіту з використанням Locale.ROOT
     */
    public static String buildReport(List<String> lines) {
        List<String> errors = new ArrayList<>();
        List<Plant> validPlants = new ArrayList<>();

        for (int index = 0; index < lines.size(); index++) {
            int lineNumber = index + 1;
            String line = lines.get(index);

            if (line == null || line.isBlank()) {
                errors.add(String.format(Locale.ROOT, "Рядок %d: порожній рядок", lineNumber));
                continue;
            }

            String[] fields = line.split(";", -1);
            if (fields.length != 5) {
                errors.add(String.format(Locale.ROOT, "Рядок %d: очікується 5 полів, отримано %d", lineNumber, fields.length));
                continue;
            }

            String species = fields[0].trim();
            String name = fields[1].trim();
            if (species.isEmpty() || name.isEmpty()) {
                errors.add(String.format(Locale.ROOT, "Рядок %d: порожній вид або назва рослини", lineNumber));
                continue;
            }

            try {
                double heightCm = Double.parseDouble(fields[2].trim());
                double price = Double.parseDouble(fields[3].trim());
                int wateringDays = Integer.parseInt(fields[4].trim());

                if (heightCm <= 0 || price < 0 || wateringDays <= 0) {
                    errors.add(String.format(Locale.ROOT, "Рядок %d: недопустиме від'ємне або нульове числове значення", lineNumber));
                    continue;
                }

                validPlants.add(new Plant(species, name, heightCm, price, wateringDays));
            } catch (NumberFormatException ex) {
                errors.add(String.format(Locale.ROOT, "Рядок %d: числове поле має помилковий формат", lineNumber));
            }
        }

        int validCount = validPlants.size();
        double totalHeight = 0.0;
        double maxPrice = 0.0;
        String mostExpensiveName = "немає";
        int minWateringDays = 0;

        if (validCount > 0) {
            maxPrice = Double.NEGATIVE_INFINITY;
            minWateringDays = Integer.MAX_VALUE;

            for (Plant plant : validPlants) {
                totalHeight += plant.heightCm();
                if (plant.price() > maxPrice) {
                    maxPrice = plant.price();
                    mostExpensiveName = plant.name();
                }
                if (plant.wateringDays() < minWateringDays) {
                    minWateringDays = plant.wateringDays();
                }
            }
        }

        double averageHeight = validCount == 0 ? 0.0 : totalHeight / validCount;

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.ROOT, "%-30s %s%n", "Показник", "Значення"));
        sb.append(String.format(Locale.ROOT, "%-30s %d%n", "Коректних записів:", validCount));
        sb.append(String.format(Locale.ROOT, "%-30s %.2f см%n", "Середня висота:", averageHeight));
        sb.append(String.format(Locale.ROOT, "%-30s %.2f грн (%s)%n", "Найдорожча рослина:", maxPrice, mostExpensiveName));
        sb.append(String.format(Locale.ROOT, "%-30s %d дн.%n", "Найменший інтервал поливу:", minWateringDays));
        sb.append(String.format(Locale.ROOT, "%-30s %d%n", "Помилок:", errors.size()));

        for (String error : errors) {
            sb.append(String.format(Locale.ROOT, "%s%n", error));
        }

        return sb.toString();
    }
}