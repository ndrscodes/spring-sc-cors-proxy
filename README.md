# spring-sc-cors-proxy
A Spring-based CORS proxy that will set the CORS "Access Control Allow Origin" policy to "*" in order to make it easier to develop certain tools. Also provides a small hack for using the Soundcloud REST Api.

## How to use
The proxy can be used by appending the URL in question to the proxie's address. For example, SoundCloud can be proxied by making a request to `localhost:8080/https://soundcloud.com`. Note that `https://` will automatically be appended to the request if no `://` is found within the specified URL, indicating that a protocol is missing.

The proxy will also automatically retry queries if it detects a certain response. If the response code of a request is 429 (Too Many Requests, this sometimes happens if you query a lot of soundcloud pages at once), the proxy will automatically retry the request after a 10 second fixed backoff. If the request still fails after 2 retries, the 429 response will sent to the client.

## The Hack
Soundcloud provides a REST api which uses a client id for authenticating REST calls. Sadly, REST application registration has been offline for ages now.
This proxy exposes a hack under `/hack/` which automatically adds a valid client_id to the request, thus making the entire REST API available.

## Known Issues
Currently, GET **AND** POST parameters will be encoded as GET parameters, as the proxy currently only supports GET requests. I definitely plan on adding POST functionality in the near future.
