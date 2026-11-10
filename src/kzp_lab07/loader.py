import csv
from pathlib import Path
from kzp_lab07.errors import DataError
from kzp_lab07.models import Plant, Tree, Flower


def load_plants(file_path: str | Path) -> list[Plant]:
    """Читає CSV-файл та перетворює рядки на об'єкти Tree чи Flower."""
    path = Path(file_path)
    if not path.exists():
        raise DataError(f"Файл не знайдено: {path}")

    plants: list[Plant] = []
    try:
        with open(path, encoding="utf-8") as f:
            reader = csv.DictReader(f)
            expected_fields = {"species", "age", "height", "price", "watering_days"}
            if not reader.fieldnames or not expected_fields.issubset(reader.fieldnames):
                raise DataError("Некоректні заголовки CSV-файлу")

            for row_idx, row in enumerate(reader, start=2):
                try:
                    species = row["species"].strip()
                    age = int(row["age"])
                    height = float(row["height"])
                    price = float(row["price"])
                    watering_days = int(row["watering_days"])

                    kind = row.get("kind", "").strip().lower()
                    if kind == "tree" or (not kind and height > 100.0):
                        plant = Tree(species, age, height, price, watering_days)
                    else:
                        plant = Flower(species, age, height, price, watering_days)

                    plants.append(plant)
                except (ValueError, KeyError) as e:
                    raise DataError(f"Помилка у рядку {row_idx}: {e}") from e
    except OSError as e:
        raise DataError(f"Помилка читання файлу: {e}") from e

    return plants
