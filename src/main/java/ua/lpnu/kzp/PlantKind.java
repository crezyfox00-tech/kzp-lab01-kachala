package ua.lpnu.kzp;

public enum PlantKind {
    TREE("дерево"),
    FLOWER("квітка");

    private final String label;

    PlantKind(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
