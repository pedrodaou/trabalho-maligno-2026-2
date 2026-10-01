package br.edu.grupo10.app;

import br.edu.grupo10.ordenacao.ParallelMergeSort;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Programa solicitado para executar a ordenacao com paralelismo. */
public final class ProgramaParalelo {
    private static final Logger LOGGER = Logger.getLogger(ProgramaParalelo.class.getName());

    private ProgramaParalelo() {
    }

    public static void main(String[] args) {
        try (Scanner entrada = new Scanner(System.in)) {
            byte[] vetor = ConsoleVetores.lerVetor(entrada);
            int processadores = Runtime.getRuntime().availableProcessors();
            ParallelMergeSort ordenador = new ParallelMergeSort();

            System.out.printf("Processadores disponiveis: %d%n", processadores);
            System.out.printf("Threads ordenadoras: %d%n", ordenador.getQuantidadeThreadsOrdenadoras());

            long inicio = System.nanoTime();
            ordenador.ordenar(vetor);
            long duracao = System.nanoTime() - inicio;

            System.out.printf("Tempo paralelo: %.3f ms%n",
                    ConsoleVetores.nanosParaMilissegundos(duracao));
            ConsoleVetores.oferecerImpressao(entrada, vetor);
        } catch (InterruptedException erro) {
            Thread.currentThread().interrupt();
            LOGGER.log(Level.SEVERE, "A execucao foi interrompida.", erro);
        } catch (IllegalArgumentException | IllegalStateException erro) {
            LOGGER.log(Level.SEVERE, "Nao foi possivel concluir o programa: " + erro.getMessage(), erro);
        }
    }
}

