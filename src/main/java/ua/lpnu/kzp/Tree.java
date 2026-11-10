package ua.lpnu.kzp;

public final class Tree extends Plant {
    private final double trunkDiameterCm;

    public Tree(String species, String name, double heightCm, double price, int wateringDays, double trunkDiameterCm) {
        super(species, name, PlantKind.TREE, heightCm, price, wateringDays);
        if (trunkDiameterCm <= 0) {
            throw new IllegalArgumentException("Діаметр стовбура має бути додатним");
        }
        this.trunkDiameterCm = trunkDiameterCm;
    }

    @Override
    public double careRequirement() {
        return trunkDiameterCm * 1.5;
    }
}