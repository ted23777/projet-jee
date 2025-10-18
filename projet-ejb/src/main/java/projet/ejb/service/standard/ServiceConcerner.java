package projet.ejb.service.standard;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

import projet.commun.dto.DtoConcerner;
import projet.commun.service.IServiceConcerner;
import projet.ejb.dao.IDaoConcerner;
import projet.ejb.data.Concerner;
import projet.ejb.data.mapper.IMapperEjb;

@Stateless
@Remote
public class ServiceConcerner implements IServiceConcerner {

    //-------
    // Champs
    //-------

    @Inject
    private IMapperEjb mapper;

    @Inject
    private IDaoConcerner daoConcerner;

    //-------
    // Actions
    //-------

    @Override
    public void inserer(DtoConcerner dtoConcerner) {
        daoConcerner.inserer(mapper.map(dtoConcerner));
    }

    @Override
    public void modifier(DtoConcerner dtoConcerner) {
        daoConcerner.modifier(mapper.map(dtoConcerner));
    }

    @Override
    public void supprimer(int idCulture, int idEntretien) {
        daoConcerner.supprimer(idCulture, idEntretien);
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public DtoConcerner retrouver(int idCulture, int idEntretien) {
        Concerner concerner = daoConcerner.retrouver(idCulture, idEntretien);
        return mapper.map(concerner);
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoConcerner> listerTout() {
        List<DtoConcerner> liste = new ArrayList<>();
        for (Concerner concerner : daoConcerner.listerTout()) {
            liste.add(mapper.map(concerner));
        }
        return liste;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoConcerner> listerParIdEntretien(int idEntretien) {
        List<DtoConcerner> liste = new ArrayList<>();
        for (Concerner concerner : daoConcerner.listerParIdEntretien(idEntretien)) {
            liste.add(mapper.map(concerner));
        }
        return liste;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoConcerner> listerParIdCulture(int idCulture) {
        List<DtoConcerner> liste = new ArrayList<>();
        for (Concerner concerner : daoConcerner.listerParIdCulture(idCulture)) {
            liste.add(mapper.map(concerner));
        }
        return liste;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public long compter() {
        return daoConcerner.compter();
    }
}
