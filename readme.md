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
