package br.edu.grupo10.ordenacao;

import java.util.Objects;

/** Implementacao sequencial do Merge Sort para vetores de byte. */
public final class MergeSort {
    private MergeSort() {
    }

    /** Ordena o vetor recebido em ordem crescente. */
    public static void ordenar(byte[] vetor) {
        Objects.requireNonNull(vetor, "O vetor nao pode ser nulo");
        byte[] auxiliar = new byte[vetor.length];
        ordenarIntervalo(vetor, auxiliar, 0, vetor.length);
    }

    static void ordenarIntervalo(byte[] vetor, byte[] auxiliar, int inicio, int fim) {
        if (fim - inicio <= 1) {
            return;
        }

        int meio = inicio + (fim - inicio) / 2;
        ordenarIntervalo(vetor, auxiliar, inicio, meio);
        ordenarIntervalo(vetor, auxiliar, meio, fim);

        // Evita uma copia quando as duas metades ja estao na ordem correta.
        if (vetor[meio - 1] <= vetor[meio]) {
            return;
        }
        intercalar(vetor, auxiliar, inicio, meio, fim);
    }

    static void intercalar(byte[] vetor, byte[] auxiliar, int inicio, int meio, int fim) {
        System.arraycopy(vetor, inicio, auxiliar, inicio, fim - inicio);

        int esquerda = inicio;
        int direita = meio;
        int destino = inicio;

        while (esquerda < meio && direita < fim) {
            if (auxiliar[esquerda] <= auxiliar[direita]) {
                vetor[destino++] = auxiliar[esquerda++];
            } else {
                vetor[destino++] = auxiliar[direita++];
            }
        }

        while (esquerda < meio) {
            vetor[destino++] = auxiliar[esquerda++];
        }
        while (direita < fim) {
            vetor[destino++] = auxiliar[direita++];
        }
    }
}

