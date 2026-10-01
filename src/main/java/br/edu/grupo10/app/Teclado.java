package br.edu.grupo10.app;

import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.IntPredicate;

/** Centraliza a leitura e a validacao dos dados digitados pelo usuario. */
public final class Teclado implements AutoCloseable {
    private final Scanner scanner;

    /** Cria um teclado que le a entrada padrao do programa. */
    public Teclado() {
        this(System.in);
    }

    /** Permite usar outra entrada, principalmente durante os testes. */
    public Teclado(InputStream entrada) {
        scanner = new Scanner(Objects.requireNonNull(entrada, "A entrada nao pode ser nula"));
    }

    /**
     * Le um inteiro e repete a pergunta enquanto o valor nao atender a
     * validacao recebida.
     */
    public int lerInteiro(
            String mensagem, IntPredicate validacao, String mensagemErro) {
        Objects.requireNonNull(validacao, "A validacao nao pode ser nula");

        while (true) {
            System.out.print(mensagem);
            if (!scanner.hasNextLine()) {
                throw new IllegalStateException("A entrada foi encerrada antes do esperado.");
            }

            String texto = scanner.nextLine().trim();
            try {
                int valor = Integer.parseInt(texto);
                if (validacao.test(valor)) {
                    return valor;
                }
            } catch (NumberFormatException ignorada) {
                // A mensagem abaixo cobre texto e numeros fora do limite de int.
            }
            System.out.println("Entrada invalida. " + mensagemErro);
        }
    }

    @Override
    public void close() {
        scanner.close();
    }
}

