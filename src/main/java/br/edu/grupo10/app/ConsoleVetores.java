package br.edu.grupo10.app;

import java.util.Arrays;
import java.util.Random;

final class ConsoleVetores {
    private ConsoleVetores() {
    }

    static byte[] lerVetor(Teclado teclado) {
        int tamanho = teclado.lerInteiro(
                "Quantidade de elementos (maior que zero): ",
                valor -> valor > 0,
                "Informe um numero inteiro maior que zero.");

        int modo = teclado.lerInteiro(
                "Preenchimento: 1 - manual | 2 - aleatorio: ",
                valor -> valor == 1 || valor == 2,
                "Escolha 1 ou 2.");

        byte[] vetor = new byte[tamanho];
        if (modo == 1) {
            preencherManualmente(teclado, vetor);
        } else {
            preencherAleatoriamente(vetor);
        }
        return vetor;
    }

    private static void preencherManualmente(Teclado teclado, byte[] vetor) {
        for (int i = 0; i < vetor.length; i++) {
            int indice = i;
            int valor = teclado.lerInteiro(
                    "Elemento [" + indice + "] (-128 a 127): ",
                    numero -> numero >= Byte.MIN_VALUE && numero <= Byte.MAX_VALUE,
                    "O valor deve estar entre -128 e 127.");
            vetor[i] = (byte) valor;
        }
    }

    private static void preencherAleatoriamente(byte[] vetor) {
        Random random = new Random();
        random.nextBytes(vetor);
    }

    static void oferecerImpressao(Teclado teclado, byte[] vetor) {
        int opcao = teclado.lerInteiro(
                "Exibicao: 1 - vetor todo | 2 - intervalo | 3 - nao exibir: ",
                valor -> valor >= 1 && valor <= 3,
                "Escolha 1, 2 ou 3.");

        if (opcao == 1) {
            System.out.println(Arrays.toString(vetor));
        } else if (opcao == 2) {
            int inicio = teclado.lerInteiro(
                    "Indice inicial (0 a " + (vetor.length - 1) + "): ",
                    valor -> valor >= 0 && valor < vetor.length,
                    "Indice inicial invalido.");
            int fim = teclado.lerInteiro(
                    "Indice final inclusivo (" + inicio + " a " + (vetor.length - 1) + "): ",
                    valor -> valor >= inicio && valor < vetor.length,
                    "Indice final invalido.");
            System.out.println(Arrays.toString(Arrays.copyOfRange(vetor, inicio, fim + 1)));
        }
    }

    static double nanosParaMilissegundos(long nanos) {
        return nanos / 1_000_000.0;
    }

    static void informarFaltaDeMemoria() {
        System.err.println("Memoria insuficiente para o vetor e os buffers de ordenacao. "
                + "Reduza a quantidade de elementos ou ajuste -Xmx conforme a memoria disponivel.");
    }
}
