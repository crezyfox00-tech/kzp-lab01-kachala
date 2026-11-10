from pathlib import Path
from kzp_lab06.analysis import average_price_by_species, make_summary
from kzp_lab06.reporting import write_report


def test_average_price_by_species_deterministic() -> None:
    """Перевіряє стабільний розрахунок середньої ціни за видами."""
    records = [
        {"species": "Туя", "age": 2, "height": 80.0, "price": 400.0, "watering_days": 2},
        {"species": "Туя", "age": 3, "height": 100.0, "price": 600.0, "watering_days": 1},
        {"species": "Сакура", "age": 4, "height": 150.0, "price": 1200.0, "watering_days": 2},
    ]
    result = average_price_by_species(records)
    assert result == {"Сакура": 1200.0, "Туя": 500.0}


def test_make_summary_with_args_and_kwargs() -> None:
    """Перевіряє правильність обчислень та роботу з додатковими метриками."""
    records = [
        {"species": "Туя", "age": 2, "height": 80.0, "price": 500.0, "watering_days": 2},
    ]
    summary = make_summary(records, "price", price="Ціна")
    assert summary["count"] == 1
    assert summary["Ціна"] == 500.0


def test_write_report(tmp_path: Path) -> None:
    """Перевіряє коректність запису звіту у тимчасовий каталог через with."""
    output = tmp_path / "sub" / "report.txt"
    write_report(output, "Тестовий звіт\n")
    assert output.read_text(encoding="utf-8") == "Тестовий звіт\n"
