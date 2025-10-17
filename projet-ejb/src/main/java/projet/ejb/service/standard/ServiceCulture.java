package projet.ejb.service.standard;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

import projet.commun.dto.DtoCulture;
import projet.commun.service.IServiceCulture;
import projet.ejb.dao.IDaoCulture;
import projet.ejb.data.Culture;
import projet.ejb.data.mapper.IMapperEjb;

@Stateless
@Remote
public class ServiceCulture implements IServiceCulture {

    //-------
    // Champs
    //-------

    @Inject
    private IMapperEjb mapper;

    @Inject
    private IDaoCulture daoCulture;

    //-------
    // Actions
    //-------

    @Override
    public int inserer(DtoCulture dtoCulture) {
        // (Option : ajouter une vérification de validité si souhaité, à la manière de ServiceCompte)
        return daoCulture.inserer(mapper.map(dtoCulture));
    }

    @Override
    public void modifier(DtoCulture dtoCulture) {
        // (Option : ajouter une vérification de validité si souhaité)
        daoCulture.modifier(mapper.map(dtoCulture));
    }

    @Override
    public void supprimer(int idCulture) {
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
        for (Culture culture : daoCulture.listerTout()) {
            liste.add(mapper.map(culture));
        }
        return liste;
    }
}
