# Gerenciamento de Ordens de Serviço com Estruturas de Dados Otimizadas

Este repositório contém uma série de implementações para o gerenciamento eficiente de Ordens de Serviço (OS) utilizando estruturas de dados variadas. Cada branch explora um método específico para balancear desempenho e armazenamento, otimizando o gerenciamento das OS em diferentes cenários.

## Estrutura das Branches

### Branch `main` - Árvore AVL com Cache Eviction
A branch principal utiliza uma **árvore AVL** para gerenciar as Ordens de Serviço, garantindo uma estrutura balanceada e permitindo busca, inserção e remoção em tempo logarítmico \(O(log n)\). Com a implementação de **Cache Eviction**, as OS menos acessadas ou antigas são removidas conforme a necessidade, otimizando o uso de memória e garantindo acesso rápido aos dados mais importantes.

**Características principais:**
- Estrutura de dados balanceada (Árvore AVL).
- Estratégia de Cache Eviction para gerenciamento de memória.
- Ideal para cenários que exigem rápida recuperação de dados ordenados.

### Branch `branHash` - Tabela Hash
Nesta branch, as Ordens de Serviço são armazenadas em uma **tabela hash**, oferecendo uma complexidade de tempo média constante \(O(1)\) para inserções e consultas. Essa abordagem é vantajosa para cenários onde a consulta rápida é priorizada e não há necessidade de manter os dados ordenados.

**Características principais:**
- Acesso e consulta rápida com complexidade constante.
- Simplicidade e eficiência para grandes volumes de dados.
- Armazenamento eficiente para operações frequentes de busca direta.

### Branch `branchMsg` - Compressão de Huffman e Processamento de Strings
Nesta implementação, as OS são armazenadas utilizando o **algoritmo de compressão de Huffman** para reduzir o espaço de armazenamento, especialmente útil para grandes volumes de dados textuais. O **processamento de strings** também está presente, permitindo manipulação avançada de texto para buscas e reconhecimento de padrões.

**Características principais:**
- Compressão de dados com o algoritmo de Huffman.
- Processamento de strings para busca e manipulação eficiente de textos.
- Ótimo para reduzir espaço em sistemas com dados textuais extensivos.

## Estrutura do Projeto

O repositório é organizado de forma modular, com cada branch oferecendo uma abordagem distinta e flexível para o gerenciamento de OS. Esse modelo permite que cada método seja testado e adaptado conforme as necessidades específicas de cada cenário de uso.

```plaintext
.
├── main
│   └── AVL Tree + Cache Eviction
│
├── branHash
│   └── Tabela Hash
│
└── branchMsg
    └── Compressão de Huffman + Processamento de Strings
