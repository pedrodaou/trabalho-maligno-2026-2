package br.edu.grupo10.app;

import br.edu.grupo10.ordenacao.MergeSort;
import br.edu.grupo10.ordenacao.ParallelMergeSort;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

/** Experimento reproduzivel: aquecimento, alternancia, validacao e mediana. */
public final class Benchmark {
    private Benchmark() {
    }

    /** Argumentos: aquecimentos repeticoes seed tamanhos... (todos opcionais). */
    public static void main(String[] args) {
        try {
            int aquecimentos = args.length > 0 ? Integer.parseInt(args[0]) : 2;
            int repeticoes = args.length > 1 ? Integer.parseInt(args[1]) : 5;
            long seed = args.length > 2 ? Long.parseLong(args[2]) : 10;
            int[] tamanhos = args.length > 3
                    ? Arrays.stream(args).skip(3).mapToInt(Integer::parseInt).toArray()
                    : new int[]{100_000, 1_000_000, 10_000_000};
            if (aquecimentos < 1 || repeticoes < 3 || Arrays.stream(tamanhos).anyMatch(n -> n < 1)) {
                throw new IllegalArgumentException(
                        "Use pelo menos 1 aquecimento, 3 repeticoes e tamanhos positivos.");
            }

            ParallelMergeSort paralelo = new ParallelMergeSort(
                    ParallelMergeSort.calcularQuantidadeThreads(), false);
            System.out.println("# data=" + OffsetDateTime.now());
            System.out.println("# java=" + System.getProperty("java.runtime.version"));
            System.out.println("# vm=" + System.getProperty("java.vm.name"));
            System.out.println("# so=" + System.getProperty("os.name") + " "
                    + System.getProperty("os.version") + " " + System.getProperty("os.arch"));
            System.out.println("# processadores=" + Runtime.getRuntime().availableProcessors());
            System.out.println("# threads=" + paralelo.getQuantidadeThreadsOrdenadoras());
            System.out.println("# heap_max_bytes=" + Runtime.getRuntime().maxMemory());
            System.out.println("# aquecimentos=" + aquecimentos + "; repeticoes=" + repeticoes
                    + "; seed=" + seed + "; logs_durante_medicao=false");
            System.out.println("tipo,elementos,rodada,primeiro,sequencial_ms,paralelo_ms,speedup");

            for (int tamanho : tamanhos) {
                byte[] original = new byte[tamanho];
                new Random(seed).nextBytes(original);
                byte[] referencia = original.clone();
                Arrays.sort(referencia);
                double[] temposSequenciais = new double[repeticoes];
                double[] temposParalelos = new double[repeticoes];

                for (int rodada = -aquecimentos; rodada < repeticoes; rodada++) {
                    // Preparacao da entrada e validacao ficam fora do intervalo cronometrado.
                    byte[] entradaSequencial = original.clone();
                    byte[] entradaParalela = original.clone();
                    long tempoSequencial;
                    long tempoParalelo;
                    boolean sequencialPrimeiro = (rodada & 1) == 0;
                    if (sequencialPrimeiro) {
                        tempoSequencial = medirSequencial(entradaSequencial);
                        tempoParalelo = medirParalelo(paralelo, entradaParalela);
                    } else {
                        tempoParalelo = medirParalelo(paralelo, entradaParalela);
                        tempoSequencial = medirSequencial(entradaSequencial);
                    }
                    if (!Arrays.equals(referencia, entradaSequencial)
                            || !Arrays.equals(referencia, entradaParalela)) {
                        throw new IllegalStateException("Resultado incorreto para " + tamanho);
                    }

                    if (rodada >= 0) {
                        double sequencialMs = ConsoleVetores.nanosParaMilissegundos(tempoSequencial);
                        double paraleloMs = ConsoleVetores.nanosParaMilissegundos(tempoParalelo);
                        temposSequenciais[rodada] = sequencialMs;
                        temposParalelos[rodada] = paraleloMs;
                        System.out.printf(Locale.ROOT, "amostra,%d,%d,%s,%.6f,%.6f,%.6f%n",
                                tamanho, rodada + 1, sequencialPrimeiro ? "sequencial" : "paralelo",
                                sequencialMs, paraleloMs, sequencialMs / paraleloMs);
                    }
                }

                double sequencialMs = mediana(temposSequenciais);
                double paraleloMs = mediana(temposParalelos);
                System.out.printf(Locale.ROOT, "mediana,%d,0,-,%.6f,%.6f,%.6f%n",
                        tamanho, sequencialMs, paraleloMs, sequencialMs / paraleloMs);
            }
        } catch (OutOfMemoryError erro) {
            ConsoleVetores.informarFaltaDeMemoria();
            System.exit(1);
        } catch (InterruptedException erro) {
            Thread.currentThread().interrupt();
            System.err.println("Benchmark interrompido; threads encerradas.");
            System.exit(1);
        } catch (IllegalArgumentException | IllegalStateException erro) {
            System.err.println(erro.getMessage());
            System.exit(1);
        }
    }

    private static long medirSequencial(byte[] vetor) {
        long inicio = System.nanoTime();
        MergeSort.ordenar(vetor);
        return System.nanoTime() - inicio;
    }

    private static long medirParalelo(ParallelMergeSort ordenador, byte[] vetor)
            throws InterruptedException {
        long inicio = System.nanoTime();
        ordenador.ordenar(vetor);
        return System.nanoTime() - inicio;
    }

    static double mediana(double[] tempos) {
        double[] ordenados = tempos.clone();
        Arrays.sort(ordenados);
        int meio = ordenados.length / 2;
        return ordenados.length % 2 == 0
                ? (ordenados[meio - 1] + ordenados[meio]) / 2 : ordenados[meio];
    }
}
