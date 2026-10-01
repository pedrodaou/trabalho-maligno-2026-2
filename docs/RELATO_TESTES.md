# Relato breve dos testes

1. Testamos vetores vazio, unitario, com valores negativos e repetidos.
2. Incluimos os limites do tipo `byte` (-128 e 127).
3. Comparamos os resultados com `Arrays.sort` como referencia.
4. Exercitamos a versao paralela com 1, 2, 3, 4, 7 e 16 threads.
5. Testamos o caso com mais threads do que elementos no vetor.
6. Validamos entradas aleatorias de 10.003 e 100.000 elementos.
7. O comparador tambem verifica se as duas implementacoes geram vetores iguais.
8. Em JDK 21, os 8 testes automatizados passaram sem falhas ou erros.
9. Teste preliminar com 1.000.000 de bytes: 151,082 ms contra 78,425 ms (1,93x).
10. Tempos definitivos e capturas da maquina da apresentacao ainda serao feitos.
