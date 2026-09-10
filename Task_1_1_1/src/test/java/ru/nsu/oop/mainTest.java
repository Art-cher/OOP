package ru.nsu.oop;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class mainTest {
    @Test
    void testMain() {
        // покрывает весь код внутри main, включая цикл замера времени
        Main.main(new String[]{});
    }
}
