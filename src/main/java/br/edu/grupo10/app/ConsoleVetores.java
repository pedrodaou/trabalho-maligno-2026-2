package br.edu.grupo10.app;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.function.IntPredicate;

final class ConsoleVetores {
    private ConsoleVetores() {
    }

    static byte[] lerVetor(Scanner entrada) {
        int tamanho = lerInteiro(
                entrada,
                "Quantidade de elementos (maior que zero): ",
                valor -> valor > 0,
                "Informe um numero inteiro maior que zero.");

        int modo = lerInteiro(
                entrada,
                "Preenchimento: 1 - manual | 2 - aleatorio: ",
                valor -> valor == 1 || valor == 2,
                "Escolha 1 ou 2.");

        byte[] vetor = new byte[tamanho];
        if (modo == 1) {
            preencherManualmente(entrada, vetor);
        } else {
            preencherAleatoriamente(vetor);
        }
        return vetor;
    }

    private static void preencherManualmente(Scanner entrada, byte[] vetor) {
        for (int i = 0; i < vetor.length; i++) {
            int indice = i;
            int valor = lerInteiro(
                    entrada,
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

    static void oferecerImpressao(Scanner entrada, byte[] vetor) {
        int opcao = lerInteiro(
                entrada,
                "Exibicao: 1 - vetor todo | 2 - intervalo | 3 - nao exibir: ",
                valor -> valor >= 1 && valor <= 3,
                "Escolha 1, 2 ou 3.");

        if (opcao == 1) {
            System.out.println(Arrays.toString(vetor));
        } else if (opcao == 2) {
            int inicio = lerInteiro(
                    entrada,
                    "Indice inicial (0 a " + (vetor.length - 1) + "): ",
                    valor -> valor >= 0 && valor < vetor.length,
                    "Indice inicial invalido.");
            int fim = lerInteiro(
                    entrada,
                    "Indice final inclusivo (" + inicio + " a " + (vetor.length - 1) + "): ",
                    valor -> valor >= inicio && valor < vetor.length,
                    "Indice final invalido.");
            System.out.println(Arrays.toString(Arrays.copyOfRange(vetor, inicio, fim + 1)));
        }
    }

    static int lerInteiro(
            Scanner entrada, String mensagem, IntPredicate validacao, String mensagemErro) {
        while (true) {
            System.out.print(mensagem);
            if (!entrada.hasNextLine()) {
                throw new IllegalStateException("A entrada foi encerrada antes do esperado.");
            }

            String texto = entrada.nextLine().trim();
            try {
                int valor = Integer.parseInt(texto);
                if (validacao.test(valor)) {
                    return valor;
                }
            } catch (NumberFormatException ignorada) {
                // A mensagem amigavel abaixo tambem cobre texto e numeros fora do limite de int.
            }
            System.out.println("Entrada invalida. " + mensagemErro);
        }
    }

    static double nanosParaMilissegundos(long nanos) {
        return nanos / 1_000_000.0;
    }
}

