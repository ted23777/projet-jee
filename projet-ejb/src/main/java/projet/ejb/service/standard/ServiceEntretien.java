package projet.ejb.service.standard;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

import projet.commun.dto.DtoEntretien;
import projet.commun.service.IServiceEntretien;
import projet.ejb.dao.IDaoEntretien;
import projet.ejb.data.Entretien;
import projet.ejb.data.mapper.IMapperEjb;

@Stateless
@Remote
public class ServiceEntretien implements IServiceEntretien {

    //-------
    // Champs
    //-------

    @Inject
    private IMapperEjb mapper;

    @Inject
    private IDaoEntretien daoEntretien;

    //-------
    // Actions
    //-------

    @Override
    public int inserer(DtoEntretien dtoEntretien) {
        return daoEntretien.inserer(mapper.map(dtoEntretien));
    }

    @Override
    public void modifier(DtoEntretien dtoEntretien) {
        daoEntretien.modifier(mapper.map(dtoEntretien));
    }

    @Override
    public void supprimer(int idEntretien) {
        daoEntretien.supprimer(idEntretien);
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public DtoEntretien retrouver(int idEntretien) {
        Entretien entretien = daoEntretien.retrouver(idEntretien);
        return mapper.map(entretien);
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoEntretien> listerTout() {
        List<DtoEntretien> liste = new ArrayList<>();
        for (Entretien entretien : daoEntretien.listerTout()) {
            liste.add(mapper.map(entretien));
        }
        return liste;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoEntretien> rechercherParTitre(String titre) {
        String crit = titre == null ? "" : titre.trim();
        if (crit.isEmpty()) {
            return listerTout();
        }
        return daoEntretien.rechercherParTitre(crit).stream().map(mapper::map).collect(Collectors.toList());
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public long compter() {
        return daoEntretien.compter();
    }
}
