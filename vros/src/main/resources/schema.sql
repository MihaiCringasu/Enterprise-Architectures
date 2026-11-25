DROP TABLE IF EXISTS route_stops CASCADE;
DROP TABLE IF EXISTS routes CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS scenario_deliveries CASCADE;
DROP TABLE IF EXISTS scenario_vehicles CASCADE;
DROP TABLE IF EXISTS scenarios CASCADE;
DROP TABLE IF EXISTS deliveries CASCADE;
DROP TABLE IF EXISTS vehicles CASCADE;
DROP TABLE IF EXISTS customers CASCADE;
DROP TABLE IF EXISTS drivers CASCADE;
DROP TABLE IF EXISTS depots CASCADE;

CREATE TABLE depots (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL UNIQUE,
                        coordinates VARCHAR(100) NOT NULL
);

CREATE TABLE drivers (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         email VARCHAR(150) NOT NULL UNIQUE,
                         phone VARCHAR(30) NOT NULL
);

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       role VARCHAR(30) NOT NULL,
                       driver_id BIGINT UNIQUE,
                       CONSTRAINT fk_user_driver FOREIGN KEY (driver_id) REFERENCES drivers(id) ON DELETE SET NULL
);

CREATE TABLE vehicles (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          capacity DOUBLE PRECISION NOT NULL,
                          available BOOLEAN NOT NULL DEFAULT TRUE,
                          depot_id BIGINT NOT NULL,
                          driver_id BIGINT UNIQUE,
                          CONSTRAINT fk_vehicle_depot FOREIGN KEY (depot_id) REFERENCES depots(id) ON DELETE CASCADE,
                          CONSTRAINT fk_vehicle_driver FOREIGN KEY (driver_id) REFERENCES drivers(id) ON DELETE SET NULL
);

CREATE TABLE customers (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           email VARCHAR(150) NOT NULL UNIQUE,
                           phone VARCHAR(30) NOT NULL,
                           address VARCHAR(255) NOT NULL,
                           coordinates VARCHAR(100) NOT NULL
);

CREATE TABLE deliveries (
                            id BIGSERIAL PRIMARY KEY,
                            customer_id BIGINT NOT NULL,
                            quantity DOUBLE PRECISION NOT NULL,
                            notes VARCHAR(255),
                            timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            CONSTRAINT fk_delivery_customer FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

CREATE TABLE scenarios (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE scenario_vehicles (
                                   scenario_id BIGINT NOT NULL,
                                   vehicle_id BIGINT NOT NULL,
                                   PRIMARY KEY (scenario_id, vehicle_id),
                                   CONSTRAINT fk_sv_scenario FOREIGN KEY (scenario_id) REFERENCES scenarios(id) ON DELETE CASCADE,
                                   CONSTRAINT fk_sv_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE
);

CREATE TABLE scenario_deliveries (
                                     scenario_id BIGINT NOT NULL,
                                     delivery_id BIGINT NOT NULL,
                                     PRIMARY KEY (scenario_id, delivery_id),
                                     CONSTRAINT fk_sd_scenario FOREIGN KEY (scenario_id) REFERENCES scenarios(id) ON DELETE CASCADE,
                                     CONSTRAINT fk_sd_delivery FOREIGN KEY (delivery_id) REFERENCES deliveries(id) ON DELETE CASCADE
);

CREATE TABLE routes (
                        id BIGSERIAL PRIMARY KEY,
                        scenario_id BIGINT NOT NULL,
                        vehicle_id BIGINT NOT NULL,
                        total_distance DOUBLE PRECISION NOT NULL,
                        total_load DOUBLE PRECISION NOT NULL,
                        CONSTRAINT fk_route_scenario FOREIGN KEY (scenario_id) REFERENCES scenarios(id) ON DELETE CASCADE,
                        CONSTRAINT fk_route_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE
);

CREATE TABLE route_stops (
                             id BIGSERIAL PRIMARY KEY,
                             route_id BIGINT NOT NULL,
                             delivery_id BIGINT NOT NULL,
                             stop_order INT NOT NULL,
                             CONSTRAINT fk_stop_route FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE,
                             CONSTRAINT fk_stop_delivery FOREIGN KEY (delivery_id) REFERENCES deliveries(id) ON DELETE CASCADE
);
