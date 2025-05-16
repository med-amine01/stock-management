-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : ven. 01 avr. 2022 à 11:03
-- Version du serveur : 10.4.22-MariaDB
-- Version de PHP : 7.4.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `gestionstock`
--

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

CREATE TABLE `client` (
  `idclient` int(11) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `prenom` varchar(255) NOT NULL,
  `mail` varchar(255) NOT NULL,
  `adresse` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `client`
--

INSERT INTO `client` (`idclient`, `nom`, `prenom`, `mail`, `adresse`) VALUES
(2, 'aaa', 'aaaaaaa', 'aaaaaa@gmail.com', 'c');

-- --------------------------------------------------------

--
-- Structure de la table `commande`
--

CREATE TABLE `commande` (
  `idcmd` int(11) NOT NULL,
  `datecmd` varchar(255) NOT NULL,
  `etatcmd` tinyint(1) NOT NULL,
  `idclient` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `employe`
--

CREATE TABLE `employe` (
  `idemp` int(11) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `prenom` varchar(255) NOT NULL,
  `adresse` varchar(255) NOT NULL,
  `mail` varchar(255) NOT NULL,
  `salaire` double NOT NULL,
  `post` varchar(255) NOT NULL,
  `tentative` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `employe`
--

INSERT INTO `employe` (`idemp`, `password`, `nom`, `prenom`, `adresse`, `mail`, `salaire`, `post`, `tentative`) VALUES
(1, 'ggg', 'aa', 'aaa', 'aaa', 'aaa@gmail.cocm', 1.1, 'ADMIN', 1),
(16, 'ggg', 'aaaa', 'aaa', 'aaa', 'aaaa@gajks.ddd', 1, 'Stock', 1),
(17, 'ggg', 'eee', 'ee', 'ee', 'aaaaaaaa@fffff.ggg', 2, 'Vendeur', 1),
(20, 'fff', 'zzzz', 'zzzzzz', 'zzzzz', 'zzzz@gmail.com', 133, 'ADMIN', 0);

-- --------------------------------------------------------

--
-- Structure de la table `entree`
--

CREATE TABLE `entree` (
  `identree` int(11) NOT NULL,
  `idemp` int(11) NOT NULL,
  `idfour` int(11) NOT NULL,
  `idpiece` int(11) NOT NULL,
  `qte` int(11) NOT NULL,
  `date` varchar(255) NOT NULL,
  `montant` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `entree`
--

INSERT INTO `entree` (`identree`, `idemp`, `idfour`, `idpiece`, `qte`, `date`, `montant`) VALUES
(17, 16, 2, 13, 4, '2022-03-25', 48.8),
(18, 16, 2, 13, 2, '2022-03-26', 24.4),
(19, 16, 2, 14, 2, '2022-03-26', 19.6),
(20, 16, 2, 14, 2, '2022-03-26', 19.6),
(21, 16, 2, 13, 2, '2022-03-26', 24.4),
(22, 16, 2, 13, 2, '2022-03-26', 24.4),
(23, 16, 2, 13, 2, '2022-04-01', 24.4);

-- --------------------------------------------------------

--
-- Structure de la table `fournisseur`
--

CREATE TABLE `fournisseur` (
  `idfour` int(11) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `adresse` varchar(255) NOT NULL,
  `tel` varchar(255) NOT NULL,
  `mail` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `fournisseur`
--

INSERT INTO `fournisseur` (`idfour`, `nom`, `adresse`, `tel`, `mail`) VALUES
(2, 'bbbbbbbbbbbbbbbbbbb', 'aaaaaaaaaaaa', '11111111', 'aaaa@gmail.aaf'),
(3, 'ssssssssss', 'ssssssssss', '12345678', 'azzz@gmail.com');

-- --------------------------------------------------------

--
-- Structure de la table `lignecmd`
--

CREATE TABLE `lignecmd` (
  `idlc` int(11) NOT NULL,
  `idcmd` int(11) NOT NULL,
  `idpiece` int(11) NOT NULL,
  `qtelc` int(11) NOT NULL,
  `prixlc` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `piece`
--

CREATE TABLE `piece` (
  `idpiece` int(11) NOT NULL,
  `marque` varchar(255) NOT NULL,
  `modele` varchar(255) NOT NULL,
  `serie` varchar(255) NOT NULL,
  `qte` int(11) NOT NULL,
  `prixunitaire` double NOT NULL,
  `idfour` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `piece`
--

INSERT INTO `piece` (`idpiece`, `marque`, `modele`, `serie`, `qte`, `prixunitaire`, `idfour`) VALUES
(13, 'yyyy', 'yyy', 'yyy', 1000, 12.2, 2),
(14, 'hhhhh', 'hhhhh', 'hhhhh', 1200, 9.8, 2);

-- --------------------------------------------------------

--
-- Structure de la table `sortie`
--

CREATE TABLE `sortie` (
  `idsortie` int(11) NOT NULL,
  `idemp` int(11) NOT NULL,
  `idpiece` int(11) NOT NULL,
  `qte` int(11) NOT NULL,
  `date` varchar(255) NOT NULL,
  `montant` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `sortie`
--

INSERT INTO `sortie` (`idsortie`, `idemp`, `idpiece`, `qte`, `date`, `montant`) VALUES
(6, 17, 13, 5, '2022-03-26', 61);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`idclient`);

--
-- Index pour la table `commande`
--
ALTER TABLE `commande`
  ADD PRIMARY KEY (`idcmd`),
  ADD KEY `cmdClient` (`idclient`);

--
-- Index pour la table `employe`
--
ALTER TABLE `employe`
  ADD PRIMARY KEY (`idemp`);

--
-- Index pour la table `entree`
--
ALTER TABLE `entree`
  ADD PRIMARY KEY (`identree`),
  ADD KEY `entree_emp` (`idemp`),
  ADD KEY `entree_piece` (`idpiece`),
  ADD KEY `entree_fournisseur` (`idfour`);

--
-- Index pour la table `fournisseur`
--
ALTER TABLE `fournisseur`
  ADD PRIMARY KEY (`idfour`);

--
-- Index pour la table `lignecmd`
--
ALTER TABLE `lignecmd`
  ADD PRIMARY KEY (`idlc`),
  ADD KEY `cmdLc` (`idcmd`),
  ADD KEY `pieceLc` (`idpiece`);

--
-- Index pour la table `piece`
--
ALTER TABLE `piece`
  ADD PRIMARY KEY (`idpiece`),
  ADD KEY `pieceFournisseur` (`idfour`);

--
-- Index pour la table `sortie`
--
ALTER TABLE `sortie`
  ADD PRIMARY KEY (`idsortie`),
  ADD KEY `sortie_emp` (`idemp`),
  ADD KEY `sortie_piece` (`idpiece`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `client`
--
ALTER TABLE `client`
  MODIFY `idclient` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `commande`
--
ALTER TABLE `commande`
  MODIFY `idcmd` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `employe`
--
ALTER TABLE `employe`
  MODIFY `idemp` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT pour la table `entree`
--
ALTER TABLE `entree`
  MODIFY `identree` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT pour la table `fournisseur`
--
ALTER TABLE `fournisseur`
  MODIFY `idfour` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `lignecmd`
--
ALTER TABLE `lignecmd`
  MODIFY `idlc` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `piece`
--
ALTER TABLE `piece`
  MODIFY `idpiece` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT pour la table `sortie`
--
ALTER TABLE `sortie`
  MODIFY `idsortie` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `commande`
--
ALTER TABLE `commande`
  ADD CONSTRAINT `cmdClient` FOREIGN KEY (`idclient`) REFERENCES `client` (`idclient`);

--
-- Contraintes pour la table `entree`
--
ALTER TABLE `entree`
  ADD CONSTRAINT `entree_emp` FOREIGN KEY (`idemp`) REFERENCES `employe` (`idemp`),
  ADD CONSTRAINT `entree_fournisseur` FOREIGN KEY (`idfour`) REFERENCES `fournisseur` (`idfour`),
  ADD CONSTRAINT `entree_piece` FOREIGN KEY (`idpiece`) REFERENCES `piece` (`idpiece`);

--
-- Contraintes pour la table `lignecmd`
--
ALTER TABLE `lignecmd`
  ADD CONSTRAINT `cmdLc` FOREIGN KEY (`idcmd`) REFERENCES `commande` (`idcmd`),
  ADD CONSTRAINT `pieceLc` FOREIGN KEY (`idpiece`) REFERENCES `piece` (`idpiece`);

--
-- Contraintes pour la table `piece`
--
ALTER TABLE `piece`
  ADD CONSTRAINT `pieceFournisseur` FOREIGN KEY (`idfour`) REFERENCES `fournisseur` (`idfour`);

--
-- Contraintes pour la table `sortie`
--
ALTER TABLE `sortie`
  ADD CONSTRAINT `sortie_emp` FOREIGN KEY (`idemp`) REFERENCES `employe` (`idemp`),
  ADD CONSTRAINT `sortie_piece` FOREIGN KEY (`idpiece`) REFERENCES `piece` (`idpiece`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
