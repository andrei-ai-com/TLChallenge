# TrueLayer Pokemon Challenge
> Spits fire yond is hot enow to melt boulders. - Charizard

Quick Demo:
* [Charizard](http://18.134.253.238:8080/pokemon/charizard)
* [Pikachu](http://18.134.253.238:8080/pokemon/pikachu)
* [Raichu](http://18.134.253.238:8080/pokemon/raichu)
* [Bulbasaur](http://18.134.253.238:8080/pokemon/bulbasaur)
* [Mewtwo](http://18.134.253.238:8080/pokemon/mewtwo/)
* [Non existing pokemon](http://18.134.253.238:8080/pokemon/andreiai)

[Swagger Documentation](http://18.134.253.238:8080/swagger-ui.html)

[API Health](http://18.134.253.238:8080/healthcheck)

## How to run it

Assuming a fresh AWS instance with Ubuntu

1. Setup docker repo
    ```
    $ sudo apt-get update
    $ sudo apt-get install \
        apt-transport-https \
        ca-certificates \
        curl \
        gnupg \
        lsb-release
    $ curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
    $ echo \
       "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
       $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
    ```
2. Install docker engine
    ```
    $ sudo apt-get update
    $ sudo apt-get install docker-ce docker-ce-cli containerd.io
    ```
3. Install maven
    ```
    $ sudo apt install maven
    ``` 
4. Clone github repository [tlchallenge](https://github.com/andrei-ai-com/TLChallenge)
    ```
    $ git clone https://github.com/andrei-ai-com/TLChallenge.git tlchallenge
    ```
5. Package the source. This will create the `tlchallenge-1.0.jar` inside the target directory
    1. If we want to run the automated tests during the build
    ```
    $ cd tlchallenge
    $ sudo mvn clean install 
    ```
   1. If we want to skip the automated tests
    ```
    $ cd tlchallenge
    $ sudo mvn clean install -DskipTests 
    ```
6. Dockerize and run the application
    ```
    $ sudo docker build -t tlchallenge:1.0 . 
    $ sudo docker run -d -p 8080:8080 -t tlchallenge:1.0 
    ```
7. The app should be available on your localhost, port 8080 --> http://127.0.0.1:8080/healthcheck or [click here - local charizard](http://127.0.0.1:8080/pokemon/charizard)

Alternatively, we can skip docker and run the app right after step 5 by running
```
$ java -jar target/tlchallenge-1.0.jar
```

## TLChallenge structure

Spring Boot App structured on functional layers:
* `Controller Layer` (facade, user entry point)
* `Service Layer` (main business logic, albeit simple in this case)
    * curating input data
    * aggregating dependencies (pokemon description & shakespeare speech)
    * caching {pokemon name - shakespeare speech}
* `Provider Layer` (third party gateways for communication)

## Testing methodology

* Testing the `Service Layer`:
   * mocking the PokeAPI
   * mocking the ShakespeareAPI
   * checking for both nominal run and dependency failure
* Testing the `Provider Layer` (PokeAPI, ShakespeareAPI) 
   * intercepting and stubbing the requests
   * checking for OK responses and client & server errors
 
## Expected API Calls

`200`: 
```
{
    "name": "charizard",
    "description": "Spits fire yond is hot enow to melt boulders. Known to cause forest fires unintentionally."
}
```
```
{
   "name": "bulbasaur",
   "description": "A strange seed wast planted on its back at birth. The plant sprouts and grows with this pokémon."
}
```
`404`:
```
Pokemon bulbasaurasd not found, please try a valid name. Examples: [pikachu, raichu, mewtwo, charizard, bulbasaur]
```

`Spring default 404 error page`: 
```
Whitelabel Error Page
This application has no explicit mapping for /error, so you are seeing this as a fallback.

Fri Mar 26 01:07:58 GMT 2021
There was an unexpected error (type=Not Found, status=404).
No message available
```
`429`:
```
429 Too Many Requests --> Here is a list of cached Shakespeare pokemons {raichu=Its tail discharges electricity into the did grind,  protecting 't from getting did shock., bulbasaur=A strange seed wast planted on its back at birth. The plant sprouts and grows with this pokémon., pikachu=At which hour several of these pokémon gather,  their electricity couldst buildeth and cause lightning storms., mewtwo='t wast did create by a scientist after years of horrific gene splicing and dna engineering experiments., charizard=Spits fire yond is hot enow to melt boulders. Known to cause forest fires unintentionally., cobalion=This legendary pokémon battled 'gainst humans to protect pokémon. Its personality is halcyon and composed.}
```
`503` (Depending on failed third party dependency):
```
The PokeAPI is currently unavailable.
```
```
The ShakespeareAPI is currently unavailable.
```
 
## Rate Limits

`Shakespeare API`: 5 requests an hour, 60 a day

To get around this an in-memory cache is used. The cached objects are returned on 429-s responses.

## Steps for improvement
* Increase test coverage
    * test the `Controller Layer` by mocking the `Service Layer`
    * test the `Controller Layer` by starting up a local server and throwing predicted requests at it (end to end test)
* Instead of using just an in-memory cache we could spin up a DB somewhere (I would suggest something like MongoDB due to its simplicity and quick READs)
* To get around the rate limits on the `Shakespeare API` we can have a worker going through the Pokemon list at a rate of 4-5 an hour and populate the above DB. In a couple of days we should have a complete collection (unless we expand internationally and require multiple languages, case in which 1 year should be enough-ish).
* Add support for HTTPS
* Get a domain name & set it up
* Add monitoring for uptime
* Add customised default page for 4xx and 5xx errors 
* Add UI & text-2-speech functionality (this one would be super fun)