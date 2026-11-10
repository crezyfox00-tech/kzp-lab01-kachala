import dataclasses
import pytest
from kzp_lab07.models import Tree, Flower, PlantSummary
from kzp_lab07.loader import load_plants
from kzp_lab07.errors import DataError


def test_property_and_setter_validation() -> None:
    """Перевіряє роботу властивостей та винятки у setter."""
    tree = Tree("Сакура", 3, 150.0, 1250.0, 2)
    assert tree.species == "Сакура"
    assert tree.age == 3

    with pytest.raises(ValueError):
        tree.age = -1
    with pytest.raises(ValueError):
        tree.height = -10.0
    with pytest.raises(ValueError):
        tree.watering_days = -5


def test_subtypes_different_care_scores() -> None:
    """Перевіряє поліморфну поведінку підтипів Tree та Flower."""
    tree = Tree("Дуб", 10, 300.0, 2500.0, 1)
    flower = Flower("Троянда", 1, 40.0, 300.0, 1)

    assert tree.care_score() != flower.care_score()


def test_summary_is_immutable() -> None:
    """Перевіряє незмінність frozen dataclass."""
    summary = PlantSummary(count=4, average_price=350.0, tallest_plant=None)
    with pytest.raises(dataclasses.FrozenInstanceError):
        summary.count = 5


def test_loader_missing_file() -> None:
    """Перевіряє обробку відсутності файлу."""
    with pytest.raises(DataError):
        load_plants("non_existent_file.csv")
