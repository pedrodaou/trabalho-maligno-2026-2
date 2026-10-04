package br.edu.grupo10.app;

import br.edu.grupo10.ordenacao.MergeSort;
import br.edu.grupo10.ordenacao.ParallelMergeSort;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Executa as duas versoes sobre copias do mesmo vetor para uma comparacao justa. */
public final class Comparador {
    private static final Logger LOGGER = Logger.getLogger(Comparador.class.getName());

    private Comparador() {
    }

    public static void main(String[] args) {
        try (Teclado teclado = new Teclado()) {
            ParallelMergeSort ordenador = new ParallelMergeSort(
                    ParallelMergeSort.calcularQuantidadeThreads(), false);
            byte[] original = ConsoleVetores.lerVetor(teclado);
            byte[] sequencial = original.clone();
            byte[] paralelo = original.clone();

            LOGGER.info("Executando a versao sequencial.");
            long inicioSequencial = System.nanoTime();
            MergeSort.ordenar(sequencial);
            long tempoSequencial = System.nanoTime() - inicioSequencial;

            LOGGER.info(() -> "Executando a versao paralela com "
                    + ordenador.getQuantidadeThreadsOrdenadoras() + " threads ordenadoras.");
            long inicioParalelo = System.nanoTime();
            ordenador.ordenar(paralelo);
            long tempoParalelo = System.nanoTime() - inicioParalelo;

            if (!Arrays.equals(sequencial, paralelo)) {
                throw new IllegalStateException("Os programas produziram resultados diferentes.");
            }

            System.out.printf("Tempo sequencial: %.3f ms%n",
                    ConsoleVetores.nanosParaMilissegundos(tempoSequencial));
            System.out.printf("Tempo paralelo:   %.3f ms%n",
                    ConsoleVetores.nanosParaMilissegundos(tempoParalelo));
            if (tempoParalelo > 0) {
                System.out.printf("Speedup (sequencial/paralelo): %.2fx%n",
                        (double) tempoSequencial / tempoParalelo);
            }
            System.out.println("Validacao: os dois resultados sao identicos.");
            ConsoleVetores.oferecerImpressao(teclado, paralelo);
        } catch (OutOfMemoryError erro) {
            ConsoleVetores.informarFaltaDeMemoria();
        } catch (InterruptedException erro) {
            Thread.currentThread().interrupt();
            LOGGER.log(Level.SEVERE, "A execucao foi interrompida.", erro);
        } catch (IllegalArgumentException | IllegalStateException erro) {
            LOGGER.log(Level.SEVERE, "Nao foi possivel concluir a comparacao: " + erro.getMessage(), erro);
        }
    }
}
