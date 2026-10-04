# Roteiro de demonstracao — grupo 10

## Preparacao

Use JDK 17 ou superior e uma maquina com pelo menos dois processadores visiveis
para a JVM. Compile com `mvn clean test package`. O pacote ZIP tambem inclui um
JAR: substitua `target/classes` por `bin/trabalho-maligno-1.0-SNAPSHOT.jar` nos
comandos abaixo para executa-lo sem Maven.

## Demonstracao sugerida (5 a 8 minutos)

1. **Entrada e sequencial:** execute `ProgramaSequencial`, escolha 7 elementos,
   preenchimento manual e os valores `38, 27, 43, 3, 9, 82, 10` (um por linha).
   Imprima todo o vetor: `[3, 9, 10, 27, 38, 43, 82]`.
2. **Paralelo:** repita com `ProgramaParalelo`. Explique a quantidade
   `availableProcessors() - 1`, as particoes e os logs das rodadas.
3. **Arquitetura:** mostre `ParallelMergeSort.ordenar()` e `executarRodada()`.
   Todos os `start()` ocorrem antes da espera com `join()`. Cada worker ordena
   recursivamente; juntadoras intercalam pares. O intervalo sem par avanca.
4. **Impressao parcial e validacao:** mostre um valor fora de -128 a 127 e a
   repeticao da pergunta; escolha exibicao por intervalo com indices validos.
5. **Comparacao:** execute `Comparador` com 1 milhao de elementos aleatorios,
   sem imprimir. Mostre os tempos e a verificacao de resultados identicos.
6. **Experimento final:** abra `DESEMPENHO.md` e explique aquecimento, cinco
   amostras, mediana, seed 10 e logs desabilitados. O CSV mostra as variacoes.
7. **Limites:** explique que buffers consomem memoria, que CPU unica e
   incompativel com a regra das ordenadoras e que interrupcoes encerram workers.
8. **Encerramento:** mostre os testes, o diario e as capturas.

## Comandos

```bash
java -cp target/classes br.edu.grupo10.app.ProgramaSequencial
java -cp target/classes br.edu.grupo10.app.ProgramaParalelo
java -Xmx512m -cp target/classes br.edu.grupo10.app.Comparador
java -Xmx512m -cp target/classes br.edu.grupo10.app.Benchmark
java -Xmx32m -cp target/classes br.edu.grupo10.app.MaiorVetorAproximado
```

## Perguntas que a equipe deve conseguir responder

- Por que o merge pressupoe duas partes ja ordenadas?
- Por que o Merge Sort tem custo O(n log n) e usa um buffer O(n)?
- Como a implementacao evita escritas concorrentes na mesma posicao?
- Por que `join()` e necessario antes da proxima rodada?
- O que acontece com uma quantidade impar de particoes?
- Por que o paralelo pode ser mais lento em um vetor pequeno?
- Por que processadores disponiveis na JVM nao significam necessariamente nucleos fisicos?

A apresentacao presencial deve seguir a escala do professor a partir de 06/10/2026.
