package ua.lpnu.kzp;

public final class Flower extends Plant {
    private final int bloomingMonths;

    public Flower(String species, String name, double heightCm, double price, int wateringDays, int bloomingMonths) {
        super(species, name, PlantKind.FLOWER, heightCm, price, wateringDays);
        if (bloomingMonths <= 0) {
            throw new IllegalArgumentException("Кількість місяців цвітіння має бути додатною");
        }
        this.bloomingMonths = bloomingMonths;
    }

    @Override
    public double careRequirement() {
        return bloomingMonths * 10.0;
    }
}