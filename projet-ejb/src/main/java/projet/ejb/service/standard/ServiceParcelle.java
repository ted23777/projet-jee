package projet.ejb.service.standard;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

import projet.commun.dto.DtoParcelle;
import projet.commun.exception.ExceptionValidation;
import projet.commun.service.IServiceParcelle;
import projet.ejb.dao.IDaoParcelle;
import projet.ejb.data.Parcelle;
import projet.ejb.data.mapper.IMapperEjb;

@Stateless
@Remote
public class ServiceParcelle implements IServiceParcelle {

    @Inject
    private IMapperEjb mapper;

    @Inject
    private IDaoParcelle daoParcelle;

    @Override
    public int inserer(DtoParcelle dtoParcelle) throws ExceptionValidation {
        valider(dtoParcelle);
        return daoParcelle.inserer(mapper.map(dtoParcelle));
    }

    @Override
    public void modifier(DtoParcelle dtoParcelle) throws ExceptionValidation {
        valider(dtoParcelle);
        daoParcelle.modifier(mapper.map(dtoParcelle));
    }

    @Override
    public void supprimer(int idParcelle) throws ExceptionValidation {
        daoParcelle.supprimer(idParcelle);
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public DtoParcelle retrouver(int idParcelle) {
        Parcelle parcelle = daoParcelle.retrouver(idParcelle);
        return mapper.map(parcelle);
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoParcelle> listerTout() {
        List<DtoParcelle> liste = new ArrayList<>();
        for (Parcelle p : daoParcelle.listerTout()) {
            liste.add(mapper.map(p));
        }
        return liste;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoParcelle> listerParCompte(int idCompte) {
        List<DtoParcelle> liste = new ArrayList<>();
        for (Parcelle p : daoParcelle.listerParCompte(idCompte)) {
            liste.add(mapper.map(p));
        }
        return liste;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoParcelle> listerLibres() {
        List<DtoParcelle> liste = new ArrayList<>();
        for (Parcelle p : daoParcelle.listerLibres()) {
            liste.add(mapper.map(p));
        }
        return liste;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoParcelle> listerOccupees() {
        List<DtoParcelle> liste = new ArrayList<>();
        for (Parcelle p : daoParcelle.listerOccupees()) {
            liste.add(mapper.map(p));
        }
        return liste;
    }

    @Override
    public long compter() {
        return daoParcelle.compter();
    }

    @Override
    public void reserverParCompte(int idParcelle, int idCompte) throws ExceptionValidation {
        if (!daoParcelle.estLibre(idParcelle)) {
            throw new ExceptionValidation("La parcelle n’est pas libre.");
        }
        daoParcelle.reserverParCompte(idParcelle, idCompte);
    }

    @Override
    public void libererParcelle(int idParcelle) {
        daoParcelle.libererParcelle(idParcelle);
    }

    @Override
    public void occuperParcelle(int idParcelle) {
        daoParcelle.occuperParcelle(idParcelle);
    }

    @Override
    public boolean estLibre(int idParcelle) {
        return daoParcelle.estLibre(idParcelle);
    }

    @Override
    public boolean appartientACompte(int idParcelle, int idCompte) {
        return daoParcelle.appartientACompte(idParcelle, idCompte);
    }

    private void valider(DtoParcelle dto) throws ExceptionValidation {
        if (dto == null) {
            throw new ExceptionValidation("Les données sont obligatoires.");
        }
        if (dto.getSurface() == null || dto.getSurface() <= 0) {
            throw new ExceptionValidation("La surface doit être supérieure à 0.");
        }
        if (dto.getLibre() == null) {
            throw new ExceptionValidation("Le statut libre/occupé est obligatoire.");
        }
    }
}
