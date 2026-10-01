package br.edu.grupo10.ordenacao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

/**
 * Merge Sort que usa threads ordenadoras e, depois, rodadas de threads
 * juntadoras ate restar um unico intervalo ordenado.
 */
public final class ParallelMergeSort {
    private static final Logger LOGGER = Logger.getLogger(ParallelMergeSort.class.getName());

    private final int quantidadeThreadsOrdenadoras;

    public ParallelMergeSort() {
        this(calcularQuantidadeThreads());
    }

    /** Construtor publico para permitir testes com quantidades diferentes de threads. */
    public ParallelMergeSort(int quantidadeThreadsOrdenadoras) {
        if (quantidadeThreadsOrdenadoras < 1) {
            throw new IllegalArgumentException("A quantidade de threads deve ser positiva");
        }
        this.quantidadeThreadsOrdenadoras = quantidadeThreadsOrdenadoras;
    }

    public static int calcularQuantidadeThreads() {
        int processadores = Runtime.getRuntime().availableProcessors();
        // Uma maquina com uma unica CPU nao pode criar zero threads ordenadoras.
        return Math.max(1, processadores - 1);
    }

    public int getQuantidadeThreadsOrdenadoras() {
        return quantidadeThreadsOrdenadoras;
    }

    /** Ordena o vetor no proprio lugar e aguarda todas as threads com join(). */
    public void ordenar(byte[] vetor) throws InterruptedException {
        Objects.requireNonNull(vetor, "O vetor nao pode ser nulo");
        if (vetor.length < 2) {
            return;
        }

        byte[] auxiliar = new byte[vetor.length];
        List<Intervalo> intervalos = particionar(vetor.length, quantidadeThreadsOrdenadoras);

        LOGGER.info(() -> String.format(
                "Iniciando %d threads ordenadoras para %,d elementos.",
                quantidadeThreadsOrdenadoras, vetor.length));

        executarRodada(intervalos, "ordenadora", intervalo ->
                MergeSort.ordenarIntervalo(
                        vetor, auxiliar, intervalo.inicio(), intervalo.fim()));

        int numeroRodada = 1;
        while (intervalos.size() > 1) {
            List<Intervalo> proximos = new ArrayList<>((intervalos.size() + 1) / 2);
            List<TarefaMerge> merges = new ArrayList<>(intervalos.size() / 2);

            for (int i = 0; i + 1 < intervalos.size(); i += 2) {
                Intervalo esquerda = intervalos.get(i);
                Intervalo direita = intervalos.get(i + 1);
                merges.add(new TarefaMerge(esquerda.inicio(), esquerda.fim(), direita.fim()));
                proximos.add(new Intervalo(esquerda.inicio(), direita.fim()));
            }
            if (intervalos.size() % 2 != 0) {
                proximos.add(intervalos.get(intervalos.size() - 1));
            }

            int rodadaAtual = numeroRodada;
            LOGGER.info(() -> String.format(
                    "Rodada de merge %d: %d threads juntadoras.",
                    rodadaAtual, merges.size()));

            executarRodada(merges, "juntadora-r" + numeroRodada, merge ->
                    MergeSort.intercalar(
                            vetor, auxiliar, merge.inicio(), merge.meio(), merge.fim()));

            intervalos = proximos;
            numeroRodada++;
        }
        LOGGER.info("Ordenacao paralela concluida.");
    }

    private static List<Intervalo> particionar(int tamanho, int quantidadePartes) {
        List<Intervalo> intervalos = new ArrayList<>(quantidadePartes);
        for (int i = 0; i < quantidadePartes; i++) {
            int inicio = (int) ((long) i * tamanho / quantidadePartes);
            int fim = (int) ((long) (i + 1) * tamanho / quantidadePartes);
            intervalos.add(new Intervalo(inicio, fim));
        }
        return intervalos;
    }

    private static <T> void executarRodada(
            List<T> tarefas, String prefixoNome, Acao<T> acao) throws InterruptedException {
        AtomicReference<Throwable> primeiraFalha = new AtomicReference<>();
        List<Thread> threads = new ArrayList<>(tarefas.size());

        for (int i = 0; i < tarefas.size(); i++) {
            T tarefa = tarefas.get(i);
            Thread thread = new Thread(() -> {
                try {
                    acao.executar(tarefa);
                } catch (Throwable falha) {
                    primeiraFalha.compareAndSet(null, falha);
                }
            }, prefixoNome + "-" + (i + 1));
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        Throwable falha = primeiraFalha.get();
        if (falha != null) {
            throw new IllegalStateException("Uma thread falhou durante a ordenacao", falha);
        }
    }

    @FunctionalInterface
    private interface Acao<T> {
        void executar(T tarefa);
    }

    private record Intervalo(int inicio, int fim) {
    }

    private record TarefaMerge(int inicio, int meio, int fim) {
    }
}

