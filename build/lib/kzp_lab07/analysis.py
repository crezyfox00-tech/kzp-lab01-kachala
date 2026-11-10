from collections import defaultdict
from kzp_lab07.models import Plant, PlantSummary


def filter_and_analyze(
    plants: list[Plant], max_watering_days: int = 2
) -> tuple[list[Plant], dict[str, float], PlantSummary]:
    """Фільтрує рослини (watering_days <= 2), рахує середні ціни та формує підсумок."""
    selected = [p for p in plants if p.watering_days <= max_watering_days]

    price_by_species = defaultdict(list)
    for p in selected:
        price_by_species[p.species].append(p.price)

    avg_price_by_species = {
        sp: sum(prices) / len(prices) for sp, prices in sorted(price_by_species.items())
    }

    tallest = max(selected, key=lambda x: x.height, default=None)

    summary = PlantSummary(
        count=len(selected),
        average_price=sum(p.price for p in selected) / len(selected) if selected else 0.0,
        tallest_plant=tallest,
    )

    return selected, avg_price_by_species, summary
