package ua.lpnu.kzp;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Головний клас запуску програми. Відповідає лише за ініціалізацію парсера параметрів і запуск контролера.
 */
public final class Main {

    /** Забороняє створення екземплярів службового класу. */
    private Main() {
    }

    /**
     * Точка входу до програми.
     *
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
        CliOptions options = CliOptions.parse(args);
        NurseryController controller = new NurseryController();
        int exitCode = controller.run(options);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    /**
     * Делегує побудову звіту контролеру для збереження зворотної сумісності з тестами Лабораторної роботи № 1.
     *
     * @param lines рядки CSV-файла
     * @return текст сформованого звіту
     */
    public static String buildReport(List<String> lines) {
        return new NurseryController().buildReport(lines);
    }
}