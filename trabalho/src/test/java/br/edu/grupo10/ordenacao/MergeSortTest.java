package br.edu.grupo10.ordenacao;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MergeSortTest {
    @Test
    void ordenaVetorComNegativosRepetidosELimites() {
        byte[] vetor = {5, -1, 5, Byte.MIN_VALUE, 0, Byte.MAX_VALUE, -1};
        byte[] esperado = vetor.clone();
        Arrays.sort(esperado);

        MergeSort.ordenar(vetor);

        assertArrayEquals(esperado, vetor);
    }

    @Test
    void aceitaVetorVazioEUnitario() {
        byte[] vazio = {};
        byte[] unitario = {42};

        MergeSort.ordenar(vazio);
        MergeSort.ordenar(unitario);

        assertArrayEquals(new byte[0], vazio);
        assertArrayEquals(new byte[]{42}, unitario);
    }

    @Test
    void rejeitaVetorNulo() {
        assertThrows(NullPointerException.class, () -> MergeSort.ordenar(null));
    }

    @Test
    void coincideComArraysSortEmVetorAleatorioGrande() {
        byte[] vetor = new byte[100_000];
        new Random(10).nextBytes(vetor);
        byte[] esperado = vetor.clone();
        Arrays.sort(esperado);

        MergeSort.ordenar(vetor);

        assertArrayEquals(esperado, vetor);
    }
}

