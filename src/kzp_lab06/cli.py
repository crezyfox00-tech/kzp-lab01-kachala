import argparse
import sys
from pathlib import Path
from .analysis import make_summary
from .errors import DataError
from .loader import load_records
from .reporting import format_report, write_report


def _print_data_error(error: DataError) -> None:
    """Виводить помилку даних у стандартний потік помилок із підтримкою UTF-8."""
    try:
        sys.stderr.reconfigure(encoding="utf-8")
    except (AttributeError, ValueError):
        pass
    print(f"Помилка даних: {error}", file=sys.stderr)


def main(argv: list[str] | None = None) -> int:
    """Обробляє аргументи CLI, завантажує дані, аналізує та записує звіт."""
    parser = argparse.ArgumentParser(description="Обробка даних розсадника рослин (Варіант 12)")
    parser.add_argument("input_csv", type=Path, help="Шлях до вхідного CSV-файлу")
    parser.add_argument("output_report", type=Path, help="Шлях до вихідного текстового звіту")
    args = parser.parse_args(argv)

    try:
        records = load_records(args.input_csv)
        summary = make_summary(records, "price", price="Сумарна ціна")
        text = format_report(summary)
        write_report(args.output_report, text)
    except DataError as error:
        _print_data_error(error)
        return 2
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
