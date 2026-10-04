# Diario de desenvolvimento — grupo 10

Versao reconstruida para revisao da equipe. Os nomes foram informados pelo
grupo; a distribuicao de tarefas, as janelas de trabalho e as impressoes
individuais abaixo sao propostas elaboradas a partir do projeto, e nao relatos
pessoais confirmados. A cronologia verificavel esta identificada separadamente.

## Integrantes e responsabilidades propostas

| Integrante | Responsabilidade principal | Classes e entregaveis |
|---|---|---|
| Pedro de Mello Porto Daou | Integracao, coordenacao do paralelismo, comparacao e entrega | ParallelMergeSort, Comparador, Benchmark, pom.xml e scripts de empacotamento. |
| Enzo Garofalo Pampana | Algoritmos de ordenacao e revisao da corretude | MergeSort, ProgramaSequencial, MergeSortTest e revisao das tarefas de merge. |
| João Gabriel da Silva Leite | Entrada/saida, validacao e documentacao de testes | Teclado, ConsoleVetores, TecladoTest e evidencias de execucao. |

As responsabilidades principais incluem revisao conjunta. ProgramaParalelo,
MaiorVetorAproximado e a documentacao final ficam sob revisao dos tres integrantes.

## Sessao inicial — 01/10/2026 (janelas propostas, duas horas)

As atividades simultaneas representam uma distribuicao sugerida dentro de uma
sessao de duas horas. Estes horarios nao vieram de registros individuais.

| Janela proposta | Integrante(s) | Atividade |
|---|---|---|
| 08:00–08:20 | Todos | Leitura do enunciado e divisao das tarefas: byte, Merge Sort recursivo, processadores - 1, join(), logs e comparacao. |
| 08:20–09:00 | Pedro de Mello Porto Daou | Organizacao do Maven e integracao de ParallelMergeSort: particionamento, start(), join() e rodadas de juntadoras. |
| 08:20–09:00 | Enzo Garofalo Pampana | Revisao de MergeSort.ordenarIntervalo() e intercalar(); conferencia dos indices, negativos e repetidos. |
| 08:20–09:00 | João Gabriel da Silva Leite | Revisao de ConsoleVetores, entrada manual/aleatoria e impressao completa/parcial; validacao de -128 a 127. |
| 09:00–09:30 | Pedro de Mello Porto Daou | Integracao de ProgramaParalelo e Comparador; conferencia da medicao e das copias da mesma entrada. |
| 09:00–09:30 | Enzo Garofalo Pampana | Revisao de ProgramaSequencial e MergeSortTest; comparacao com Arrays.sort como referencia. |
| 09:00–09:30 | João Gabriel da Silva Leite | Revisao de Teclado, repeticao de perguntas invalidas e testes da entrada. |
| 09:30–10:00 | Todos | Revisao conjunta, conferencia do README e organizacao do fechamento para entrega. |

## Fechamento — 04/10/2026 (distribuicao proposta)

Os horarios de 16:45 a 16:59 correspondem a janela registrada da sessao tecnica.
A atribuicao aos integrantes abaixo e proposta para revisao; a implementacao
na conversa contou com assistencia automatizada de Codex.

| Janela proposta | Integrante(s) | Atividade |
|---|---|---|
| 16:45–16:47 | Pedro de Mello Porto Daou | Revisao do cancelamento em ParallelMergeSort e da espera de todas as threads antes de propagar interrupcoes. |
| 16:45–16:47 | Enzo Garofalo Pampana | Revisao das verificacoes de cancelamento no MergeSort e dos testes de negativos, repetidos e particoes impares. |
| 16:45–16:47 | João Gabriel da Silva Leite | Revisao das mensagens de memoria insuficiente e CPU unica; preparo dos cenarios de entrada e intervalo. |
| 16:47–16:49 | Pedro de Mello Porto Daou | Revisao do Benchmark: seed 10, aquecimento, alternancia de ordem e mediana de cinco amostras. |
| 16:47–16:49 | Enzo Garofalo Pampana | Conferencia dos resultados com Arrays.sort e do que esta incluido nos intervalos cronometrados. |
| 16:47–16:49 | João Gabriel da Silva Leite | Organizacao das capturas das paginas de evidencia, logs originais e relato de dez linhas. |
| 16:49–16:59 | Todos | Revisao dos resultados, conclusao, roteiro e pacote ZIP com JAR e manifesto SHA-256. |

## Impressoes individuais — textos sugeridos

### Pedro de Mello Porto Daou

O ponto que mais chamou minha atencao foi a coordenacao entre as etapas do
paralelismo. A proxima rodada de merge depende da finalizacao de todas as
tarefas anteriores, o que torna join() indispensavel. Comparar copias da mesma
entrada e usar a mediana ajuda a interpretar os resultados com mais cuidado.
Tambem considero importante tratar a interrupcao de forma que nenhuma thread
continue alterando o vetor depois de o metodo devolver o controle.

### Enzo Garofalo Pampana

A parte mais interessante foi relacionar a divisao recursiva do Merge Sort
com a operacao de intercalar. Os indices de inicio, meio e fim precisam estar
bem definidos para nao perder nem repetir elementos. Os testes de negativos,
repetidos e particoes impares mostram que observar um unico vetor correto nao
basta para validar a implementacao. Arrays.sort oferece uma referencia
independente para conferir o algoritmo que desenvolvemos.

### João Gabriel da Silva Leite

A entrada do usuario exige cuidado: tamanho invalido, valor fora do limite
de byte e indices incorretos devem receber mensagens claras. Teclado concentra
a leitura e validacao, enquanto ConsoleVetores cuida das regras dos vetores.
Os registros de execucao e o relato de testes facilitam a demonstracao.
Outro aprendizado e que caber um vetor na memoria nao garante espaco para
as copias e o buffer auxiliar usados durante a ordenacao.

## Cronologia verificavel

Horarios em America/Sao_Paulo (UTC-03). As datas abaixo provem do historico Git
e dos registros de execucao desta sessao. Horarios de commits nao representam
a duracao total do trabalho de uma pessoa.

| Data e horario | Registro | Atividade |
|---|---|---|
| 01/10/2026 09:00:31 | Commit inicial `d8a7016` | Criacao do repositorio remoto. |
| 01/10/2026 09:01:46 | Commit `28a49a7` | Integracao dos algoritmos sequencial/paralelo, programas, comparador, Maven, testes, diario e planejamento; inclui MaiorVetorAproximado. |
| 01/10/2026 09:06:11 | Commit `f67924d` | Extracao de leitura/validacao para Teclado, integracao nos programas e testes de entrada. |
| 04/10/2026 16:45 | Inicio registrado da sessao de fechamento | Auditoria dos requisitos e identificacao dos ajustes finais. |
| 04/10/2026 16:47 | Registro do build | Ajustes de cancelamento, erros de memoria, regra de processadores e benchmark; 14 testes aprovados. |
| 04/10/2026 16:49 | Evidencias TXT/PNG e CSV | Treze cenarios de console, capturas da saida real e benchmark de tres tamanhos. |
| 04/10/2026 16:58 | Build final | Nova compilacao limpa e empacotamento Maven; 14 testes sem falhas. |
| 04/10/2026 16:59:20 | Commit `22eabbe` | Integracao dos ajustes, benchmark, evidencias, roteiro e scripts; enviado ao GitHub. |
| 04/10/2026, apos as medicoes | Documentos e pacote desta entrega | Consolidacao do relato, conclusao tecnica, roteiro de demonstracao e ZIP com integridade SHA-256. |

## Responsabilidade e participacao

O historico Git atribui os commits de 01/10 ao usuario `pedrodaou`. A implementacao
e as verificacoes desta conversa tiveram assistencia automatizada de Codex,
solicitada pelo usuario. Isso nao determina qual integrante participou de cada
atividade fora desta sessao.

Os integrantes informados sao Pedro de Mello Porto Daou, Enzo Garofalo Pampana
e João Gabriel da Silva Leite. A equipe deve revisar as atribuicoes, horarios e
textos sugeridos acima para que a versao submetida corresponda a sua experiencia.

## Observacoes tecnicas registradas

- Particoes impares exigem transportar o intervalo sem par para a rodada seguinte.
- As ordenadoras e juntadoras escrevem em intervalos disjuntos do mesmo vetor;
  `join()` estabelece a espera entre rodadas e a visibilidade dos resultados.
- Interromper apenas a coordenadora nao basta: os workers precisam observar o
  cancelamento e finalizar antes de o metodo devolver o controle.
- Byte em Java e assinado: o intervalo permitido e de -128 a 127.
- Um vetor que cabe sozinho pode nao caber com buffers e copias da comparacao.
- Logs, aquecimento e ordem de execucao influenciam a comparacao de desempenho.

## Conclusao tecnica

As duas implementacoes produziram resultados corretos nos testes automatizados
e nos cenarios de console. O benchmark com 16 processadores disponiveis e 15
ordenadoras obteve speedups medianos de 1,76x, 5,07x e 6,73x para 100 mil,
1 milhao e 10 milhoes de elementos, respectivamente. Algumas amostras pequenas
foram mais lentas na versao paralela, mostrando o custo de criacao e sincronizacao
de threads. O ganho aumentou com o tamanho neste ambiente, mas nao e universal.
Os dados brutos permitem conferir o resultado. Como sintese proposta para a
equipe, o projeto conecta corretude, sincronizacao, validacao de entrada e
medicao de desempenho: um ganho de velocidade so e util quando o resultado
permanece correto e a execucao e explicavel e reproduzivel.
