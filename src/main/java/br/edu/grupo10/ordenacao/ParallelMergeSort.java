package br.edu.grupo10.ordenacao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.logging.Logger;

/**
 * Merge Sort que usa threads ordenadoras e, depois, rodadas de threads
 * juntadoras ate restar um unico intervalo ordenado.
 */
public final class ParallelMergeSort {
    private static final Logger LOGGER = Logger.getLogger(ParallelMergeSort.class.getName());

    private final int quantidadeThreadsOrdenadoras;
    private final boolean logsHabilitados;

    public ParallelMergeSort() {
        this(calcularQuantidadeThreads());
    }

    /** Construtor publico para permitir testes com quantidades diferentes de threads. */
    public ParallelMergeSort(int quantidadeThreadsOrdenadoras) {
        this(quantidadeThreadsOrdenadoras, true);
    }

    /** Permite medir o algoritmo sem incluir o custo da escrita dos logs. */
    public ParallelMergeSort(int quantidadeThreadsOrdenadoras, boolean logsHabilitados) {
        if (quantidadeThreadsOrdenadoras < 1) {
            throw new IllegalArgumentException("A quantidade de threads deve ser positiva");
        }
        this.quantidadeThreadsOrdenadoras = quantidadeThreadsOrdenadoras;
        this.logsHabilitados = logsHabilitados;
    }

    public static int calcularQuantidadeThreads() {
        int processadores = Runtime.getRuntime().availableProcessors();
        if (processadores < 2) {
            throw new IllegalStateException(
                    "O programa paralelo requer pelo menos 2 processadores disponiveis. "
                            + "Use ProgramaSequencial nesta maquina.");
        }
        return processadores - 1;
    }

    public int getQuantidadeThreadsOrdenadoras() {
        return quantidadeThreadsOrdenadoras;
    }

    /** Ordena o vetor no proprio lugar e aguarda todas as threads com join(). */
    public void ordenar(byte[] vetor) throws InterruptedException {
        Objects.requireNonNull(vetor, "O vetor nao pode ser nulo");
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("Ordenacao interrompida antes do inicio");
        }
        if (vetor.length < 2) {
            return;
        }

        byte[] auxiliar = new byte[vetor.length];
        List<Intervalo> intervalos = particionar(vetor.length, quantidadeThreadsOrdenadoras);

        if (logsHabilitados) {
            LOGGER.info(() -> String.format(
                    "Iniciando %d threads ordenadoras para %,d elementos.",
                    quantidadeThreadsOrdenadoras, vetor.length));
        }

        executarRodada(intervalos, "ordenadora", (intervalo, cancelada) ->
                MergeSort.ordenarIntervalo(
                        vetor, auxiliar, intervalo.inicio(), intervalo.fim(), cancelada));

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
            if (logsHabilitados) {
                LOGGER.info(() -> String.format(
                        "Rodada de merge %d: %d threads juntadoras.",
                        rodadaAtual, merges.size()));
            }

            executarRodada(merges, "juntadora-r" + numeroRodada, (merge, cancelada) ->
                    MergeSort.intercalar(
                            vetor, auxiliar, merge.inicio(), merge.meio(), merge.fim(), cancelada));

            intervalos = proximos;
            numeroRodada++;
        }
        if (logsHabilitados) {
            LOGGER.info("Ordenacao paralela concluida.");
        }
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
        AtomicBoolean cancelada = new AtomicBoolean();
        List<Thread> threads = new ArrayList<>(tarefas.size());

        for (int i = 0; i < tarefas.size(); i++) {
            T tarefa = tarefas.get(i);
            Thread thread = new Thread(() -> {
                try {
                    acao.executar(tarefa, cancelada::get);
                } catch (Throwable falha) {
                    primeiraFalha.compareAndSet(null, falha);
                    cancelada.set(true);
                }
            }, prefixoNome + "-" + (i + 1));
            threads.add(thread);
        }

        // Se uma thread nao puder iniciar, nenhuma ja iniciada fica abandonada.
        try {
            for (Thread thread : threads) {
                thread.start();
            }
        } catch (RuntimeException | Error falha) {
            cancelar(threads, cancelada);
            aguardarTodas(threads, cancelada);
            throw falha;
        }

        if (aguardarTodas(threads, cancelada)) {
            throw new InterruptedException("Ordenacao cancelada; todas as threads finalizaram");
        }

        Throwable falha = primeiraFalha.get();
        if (falha instanceof Error erro) {
            throw erro;
        }
        if (falha != null) {
            throw new IllegalStateException("Uma thread falhou durante a ordenacao", falha);
        }
    }

    private static void cancelar(List<Thread> threads, AtomicBoolean cancelada) {
        cancelada.set(true);
        threads.forEach(Thread::interrupt);
    }

    /** Aguarda ate mesmo apos interrupcoes, garantindo que nao restem escritas no vetor. */
    private static boolean aguardarTodas(List<Thread> threads, AtomicBoolean cancelada) {
        boolean interrompida = Thread.interrupted();
        if (interrompida) {
            cancelar(threads, cancelada);
        }
        for (Thread thread : threads) {
            boolean terminou = false;
            while (!terminou) {
                try {
                    thread.join();
                    terminou = true;
                } catch (InterruptedException erro) {
                    interrompida = true;
                    cancelar(threads, cancelada);
                }
            }
        }
        if (Thread.interrupted()) {
            interrompida = true;
        }
        if (interrompida) {
            Thread.currentThread().interrupt();
        }
        return interrompida;
    }

    @FunctionalInterface
    private interface Acao<T> {
        void executar(T tarefa, BooleanSupplier cancelada);
    }

    private record Intervalo(int inicio, int fim) {
    }

    private record TarefaMerge(int inicio, int meio, int fim) {
    }
}
