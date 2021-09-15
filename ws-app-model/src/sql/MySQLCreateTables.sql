-- ----------------------------------------------------------------------------
-- Bikes Model
-------------------------------------------------------------------------------


DROP TABLE Rent;
DROP TABLE Bike;

-- --------------------------------- Movie ------------------------------------
CREATE TABLE Bike ( bikeId BIGINT NOT NULL AUTO_INCREMENT,
    model VARCHAR(255) COLLATE latin1_bin NOT NULL,
    units SMALLINT NOT NULL,
    description VARCHAR(1024) COLLATE latin1_bin NOT NULL,
    price FLOAT NOT NULL,
    creationDate DATETIME NOT NULL,
    firstDate DATETIME NOT NULL,
    averageScore FLOAT NOT NULL,
    reviewCount BIGINT NOT NULL,
    CONSTRAINT BikePK PRIMARY KEY(bikeId), 
    CONSTRAINT validUnits CHECK ( units >= 0 ),
    CONSTRAINT validPrice CHECK ( price >= 0 AND price <= 1000) ) ENGINE = InnoDB;
    
-- --------------------------------- Sale ------------------------------------

CREATE TABLE Rent ( rentalId BIGINT NOT NULL AUTO_INCREMENT,
    bikeId BIGINT NOT NULL,
    userId VARCHAR(40) COLLATE latin1_bin NOT NULL,
    initialDate DATETIME NOT NULL,
    expirationDate DATETIME NOT NULL,
    creditCardNumber VARCHAR(16),
    units SMALLINT NOT NULL,
    saleDate DATETIME NOT NULL,
    score FLOAT,
    CONSTRAINT RentPK PRIMARY KEY(rentalId),
    CONSTRAINT RentBikeIdFK FOREIGN KEY(bikeId)
        REFERENCES Bike(bikeId) ON DELETE CASCADE ) ENGINE = InnoDB;
