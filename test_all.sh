#!/usr/bin/env bash

# Diretório raiz da pasta do trabalho
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TRABALHO_DIR="$ROOT_DIR/trabalho"

if [ ! -d "$TRABALHO_DIR" ]; then
    TRABALHO_DIR="$ROOT_DIR"
fi

cd "$TRABALHO_DIR" || exit 1

mkdir -p bin

echo "============================================================"
echo " 🚀 COMPILANDO O PROJETO E EXECUTANDO BATERIA COMPLETA DE TESTES"
echo "============================================================"

javac -d bin $(find src/main/java -name '*.java')

if [ $? -ne 0 ]; then
    echo "❌ Erro ao compilar o projeto."
    exit 1
fi
echo "✅ Compilação efetuada com sucesso!"
echo ""

echo "------------------------------------------------------------"
echo " 🧪 1. CENÁRIO: Entrada Pequena de Exemplo (7 Elementos - Manual)"
echo "------------------------------------------------------------"
printf "7\n1\n38\n27\n43\n3\n9\n82\n10\n1\n" | java -cp bin br.edu.grupo10.app.ProgramaParalelo
echo ""

echo "------------------------------------------------------------"
echo " 🧪 2. CENÁRIO: Vetor Pequeno (50 Elementos - Aleatório - Exibição Total)"
echo "------------------------------------------------------------"
printf "50\n2\n1\n" | java -cp bin br.edu.grupo10.app.ProgramaSequencial
echo ""

echo "------------------------------------------------------------"
echo " 🧪 3. CENÁRIO: Comparação Direta (100.000 Elementos - Validação + Speedup)"
echo "------------------------------------------------------------"
printf "100000\n2\n3\n" | java -cp bin br.edu.grupo10.app.Comparador
echo ""

echo "------------------------------------------------------------"
echo " 🧪 4. CENÁRIO: Vetor Grande (1.000.000 Elementos - Exibição de Intervalo parcial [0 a 10])"
echo "------------------------------------------------------------"
printf "1000000\n2\n2\n0\n10\n" | java -cp bin br.edu.grupo10.app.ProgramaParalelo
echo ""

echo "------------------------------------------------------------"
echo " 🧪 5. CENÁRIO: Teste de Validação de Entrada Inválida / Erro"
echo "------------------------------------------------------------"
# Testa digitar valor inválido 'abc', tamanho negativo '-5', preenchimento inválido '99', e valor fora de byte '300'
printf "abc\n-5\n10\n99\n1\n300\n50\n-128\n127\n0\n1\n2\n3\n4\n5\n6\n1\n" | java -cp bin br.edu.grupo10.app.ProgramaParalelo
echo ""

echo "------------------------------------------------------------"
echo " 🧪 6. CENÁRIO: Benchmark Completo (100k, 1M, 10M com Mediana e Warmup)"
echo "------------------------------------------------------------"
java -Xmx512m -cp bin br.edu.grupo10.app.Benchmark
echo ""

echo "------------------------------------------------------------"
echo " 🧪 7. CENÁRIO: Teste de Capacidade Máxima de Memória (Maior Vetor)"
echo "------------------------------------------------------------"
java -Xmx256m -cp bin br.edu.grupo10.app.MaiorVetorAproximado
echo ""

echo "============================================================"
echo " ✅ TODOS OS CENÁRIOS FORAM EXECUTADOS COM SUCESSO!"
echo "============================================================"
