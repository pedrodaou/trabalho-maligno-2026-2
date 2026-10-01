package br.edu.grupo10.ordenacao;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParallelMergeSortTest {
    @Test
    void ordenaCorretamenteComDiferentesQuantidadesDeThreads() throws InterruptedException {
        int[] quantidades = {1, 2, 3, 4, 7, 16};
        Random random = new Random(10);

        for (int quantidade : quantidades) {
            byte[] vetor = new byte[10_003];
            random.nextBytes(vetor);
            byte[] esperado = vetor.clone();
            Arrays.sort(esperado);

            new ParallelMergeSort(quantidade).ordenar(vetor);

            assertArrayEquals(esperado, vetor,
                    "Falhou com " + quantidade + " threads");
        }
    }

    @Test
    void funcionaQuandoHaMaisThreadsDoQueElementos() throws InterruptedException {
        byte[] vetor = {3, 1, 2};

        new ParallelMergeSort(8).ordenar(vetor);

        assertArrayEquals(new byte[]{1, 2, 3}, vetor);
    }

    @Test
    void quantidadePadraoSegueRegraDoEnunciado() {
        int processadores = Runtime.getRuntime().availableProcessors();
        int esperado = Math.max(1, processadores - 1);

        assertTrue(processadores >= 1);
        assertTrue(ParallelMergeSort.calcularQuantidadeThreads() == esperado);
    }

    @Test
    void rejeitaQuantidadeInvalidaDeThreads() {
        assertThrows(IllegalArgumentException.class, () -> new ParallelMergeSort(0));
    }
}

