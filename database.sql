
CREATE DATABASE projetofinal;

USE projetofinal;

CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_completo VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    genero VARCHAR(20) NOT NULL,
    imagem_perfil BLOB,
    email VARCHAR(100) NOT NULL,
    nome_usuario VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE agenda (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(100),
    usuario_id INT,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);

CREATE TABLE compromisso (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descricao VARCHAR(100),
    data_inicio DATETIME NOT NULL,
    data_termino DATETIME NOT NULL,
    local VARCHAR(255),
    notificacao DATETIME,
    usuario_id INT,
    agenda_id INT,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE, 
    FOREIGN KEY (agenda_id) REFERENCES agenda(id) ON DELETE CASCADE
);

CREATE TABLE compromisso_convidados (
    compromisso_id INT,
    usuario_id INT,
    PRIMARY KEY (compromisso_id, usuario_id),
    FOREIGN KEY (compromisso_id) REFERENCES compromisso(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);