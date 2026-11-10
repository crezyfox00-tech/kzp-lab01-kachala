package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PlantTest {
    @Test
    void testValidTree() {
        Tree t = new Tree("Хвойні", "Туя", 150.0, 450.0, 7, 12.0);
        assertEquals("Туя", t.getName());
        assertEquals(PlantKind.TREE, t.getKind());
        assertEquals(18.0, t.careRequirement(), 0.001);
    }

    @Test
    void testValidFlower() {
        Flower f = new Flower("Кімнатні", "Фікус", 60.0, 300.0, 3, 6);
        assertEquals("Фікус", f.getName());
        assertEquals(PlantKind.FLOWER, f.getKind());
        assertEquals(60.0, f.careRequirement(), 0.001);
    }

    @Test
    void testInvalidFields() {
        assertThrows(IllegalArgumentException.class, () -> new Tree("", "Туя", 150.0, 450.0, 7, 12.0));
        assertThrows(IllegalArgumentException.class, () -> new Flower("Кімнатні", "Фікус", -10.0, 300.0, 3, 6));
    }

    @Test
    void testEqualsAndHashCode() {
        Tree t1 = new Tree("Хвойні", "Туя", 150.0, 450.0, 7, 12.0);
        Flower f1 = new Flower("Декоративні", "Туя", 60.0, 300.0, 3, 6);
        Tree t2 = new Tree("Хвойні", "Туя", 200.0, 500.0, 10, 15.0);
        assertEquals(t1, t2);
        assertNotEquals(t1, f1);
        assertEquals(t1.hashCode(), t2.hashCode());
    }
}
