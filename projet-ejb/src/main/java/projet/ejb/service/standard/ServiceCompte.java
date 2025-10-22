package projet.ejb.service.standard;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

import projet.commun.dto.DtoCompte;
import projet.commun.dto.DtoParcelle;
import projet.commun.exception.ExceptionValidation;
import projet.commun.service.IServiceCompte;
import projet.ejb.dao.IDaoCompte;
import projet.ejb.data.Compte;
import projet.ejb.data.Parcelle;
import projet.ejb.data.mapper.IMapperEjb;

@Stateless
@Remote
public class ServiceCompte implements IServiceCompte {

    // =======
    // Champs
    // =======

    @Inject
    private IMapperEjb mapper;

    @Inject
    private IDaoCompte daoCompte;

    // =======
    // CRUD standard
    // =======

    @Override
    public int inserer(DtoCompte dtoCompte) throws ExceptionValidation {
        verifierValiditeDonnees(dtoCompte);
        Compte compte = mapper.map(dtoCompte);
        return daoCompte.inserer(compte);
    }

    @Override
    public void modifier(DtoCompte dtoCompte) throws ExceptionValidation {
        verifierValiditeDonnees(dtoCompte);
        daoCompte.modifier(mapper.map(dtoCompte));
    }

    @Override
    public void supprimer(int idCompte) throws ExceptionValidation {
        daoCompte.supprimer(idCompte);
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public DtoCompte retrouver(int idCompte) {
        Compte compte = daoCompte.retrouver(idCompte);
        return mapper.map(compte);
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoCompte> listerTout() {
        List<DtoCompte> liste = new ArrayList<>();
        for (Compte compte : daoCompte.listerTout()) {
            liste.add(mapper.map(compte));
        }
        return liste;
    }

    // =======
    // Méthodes spécifiques
    // =======

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public DtoCompte validerAuthentification(String email, String motDePasse) {
        Compte compte = daoCompte.validerAuthentification(email, motDePasse);
        return mapper.map(compte);
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public boolean verifierUniciteMail(String email, int idCompte) {
        return daoCompte.verifierUniciteMail(email, idCompte);
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoParcelle> listerParcellesDuCompte(int idCompte) {
        List<DtoParcelle> liste = new ArrayList<>();
        for (Parcelle parcelle : daoCompte.listerParcellesDuCompte(idCompte)) {
            liste.add(mapper.map(parcelle));
        }
        return liste;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoCompte> listerComptesAvecParcelles() {
        List<DtoCompte> liste = new ArrayList<>();
        for (Compte compte : daoCompte.listerComptesAvecParcelles()) {
            liste.add(mapper.map(compte));
        }
        return liste;
    }

    // =======
    // Validation métier
    // =======

    private void verifierValiditeDonnees(DtoCompte dtoCompte) throws ExceptionValidation {
        StringBuilder message = new StringBuilder();

        // Nom
        if (dtoCompte.getNom() == null || dtoCompte.getNom().trim().isEmpty()) {
            message.append("\nLe nom doit être renseigné.");
        } else if (dtoCompte.getNom().length() < 3) {
            message.append("\nLe nom est trop court (min 3 caractères).");
        } else if (dtoCompte.getNom().length() > 50) {
            message.append("\nLe nom est trop long (max 50 caractères).");
        }

        // Adresse mail
        if (dtoCompte.getAdresseMail() == null || dtoCompte.getAdresseMail().trim().isEmpty()) {
            message.append("\nL'adresse e-mail doit être renseignée.");
        } else if (!dtoCompte.getAdresseMail().contains("@")) {
            message.append("\nL'adresse e-mail est invalide.");
        } else if (!daoCompte.verifierUniciteMail(dtoCompte.getAdresseMail(), dtoCompte.getId())) {
            message.append("\nL'adresse e-mail " + dtoCompte.getAdresseMail() + " est déjà utilisée.");
        }

        // Mot de passe
        if (dtoCompte.getMotDePasse() == null || dtoCompte.getMotDePasse().trim().isEmpty()) {
            message.append("\nLe mot de passe doit être renseigné.");
        } else if (dtoCompte.getMotDePasse().length() < 3) {
            message.append("\nLe mot de passe est trop court (min 3 caractères).");
        } else if (dtoCompte.getMotDePasse().length() > 50) {
            message.append("\nLe mot de passe est trop long (max 50 caractères).");
        }

        // Solde (optionnel, mais vérifiable)
        if (dtoCompte.getSolde() != null && dtoCompte.getSolde() < 0) {
            message.append("\nLe solde ne peut pas être négatif.");
        }

        if (message.length() > 0) {
            throw new ExceptionValidation(message.toString().substring(1));
        }
    }
}
