package com.example.vros.shared;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenApiController {

    @GetMapping(value = "/v3/api-docs", produces = MediaType.APPLICATION_JSON_VALUE)
    public String apiDocs() {
        return """
        {
          "openapi": "3.0.1",
          "info": { "title": "VROS REST API", "version": "1.0" },
          "paths": {
            "/api/customers": {
              "get": { "summary": "List all customers (JSON / XML)" },
              "post": { "summary": "Create a new customer" }
            },
            "/api/customers/{id}": {
              "get": { "summary": "Get customer by ID" },
              "put": { "summary": "Update customer" },
              "delete": { "summary": "Delete customer" }
            },
            "/api/vehicles": {
              "get": { "summary": "List all vehicles" },
              "post": { "summary": "Create a new vehicle" }
            },
            "/api/vehicles/{id}": {
              "get": { "summary": "Get vehicle by ID (with ETag)" },
              "put": { "summary": "Update vehicle" },
              "delete": { "summary": "Delete vehicle" }
            },
            "/api/deliveries": {
              "get": { "summary": "List all deliveries" },
              "post": { "summary": "Create a new delivery" }
            },
            "/api/scenarios": {
              "get": { "summary": "List all scenarios" },
              "post": { "summary": "Create a new scenario" }
            }
          }
        }
        """;
    }

    @GetMapping(value = "/swagger-ui.html", produces = MediaType.TEXT_HTML_VALUE)
    public String swaggerUi() {
        return """
        <!DOCTYPE html>
        <html>
        <head>
          <title>VROS Swagger UI</title>
          <link rel="stylesheet" href="https://unpkg.com" />
        </head>
        <body>
          <div id="swagger-ui"></div>
          <script src="https://unpkg.com"></script>
          <script>
            SwaggerUIBundle({ url: '/v3/api-docs', dom_id: '#swagger-ui' });
          </script>
        </body>
        </html>
        """;
    }
}
