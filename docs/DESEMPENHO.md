# Experimento de desempenho

Execucao real em 04/10/2026. Ambiente registrado abaixo; resultados especificos desta maquina.

```text
# data=2026-10-04T16:49:27.997750735-03:00
# java=21.0.12.1+1-LTS
# vm=OpenJDK 64-Bit Server VM
# so=Linux 7.0.0-31-generic amd64
# processadores=16
# threads=15
# heap_max_bytes=536870912
# aquecimentos=2; repeticoes=5; seed=10; logs_durante_medicao=false
```

## Metodo

Maquina de execucao: Intel Core i7-13620H, aproximadamente 14,6 GiB de RAM;
JDK em Docker (`eclipse-temurin:21-jdk`), heap de 512 MiB. Outros servicos
estavam ativos no host; nao houve isolamento exclusivo de CPU para o experimento.

Por tamanho: duas rodadas de aquecimento, depois cinco amostras. A ordem sequencial/paralela
e alternada. Entrada aleatoria reproduzivel com seed 10. Cada resultado e conferido contra
`Arrays.sort` fora da medicao. A geracao, clonagem, validacao e impressao ficam fora dos tempos.
A alocacao dos buffers internos e a criacao/join das threads entram nos tempos.
Os logs do algoritmo paralelo ficam desabilitados neste experimento.

| Elementos | Mediana sequencial (ms) | Mediana paralela (ms) | Speedup |
|---:|---:|---:|---:|
| 100,000 | 8.462 | 4.806 | 1.76x |
| 1,000,000 | 92.826 | 18.307 | 5.07x |
| 10,000,000 | 995.178 | 147.866 | 6.73x |

Speedup = mediana sequencial / mediana paralela. Abaixo de 1x, o paralelo foi mais lento.
Nao ha garantia de aceleracao: criacao de threads, agendamento, GC e largura de banda da
memoria influenciam os tempos. Cinco amostras nao constituem um benchmark estatistico completo.

## Reproducao

```bash
java -Xmx512m -cp target/classes br.edu.grupo10.app.Benchmark 2 5 10 100000 1000000 10000000
```

Dados brutos: [benchmark.csv](evidencias/benchmark.csv).
