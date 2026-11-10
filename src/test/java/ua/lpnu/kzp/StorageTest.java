package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class StorageTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void repositoryIsGenericAndFindsRecords() {
        Repository<PlantRecord> repository = new Repository<>();
        repository.add(new PlantRecord("Сакура японська", 3, 150.0, 1250.0, 3));
        repository.add(new PlantRecord("Туя західна", 2, 80.0, 450.0, 5));

        assertEquals(1, repository.find(item -> item.ageYears() == 3).size());
        assertEquals(2, repository.all().size());
    }

    @Test
    void exportEscapesCommaAndRoundTripPreservesData() throws Exception {
        List<PlantRecord> original = List.of(
                new PlantRecord("Сакура, декоративна", 3, 150.0, 1250.0, 3),
                new PlantRecord("Туя західна", 2, 80.0, 450.0, 5)
        );
        Path path = temporaryDirectory.resolve("plants.csv");
        CsvExporter.write(path, PlantRecord.class, original);

        String csv = Files.readString(path, StandardCharsets.UTF_8);
        List<List<String>> rows = CsvParser.parseDocument(csv);
        List<PlantRecord> restored = rows.stream()
                .skip(1)
                .map(fields -> {
                    try {
                        return PlantRecord.fromFields(fields);
                    } catch (DataStorageException exception) {
                        throw new IllegalStateException(exception);
                    }
                })
                .toList();

        assertEquals(3, rows.size());
        assertTrue(csv.contains("\"Сакура, декоративна\""));
        assertEquals(original, restored);
    }

    @Test
    void wrapsFileErrorWithCause() throws Exception {
        Path directory = temporaryDirectory.resolve("not-a-file");
        Files.createDirectory(directory);
        DataStorageException exception = assertThrows(DataStorageException.class,
                () -> CsvExporter.write(directory, PlantRecord.class, List.of()));
        assertTrue(exception.getCause() instanceof java.io.IOException);
    }
}