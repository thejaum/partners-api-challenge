### Este é um serviço que tem como objetivo cadastrar parceiros, que são pontos de vendas com uma geolocalização específica, e permitir que, a partir de um ponto em latitude/logitude, seja encontrado o parceiro mais próximo. O arquivo MD do desafio pode ser encontrado em `/docs/backend.md`

## Por trás das escolhas
### Armazenamento dos Parceiros
Para armazenar uma grande quantidade de parceiros, considerando seus campos Address e CoverageArea, que seguem o padrão GeoJSON, escolhi utilizar o banco Postgres com a extensão PostGIS, esta extensão não só me da a tipagem Geometry no banco para salvar esses campos GeoJson como também uma serie de recursos para trabalhar com geolocalizão direto nas querys.
### Linguagem, Frameworks e Bibliotecas
Escolhi criar os serviços utilizando Java na sua versão 11 e o Spring Framework na versão 2.4.5. O Java escolhi por ter uma maior maestria com a linguagem e considerar o Ecossistema do Spring uma forma rapida e eficaz de tirar as ideias do papel, também considerando o uso do SpringBoot para configuração e serviço de aplicação embarcado. Para lidar com a serialização e deserialização dos objetos GeoJson utilizei a biblioteca Geojson-Jackson(https://github.com/opendatalab-de/geojson-jackson) e para a manipulação dos objetos geometricos e calculos utilizei o Geotools(https://geotools.org/)
### Arquitetura
Pensando em dividir as responsabilidades para conseguir escalonar o mecanismo de busca, que imagino em uma possível produção desse serviço ser o concentrador da maior demanda, escolhi criar um barramento basedo em micro serviços, um projeto multi modular com dois módulos executaveis, sendo eles o Data e o Nearest.
#### Data
Serviço responsável pela camada de persistencia de novos Parceiros.
#### Nearest
Serviço responsável pela camada de busca do Parceiro mais próximo com base em uma geolocalização específica.
#### Labs
Serviço criado para experimentar algumas ideias, validar casos de uso e afins.
## Parceiro mais próximo - Nearest
Abaixo a palavra input sempre se refere a latitude e longitude informada do ponto de origem.
#### Base
A escolha do PostGIS se deu para colocar em prática a primeira ideia desse mecanismo, "Buscar inicialmente apenas parceiros que estão em um raio X de kilometros", filtrando assim potenciais parceiros antes de pensar em área de cobertura. Tomei esse caminho pensando em uma base de produção, em alta escala de dados e requisições, quanto maior o número de áreas de coberturas para buscar o input, maior desperdicio de processamento.
#### Range
A primeira Query feita portanto utiliza uma função do PostGIS chamada ST_Distance, que com base no campo Address que é um Point, trás apenas parceiros que estão em um raio X de kilometros do input de latitude/longitude.
#### Área de cobertura
A validação da área de cobertura poderia também ser feita pelo PostGIS, seria uma segunda condicional utilizando o ST_Contains, mas tanto para dividir o processamento com a aplicação e colocar a segunda ideia do mecanismos em prática, a partir desse ponto, eu decidi trazer para a apalicação. Tendo em mãos uma lista de parceiros que estão no range utilizei a biblioteca do GeoTools para calcular os parceiros que o input esta dentro da area de cobertura.
#### Mais próximo
Após calcular o parceiro em que o input está dentro da área de cobertura, no caso de encontrar mais de um, preciso escolher o mais próximo, aqui começa a segunda ideia do mecanismo. Partindo do principio que, não necessariamente o parceiro mais próximo é o que esta a uma distancia menor em kilometros, logo que ruas, rodoviais e afins influenciam diretamente nisso, nessa etapa criei o que chamei de RouteGateway, onde podemos direcionar diferentes estrategias para dizer qual é o mais próximo.
#### RouteGateway - Standard
Como estratégia padrão, intero entre os parceiros disponíveis e calculo aquele que o Address esta, em kilometros, mais próximo do input.
#### RouteGateway - Route
A estrategia route tem como objetivo considerar fatores extra distancia em kilometros, como os mencionados acima, aqui para prova de conceito eu estou utilizando o serviço Routing API da TomTom(https://www.tomtom.com/pt_br/), onde enviamos o input e o destino de cada potencial parceiro, e com base no que consideramos importante da resposta da API, calculo o parceiro ideal. Neste caso decidi utilizar o campo que ele trás chamado travelTimeInSeconds, que representa com base nas ruas e avenidas, dirigindo um veiculo, quanto tempo levaria de um ponto ao outro em segundos.

--docker container run -it --name mvn001 -v F:\desenv\challenges\partners-api-challenge:/usr/src/mymaven -w /usr/src/mymaven maven:3.6.0-jdk-11-slim mvn clean package -DskipTests