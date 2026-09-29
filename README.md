# API de Livros

API REST simples feita com **Java** e **Spring Boot** para cadastrar, consultar, atualizar e excluir livros. Os dados ficam armazenados **em memória**, em uma lista, e são perdidos quando a aplicação é reiniciada.

Projeto desenvolvido para atividade da disciplina. Camadas utilizadas: apenas `model` e `controller`.

## Tecnologias

- Java 17
- Spring Boot (Spring Web)
- Maven (via Maven Wrapper, não precisa instalar)
- Postman para os testes

## Pré-requisitos

- **JDK 17 completo** (não apenas o JRE). Recomendado: [Temurin 17](https://adoptium.net)
- Variável de ambiente `JAVA_HOME` apontando para a pasta do JDK 17
- Postman

Para conferir, rode no terminal:

```
java -version
javac -version
```

Os dois devem mostrar a versão 17.

## Como executar

1. Clone o repositório e entre na pasta do projeto (a que contém o `pom.xml`):

```
git clone URL_DO_REPOSITORIO
cd api
```

2. Suba a aplicação:

```
.\mvnw.cmd spring-boot:run      (Windows PowerShell)
./mvnw spring-boot:run          (Linux/Mac)
```

3. Aguarde a mensagem `Started ApiApplication`. A API fica disponível em:

```
http://localhost:8080/livros
```

Para parar, use `Ctrl+C` no terminal.

## Estrutura do projeto

```
src/main/java/com/example/api/
├── ApiApplication.java
├── model/
│   └── Livro.java
└── controller/
    └── LivroController.java
```

## Modelo de dados

| Campo | Tipo | Observação |
|---|---|---|
| id | Long | Gerado automaticamente pela API |
| titulo | String | |
| autor | String | |
| genero | String | |
| anoPublicacao | Integer | |
| disponivel | Boolean | |

A aplicação já inicia com 4 livros cadastrados (IDs 1 a 4) para facilitar os testes.

## Endpoints

| Método | Rota | Descrição | Status de retorno |
|---|---|---|---|
| GET | `/livros` | Lista todos os livros (aceita filtros) | 200 |
| GET | `/livros/{id}` | Busca um livro pelo ID | 200 ou 404 |
| POST | `/livros` | Cadastra um novo livro | 201 |
| PUT | `/livros/{id}` | Atualiza um livro existente | 200 ou 404 |
| DELETE | `/livros/{id}` | Exclui um livro | 204 ou 404 |

## Filtros (`@RequestParam`)

Todos são opcionais e **podem ser combinados** no `GET /livros`:

| Parâmetro | Tipo | Comportamento |
|---|---|---|
| autor | texto | Contém o texto informado (ignora maiúsculas/minúsculas) |
| genero | texto | Igual ao gênero informado (ignora maiúsculas/minúsculas) |
| disponivel | true/false | Filtra pela disponibilidade |
| anoMinimo | número | Livros publicados a partir do ano informado |

Exemplos:

```
GET /livros?autor=machado
GET /livros?genero=fantasia
GET /livros?disponivel=true
GET /livros?anoMinimo=1900
GET /livros?autor=machado&disponivel=true
```

## Como enviar os dados no Postman

| Anotação | Onde vai no Postman | Exemplo |
|---|---|---|
| `@PathVariable` | Na própria URL | `/livros/1` |
| `@RequestParam` | Aba **Params** (depois do `?`) | `/livros?autor=machado` |
| `@RequestBody` | Aba **Body > raw > JSON** | POST e PUT |

**Atenção:** no POST e no PUT os dados vão no **Body** em JSON. Se forem colocados em Params, a API responde `400 Bad Request`.

### Exemplo de body (POST e PUT)

```json
{
  "titulo": "Harry Potter e a Pedra Filosofal",
  "autor": "J.K. Rowling",
  "genero": "Fantasia",
  "anoPublicacao": 1997,
  "disponivel": true
}
```

## Ordem sugerida de testes

Reinicie a API antes, para os IDs baterem.

| # | Método | URL | Esperado |
|---|---|---|---|
| 1 | GET | `/livros` | 200, 4 livros |
| 2 | GET | `/livros/1` | 200, Dom Casmurro |
| 3 | GET | `/livros/99` | 404 |
| 4 | POST | `/livros` (body acima) | 201, com `id: 5` |
| 5 | PUT | `/livros/5` (com `disponivel: false`) | 200 |
| 6 | DELETE | `/livros/5` | 204 |
| 7 | GET | `/livros/5` | 404 |
| 8 | GET | `/livros?autor=machado` | 2 livros |
| 9 | GET | `/livros?disponivel=true` | 3 livros |
| 10 | GET | `/livros?genero=fantasia` | O Hobbit |
| 11 | GET | `/livros?anoMinimo=1900` | O Hobbit e 1984 |
| 12 | GET | `/livros?autor=machado&disponivel=true` | Dom Casmurro |

## Problemas comuns

| Problema | Causa provável | Solução |
|---|---|---|
| 404 em `/livros` | API não reiniciada ou arquivos não salvos | Salvar tudo, `Ctrl+C` e subir de novo |
| 400 no POST/PUT | Dados enviados em Params | Usar Body > raw > JSON |
| `No compiler is provided` | Java sem o compilador (JRE) | Instalar o JDK 17 completo |
| `class file has wrong version` | `JAVA_HOME` aponta para Java antigo | Apontar `JAVA_HOME` para o JDK 17 |
| `mvnw.cmd não é reconhecido` | PowerShell exige o caminho | Usar `.\mvnw.cmd` (com `.\`) |
| Porta 8080 ocupada | Outro programa usando a porta | Colocar `server.port=8081` em `application.properties` |

## Integrantes

- Nome 1
- Nome 2
- Nome 3
