package projet.ejb.service.standard;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

import projet.commun.dto.DtoParcelle;
import projet.commun.service.IServiceParcelle;
import projet.ejb.dao.IDaoParcelle;
import projet.ejb.data.Parcelle;
import projet.ejb.data.mapper.IMapperEjb;

@Stateless
@Remote
public class ServiceParcelle implements IServiceParcelle {

    //-------
    // Champs
    //-------

    @Inject
    private IMapperEjb mapper;

    @Inject
    private IDaoParcelle daoParcelle;

    //-------
    // Actions
    //-------

    @Override
    public int inserer(DtoParcelle dtoParcelle) {
        // (Option : ajouter une vérification de validité si souhaité, à la manière de ServiceCompte)
        return daoParcelle.inserer(mapper.map(dtoParcelle));
    }

    @Override
    public void modifier(DtoParcelle dtoParcelle) {
        // (Option : ajouter une vérification de validité si souhaité)
        daoParcelle.modifier(mapper.map(dtoParcelle));
    }

    @Override
    public void supprimer(int idParcelle) {
        daoParcelle.supprimer(idParcelle);
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public DtoParcelle retrouver(int idParcelle) {
        Parcelle Parcelle = daoParcelle.retrouver(idParcelle);
        return mapper.map(Parcelle);
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoParcelle> listerTout() {
        List<DtoParcelle> liste = new ArrayList<>();
        for (Parcelle Parcelle : daoParcelle.listerTout()) {
            liste.add(mapper.map(Parcelle));
        }
        return liste;
    }
}
