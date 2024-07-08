# Application Gateway
This project implements an API gateway to simplify transitioning from the old VPP system to the new VPP system.


## Route Options
The gateway is capable of two configurations.
### Service Filter
A service filter is designed to direct all incoming requests for a specific host and path to a new host.

<b>Use case</b>: "Rewrite all calls to http://service1.example.com to http://service1:8080"

This can be done by adding a section in the `app.services` section of the properties.yml file. The following properties can be defined:

| Name              | Example                   | Description                                                                                                                     |
|-------------------|---------------------------|---------------------------------------------------------------------------------------------------------------------------------|
| route-host        | "*.test.com"              | The incoming host name to apply the filter to. Wild cards (*) can be used to include more than one subdomain                    | 
| route-filter-path | "/api/**"                 | The incoming request path to apply the filter to. A single * will include that sub-level. Two asterisks will include all sub-levls |
| source-uri        | http://service-1:8080/api | The uri to direct the traffic to.                                                                                               |   

### Rewrite Path Filter
This filter is designed to rewrite a specific route to another service.

<b>Use case</b>: "Make the route http://service1.example.com/api/movies route to http://service2:8080/api/titles"

This can be done by adding a section in the `app.rewrites` section of the properties.yml file. The following properties can be defined:

| Name                | Example                   | Description                                                                                                                        |
|---------------------|---------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| route-host          | "*.test.com"              | The incoming host name to apply the filter to. Wild cards (*) can be used to include more than one subdomain                       | 
| route-filter-path   | "/api/**"                 | The incoming request path to apply the filter to. A single * will include that sub-level. Two asterisks will include all sub-levls |
| incoming-path-regex | "/api/v1/movies"          | A string or regular expression that will be edited to the target-path-regex                                                        |
| target-path-regex   | "/api/v2/titles"          | A string or regular expression that will replace the incoming-path-regex                                                           |
| beta-only           | true                      | If enabled, checks that a header of `X-gateway-beta` is on the request header before calling the route                             |
| source-uri          | http://service-1:8080/api | The uri to direct the traffic to.                                                                                                  |   
| allowed-methods     | - GET                     | A list of allowed method types. By default, all method types are forwarded                                                         |

## Request/Response Mapping
In addition to changing the URL to rewrite requests to, we can also change the shape of the request or response bodies.
To do this, implement an instance of `ModifyRequestRouteFilter` or `ModifyResponseRouteFilter`(or both). The id field will be used to match with the key under `app.rewrites.routes` 

## Testing New Routes
To allow for safe rollouts of new endpoints, we can set up new routes to only be accessible via a request header of `X-gateway-beta` with a value of `"true"`.

To enable this functionality, make sure the `beta-only` field is set to `true` on the rewrite path filter
