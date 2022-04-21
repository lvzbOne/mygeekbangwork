
-- 手动批量创建t_order0 ~ t_order15
DROP PROCEDURE IF EXISTS orders_initData_one;
DELIMITER $
CREATE PROCEDURE orders_initData_one()
BEGIN
    DECLARE i INT DEFAULT 1;
		DECLARE table_name VARCHAR(20);

    WHILE i<=15 DO
			-- IF i < 10 THEN
			-- SET table_name = CONCAT('t_order_0',i);
			-- ELSE
			SET table_name = CONCAT('t_order_',i);
			-- END IF;

		  SET @csql = CONCAT('CREATE TABLE IF NOT EXISTS ',table_name,'(order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));');

PREPARE create_stmt FROM @csql;
EXECUTE create_stmt;

SET i = i+1;
END WHILE;
END $
CALL orders_initData_one();

-- 手动批量删除表
DROP PROCEDURE IF EXISTS orders_initData_one;
DELIMITER $
CREATE PROCEDURE orders_initData_one()
BEGIN
    DECLARE i INT DEFAULT 1;
		DECLARE table_name VARCHAR(20);

    WHILE i<=15 DO
-- 			IF i < 10 THEN
-- 			SET table_name = CONCAT('t_order_0',i);
-- 			ELSE
			SET table_name = CONCAT('t_order_',i);
-- 			END IF;
--
		  SET @csql = CONCAT('DROP TABLE ',table_name);

PREPARE create_stmt FROM @csql;
EXECUTE create_stmt;

SET i = i+1;
END WHILE;
END $
CALL orders_initData_one();