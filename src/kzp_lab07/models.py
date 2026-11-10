from dataclasses import dataclass
from typing import Protocol


class Cared(Protocol):
    def care_score(self) -> float: ...


class Plant:
    def __init__(
        self, species: str, age: int, height: float, price: float, watering_days: int
    ) -> None:
        self.species = species
        self.age = age
        self.height = height
        self.price = price
        self.watering_days = watering_days

    @property
    def species(self) -> str:
        return self._species

    @species.setter
    def species(self, value: str) -> None:
        if not value or not value.strip():
            raise ValueError("Назва виду не може бути порожньою")
        self._species = value.strip()

    @property
    def age(self) -> int:
        return self._age

    @age.setter
    def age(self, value: int) -> None:
        if value < 0:
            raise ValueError("Вік не може бути від'ємним")
        self._age = value

    @property
    def height(self) -> float:
        return self._height

    @height.setter
    def height(self, value: float) -> None:
        if value < 0:
            raise ValueError("Висота не може бути від'ємною")
        self._height = value

    @property
    def price(self) -> float:
        return self._price

    @price.setter
    def price(self, value: float) -> None:
        if value < 0:
            raise ValueError("Ціна не може бути від'ємною")
        self._price = value

    @property
    def watering_days(self) -> int:
        return self._watering_days

    @watering_days.setter
    def watering_days(self, value: int) -> None:
        if value < 0:
            raise ValueError("Дні поливу не можуть бути від'ємними")
        self._watering_days = value

    def care_score(self) -> float:
        raise NotImplementedError

    def __str__(self) -> str:
        return f"{self.species} (вік: {self.age}р., висота: {self.height}см)"

    def __repr__(self) -> str:
        cls_name = type(self).__name__
        p1 = f"species={self.species!r}, age={self.age!r}"
        p2 = f"height={self.height!r}, price={self.price!r}, watering_days={self.watering_days!r}"
        return f"{cls_name}({p1}, {p2})"

    def __eq__(self, other: object) -> bool:
        if not isinstance(other, Plant):
            return NotImplemented
        return type(self) is type(other) and self.__dict__ == other.__dict__


class Tree(Plant):
    def care_score(self) -> float:
        days = self.watering_days if self.watering_days > 0 else 1
        return (self.height / days) + (self.age * 2)


class Flower(Plant):
    def care_score(self) -> float:
        days = self.watering_days if self.watering_days > 0 else 1
        return (self.price / (days + 1)) + self.height


@dataclass(frozen=True)
class PlantSummary:
    count: int
    average_price: float
    tallest_plant: Plant | None
