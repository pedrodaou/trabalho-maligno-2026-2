# Plano de entrega em cinco dias

## Dia 1 — MVP executavel (2 horas)

Entender os requisitos, implementar os dois algoritmos, criar a interface de
console, medir tempos e cobrir os casos essenciais com testes.

## Dia 2 — revisao e robustez

- Cada integrante revisa uma area diferente: algoritmo, concorrencia e console.
- Rodar testes em pelo menos duas maquinas/JDKs.
- Conferir manualmente valores invalidos, interrupcoes e vetores pequenos.
- Registrar horarios, autores, decisoes e impressoes no diario.

## Dia 3 — experimento de desempenho

- Definir tamanhos reproduziveis, por exemplo: 100 mil, 1 milhao, 10 milhoes.
- Fazer uma execucao de aquecimento e ao menos cinco medicoes por tamanho.
- Registrar mediana, processadores, memoria, JDK e quantidade de threads.
- Evitar imprimir o vetor durante a medicao.

## Dia 4 — acabamento

- Corrigir os problemas encontrados nos testes.
- Melhorar mensagens e revisar se todas as obrigacoes do enunciado aparecem.
- Revisar comentarios, nomes e explicacao oral do fluxo das threads.

## Dia 5 — pacote de entrega

- Rodar `mvn clean test package` em uma copia limpa.
- Produzir capturas das versoes sequencial, paralela e comparativa.
- Atualizar o relato de testes (maximo de 10 linhas) e concluir o diario.
- Conferir o conteudo final no Canvas antes do envio.

