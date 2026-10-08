package br.edu.grupo10.ordenacao;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

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
        assertTrue(processadores >= 1);
        if (processadores == 1) {
            assertThrows(IllegalStateException.class, ParallelMergeSort::calcularQuantidadeThreads);
        } else {
            assertTrue(ParallelMergeSort.calcularQuantidadeThreads() == processadores - 1);
        }
    }

    @Test
    void rejeitaQuantidadeInvalidaDeThreads() {
        assertThrows(IllegalArgumentException.class, () -> new ParallelMergeSort(0));
    }

    @Test
    void interrupcaoAnteriorNaoAlteraVetor() {
        byte[] vetor = {3, 1, 2};
        try {
            Thread.currentThread().interrupt();
            assertThrows(InterruptedException.class, () -> new ParallelMergeSort(2).ordenar(vetor));
            assertArrayEquals(new byte[]{3, 1, 2}, vetor);
            assertTrue(Thread.currentThread().isInterrupted());
        } finally {
            Thread.interrupted();
        }
    }

    @Test
    void interrupcaoDuranteOrdenacaoEncerraWorkersAntesDeRetornar() {
        assertTimeoutPreemptively(Duration.ofSeconds(15), () -> {
            byte[] vetor = new byte[10_000_000];
            new Random(10).nextBytes(vetor);
            AtomicBoolean interrompida = new AtomicBoolean();
            AtomicReference<Throwable> falha = new AtomicReference<>();
            CountDownLatch inicio = new CountDownLatch(1);
            Thread coordenadora = new Thread(() -> {
                inicio.countDown();
                try {
                    new ParallelMergeSort(4, false).ordenar(vetor);
                } catch (InterruptedException erro) {
                    interrompida.set(Thread.currentThread().isInterrupted());
                } catch (Throwable erro) {
                    falha.set(erro);
                }
            });
            coordenadora.start();
            assertTrue(inicio.await(1, TimeUnit.SECONDS));
            Thread worker = null;
            try {
                long limite = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
                while (worker == null && coordenadora.isAlive() && System.nanoTime() < limite) {
                    worker = Thread.getAllStackTraces().keySet().stream()
                            .filter(t -> t.isAlive() && t.getName().startsWith("ordenadora-"))
                            .findFirst().orElse(null);
                    Thread.yield();
                }
                assertTrue(worker != null, "O teste deve interromper com um worker ativo");
                coordenadora.interrupt();
                coordenadora.join(5_000);
                assertFalse(coordenadora.isAlive());
                assertTrue(interrompida.get());
                assertNull(falha.get());
                assertTrue(Thread.getAllStackTraces().keySet().stream().noneMatch(t ->
                        t.isAlive() && (t.getName().startsWith("ordenadora-")
                                || t.getName().startsWith("juntadora-"))));
            } finally {
                coordenadora.interrupt();
                coordenadora.join();
            }
        });
    }

    @Test
    void aceitaVazioUnitarioENegativosNoModoSemLogs() throws InterruptedException {
        ParallelMergeSort ordenador = new ParallelMergeSort(3, false);
        byte[] vazio = {};
        byte[] unitario = {-128};
        byte[] vetor = {127, -128, 0, -1, -1};
        ordenador.ordenar(vazio);
        ordenador.ordenar(unitario);
        ordenador.ordenar(vetor);
        assertArrayEquals(new byte[0], vazio);
        assertArrayEquals(new byte[]{-128}, unitario);
        assertArrayEquals(new byte[]{-128, -1, -1, 0, 127}, vetor);
    }
}
