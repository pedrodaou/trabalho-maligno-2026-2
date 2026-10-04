# Trabalho de Programacao Paralela e Distribuida

Trabalho do grupo 10: Merge Sort sequencial e paralelo para vetores de `byte`.
O codigo, os resultados experimentais e as evidencias de execucao estao incluidos.

## O que ja funciona

- Merge Sort sequencial recursivo;
- particionamento em partes aproximadamente iguais;
- `processadores disponiveis - 1` threads ordenadoras;
- rodadas de threads juntadoras, reduzindo os intervalos ate sobrar um;
- espera explicita com `join()` e propagacao de falhas das threads;
- cancelamento cooperativo, aguardando todas as threads antes de retornar;
- mensagens para falta de memoria e maquina com apenas um processador;
- preenchimento manual ou aleatorio;
- leitura e validacao centralizadas na classe `Teclado`;
- impressao do vetor inteiro, de um intervalo ou de nenhum elemento;
- medicao isolada dos tempos sequencial e paralelo;
- comparacao das duas versoes sobre copias da mesma entrada;
- benchmark reproduzivel com aquecimento, alternancia, mediana e validacao;
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
java -Xmx512m -cp target/classes br.edu.grupo10.app.Benchmark
java -Xmx8G -cp target/classes br.edu.grupo10.app.MaiorVetorAproximado
```

O `Comparador` e o melhor ponto de partida para a demonstracao: ele cria uma
entrada, ordena copias com as duas implementacoes, confere se os resultados sao
identicos e mostra os tempos e o speedup.

`ProgramaParalelo` mantem logs das rodadas para a demonstracao. `Comparador` e
`Benchmark` desabilitam os logs internos durante a medicao. Os dois programas
de ordenacao usam `System.nanoTime()`; entrada e impressao ficam fora do tempo.

O `MaiorVetorAproximado`, fornecido como exemplo no enunciado, aumenta a
alocacao em aproximadamente 50% por tentativa ate atingir o limite do heap.
Use `-Xmx8G` apenas em uma maquina que possa disponibilizar 8 GB para a JVM;
caso contrario, escolha um limite menor, como `-Xmx1G`.
O maior vetor que cabe sozinho nao e o maior vetor que pode ser ordenado:
o algoritmo precisa de um buffer auxiliar, e o comparador tambem faz copias.

A JVM precisa enxergar pelo menos dois processadores para a execucao paralela.
A quantidade padrao de ordenadoras e exatamente `availableProcessors() - 1`.
Com apenas um processador, o programa informa a incompatibilidade; use a versao
sequencial. Os construtores com quantidade explicita existem para testes.

Se a ordenacao paralela for interrompida, o vetor pode ficar parcialmente
ordenado. Todas as threads sao aguardadas antes de propagar `InterruptedException`,
e o indicador de interrupcao da coordenadora e preservado.

## Resultados e entrega

- [Relato de testes (10 linhas)](docs/RELATO_TESTES.md)
- [Metodo, ambiente e resultados de desempenho](docs/DESEMPENHO.md)
- [Capturas e registros originais](docs/evidencias/README.md)
- [Diario com cronologia verificavel e conclusao](docs/DIARIO.md)
- [Roteiro para demonstracao](docs/APRESENTACAO.md)
- [Checklist de entrega](docs/ENTREGA.md)

## Reproduzir as evidencias

Depois de compilar, execute com Python 3.9 ou superior:

```bash
python3 scripts/validar_entrega.py --capturas
```

O script usa o JDK local; se nao houver Java, usa Docker com
`eclipse-temurin:21-jdk`. Firefox e necessario apenas para `--capturas`.
Os PNGs sao capturas de paginas que exibem a saida real dos programas, com
comando, entrada e horario. Os arquivos TXT conservam a saida original completa.

Para compilar com Docker, sem instalar JDK/Maven na maquina:

```bash
docker run --rm -v "$PWD":/app -w /app maven:3.9.9-eclipse-temurin-21 mvn -B -ntp clean test package
```

Para montar o pacote com os arquivos rastreados pelo Git e o JAR compilado:

```bash
python3 scripts/empacotar_entrega.py
```

O ZIP sera criado em `dist/trabalho-maligno-2026-2.zip`, com fontes, testes,
documentos, capturas, JAR e manifesto de integridade SHA-256. Dentro do pacote,
os programas tambem podem ser executados sem Maven:

```bash
java -cp bin/trabalho-maligno-1.0-SNAPSHOT.jar br.edu.grupo10.app.ProgramaSequencial
java -cp bin/trabalho-maligno-1.0-SNAPSHOT.jar br.edu.grupo10.app.ProgramaParalelo
```

## Organizacao

```text
src/main/java/br/edu/grupo10/
|-- app/          # Teclado, entrada/saida e programas executaveis
`-- ordenacao/    # algoritmos sequencial e paralelo
src/test/java/    # testes automatizados
docs/             # diario, testes, desempenho, roteiro e evidencias
scripts/          # validacao e empacotamento reproduziveis
```

## Observacao sobre desempenho

Use vetores grandes para a comparacao. Em entradas pequenas, criar e sincronizar
threads custa mais do que a propria ordenacao e a versao paralela pode ser mais
lenta. Execute algumas vezes antes de registrar os resultados, pois a JVM passa
por aquecimento e o sistema operacional pode interferir na medicao.
