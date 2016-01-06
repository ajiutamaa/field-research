CREATE TABLE IF NOT EXISTS field_sensor_data (
  farmer_field_visit_id INT REFERENCES farmer_field_visit ON DELETE SET NULL,
  record_timestamp TIMESTAMP NOT NULL,
  position geography(Point,4326) NOT NULL,
  rainfall NUMERIC,
  humidity NUMERIC,
  temperature NUMERIC,
  light_intensity NUMERIC,
  UNIQUE (record_timestamp)
)