-- Inserts de las Comunidades Autónomas, ignora si se produce un error en la insercción
INSERT IGNORE INTO regions (code, name) VALUES
('01', 'ANDALUCÍA'),
('02', 'ARAGÓN'),
('03', 'ASTURIAS'),
('04', 'BALEARES'),
('05', 'CANARIAS'),
('06', 'CANTABRIA'),
('07', 'CASTILLA Y LEÓN'),
('08', 'CASTILLA-LA MANCHA'),
('09', 'CATALUÑA'),
('10', 'COMUNIDAD VALENCIANA'),
('11', 'EXTREMADURA'),
('12', 'GALICIA'),
('13', 'MADRID'),
('14', 'MURCIA'),
('15', 'NAVARRA'),
('16', 'PAÍS VASCO'),
('17', 'LA RIOJA'),
('18', 'CEUTA Y MELILLA');


-- Insertar datos de las provincias españolas con los códigos correctos
INSERT IGNORE INTO provinces (code, name, id_region) VALUES
('01', 'Araba/Álava', 16),
('02', 'Albacete', 8),
('03', 'Alicante/Alacant', 10),
('04', 'Almería', 1),
('05', 'Ávila', 7),
('06', 'Badajoz', 11),
('07', 'Balears, Illes', 4),
('08', 'Barcelona', 9),
('09', 'Burgos', 7),
('10', 'Cáceres', 11),
('11', 'Cádiz', 1),
('12', 'Castellón/Castelló', 10),
('13', 'Ciudad Real', 8),
('14', 'Córdoba', 1),
('15', 'Coruña, A', 12),
('16', 'Cuenca', 8),
('17', 'Girona', 9),
('18', 'Granada', 1),
('19', 'Guadalajara', 8),
('20', 'Gipuzkoa', 16),
('21', 'Huelva', 1),
('22', 'Huesca', 2),
('23', 'Jaén', 1),
('24', 'León', 7),
('25', 'Lleida', 9),
('26', 'Rioja, La', 17),
('27', 'Lugo', 12),
('28', 'Madrid', 13),
('29', 'Málaga', 1),
('30', 'Murcia', 14),
('31', 'Navarra', 15),
('32', 'Ourense', 12),
('33', 'Asturias', 3),
('34', 'Palencia', 7),
('35', 'Palmas, Las', 5),
('36', 'Pontevedra', 12),
('37', 'Salamanca', 7),
('38', 'Santa Cruz de Tenerife', 5),
('39', 'Cantabria', 6),
('40', 'Segovia', 7),
('41', 'Sevilla', 1),
('42', 'Soria', 7),
('43', 'Tarragona', 9),
('44', 'Teruel', 2),
('45', 'Toledo', 8),
('46', 'Valencia/València', 10),
('47', 'Valladolid', 7),
('48', 'Bizkaia', 16),
('49', 'Zamora', 7),
('50', 'Zaragoza', 2),
('51', 'Ceuta', 18),
('52', 'Melilla', 18);

-- Insertar algunos supermercados en la tabla 'supermarket'
INSERT IGNORE INTO supermarkets (id, name) VALUES
(1, 'Mercadona'),
(2, 'Lidl');

-- Insertar algunas ubicaciones en la tabla 'location'
INSERT IGNORE INTO locations (id, address, city, supermarket_id, province_id) VALUES
(1, 'CTRA, Camino de Tomares', 'Castilleja de la Cuesta', 1, 41),
(2, 'Av. Canal Sur, s/n', 'Tomares', 1, 41),
(3, 'Avenida de Bormujos', 'Bormujos', 2, 41);

