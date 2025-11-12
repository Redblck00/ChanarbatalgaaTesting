import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import src.main.BubbleSort;

class BubbleSortTest {

    @Test
    @DisplayName("T1: Аль хэдийн эрэмбэлэгдсэн массив")
    void testAlreadySorted() {
        int[] input = { 1, 2, 3, 4 };
        int[] expected = { 1, 2, 3, 4 };
        int[] result = BubbleSort.bubbleSort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("T2: 1 swap хийгдэнэ")
    void testSingleSwap() {
        int[] input = { 2, 1 };
        int[] expected = { 1, 2 };
        int[] result = BubbleSort.bubbleSort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("T3: Олон swap, recursion дуудагдана")
    void testMultipleSwapsWithRecursion() {
        int[] input = { 3, 2, 1 };
        int[] expected = { 1, 2, 3 };
        int[] result = BubbleSort.bubbleSort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("T4: Хоосон массив")
    void testEmptyArray() {
        int[] input = {};
        int[] expected = {};
        int[] result = BubbleSort.bubbleSort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("T5: 1 элемент")
    void testSingleElement() {
        int[] input = { 5 };
        int[] expected = { 5 };
        int[] result = BubbleSort.bubbleSort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("T6: Урвуу эрэмбэлэгдсэн")
    void testReverseSorted() {
        int[] input = { 5, 4, 3, 2, 1 };
        int[] expected = { 1, 2, 3, 4, 5 };
        int[] result = BubbleSort.bubbleSort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("T7: Давхардсан утгууд")
    void testDuplicates() {
        int[] input = { 3, 1, 2, 1, 3 };
        int[] expected = { 1, 1, 2, 3, 3 };
        int[] result = BubbleSort.bubbleSort(input);
        assertArrayEquals(expected, result);
    }
}