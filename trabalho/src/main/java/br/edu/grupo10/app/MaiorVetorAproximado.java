package br.edu.grupo10.app;

/**
 * Programa auxiliar apresentado no enunciado para estimar o maior vetor de
 * bytes que cabe no heap disponibilizado para a JVM.
 */
public final class MaiorVetorAproximado {
    private static final int TAMANHO_INICIAL = 1_000_000;

    private MaiorVetorAproximado() {
    }

    public static void main(String[] args) {
        System.out.println("Estimando o maior tamanho possivel de vetor em Java...");
        long inicio = System.currentTimeMillis();

        int tamanho = TAMANHO_INICIAL;
        int ultimoBemSucedido = 0;

        while (true) {
            try {
                byte[] vetor = new byte[tamanho];
                ultimoBemSucedido = tamanho;

                // Toca o vetor para que a JVM materialize a alocacao.
                vetor[0] = 1;
                vetor[vetor.length - 1] = 1;

                System.out.printf("Alocado com sucesso: %,d elementos%n", ultimoBemSucedido);

                vetor = null;
                System.gc();

                // Impede overflow ao aumentar o tamanho em aproximadamente 50%.
                if (tamanho > Integer.MAX_VALUE / 3 * 2) {
                    break;
                }
                tamanho /= 2;
                tamanho *= 3;
            } catch (OutOfMemoryError erro) {
                System.out.printf("Falhou em %,d elementos%n", tamanho);
                break;
            }
        }

        long fim = System.currentTimeMillis();
        System.out.println("\nMaior vetor que coube (aproximadamente): "
                + String.format("%,d", ultimoBemSucedido));
        System.out.printf("Memoria estimada: %.2f MB%n",
                ultimoBemSucedido * 1.0 / (1024 * 1024));
        System.out.printf("Tempo total: %.2f segundos%n", (fim - inicio) / 1000.0);
    }
}

