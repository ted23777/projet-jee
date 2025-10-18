package projet.ejb.service.standard;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

import projet.commun.dto.DtoMouvement;
import projet.commun.service.IServiceMouvement;
import projet.ejb.dao.IDaoMouvement;
import projet.ejb.data.Mouvement;
import projet.ejb.data.mapper.IMapperEjb;

@Stateless
@Remote
public class ServiceMouvement implements IServiceMouvement {

    //-------
    // Champs
    //-------

    @Inject
    private IMapperEjb mapper;

    @Inject
    private IDaoMouvement daoMouvement;

    //-------
    // Actions
    //-------

    @Override
    public int inserer(DtoMouvement dtoMouvement) {
        return daoMouvement.inserer(mapper.map(dtoMouvement));
    }

    @Override
    public void modifier(DtoMouvement dtoMouvement) {
        daoMouvement.modifier(mapper.map(dtoMouvement));
    }

    @Override
    public void supprimer(int idMouvement) {
        daoMouvement.supprimer(idMouvement);
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public DtoMouvement retrouver(int idMouvement) {
        Mouvement mouvement = daoMouvement.retrouver(idMouvement);
        return mapper.map(mouvement);
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoMouvement> listerTout() {
        List<DtoMouvement> liste = new ArrayList<>();
        for (Mouvement mouvement : daoMouvement.listerTout()) {
            liste.add(mapper.map(mouvement));
        }
        return liste;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoMouvement> rechercherParLibelle(String libelle) {
        String crit = libelle == null ? "" : libelle.trim();
        if (crit.isEmpty()) {
            return listerTout();
        }
        return daoMouvement.rechercherParLibelle(crit).stream().map(mapper::map).collect(Collectors.toList());
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoMouvement> listerParIdCompte(int idCompte) {
        List<DtoMouvement> liste = new ArrayList<>();
        for (Mouvement mouvement : daoMouvement.listerParIdCompte(idCompte)) {
            liste.add(mapper.map(mouvement));
        }
        return liste;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public long compter() {
        return daoMouvement.compter();
    }
}
