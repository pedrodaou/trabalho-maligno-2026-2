package br.edu.grupo10.app;

import br.edu.grupo10.ordenacao.MergeSort;

import java.util.logging.Level;
import java.util.logging.Logger;

/** Programa solicitado para executar a ordenacao sem paralelismo. */
public final class ProgramaSequencial {
    private static final Logger LOGGER = Logger.getLogger(ProgramaSequencial.class.getName());

    private ProgramaSequencial() {
    }

    public static void main(String[] args) {
        try (Teclado teclado = new Teclado()) {
            byte[] vetor = ConsoleVetores.lerVetor(teclado);
            LOGGER.info(() -> String.format(
                    "Iniciando Merge Sort sequencial com %,d elementos.", vetor.length));

            long inicio = System.nanoTime();
            MergeSort.ordenar(vetor);
            long duracao = System.nanoTime() - inicio;

            LOGGER.info("Ordenacao sequencial concluida.");
            System.out.printf("Tempo sequencial: %.3f ms%n",
                    ConsoleVetores.nanosParaMilissegundos(duracao));
            ConsoleVetores.oferecerImpressao(teclado, vetor);
        } catch (OutOfMemoryError erro) {
            ConsoleVetores.informarFaltaDeMemoria();
        } catch (IllegalArgumentException | IllegalStateException erro) {
            LOGGER.log(Level.SEVERE, "Nao foi possivel concluir o programa: " + erro.getMessage(), erro);
        }
    }
}
