package br.edu.grupo10.app;

import java.util.Scanner;

/**
 * Menu interativo em Java para facilitar a execucao dos programas do trabalho.
 */
public class Menu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n==========================================");
            System.out.println("      MENU DE EXECUCAO - GRUPO 10         ");
            System.out.println("==========================================");
            System.out.println("1) Programa Paralelo (Merge Sort com Threads)");
            System.out.println("2) Programa Sequencial (Merge Sort Sem Threads)");
            System.out.println("3) Comparador (Sequencial vs Paralelo + Speedup)");
            System.out.println("4) Benchmark Automatizado (Experimento de Desempenho)");
            System.out.println("5) Maior Vetor Aproximado (Teste de Heap/Memoria)");
            System.out.println("0) Sair");
            System.out.println("==========================================");
            System.out.print("Escolha uma opcao [0-5]: ");

            String entrada = scanner.nextLine().trim();

            System.out.println();
            try {
                switch (entrada) {
                    case "1":
                        System.out.println("--- Executando ProgramaParalelo ---");
                        ProgramaParalelo.main(new String[0]);
                        break;
                    case "2":
                        System.out.println("--- Executando ProgramaSequencial ---");
                        ProgramaSequencial.main(new String[0]);
                        break;
                    case "3":
                        System.out.println("--- Executando Comparador ---");
                        Comparador.main(new String[0]);
                        break;
                    case "4":
                        System.out.println("--- Executando Benchmark ---");
                        Benchmark.main(new String[0]);
                        break;
                    case "5":
                        System.out.println("--- Executando MaiorVetorAproximado ---");
                        MaiorVetorAproximado.main(new String[0]);
                        break;
                    case "0":
                        System.out.println("Encerrando o menu. Ate logo!");
                        return;
                    default:
                        System.out.println("Opcao invalida! Digite um numero de 0 a 5.");
                        break;
                }
            } catch (Exception e) {
                System.err.println("Erro ao executar a opcao selecionada: " + e.getMessage());
            }
        }
    }
}
