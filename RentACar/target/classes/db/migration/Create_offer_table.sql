CREATE TABLE offer (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       user_name VARCHAR(255) NOT NULL,
                       car_id BIGINT NOT NULL,
                       rental_days INT NOT NULL,
                       total_price DOUBLE NOT NULL,
                       CONSTRAINT fk_car FOREIGN KEY (car_id) REFERENCES car(id)
);