-- Progettazione Web 
DROP DATABASE if exists robin; 
CREATE DATABASE robin; 
USE robin; 


CREATE TABLE IF NOT EXISTS `comandocapito` (
  `id` int NOT NULL AUTO_INCREMENT,
  `comando` varchar(200) NOT NULL,
  `capito` tinyint NOT NULL,
  `giorno` timestamp NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `comandoazione` (
  `id` int NOT NULL,
  `azione` varchar(200) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci AUTO_INCREMENT=1 ;
