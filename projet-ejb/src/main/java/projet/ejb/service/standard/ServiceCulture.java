package projet.ejb.service.standard;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

import projet.commun.dto.DtoCulture;
import projet.commun.exception.ExceptionValidation;
import projet.commun.service.IServiceCulture;
import projet.ejb.dao.IDaoCulture;
import projet.ejb.data.Culture;
import projet.ejb.data.mapper.IMapperEjb;

@Stateless
@Remote
public class ServiceCulture implements IServiceCulture {

    @Inject
    private IMapperEjb mapper;

    @Inject
    private IDaoCulture daoCulture;

    @Override
    public int inserer(DtoCulture dtoCulture) throws ExceptionValidation {
        valider(dtoCulture);
        return daoCulture.inserer(mapper.map(dtoCulture));
    }

    @Override
    public void modifier(DtoCulture dtoCulture) throws ExceptionValidation {
        valider(dtoCulture);
        daoCulture.modifier(mapper.map(dtoCulture));
    }

    @Override
    public void supprimer(int idCulture) throws ExceptionValidation {
        daoCulture.supprimer(idCulture);
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public DtoCulture retrouver(int idCulture) {
        Culture culture = daoCulture.retrouver(idCulture);
        return mapper.map(culture);
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoCulture> listerTout() {
        List<DtoCulture> liste = new ArrayList<>();
        for (Culture c : daoCulture.listerTout()) {
            liste.add(mapper.map(c));
        }
        return liste;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoCulture> rechercherParNom(String nom) {
        String crit = nom == null ? "" : nom.trim().toLowerCase();
        List<DtoCulture> liste = new ArrayList<>();
        for (Culture c : daoCulture.rechercherParNom(crit)) {
            liste.add(mapper.map(c));
        }
        return liste;
    }

    @Override
    public long compter() {
        return daoCulture.compter();
    }

    @Override
    public DtoCulture retrouverParNom(String nom) {
        Culture culture = daoCulture.retrouverParNom(nom);
        return mapper.map(culture);
    }

    private void valider(DtoCulture dto) throws ExceptionValidation {
        if (dto == null) throw new ExceptionValidation("Les données sont obligatoires.");
        if (dto.getNom() == null || dto.getNom().isEmpty())
            throw new ExceptionValidation("Le nom de la culture est obligatoire.");
    }
}
