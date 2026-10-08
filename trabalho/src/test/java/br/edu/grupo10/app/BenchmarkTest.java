package br.edu.grupo10.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class BenchmarkTest {
    @Test
    void calculaMedianaSemAlterarAmostras() {
        double[] amostras = {100, 1, 2};
        assertEquals(2, Benchmark.mediana(amostras));
        assertArrayEquals(new double[]{100, 1, 2}, amostras);
        assertEquals(2.5, Benchmark.mediana(new double[]{4, 1, 3, 2}));
    }
}
