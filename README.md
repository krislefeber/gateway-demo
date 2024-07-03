# API Gateway Demos

The purpose of this project is to evaluate various API gateways.

# Getting Started

To get started, run `docker-compose up` from this directory. If you make any edits, remember to run `docker-compose build` for those edits to take effect.

## Sample Data

Wiremock is setup on two different containers, service-1 and service-2. Their configurations can be found below

- [service-1](./wiremock/service-1/mappings/)
- [service-2](./wiremock/service-1/mappings/)

In order to call them, you will need to start here for the various gateways:

- Spring Cloud Gateway(SCG): [http://localhost:8080](http://localhost:8080)
- Kong: [http://localhost:8081](http://localhost:8081)

The following is setup on SCG

- View the /v1 endpoint that is not overridden:
  - http://localhost:8080/api/v1/series
- View the /v2 endpoints:
  - http://localhost:8080/api/v2/titles (this is supposed to be represent a replacement for /api/v1/movies)
- View the /v1 endpoint overridden with the call to /v2
  - http://localhost:8080/api/v1/movies
