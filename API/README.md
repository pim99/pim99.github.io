# API do PIM

Para a primeira execução da API, certifique-se de configurar o `applications.properties` com as informações do seu banco de dados (URL, Username e Password). ALém disso, para a criação das tabelas e dados iniciais, defina as seguintes propriedades:

`spring.datasource.initialization-mode=always`

`spring.jpa.hibernate.ddl-auto=create`

Posteriormente, alterar as propriedades para evitar que o Banco de Dados seja recriado:


`spring.datasource.initialization-mode=never`

`spring.jpa.hibernate.ddl-auto=update`
