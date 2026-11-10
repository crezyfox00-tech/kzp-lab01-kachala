import csv
from pathlib import Path
from .errors import DataError

REQUIRED_FIELDS = {"species", "age", "height", "price", "watering_days"}


def load_records(path: Path) -> list[dict[str, object]]:
    """Читає CSV-файл, перетворює поля та перевіряє записи для розсадника рослин."""
    try:
        with path.open("r", encoding="utf-8", newline="") as source:
            reader = csv.DictReader(source)
            if reader.fieldnames is None or set(reader.fieldnames) != REQUIRED_FIELDS:
                raise DataError("Некоректний заголовок CSV (очікуються поля розсадника)")

            records: list[dict[str, object]] = []
            for row_number, row in enumerate(reader, start=2):
                try:
                    species = row["species"].strip()
                    age = int(row["age"])
                    height = float(row["height"])
                    price = float(row["price"])
                    watering_days = int(row["watering_days"])
                except (AttributeError, KeyError, TypeError, ValueError) as error:
                    raise DataError(f"Рядок {row_number}: некоректне або пропущене поле") from error

                if not species:
                    raise DataError(f"Рядок {row_number}: назва виду рослини порожня")
                if age < 0 or height <= 0 or price < 0 or watering_days < 0:
                    raise DataError(
                        f"Рядок {row_number}: числові параметри не можуть бути від'ємними"
                    )

                records.append(
                    {
                        "species": species,
                        "age": age,
                        "height": height,
                        "price": price,
                        "watering_days": watering_days,
                    }
                )
            return records
    except OSError as error:
        raise DataError(f"Не вдалося прочитати файл: {path}") from error
