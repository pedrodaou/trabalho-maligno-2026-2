1. Em JDK 21, os 14 testes automatizados passaram sem falhas ou erros.
2. Merge Sort foi conferido contra Arrays.sort com negativos, repetidos e limites de byte.
3. Testamos vetores vazio/unitario e particoes com 1, 2, 3, 4, 7 e 16 threads.
4. Mais threads que elementos e particoes impares produziram a ordenacao correta.
5. Interrupcoes antes/durante a ordenacao preservaram o sinal e encerraram os workers.
6. Treze cenarios de console validaram entrada manual, intervalo, erros de memoria e CPU unica.
7. MaiorVetorAproximado foi executado com heap limitado a 32 MB e tratou o esgotamento.
8. Benchmark usou seed 10, dois aquecimentos e cinco amostras por tamanho, alternando a ordem.
9. Para 100 mil, 1 milhao e 10 milhoes de bytes, os speedups medianos foram 1,76x, 5,07x e 6,73x.
10. Tempos, ambiente, capturas e logs originais estao em DESEMPENHO.md e evidencias/.
