from collections.abc import Iterable


def available_plants(records: Iterable[dict[str, object]]) -> list[dict[str, object]]:
    """Відбирає рослини з періодичністю поливу <= 2 дні (вимога Варіанта 12)."""
    return [record for record in records if int(record["watering_days"]) <= 2]


def average_price_by_species(records: list[dict[str, object]]) -> dict[str, float]:
    """Обчислює середню ціну за видом рослини у стабільному порядку."""
    species_set = {str(record["species"]) for record in records}
    result = {}
    for sp in sorted(species_set):
        subset = [float(r["price"]) for r in records if r["species"] == sp]
        result[sp] = sum(subset) / len(subset) if subset else 0.0
    return result


def make_summary(
    records: list[dict[str, object]],
    *metrics: str,
    max_watering_days: int = 2,
    **labels: str,
) -> dict[str, object]:
    """Формує підсумок, множину видів та метрики для відібраних рослин."""
    selected = [record for record in records if int(record["watering_days"]) <= max_watering_days]

    species_set = {str(record["species"]) for record in selected}
    avg_prices = average_price_by_species(selected)

    # Найвища рослина (для звіту Варіанта 12)
    tallest = max(selected, key=lambda r: float(r["height"]), default=None)

    result: dict[str, object] = {
        "count": len(selected),
        "species_set": species_set,
        "average_price_by_species": avg_prices,
        "tallest_plant": tallest,
    }

    for metric in metrics:
        if selected and any(metric not in record for record in selected):
            raise ValueError(f"Невідома метрика: {metric}")
        result[labels.get(metric, metric)] = (
            sum(float(record[metric]) for record in selected) if selected else 0.0
        )

    return result
