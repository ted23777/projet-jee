package projet.ejb.service.standard;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

import projet.commun.dto.DtoContenir;
import projet.commun.dto.DtoCulture;
import projet.commun.dto.DtoParcelle;
import projet.commun.exception.ExceptionValidation;
import projet.commun.service.IServiceContenir;
import projet.ejb.dao.IDaoContenir;
import projet.ejb.dao.IDaoCulture;
import projet.ejb.dao.IDaoParcelle;
import projet.ejb.data.Contenir;
import projet.ejb.data.Culture;
import projet.ejb.data.Parcelle;
import projet.ejb.data.mapper.IMapperEjb;

@Stateless
@Remote
public class ServiceContenir implements IServiceContenir {

	@EJB
	private IDaoParcelle daoParcelle;
	    
	@EJB
	private IDaoCulture daoCulture;
	
    @Inject
    private IMapperEjb mapper;

    @Inject
    private IDaoContenir daoContenir;

    @Override
    public void inserer(DtoContenir dtoContenir) throws ExceptionValidation {
        valider(dtoContenir);
        daoContenir.inserer(mapper.map(dtoContenir));
    }

    @Override
    public void modifier(DtoContenir dtoContenir) throws ExceptionValidation {
        valider(dtoContenir);
        daoContenir.modifier(mapper.map(dtoContenir));
    }

    @Override
    public void supprimer(int idContenir) throws ExceptionValidation {
        Contenir contenir = daoContenir.retrouver(idContenir);
        if (contenir == null) {
            throw new ExceptionValidation("Association parcelle-culture introuvable.");
        }
        daoContenir.supprimer(idContenir);
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public DtoContenir retrouver(int idContenir) {
        Contenir contenir = daoContenir.retrouver(idContenir);
        return (contenir != null) ? mapper.map(contenir) : null;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoContenir> listerTout() {
        List<DtoContenir> liste = new ArrayList<>();
        for (Contenir c : daoContenir.listerTout()) {
            liste.add(mapper.map(c));
        }
        return liste;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoContenir> listerParParcelle(int idParcelle) {
        List<DtoContenir> liste = new ArrayList<>();
        for (Contenir c : daoContenir.listerParParcelle(idParcelle)) {
            liste.add(mapper.map(c));
        }
        return liste;
    }

    @Override
    @TransactionAttribute(NOT_SUPPORTED)
    public List<DtoContenir> listerParCulture(int idCulture) {
        List<DtoContenir> liste = new ArrayList<>();
        for (Contenir c : daoContenir.listerParCulture(idCulture)) {
            liste.add(mapper.map(c));
        }
        return liste;
    }

    @Override
    public long compter() {
        return daoContenir.compter();
    }

    @Override
    public double getSommePartsParcelle(int idParcelle) {
        return daoContenir.calculerPartTotaleParParcelle(idParcelle);
    }
	/*
	 * @Override public Double getPartRestante(int idParcelle) { return 100 -
	 * daoContenir.calculerPartTotaleParParcelle(idParcelle); }
	 */
    
    @Override
    public Double getPartRestante(int idParcelle) {
        double somme = getSommePartsParcelle(idParcelle);
        return 100.0 - somme;
    }

    @Override
    public void ajouterCultureAParcelle(DtoContenir dtoContenir) throws ExceptionValidation {
        double part = dtoContenir.getPart();
        
        // Vérification que la part est positive
        if (part <= 0) {
            throw new ExceptionValidation("La part doit être positive.");
        }

        // Vérification que la somme des parts ne dépasse pas 100%
        double sommeParts = getSommePartsParcelle(dtoContenir.getIdParcelle());
        if (sommeParts + part > 100) {
            throw new ExceptionValidation("La somme des parts dépasse 100 %.");
        }

        // Ajout de la culture à la parcelle dans la base de données
        daoContenir.ajouterCultureAParcelle(dtoContenir.getIdParcelle(), dtoContenir.getIdCulture(), part);
    }


    @Override
    public void retirerCultureDeParcelle(int idParcelle, int idCulture) {
        daoContenir.retirerCultureDeParcelle(idParcelle, idCulture);
    }

    @Override
    public void modifierPartCulture(int idParcelle, int idCulture, double nouvellePart) {
        daoContenir.modifierPartCulture(idParcelle, idCulture, nouvellePart);
    }

    @Override
    public List<DtoCulture> listerCulturesDeParcelle(int idParcelle) {
        List<DtoCulture> liste = new ArrayList<>();
        for (var c : daoContenir.listerCulturesDeParcelle(idParcelle)) {
            liste.add(mapper.map(c));
        }
        return liste;
    }

    @Override
    public List<DtoParcelle> listerParcellesAvecCulture(int idCulture) {
        List<DtoParcelle> liste = new ArrayList<>();
        for (var p : daoContenir.listerParcellesAvecCulture(idCulture)) {
            liste.add(mapper.map(p));
        }
        return liste;
    }

	private void valider(DtoContenir dto) throws ExceptionValidation {
		if (dto == null)
			throw new ExceptionValidation("Les données sont obligatoires.");
		if (dto.getIdParcelle() == null)
			throw new ExceptionValidation("La parcelle est obligatoire.");
		if (dto.getIdCulture() == null)
			throw new ExceptionValidation("La culture est obligatoire.");
		if (dto.getPart() == null || dto.getPart() <= 0)
			throw new ExceptionValidation("La part doit être supérieure à 0.");
	}

	@Override
	public void ajouterCultureAParcelle(Integer idParcelleSelectionnee, Integer idCultureAAjouter, Double partAAjouter)
			throws ExceptionValidation {
		// Validation 1 : Parcelle existe
        Parcelle parcelle = daoParcelle.retrouver(idParcelleSelectionnee);
        if (parcelle == null) {
            throw new ExceptionValidation("La parcelle n'existe pas");
        }
        
        // Validation 2 : Culture existe
        Culture culture = daoCulture.retrouver(idCultureAAjouter);
        if (culture == null) {
            throw new ExceptionValidation("La culture n'existe pas");
        }
        
        // Validation 3 : Part positive
        if (partAAjouter <= 0) {
            throw new ExceptionValidation("La part doit être supérieure à 0");
        }
        
        // Validation 4 : Part ne dépasse pas 100
        if (partAAjouter > 100) {
            throw new ExceptionValidation("La part ne peut pas dépasser 100%");
        }
        
        // Validation 5 : Part totale ne dépasse pas 100%
        double partActuelle = getSommePartsParcelle(idParcelleSelectionnee);
        if (partActuelle + partAAjouter > 100.0) {
            double restante = 100.0 - partActuelle;
            throw new ExceptionValidation(
                String.format("Part insuffisante. Disponible : %.2f%%, Demandé : %.2f%%", 
                              restante, partAAjouter)
            );
        }
        
        // Validation 6 : Culture pas déjà présente
        List<Contenir> existants = daoContenir.listerParParcelle(idParcelleSelectionnee);
        for (Contenir c : existants) {
            if (c.getCulture().getId() == idCultureAAjouter) {
                throw new ExceptionValidation(
                    "Cette culture est déjà plantée dans cette parcelle"
                );
            }
        }
        
        // Insertion
        Contenir contenir = new Contenir(parcelle, culture, partAAjouter);
        daoContenir.inserer(contenir);
		
	}
}
