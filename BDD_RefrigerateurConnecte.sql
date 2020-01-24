CREATE TABLE ListeDeCourse(
	nom varchar(50) NOT NULL,
    idLDC int AUTO_INCREMENT,
    PRIMARY KEY (idLDC)
);

CREATE TABLE typeAliment(
	Categorie varchar(50) NOT NULL,
    PRIMARY KEY (Categorie)
);

CREATE TABLE AlimentFrigo (
	nom varchar(50) NOT NULL,
    Categorie varchar(50) ,
    PRIMARY KEY (nom),
    FOREIGN KEY (Categorie) references typeAliment(Categorie)
);

CREATE TABLE AlimentLDC (
	nom varchar(50) NOT NULL,
    Categorie varchar(50),
    idLDC int,
    PRIMARY KEY (nom,idLDC),
    FOREIGN KEY (idLDC) references ListeDeCourse(idLDC) ON DELETE CASCADE,
    FOREIGN KEY (Categorie) references typeAliment(Categorie) ON DELETE CASCADE
);

CREATE TABLE Notes(
	titre varchar(100),
    contenu varchar(1000),
    PRIMARY KEY (titre)
);