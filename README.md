

### Este é um serviço que tem como objetivo cadastrar parceiros, que são pontos de vendas com uma geolocalização específica, e permitir que, a partir de um ponto em latitude/longitude, seja encontrado o parceiro mais próximo. O arquivo MD do desafio pode ser encontrado em `/docs/backend.md`

## Por trás das escolhas
### Armazenamento dos Parceiros
Para armazenar uma grande quantidade de parceiros, considerando seus campos Address e CoverageArea, que seguem o padrão GeoJSON, escolhi utilizar o banco Postgres com a extensão PostGIS, esta extensão não só me da a tipagem Geometry no banco para salvar esses campos GeoJson como também uma serie de recursos para trabalhar com geolocalizão direto nas querys.
### Linguagem, Frameworks e Bibliotecas
Escolhi criar os serviços utilizando Java na sua versão 11 e o Spring Framework na versão 2.4.5. O Java escolhi por ter uma maior maestria com a linguagem e considerar o ecossistema do Spring uma forma rápida e eficaz de tirar as ideias do papel, também considerando o uso do SpringBoot para configuração e serviço de aplicação embarcado. Para lidar com a serialização e deserialização dos objetos GeoJson utilizei a biblioteca Geojson-Jackson(https://github.com/opendatalab-de/geojson-jackson) e para a manipulação dos objetos geometricos e calculos utilizei o Geotools(https://geotools.org/)
### Arquitetura
Pensando em dividir as responsabilidades para conseguir escalonar o mecanismo de busca, que imagino em uma possível produção desse serviço ser o concentrador da maior demanda, escolhi criar um barramento basedo em micro serviços, um projeto multi modular com dois módulos executaveis, sendo eles o Data e o Nearest.
#### Data
Serviço responsável pela camada de persistencia de novos Parceiros.
#### Nearest
Serviço responsável pela camada de busca do Parceiro mais próximo com base em uma geolocalização específica.
#### Labs
Serviço criado para experimentar algumas ideias, validar casos de uso e afins.
## Parceiro mais próximo - Nearest
Obs: Abaixo a palavra input sempre se refere a latitude e longitude informada do ponto de origem.
#### Base
A escolha do PostGIS se deu para colocar em prática a primeira ideia desse mecanismo, buscar inicialmente apenas parceiros que estão em um raio X de kilometros, filtrando assim potenciais parceiros antes de pensar em área de cobertura. Tomei esse caminho pensando em uma base de produção, em alta escala de dados e requisições, quanto maior o número de áreas de coberturas para buscar o input, maior desperdicio de processamento.
#### Range
A primeira Query feita portanto utiliza uma função do PostGIS chamada ST_Distance, que com base no campo Address que é um Point, trás apenas parceiros que estão em um raio X de kilometros do input.
#### Área de cobertura
A validação da área de cobertura poderia também ser feita pelo PostGIS, seria por exemplo uma segunda condicional utilizando o ST_Contains, mas tanto para dividir o processamento com a aplicação e colocar a segunda ideia do mecanismos em prática, a partir desse ponto, eu decidi trazer para a apalicação. Tendo em mãos uma lista de parceiros que estão no range, utilizei a biblioteca do GeoTools para calcular os parceiros que o input esta dentro da area de cobertura.
#### Mais próximo
Após calcular o parceiro em que o input está dentro da área de cobertura, no caso de encontrar mais de um, preciso escolher o mais próximo, aqui começa a segunda ideia do mecanismo. Partindo do principio que, não necessariamente o parceiro mais próximo é o que esta a uma distancia menor em kilometros, logo que ruas, rodoviais e afins influenciam diretamente nisso, nessa etapa criei o que chamei de RouteGateway, onde podemos direcionar diferentes estrategias para dizer qual é o mais próximo.
#### RouteGateway - Standard
Como estratégia padrão, percorro a lista de parceiros disponíveis e calculo aquele que o Address esta, em kilometros, mais próximo do input.
#### RouteGateway - Route
A estrategia route tem como objetivo considerar fatores extra distancia em kilometros, como os mencionados acima, aqui para prova de conceito eu estou utilizando o serviço Routing API da TomTom(https://www.tomtom.com/pt_br/), por proporcionar um uso gratuito para fins de desenvolvimento, mas poderiamos utulizar o Distance Matrix API da Google também. No Tomtom é enviado o input e o destino de cada potencial parceiro, e com base no que consideramos importante da resposta da API, calculo o parceiro ideal. Neste caso decidi utilizar o campo que ele trás chamado travelTimeInSeconds, que trás quanto tempo levaria de um ponto ao outro em segundos, com base nas ruas,avenidas e considerandi estar dirigindo um veículo.
## Como rodar o projeto
### Docker
Você vai precisar ter o Docker instalado na sua maquina, vamos utilizar as seguintes imagens
- postgis/postgis https://registry.hub.docker.com/r/postgis/postgis
- openjdk:11.0.4-jre-slim https://hub.docker.com/_/openjdk?tab=tags&page=1&name=11.0.4-jre-slim
- (Opcional) maven:3.6.0-jdk-11-slim https://hub.docker.com/_/maven
### Build
Primeiro passo é buildar o projeto para gerar os arquivos binarios, vamos precisar dos recursos :
- JDK do Java na versão 11 (https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- Maven em um versão 3.6.0 ou superior (https://maven.apache.org/download.cgi)

#### Se tiver ambos instalados e configurados
Acesse o diretorio raiz do do projeto na sua maquina e rode o comando
```  
mvn clean install  
```  
#### Se não tiver ambos instalados
Você pode utilizar uma imagem do próprio Maven(https://github.com/carlossg/docker-maven), informando na tag a versão da JDK. Para buildar, acesse o diretorio raiz do projeto na sua maquina e rode o comando
```  
docker container run -it --name mvnchallenge -v DiretorioDoProjeto:/usr/src/mymaven -w /usr/src/mymaven maven:3.6.0-jdk-11-slim mvn clean install  
```  
Exemplo no Windows para o DiretorioDoProjeto -> F:\desenv\challenges\partners-api-challenge
### Run
Após gerar os arquivos binarios, basta acessar a pasta Docker que se encontra na Raiz do projeto, e rodar o seguinte comando
```  
docker-compose up -d  
```  
Pronto, o banco de dados Postgres/PostGIS e os micro serviços Data e Nearest estão no ar!
### .env
Na pasta Docker se encontra o arquivo .env, nele você pode alterar as seguintes variaveis
```  
#Services Ports DATA_EXTERNAL_PORT=9040 NEAREST_EXTERNAL_PORT=9041 POSTGRES_EXTERNAL_PORT=5435  
```  
Caso alguma destas portas esteja em uso na sua maquina.


## Consumindo os endpoints
### Swagger
Com os serviços no ar você pode acessar o Swagger de ambos  
Data -> http://localhost:9040/challenge/data/swagger-ui.html  
Nearest -> http://localhost:9041/challenge/nearest/swagger-ui.html

### Cadastrando um novo Parceiro.
```  
POST -> http://localhost:9040/challenge/data/v1/partners/  
Body  
{  
 "tradingName": "Bar do João", "ownerName": "Joao Castro", "document": "12345678952", "coverageArea": { "type": "MultiPolygon", "coordinates": [[ [[ -49.07109975814819, -22.323579248218913 ], [ -49.07592773437499, -22.328581231115066 ], [ -49.06710863113403, -22.332094421405362 ], [ -49.07109975814819, -22.323579248218913 ]], [[ -49.068589210510254, -22.32346015120244 ], [ -49.06464099884033, -22.326179507739987 ], [ -49.06556367874145, -22.32127668788629 ], [ -49.068589210510254, -22.32346015120244 ]] ]] }, "address": { "type": "Point", "coordinates": [ -49.07230138778686, -22.329355331490987 ] }}  
  
Lembrando que os campos coverageArea e address respeitam o padrão GeoJson.  
```  
### Buscando um Parceiro existente pelo ID.
```  
GET -> http://localhost:9040/challenge/data/v1/partners/{id}  
```  
### Buscando o Parceiro mais próximo.
```  
GET -> http://localhost:9001/challenge/nearest/v1/partners/?long=-46.636962890625&lat=-23.575630309112977&mode=Route  
```  

## Roadmap importante para o projeto na minha visão
### Cache para aliviar o banco
Acredito que poderia evoluir a primeira busca do Range e utilizar algo como o Redis para cachear algumas 'zonas', considerando grandes metropolis, onde muitas pessoas vão consumir de pontos iniciais muito parecidos, poderia aliviar bastante. Precisaria considerar um mecanismo para limpar esse cache sempre que novos parceiros, nessas 'zonas', ficassem ativos ou vice-versa.
### Loadbalance e Service Discovery
Utilizar alguma tecnologia(por exemplo Kubernetes) para subir novas instancias do Nearest com base em quantidade de requisições, uso de hardware ou alguma outra estrategia.
### Reatividade
Utilizar o WebFlux para tornar os endpoints reativos.
### Validação dos dados
Validação mais apurada dos dados de entrada, como por exemplo validar se o campo Document no cadastro de um parceiro é um CPF ou CNPJ válido.
### Melhorar o OpenAPI
Melhorar a documentação dos Endpoints, a interpretação 'crua' dos GeoJson ficam confusos no Swagger.