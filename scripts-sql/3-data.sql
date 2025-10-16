SET search_path TO projet;


-- Supprime toutes les données
DELETE FROM compte;


-- Insère les données

-- Insertion dans la table compte
INSERT INTO compte(nom, prenom, adresseMail, motDePasse, solde, admin)
VALUES
('Dupont', 'Jean', 'jean@3il.com', 'mdp123', 1000.00, true),
('Durand', 'Marie', 'marie.durand@example.com', 'password456', 500.00, false),
('Martin', 'Pierre', 'pierre.martin@example.com', '1234motdepasse', 1500.50, false),
('Lemoine', 'Lucie', 'lucie.lemoine@example.com', 'securepassword789', 200.75, true);

-- Insertion dans la table parcelle
INSERT INTO parcelle(surface, libre, idCompte)
VALUES
(150.5, true, 1),
(120.0, false, 2),
(200.0, true, 3),
(180.75, false, 4);

-- Insertion dans la table mouvement
INSERT INTO mouvement(date_, libellé, montant, idCompte)
VALUES
('2025-10-01', 'Achat de semences', 250.00, 1),
('2025-10-02', 'Entretien parcelle', 100.50, 2),
('2025-10-03', 'Vente de récolte', 500.00, 3),
('2025-10-04', 'Remboursement prêt', 150.00, 4);

-- Insertion dans la table culture
INSERT INTO culture(nom)
VALUES
('Blé'),
('Maïs'),
('Tomates'),
('Carottes');

-- Insertion dans la table entretien
INSERT INTO entretien(date_, titre)
VALUES
('2025-10-01', 'Entretien de la parcelle de Blé'),
('2025-10-02', 'Entretien de la parcelle de Maïs'),
('2025-10-03', 'Récolte de tomates'),
('2025-10-04', 'Aération des carottes');

-- Insertion dans la table contenir
INSERT INTO contenir(idParcelle, idCulture, part)
VALUES
(1, 1, 0.60),
(1, 2, 0.40),
(2, 3, 1.00),
(3, 4, 0.75),
(4, 1, 0.25);

-- Insertion dans la table concerner
INSERT INTO concerner(idCulture, idEntretien)
VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4);

 
