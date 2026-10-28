package ua.lpnu.kzp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;

/**
 * Відповідає за розбір аргументів командного рядка та зчитування версії продукту з ресурсів Maven.
 *
 * @param inputPath   шлях до вхідного CSV-файла
 * @param outputPath  шлях до вихідного файла звіту
 * @param showHelp    ознака виведення довідки
 * @param showVersion ознака виведення версії
 */
public record CliOptions(
        Path inputPath,
        Path outputPath,
        boolean showHelp,
        boolean showVersion
) {

    /**
     * Перевіряє, що шляхи не є null.
     */
    public CliOptions {
        Objects.requireNonNull(inputPath, "Шлях входу не може бути null");
        Objects.requireNonNull(outputPath, "Шлях виходу не може бути null");
    }

    /**
     * Розбирає масив аргументів командного рядка.
     *
     * @param args аргументи командного рядка
     * @return об'єкт налаштувань запуску CliOptions
     */
    public static CliOptions parse(String[] args) {
        Path input = Path.of("data", "input.csv");
        Path output = Path.of("out", "report.txt");
        boolean help = false;
        boolean version = false;

        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if ("--help".equals(args[i])) {
                    help = true;
                } else if ("--version".equals(args[i])) {
                    version = true;
                } else if ("--input".equals(args[i]) && i + 1 < args.length) {
                    input = Path.of(args[++i]);
                } else if ("--output".equals(args[i]) && i + 1 < args.length) {
                    output = Path.of(args[++i]);
                }
            }
        }
        return new CliOptions(input, output, help, version);
    }

    /**
     * Автоматично зчитує версію програми, підставлену Maven із pom.xml під час збірки.
     *
     * @return рядок версії проєкту
     */
    public static String readProjectVersion() {
        try (InputStream stream = CliOptions.class.getResourceAsStream("/app.properties")) {
            if (stream != null) {
                Properties props = new Properties();
                props.load(stream);
                return props.getProperty("app.version", "unknown");
            }
        } catch (IOException ignored) {
            // Якщо ресурс недоступний, повертаємо запасне значення
        }
        return "unknown";
    }
}