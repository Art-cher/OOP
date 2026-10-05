package ru.nsu.oop.parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Парсер строки означивания переменных вида {@code "x = 10; y = 13"}.
 */
public class VarsParser {

    /**
     * Разбирает строку означивания в отображение «имя переменной → значение».
     *
     * <p>Если строка {@code null} или пустая — возвращается пустое отображение.</p>
     *
     * @param vars строка с присваиваниями, разделёнными {@code ';'}
     * @return отображение имён переменных в их целочисленные значения
     * @throws IllegalArgumentException если присваивание имеет неверный формат,
     *                                  имя переменной пусто или значение
     *                                  не является целым числом
     */
    public static Map<String, Integer> parseVars(String vars) {
        Map<String, Integer> map = new HashMap<>();
        if (vars == null || vars.isBlank()) {
            return map;
        }

        List<String> parts = new ArrayList<>(List.of(vars.split(";"))); 

        for (String part : parts) {
            List<String> pair = new ArrayList<>(List.of(part.split("=")));

            if (pair.size() != 2) {
                throw new IllegalArgumentException(
                        "Некорректное присваивание: \"" + part
                                + "\". Ожидается формат \"имя = значение\""
                );
            }

            String key = pair.get(0).trim();
            if (key.isEmpty()) {
                throw new IllegalArgumentException(
                        "Пустое имя переменной в \"" + part + "\""
                );
            }



            int value;
            String rawValue = pair.get(1).trim();
            try {
                value = Integer.parseInt(rawValue);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Значение переменной \"" + key
                                + "\" не является целым числом: \"" + rawValue + "\""
                );
            }

            map.put(key, value);
        }

        return map;
    }
}