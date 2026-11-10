from pathlib import Path


def format_report(summary: dict[str, object]) -> str:
    """Формує детермінований текстовий звіт для розсадника рослин (Варіант 12)."""
    lines = [f"Записів у звіті (полив <= 2 дні): {summary['count']}"]

    species_set = summary["species_set"]
    lines.append(f"Унікальні види: {', '.join(sorted(species_set))}")

    avg_prices = summary["average_price_by_species"]
    lines.append("Середня ціна за видом:")
    for sp, price in sorted(avg_prices.items()):
        lines.append(f"  - {sp}: {price:.2f}")

    tallest = summary.get("tallest_plant")
    if tallest:
        species_name = tallest["species"]
        height_val = tallest["height"]
        age_val = tallest["age"]
        lines.append(
            f"Найвища рослина: {species_name} (висота: {height_val} см, вік: {age_val} р.)"
        )
    else:
        lines.append("Найвища рослина: відсутня")

    return "\n".join(lines) + "\n"


def write_report(path: Path, text: str) -> None:
    """Створює каталог за потреби та записує звіт у кодуванні UTF-8 із newline='\n'."""
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("w", encoding="utf-8", newline="\n") as target:
        target.write(text)
