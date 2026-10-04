# Diario de desenvolvimento — grupo 10

Horarios em America/Sao_Paulo (UTC-03). As datas abaixo provem do historico Git
e dos registros de execucao desta sessao. Horarios de commits nao representam
a duracao total do trabalho de uma pessoa.

## Cronologia verificavel

| Data e horario | Registro | Atividade |
|---|---|---|
| 01/10/2026 09:00:31 | Commit inicial `d8a7016` | Criacao do repositorio remoto. |
| 01/10/2026 09:01:46 | Commit `28a49a7` | Integracao dos algoritmos sequencial/paralelo, programas, comparador, Maven, testes, diario e planejamento; inclui MaiorVetorAproximado. |
| 01/10/2026 09:06:11 | Commit `f67924d` | Extracao de leitura/validacao para Teclado, integracao nos programas e testes de entrada. |
| 04/10/2026 16:45 | Inicio registrado da sessao de fechamento | Auditoria dos requisitos e identificacao dos ajustes finais. |
| 04/10/2026 16:47 | Registro do build | Ajustes de cancelamento, erros de memoria, regra de processadores e benchmark; 14 testes aprovados. |
| 04/10/2026 16:49 | Evidencias TXT/PNG e CSV | Treze cenarios de console, capturas da saida real e benchmark de tres tamanhos. |
| 04/10/2026 16:58 | Build final | Nova compilacao limpa e empacotamento Maven; 14 testes sem falhas. |
| 04/10/2026, apos as medicoes | Documentos e pacote desta entrega | Consolidacao do relato, conclusao tecnica, roteiro de demonstracao e ZIP com integridade SHA-256. |

## Responsabilidade e participacao

O historico Git atribui os commits de 01/10 ao usuario `pedrodaou`. A implementacao
e as verificacoes desta conversa tiveram assistencia automatizada de Codex,
solicitada pelo usuario. Isso nao determina qual integrante participou de cada
atividade fora desta sessao.

Os nomes completos confirmados, a divisao real de tarefas entre integrantes,
os horarios de suas sessoes e as impressoes individuais ainda nao foram informados.
Esses dados precisam ser fornecidos pela equipe antes do envio academico para
atender integralmente ao requisito de diario por membro.

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
Os dados brutos permitem conferir o resultado. A conclusao sobre a experiencia
pessoal de cada integrante depende dos relatos reais da equipe.
