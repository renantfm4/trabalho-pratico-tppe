# Trabalho Prático - Técnicas de Programação para Plataformas Emergentes (TPPE)
## Etapa 1: TDD (Test-Driven Development) - Curadoria e Deduplicação de Dados Científicos

Este projeto consiste em uma ferramenta de curadoria de dados para repositórios científicos, focada na identificação e unificação de registros de autores duplicados. O desenvolvimento seguiu rigorosamente a técnica de **TDD (Test-Driven Development)**, garantindo a cobertura dos 5 casos de deduplicação propostos.

---

### Integrantes do Grupo

| Nome                      | Matrícula |                                              Usuário Git |
|:--------------------------|:---------:|---------------------------------------------------------:|
| Danielle Rodrigues Silva  | 211061574 |               [@Danizelle](https://github.com/Danizelle) |
| Luiz Gustavo Lopes Campos | 180023179 |     [@luiz-gl-campos](https://github.com/Luiz-GL-Campos) |
| Pedro Lucas Santana       | 202017049 |         [@pedrolucas12](https://github.com/pedrolucas12) |
| Pedro Victor Lima Torreão | 190036761 |     [@PedroTorreao21](https://github.com/PedroTorreao21) |
| Renan Araújo              |           |               [@renantfm4](https://github.com/renantfm4) |


---

### Especificações Técnicas
* **Linguagem**: Java 17 (Amazon Corretto ou Eclipse Temurin)
* **Gerenciador de Dependências**: Apache Maven 3.9.x
* **Framework de Testes**: JUnit Jupiter (Versão 5.10.2)
* **Framework de Suítes**: JUnit Platform Suite (Versão 1.10.2)

---

### 1. Configuração do Ambiente

#### 1.1 Instalação do JDK 17
1. Faça o download do **JDK 17 (LTS)** no site da [Adoptium (Temurin)](https://adoptium.net/temurin/releases/?version=17).
2. Execute o instalador e, durante o processo, certifique-se de marcar a opção **"Set JAVA_HOME variable"**.

#### 1.2 Configuração das Variáveis de Ambiente (Windows)
Caso precise configurar manualmente:
1. Abra o menu Iniciar e digite "Editar as variáveis de ambiente do sistema".
2. Em **Variáveis do Sistema**, crie uma nova variável:
    - **Nome**: `JAVA_HOME`
    - **Valor**: Caminho da pasta de instalação do JDK (ex: `C:\Program Files\Eclipse Foundation\jdk-17...`).
3. Edite a variável **`Path`** e adicione uma nova linha no topo:
    - `%JAVA_HOME%\bin`
4. Reinicie seu terminal e verifique com o comando: `java -version`.

#### 1.3 Instalação do Maven
1. Baixe o Maven em [maven.apache.org](https://maven.apache.org/download.cgi).
2. Extraia o conteúdo em uma pasta permanente.
3. Adicione a pasta `bin` do Maven (ex: `C:\apache-maven\bin`) à variável **`Path`** do sistema.
4. Verifique a instalação com o comando: `mvn -version`.

---

### 2. Estrutura do Projeto

O projeto segue a estrutura padrão do Maven (**Convention over Configuration**):
* `src/main/java`: Contém a lógica de produção e o algoritmo de deduplicação dinâmica.
* `src/test/java`: Contém as suítes de teste, testes parametrizados por categoria (`@Tag`) e testes de exceção.

---

### 3. Como Executar os Testes

Para garantir o funcionamento correto e a validação do estágio **Green** do TDD, execute os comandos abaixo no terminal da raiz do projeto:

**Limpar e executar todos os testes:**
```bash
mvn clean test
```

**Executar apenas uma categoria específica (ex: Casos de Equivalência):**
```bash
mvn test -Dgroups="Equivalencia"
```

Os testes cobrem:
- **Caso 1**: Diferenças tipográficas e acentuação.
- **Caso 2**: Sobrenome + Iniciais simples.
- **Caso 3**: Partículas de ligação ("de", "da", "do") e pontos opcionais.
- **Caso 4**: Iniciais agrupadas (ex: VC Junior).
- **Caso 5**: Unificação de registros pelo menor ID encontrado.
- **Testes de Exceção**: Validação de entradas nulas, vazias ou IDs negativos.

---

### 4. Critérios de Implementação (Deduplicação Dinâmica)
Diferente de abordagens *hard-coded*, este projeto utiliza um **Algoritmo de Peso de Grafia**. Em situações de duplicidade, o sistema analisa dinamicamente:
1. A presença de caracteres acentuados legítimos (Unicode).
2. A presença de partículas de ligação.
3. A ausência de pontos de abreviação.

A string com maior "pontuação de integridade" é eleita automaticamente como o **Padrão-Ouro** para todos os registros unificados.