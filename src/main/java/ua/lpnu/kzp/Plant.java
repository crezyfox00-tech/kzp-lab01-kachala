package ua.lpnu.kzp;

/**
 * Представляє один коректний запис про рослину в розсаднику (Варіант 12).
 *
 * @param species      вид рослини
 * @param name         назва рослини
 * @param heightCm     висота в сантиметрах
 * @param price        ціна в гривнях
 * @param wateringDays інтервал поливу в днях
 */
public record Plant(
        String species,
        String name,
        double heightCm,
        double price,
        int wateringDays
) {
}