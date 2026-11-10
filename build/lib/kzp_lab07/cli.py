import argparse
import sys
from kzp_lab07.loader import load_plants
from kzp_lab07.analysis import filter_and_analyze
from kzp_lab07.reporting import generate_report
from kzp_lab07.errors import DataError


def main() -> int:
    parser = argparse.ArgumentParser(
        description="Обробка даних розсадника рослин (Лабораторна робота №7)"
    )
    parser.add_argument("input", help="Шлях до вхідного CSV-файлу")
    parser.add_argument("output", help="Шлях для збереження текстового звіту")

    args = parser.parse_args()

    try:
        plants = load_plants(args.input)
        selected, avg_prices, summary = filter_and_analyze(plants)
        generate_report(selected, avg_prices, summary, args.output)
        print(f"Звіт успішно згенеровано: {args.output}")
        return 0
    except DataError as e:
        print(f"Помилка даних: {e}", file=sys.stderr)
        return 2
    except Exception as e:
        print(f"Неочікувана помилка: {e}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    sys.exit(main())
