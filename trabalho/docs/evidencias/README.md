# Evidencias de execucao

Os arquivos TXT registram execucoes reais em 04/10/2026: classe, comando,
entrada fornecida, horario e saida original combinada de stdout/stderr.
Os HTMLs exibem esses registros. Os PNGs foram capturados pelo Firefox dessas
paginas de evidencia; nao sao capturas de um terminal interativo.

| Captura | Registro original | Conteudo |
|---|---|---|
| [Sequencial](sequencial.png) | [TXT](sequencial.txt) | Exemplo do enunciado ordenado sem threads. |
| [Paralelo](paralelo.png) | [TXT](paralelo.txt) | 16 processadores, 15 ordenadoras e rodadas de juntadoras. |
| [Comparador](comparador.png) | [TXT](comparador.txt) | Tempos, speedup e igualdade dos resultados. |
| [Maior vetor](maior-vetor.png) | [TXT](maior-vetor.txt) | Alocacao crescente e falta de memoria tratada com -Xmx32m. |

Outros registros cobrem entrada invalida, impressao de intervalo, falta de
memoria na entrada/buffers e incompatibilidade com CPU unica. `benchmark.csv`
conserva todas as amostras e medianas; `ambiente.json` identifica a execucao.
`testes.txt` registra o build e os resultados da suite automatizada.

Para reproduzir tudo apos compilar:

```bash
python3 scripts/validar_entrega.py --capturas
```

Ao executar em outra maquina, os tempos e horarios serao diferentes.
Se o professor exigir capturas do terminal da apresentacao, execute os comandos
do roteiro e capture a tela nessa maquina tambem.
