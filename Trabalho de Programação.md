# 1º Trabalho de Programação Paralela e Distribuída em Java

**Grupo:** 10

RA: 24008944 Nome: Enzo Garofalo Pampana
RA: 21010698 Nome: Pedro de Mello Porto Daou
RA: 24757392 Nome: João Gabriel da Silva Leite

---

## Subsídio 1: A operação de Merge ou de intercalação

Fazer *merge* (ou "intercalação") de dois vetores ordenados significa combinar dois vetores (ou listas) que já estão em ordem crescente (ou decrescente) em um único vetor final também ordenado.

A grande vantagem é que, como os vetores já estão ordenados, não precisamos reordenar tudo do zero — só precisamos comparar elementos de maneira eficiente.

**Exemplo concreto**
Digamos que temos:
A = [1, 4, 7, 10]
B = [2, 3, 6, 11, 12]
Ambos estão em ordem crescente.
Queremos produzir um vetor C, também ordenado:
C = [1, 2, 3, 4, 6, 7, 10, 11, 12]

**Ideia central**
Mantemos dois ponteiros (índices), um para cada vetor:
- i aponta para o elemento atual de A
- j aponta para o elemento atual de B

Comparamos A[i] e B[j]. Inserimos o menor dos dois em C. Avançamos o ponteiro do vetor de onde o elemento foi retirado.

Repetimos até que um dos vetores acabe. Quando um vetor termina, copiamos os elementos restantes do outro vetor.

**Passo a passo detalhado do exemplo**
Começo:
A = [1, 4, 7, 10]
B = [2, 3, 6, 11, 12]

C = []
i = 0, j = 0

| Passo | A[i] | B[j] | Menor | Ação | C após passo |
|---|---|---|---|---|---|
| 1 | 1 | 2 | 1 | adiciona 1 de A | [1] |
| 2 | 4 | 2 | 2 | adiciona 2 de B | [1, 2] |
| 3 | 4 | 3 | 3 | adiciona 3 de B | [1, 2, 3] |
| 4 | 4 | 6 | 4 | adiciona 4 de A | [1, 2, 3, 4] |
| 5 | 7 | 6 | 6 | adiciona 6 de B | [1, 2, 3, 4, 6] |
| 6 | 7 | 11 | 7 | adiciona 7 de A | [1, 2, 3, 4, 6, 7] |
| 7 | 10 | 11 | 10 | adiciona 10 de A | [1, 2, 3, 4, 6, 7, 10] |

Agora A acabou (i passou do final).
Copiamos o resto de B: 11, 12.

Resultado final:
C = [1, 2, 3, 4, 6, 7, 10, 11, 12]

**Intuição visual**
Pense em dois baralhos ordenados colocados lado a lado.
Você vai revelando a carta de cima de cada um, escolhendo a menor e colocando-a em uma nova pilha.
Quando um baralho acaba, você apenas empilha o resto do outro.

---

## Subsídio 2: Ordenação por intercalação (Merge Sort)

O Merge Sort é um dos algoritmos de ordenação mais importantes da computação, e entender minuciosamente seu funcionamento é essencial para compreender conceitos como divisão e conquista, recursão, e complexidade ótima.

Vamos agora explicar de forma mais profunda e estruturada, passo a passo, como se estivéssemos desmontando o algoritmo peça por peça para ver como funciona por dentro.

**Ideia geral**
O Merge Sort (ou "ordenação por intercalação") é um algoritmo de ordenação que segue a estratégia de dividir para conquistar (*divide and conquer*).

A ideia central é:
1. Dividir o vetor em duas metades.
2. Ordenar recursivamente cada metade.
3. Unir (merge) as duas metades ordenadas, produzindo um vetor final ordenado.

**Estrutura geral do algoritmo**
O algoritmo pode ser descrito recursivamente:

```
mergeSort(vetor):
    se o vetor tem 0 ou 1 elemento:
        já está ordenado → retorna o vetor
    senão:
        divide o vetor em duas metades:
            esquerda = primeira metade
            direita = segunda metade
        ordena recursivamente a metade esquerda
        ordena recursivamente a metade direita
        une (merge) as duas metades ordenadas
        retorna o vetor resultante
```

**Exemplo passo a passo**
Vamos ordenar o vetor:
[38, 27, 43, 3, 9, 82, 10]

**Passo 1 — Divisão inicial**
Dividimos o vetor no meio:
Esquerda: [38, 27, 43]
Direita: [3, 9, 82, 10]

Agora aplicamos o mesmo processo **recursivamente** em cada parte.

**Passo 2 — Dividindo a parte esquerda [38, 27, 43]**
Esquerda: [38]
Direita: [27, 43]

- [38] já está ordenado (apenas um elemento)
- Agora dividimos [27, 43]:
  - Esquerda: [27]
  - Direita: [43]
  - Ambas têm um elemento → já estão ordenadas.
- Fazemos o **merge** de [27] e [43] → [27, 43].

Agora fazemos merge de [38] com [27, 43]: **[27, 38, 43]**

**Passo 3 — Dividindo a parte direita [3, 9, 82, 10]**
Esquerda: [3, 9]
Direita: [82, 10]

- [3, 9] → divide em [3] e [9] → merge → [3, 9].
- [82, 10] → divide em [82] e [10] → merge → [10, 82].
- Agora merge de [3, 9] e [10, 82] → [3, 9, 10, 82].

**Passo 4 — Merge final**
Agora temos:
Esquerda ordenada: [27, 38, 43]
Direita ordenada: [3, 9, 10, 82]
Fazemos o merge final:
→ **[3, 9, 10, 27, 38, 43, 82]**
E pronto: o vetor está ordenado.

**Estrutura recursiva visual**
Podemos visualizar o processo assim (cada nível representa uma divisão):

```
                [38,27,43,3,9,82,10]
                 /                 \
        [38,27,43]                [3,9,82,10]
        /       \                 /          \
    [38]     [27,43]          [3,9]         [82,10]
              /   \           /   \          /   \
          [27]   [43]      [3]   [9]      [82]   [10]
              \   /           \   /          \   /
             [27,43]          [3,9]         [10,82]
                  \              /             /
              [27,38,43]      [3,9,10,82]
                       \         /
                 [3,9,10,27,38,43,82]
```

**Intuição conceitual**
Imagine que você tem vários baralhos pequenos ordenados e quer formar um grande baralho ordenado.
Em vez de embaralhar tudo e ordenar de novo, você vai intercalar os pequenos de dois em dois, sempre mantendo a ordem.
O Merge Sort faz exatamente isso — mas de forma automática e sistemática, usando recursão.

---

## Objetivo do trabalho

Ordenar um grande vetor de números inteiros do tipo byte. Para tanto, você deve implementar um programa paralelo de ordenação em Java, o qual gera umvetor de números inteiros tipo byte, particiona o vetor em partes de tamanho aproximadamente iguais, e envia essas partes a diferentes linhas de execução realmente paralelas (devem existir em número exatamente igual à quantidade de processadores menos um) que executam a ordenação em paralelo; são as threads ordenadoras.

Assim, várias threads devem ser postas em execução para realizar simultaneamente partes da ordenação designada pela main ; é claro que para ter simultaneidade real é preciso por em execução uma quantidade de threads no máximo igual à quantidade de processadores que há na máquina.

Em suma, cada thread ordenadora ordena a parte que lhe cabe recursivamente pelo método Merge Sort, os resultados de todas as threads serão juntadas 2 a 2 num vetor só por threads juntadoras (que fazem merge) até obter um só vetor . Várias rodadas de execução e threads juntadoras devem ser necessárias, cada rodada diminuindo pela metade a quantidade necessária de juntadoras.

**Boas práticas exigidas e outras obrigações**
- Ofereça ao usuário a possibilidade de decidir quantos elementos quer ter no vetor a ser ordenado, bem como de preenchê-lo à mão ou de forma automática com números aleatórios.
- Ofereça ao usuário, ao final do processo, a opção de decidir printar todo o vetor ordenado ou a parte que decidir printar dele.
- Capture e trate exceções adequadamente.
- Use join() para aguardar a finalização das threads.
- Insira mensagens de log informativas em ambos os programas.
- Faça também um programa que realize a ordenação sem paralelismo .
- Meça os tempos de execução de ambos os programas para fins de comparação.
- Elabore um diário sobre o desenvolvimento da atividade, relatando a cronologia do desenvolvimento da atividade, com especial ênfase nas atividades desenvolvidas por cada membro do time de devs, incluindo timestamps. Inclua também as impresssões dos devs, bem como uma conclusão.
- Além de entregar a atividade no Canvas, demonstre-a ao professor em aula a partir de 06/outubro, conforme escala a ser divulgada.

**Entrega**
- Código-fonte completo.
- Capturas de tela mostrando os programas em execução.
- Relato breve (até 10 linhas) sobre os testes realizados.

**Descobrir quantos processadores há na máquina?**
int quantidade = Runtime.getRuntime().availableProcessors ( );

**Como descobrir o que é um vetor grande e como medir o tempo de execução dum programa?**

```java
public class MaiorVetorAproximado {
  public static void main(String[ ] args) {
    System.out.println("Estimando o maior tamanho possível de vetor em Java...");
    long inicio = System.currentTimeMillis();

    int tamanho = 1_000_000;  // começa com 1 milhão
    int ultimoBemSucedido = 0;

    while (true) {
      try {
        byte[ ] vetor = new byte[tamanho];
        ultimoBemSucedido = tamanho;
        vetor = null;  // libera
        System.gc();

        // aumenta o tamanho em 50% para a próxima tentativa
        if (tamanho > Integer.MAX_VALUE / 3 * 2) break;

        tamanho /= 2;
        tamanho *= 3;

        System.out.printf("Alocado com sucesso: %,d elementos%n", ultimoBemSucedido);
      } catch (OutOfMemoryError e) {
        System.out.printf("Falhou em %,d elementos%n", tamanho);
        break;
      }
    }

    long fim = System.currentTimeMillis();
    System.out.println("\nMaior vetor que coube (aproximadamente): "+
                        String.format("%,d", ultimoBemSucedido));
    System.out.printf("Memória estimada: %.2f MB%n",
                        ultimoBemSucedido * 1.0 / (1024 * 1024));
    System.out.printf("Tempo total: %.2f segundos%n", (fim - inicio) / 1000.0);
  }
}
```

Caixa de comentário ao lado do código:
```
// rode o programa com o comando:
// java -Xmx8G MaiorVetorAproximado
// para disponibilizar 8Gb de memória para uso do Java
```

---

**Observações:**
- A ordem das páginas é: capa e Subsídio 1 (foto 2), continuação do Subsídio 1 e início do Subsídio 2 (foto 1), continuação do pseudocódigo e exemplo passo a passo (foto 5), estrutura visual e objetivo (foto 3) e, por fim, o fim das obrigações, a entrega e o código (foto 4).
- Mantive os erros de digitação do original ("umvetor", "impresssões", "Gb").
- Os nomes manuscritos na capa estão difíceis de ler, então confira-os.
- A árvore em ASCII foi reconstruída com o espaçamento aproximado.