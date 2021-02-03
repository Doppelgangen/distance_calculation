CREATE TABLE `cities` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(85) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
)AUTO_INCREMENT=1;

CREATE TABLE `distances` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `from_city` bigint NOT NULL,
  `to_city` bigint NOT NULL,
  `distance` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_origin_idx` (`from_city`),
  KEY `city_dest_fk_idx` (`to_city`),
  CONSTRAINT `city_dest_fk` FOREIGN KEY (`to_city`) REFERENCES `cities` (`id`),
  CONSTRAINT `city_origin_fk` FOREIGN KEY (`from_city`) REFERENCES `cities` (`id`)
) AUTO_INCREMENT=1;

INSERT INTO `cities` (`id`,`name`,`latitude`,`longitude`) VALUES (1,'Samara',53.2038,50.1606);
INSERT INTO `cities` (`id`,`name`,`latitude`,`longitude`) VALUES (2,'Helsinki',60.1699,24.9384);
INSERT INTO `cities` (`name`,`latitude`,`longitude`) VALUES ('Melbourne',-37.8136,144.9631);
INSERT INTO `cities` (`name`,`latitude`,`longitude`) VALUES ('London',51.5074,-0.1278);
INSERT INTO `cities` (`name`,`latitude`,`longitude`) VALUES ('Berlin',52.5167,13.3833);
INSERT INTO `cities` (`name`,`latitude`,`longitude`) VALUES ('Ottawa',45.4215,-75.6972);
INSERT INTO `cities` (`name`,`latitude`,`longitude`) VALUES ('Madagascar',-18.7669,46.8691);
INSERT INTO `cities` (`name`,`latitude`,`longitude`) VALUES ('Paris',48.8566,2.3522);
INSERT INTO `cities` (`name`,`latitude`,`longitude`) VALUES ('Vatican City',41.9029,12.4534);
INSERT INTO `cities` (`name`,`latitude`,`longitude`) VALUES ('Belgrade',44.7866,20.4489);


INSERT INTO `distances` (`id`,`from_city`,`to_city`,`distance`) VALUES (1,1,2,1709.5919278940116);
INSERT INTO `distances` (`id`,`from_city`,`to_city`,`distance`) VALUES (2,2,1,1709.5919278940116);
