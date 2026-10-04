package br.edu.grupo10.app;

import br.edu.grupo10.ordenacao.ParallelMergeSort;

import java.util.logging.Level;
import java.util.logging.Logger;

/** Programa solicitado para executar a ordenacao com paralelismo. */
public final class ProgramaParalelo {
    private static final Logger LOGGER = Logger.getLogger(ProgramaParalelo.class.getName());

    private ProgramaParalelo() {
    }

    public static void main(String[] args) {
        try (Teclado teclado = new Teclado()) {
            ParallelMergeSort ordenador = new ParallelMergeSort();
            byte[] vetor = ConsoleVetores.lerVetor(teclado);
            int processadores = Runtime.getRuntime().availableProcessors();

            System.out.printf("Processadores disponiveis: %d%n", processadores);
            System.out.printf("Threads ordenadoras: %d%n", ordenador.getQuantidadeThreadsOrdenadoras());

            long inicio = System.nanoTime();
            ordenador.ordenar(vetor);
            long duracao = System.nanoTime() - inicio;

            System.out.printf("Tempo paralelo: %.3f ms%n",
                    ConsoleVetores.nanosParaMilissegundos(duracao));
            ConsoleVetores.oferecerImpressao(teclado, vetor);
        } catch (OutOfMemoryError erro) {
            ConsoleVetores.informarFaltaDeMemoria();
        } catch (InterruptedException erro) {
            Thread.currentThread().interrupt();
            LOGGER.log(Level.SEVERE, "A execucao foi interrompida.", erro);
        } catch (IllegalArgumentException | IllegalStateException erro) {
            LOGGER.log(Level.SEVERE, "Nao foi possivel concluir o programa: " + erro.getMessage(), erro);
        }
    }
}
