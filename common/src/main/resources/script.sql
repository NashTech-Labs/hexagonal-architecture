CREATE TABLE IF NOT EXISTS journal (
 "ordering" BIGINT AUTO_INCREMENT (1,1) NOT NULL,
 "deleted" BIT NULL DEFAULT 0,
 "persistence_id" VARCHAR(255) NOT NULL,
 "sequence_number" NUMERIC(10, 0) NOT NULL,
 "tags" VARCHAR(255) NULL DEFAULT NULL,
 "message" VARBINARY(max) NOT NULL,
 PRIMARY KEY ("persistence_id", "sequence_number")
);

CREATE TABLE IF NOT EXISTS snapshot (
"persistence_id" VARCHAR(255) NOT NULL,
"sequence_number" NUMERIC(10, 0) NOT NULL,
"created" NUMERIC NOT NULL,
"snapshot" VARBINARY(max) NOT NULL,
PRIMARY KEY ("persistence_id", "sequence_number")
);

CREATE TABLE IF NOT EXISTS orders (
"order_id" VARCHAR(255) NOT NULL,
"side" VARCHAR(255) NOT NULL,
"price" FLOAT NOT NULL,
"quantity" INTEGER NOT NULL,
"product_code" INTEGER NOT NULL,
"product_type" VARCHAR(255) NOT NULL,
"created_timestamp" BIGINT NOT NULL,
"status" VARCHAR(255) NOT NULL,
"source" VARCHAR(255) NOT NULL,
PRIMARY KEY ("order_id")
);