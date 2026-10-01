# Trabalho de Programacao Paralela e Distribuida

Primeiro marco funcional do trabalho de Merge Sort para vetores de `byte`.

## O que ja funciona

- Merge Sort sequencial recursivo;
- particionamento em partes aproximadamente iguais;
- `processadores disponiveis - 1` threads ordenadoras;
- rodadas de threads juntadoras, reduzindo os intervalos ate sobrar um;
- espera explicita com `join()` e propagacao de falhas das threads;
- preenchimento manual ou aleatorio;
- leitura e validacao centralizadas na classe `Teclado`;
- impressao do vetor inteiro, de um intervalo ou de nenhum elemento;
- medicao isolada dos tempos sequencial e paralelo;
- comparacao das duas versoes sobre copias da mesma entrada;
- testes com negativos, repetidos, limites de `byte`, vetores grandes e mais
  threads do que elementos.

## Requisitos

- JDK 17 ou superior;
- Maven 3.9 ou superior (recomendado para executar os testes).

## Compilar e testar

```bash
mvn test
mvn package
```

Tambem e possivel compilar sem Maven:

```bash
mkdir -p out
javac -d out $(find src/main/java -name '*.java')
```

## Executar

Depois de `mvn package`, escolha um dos programas:

```bash
java -cp target/classes br.edu.grupo10.app.ProgramaSequencial
java -cp target/classes br.edu.grupo10.app.ProgramaParalelo
java -cp target/classes br.edu.grupo10.app.Comparador
java -Xmx8G -cp target/classes br.edu.grupo10.app.MaiorVetorAproximado
```

O `Comparador` e o melhor ponto de partida para a demonstracao: ele cria uma
entrada, ordena copias com as duas implementacoes, confere se os resultados sao
identicos e mostra os tempos e o speedup.

O `MaiorVetorAproximado`, fornecido como exemplo no enunciado, aumenta a
alocacao em aproximadamente 50% por tentativa ate atingir o limite do heap.
Use `-Xmx8G` apenas em uma maquina que possa disponibilizar 8 GB para a JVM;
caso contrario, escolha um limite menor, como `-Xmx1G`.

## Organizacao

```text
src/main/java/br/edu/grupo10/
|-- app/          # Teclado, entrada/saida e programas executaveis
`-- ordenacao/    # algoritmos sequencial e paralelo
src/test/java/    # testes automatizados
docs/             # diario, planejamento e relato de testes
```

## Observacao sobre desempenho

Use vetores grandes para a comparacao. Em entradas pequenas, criar e sincronizar
threads custa mais do que a propria ordenacao e a versao paralela pode ser mais
lenta. Execute algumas vezes antes de registrar os resultados, pois a JVM passa
por aquecimento e o sistema operacional pode interferir na medicao.
