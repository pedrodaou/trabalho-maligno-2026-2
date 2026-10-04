# Checklist de entrega

## Incluido no projeto e no pacote

- [x] Codigo Java: MergeSort, ParallelMergeSort, ProgramaSequencial, ProgramaParalelo,
  Comparador, Teclado, ConsoleVetores, MaiorVetorAproximado e Benchmark.
- [x] Entrada manual/aleatoria, logs, impressao completa/parcial e medicao de tempo.
- [x] Ordenadoras em quantidade processadores - 1 e juntadoras em rodadas com join().
- [x] Tratamento de entradas invalidas, memoria insuficiente e interrupcoes.
- [x] Quatorze testes automatizados e treze cenarios de console validados.
- [x] Capturas das paginas de evidencia com saida real, HTMLs e logs originais.
- [x] Relato de testes com dez linhas.
- [x] Desempenho com ambiente, aquecimento, repeticoes, medianas e CSV.
- [x] Diario com cronologia verificavel e conclusao tecnica.
- [x] Roteiro de demonstracao e script de empacotamento com SHA-256.

## Acoes dependentes da equipe ou do ambiente externo

- [ ] Completar no diario os nomes confirmados, atividades, horarios e impressoes
  individuais dos integrantes. Essas informacoes nao podem ser inferidas dos commits.
- [ ] Se exigido pelo professor, fazer capturas do terminal na maquina da apresentacao.
- [ ] Enviar `dist/trabalho-maligno-2026-2.zip` ao Canvas e conferir o recebimento.
- [ ] Confirmar a escala e realizar a demonstracao presencial a partir de 06/10/2026.

Canvas e apresentacao presencial nao foram realizados por esta sessao.
Nao ha acesso autenticado ao Canvas disponivel no ambiente desta conversa.

## Conferencia do pacote

```bash
mvn clean test package
python3 scripts/empacotar_entrega.py
unzip -l dist/trabalho-maligno-2026-2.zip
```

O ZIP contem fontes, testes, scripts, documentos, evidencias, JAR em `bin/`
e `SHA256SUMS.txt`. A pasta `dist/` e ignorada pelo Git para evitar versionar
o ZIP gerado; fontes e evidencias ficam no repositorio.
