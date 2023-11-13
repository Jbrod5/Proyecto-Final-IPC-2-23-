-- Base de datos joblink, proyecto final IPC2 - Jorge Bravo 202131782
DROP DATABASE IF EXISTS joblink; 
CREATE DATABASE joblink; 
USE joblink;

-- Categorias de ofertas de empleo
CREATE TABLE CategoriasOfertasEmpleo(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    categoria VARCHAR(50) NOT NULL,
    habilitada TINYINT(1) NOT NULL  
);

-- TIPOS DE USUARIO - tambien lleva el conteo de cada tipo de usuario
CREATE TABLE TipoUsuario(
    id INT PRIMARY KEY NOT NULL, 
    tipo VARCHAR(25), 
    cantidad INT NOT NULL
);

    -- Llenar la tabla de tipos de usuario con los tipos de usuario
    INSERT INTO TipoUsuario (id, tipo, cantidad) VALUES (1, "Administrador", 0);
    INSERT INTO TipoUsuario (id, tipo, cantidad) VALUES (2, "Empleador"    , 0);
    INSERT INTO TipoUsuario (id, tipo, cantidad) VALUES (3, "Solicitante"  , 0);
    INSERT INTO TipoUsuario (id, tipo, cantidad) VALUES (4, "Visitante"    , 0);



-- Usuario, tipo referencia a tabla Tipos de usuario
CREATE TABLE Usuario(
    username VARCHAR(50) PRIMARY KEY NOT NULL, 
    nombre VARCHAR(50) NOT NULL, 
    password VARCHAR(50) NOT NULL, 
    direccion VARCHAR(50) NOT NULL,
    correo VARCHAR(50) NOT NULL, 
    cui INT NOT NULL, 
    fecha_nacimiento DATETIME NOT NULL, 
    tipo INT,

    perfil_completado TINYINT(1) NOT NULL,

    CONSTRAINT FK_USUARIO_TO_TIPOUSUARIO FOREIGN KEY (tipo) REFERENCES TipoUsuario(id)
);

    -- Array de numeros de telefono, unicamente referencia a usuario
    CREATE TABLE NumeroTelefono(
        username VARCHAR(50) NOT NULL, 
        numero INT NOT NULL,

        CONSTRAINT UNIQUE_Usuario_numero UNIQUE(username, numero),
        CONSTRAINT FK_NUMERO_TO_USUARIO FOREIGN KEY (username) REFERENCES Usuario(username)
    );


    -- Especializacion de usuario solicitante - username unico
    CREATE TABLE Solicitante(
        username VARCHAR(50) NOT NULL,
        -- curriculum TINYINT(1) NOT NULL,
        curriculum VARCHAR(250) NOT NULL, 

        CONSTRAINT UNIQUE_USUARIO_SOLICITANTE UNIQUE (username),
        CONSTRAINT FK_SOLICITANTE_TO_USUARIO FOREIGN KEY (username) REFERENCES Usuario(username)
    );

        
        -- Array de categorias de interes a un usuario solicitante - tupla unica 
        CREATE TABLE CategoriasInteres(
            username VARCHAR(50) NOT NULL, 
            categoria INT NOT NULL, 

            CONSTRAINT UNIQUE_USERNAME_CATEGORIA UNIQUE(username, categoria),
            CONSTRAINT FK_CATINTERES_TO_USUARIO FOREIGN KEY (username) REFERENCES Usuario(username),
            CONSTRAINT FK_CATINTERES_TO_CATOFERTAS FOREIGN KEY (categoria) REFERENCES CategoriasOfertasEmpleo(id)
        );

    -- Especializacion de usuario empleador - username unico
    CREATE TABLE Empleador(
        username VARCHAR(50) NOT NULL, 
        mision VARCHAR (500) NOT NULL, 
        vision VARCHAR (500) NOT NULL,
        metodo_pago INT NOT NULL, 

        CONSTRAINT UNIQUE_USUARIO_EMPLEADOR UNIQUE(username),
        CONSTRAINT FK_EMPLEADOR_TO_USUARIO FOREIGN KEY (username) REFERENCES Usuario(username)
    );






-- Modalidades de trabajo, presencial remoto hibrido 
CREATE TABLE ModalidadEmpleo(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT, 
    modalidad VARCHAR(25) NOT NULL
);

-- Estados de las ofertas de empleo - seleccion, activa, finalizada, eliminada
CREATE TABLE EstadosOfertaEmpleo(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    estado VARCHAR(25)
);

-- Ofertas de empleo disponibles publicadas por empleadores 
CREATE TABLE OfertaEmpleo(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT, 
    nombre VARCHAR(50) NOT NULL, 
    descripcion VARCHAR (150) NOT NULL, 
    
    categoria INT NOT NULL,

    fecha_publicacion DATETIME NOT NULL, 
    fecha_limite_aplicacion DATETIME NOT NULL, 
    salario_aproximado INT NOT NULL, 

    modalidad INT NOT NULL, 

    ubicacion VARCHAR(50),
    detalles VARCHAR(500),

    empleador VARCHAR(50),

    estado INT NOT NULL, 

    CONSTRAINT FK_OFEMPLEO_TO_CATOFERTA FOREIGN KEY (categoria) REFERENCES CategoriasOfertasEmpleo(id),
    CONSTRAINT FK_OFEMPLEO_TO_MODEMPLEO FOREIGN KEY (modalidad) REFERENCES ModalidadEmpleo(id),
    CONSTRAINT FK_OFEMPLEO_TO_USUARIO   FOREIGN KEY (empleador) REFERENCES Usuario(username),
    CONSTRAINT FK_OFEMPLEO_TO_ESOFERTAS FOREIGN KEY (estado)    REFERENCES EstadosOfertaEmpleo(id)
);

    -- Estados posibles de una aplicacion - entrevistable, rechazado, aceptado, retirado
    CREATE TABLE EstadosAplicacion(
        id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        estado VARCHAR(25)
    );

    -- Tabla de aplicaciones, tupla unica oferta _ solicitante 
    CREATE TABLE APLICACION(
        oferta INT NOT NULL, 
        solicitante VARCHAR(50) NOT NULL, 
        
        razon_aplicacion VARCHAR(250) NOT NULL,
        
        estado INT NOT NULL,

        reporte VARCHAR(500),

        CONSTRAINT UNIQUE_OFERTA_SOLICITANTE UNIQUE(oferta, solicitante), 
        CONSTRAINT FK_APLICACION_TO_OFEMPLEO FOREIGN KEY (oferta)      REFERENCES OfertaEmpleo(id),
        CONSTRAINT FK_APLICACION_TO_USUARIO  FOREIGN KEY (solicitante) REFERENCES Usuario(username),
        CONSTRAINT FK_APLICACION_TO_ESAPLIC  FOREIGN KEY (estado)      REFERENCES EstadosAplicacion(id)
    );

    -- Constancia de eliminacion de una oferta de empleo 
    CREATE TABLE EliminacionOfertaEmpleo(
        oferta INT NOT NULL,
        motivo VARCHAR (300) NOT NULL,

        CONSTRAINT UNIQUE_ELIMINACION_OFERTA UNIQUE (oferta),
        CONSTRAINT FK_ELIMINACIONOF_TO_OFEMPLEO FOREIGN KEY (oferta) REFERENCES OfertaEmpleo(id)
    );






-- Para reportes administracion
    CREATE TABLE CantidadVisitasInvitados(
        cantidad INT NOT NULL
    );

    CREATE TABLE ComisionesCobradasPorContratacion(
        categoria INT NOT NULL, 
        fecha_ingreso DATETIME NOT NULL, 
        cantidad_cobrada INT NOT NULL,

        CONSTRAINT FK_COMCOBRCONTR_TO_CATOFCONTR FOREIGN KEY (categoria) REFERENCES CategoriasOfertasEmpleo(id)
    );



-- Parametros administracion    
CREATE TABLE Parametros(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    nombre VARCHAR (25) NOT NULL,
    descripcion VARCHAR(125) NOT NULL,
    fecha_modificacion DATETIME NOT NULL, 
    valor INT NOT NULL
);


CREATE TABLE ComisionPorContratacion(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT, 
    valor INT NOT NULL,
    fecha DATETIME NOT NULL
);