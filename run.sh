#!/usr/bin/env bash

# Diretório raiz da pasta do trabalho
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TRABALHO_DIR="$ROOT_DIR/trabalho"

if [ ! -d "$TRABALHO_DIR" ]; then
    TRABALHO_DIR="$ROOT_DIR"
fi

cd "$TRABALHO_DIR" || exit 1

mkdir -p bin

echo "=========================================="
echo "  Compilando o Projeto Java..."
echo "=========================================="

javac -d bin $(find src/main/java -name '*.java')

if [ $? -ne 0 ]; then
    echo "❌ Erro ao compilar o projeto."
    exit 1
fi

echo "✅ Compilação concluída com sucesso!"
echo ""

while true; do
    echo "=========================================="
    echo "      MENU DE EXECUÇÃO - GRUPO 10         "
    echo "=========================================="
    echo "1) Programa Paralelo (Merge Sort com Threads)"
    echo "2) Programa Sequencial (Merge Sort Sem Threads)"
    echo "3) Comparador (Sequencial vs Paralelo + Speedup)"
    echo "4) Benchmark Automatizado (Experimento de Desempenho)"
    echo "5) Maior Vetor Aproximado (Teste de Heap/Memória)"
    echo "0) Sair"
    echo "=========================================="
    read -p "Escolha uma opção [0-5]: " opcao
    echo ""

    case $opcao in
        1)
            echo "🚀 Executando ProgramaParalelo..."
            java -cp bin br.edu.grupo10.app.ProgramaParalelo
            ;;
        2)
            echo "🚀 Executando ProgramaSequencial..."
            java -cp bin br.edu.grupo10.app.ProgramaSequencial
            ;;
        3)
            echo "🚀 Executando Comparador..."
            java -cp bin br.edu.grupo10.app.Comparador
            ;;
        4)
            echo "🚀 Executando Benchmark (com limitador de memória 512M)..."
            java -Xmx512m -cp bin br.edu.grupo10.app.Benchmark
            ;;
        5)
            echo "🚀 Executando MaiorVetorAproximado (com limitador de memória 512M)..."
            java -Xmx512m -cp bin br.edu.grupo10.app.MaiorVetorAproximado
            ;;
        0)
            echo "Encerrando. Até logo!"
            exit 0
            ;;
        *)
            echo "⚠️ Opção inválida! Escolha um número de 0 a 5."
            ;;
    esac
    echo ""
    read -p "Pressione ENTER para voltar ao menu..."
    echo ""
done
