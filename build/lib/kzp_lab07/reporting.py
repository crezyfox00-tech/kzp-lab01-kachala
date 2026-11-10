from pathlib import Path
from kzp_lab07.models import Plant, PlantSummary


def generate_report(
    selected: list[Plant],
    avg_prices: dict[str, float],
    summary: PlantSummary,
    output_path: str | Path,
) -> None:
    """Зберігає текстовий звіт у файл у форматі UTF-8."""
    path = Path(output_path)
    path.parent.mkdir(parents=True, exist_ok=True)

    species_set = {p.species for p in selected}

    lines = [f"Унікальні види: {', '.join(sorted(species_set))}", "Середня ціна за видом:"]
    for sp, pr in avg_prices.items():
        lines.append(f" - {sp}: {pr:.2f}")

    if summary.tallest_plant:
        tp = summary.tallest_plant
        lines.append(f"Найвища рослина: {tp.species} (висота: {tp.height} см, вік: {tp.age} р.)")
    else:
        lines.append("Найвища рослина: відсутня")

    with open(path, "w", encoding="utf-8", newline="\n") as f:
        f.write("\n".join(lines) + "\n")
