package projet.jsf.model.standard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import projet.commun.dto.DtoContenir;
import projet.commun.dto.DtoCulture;
import projet.commun.exception.ExceptionValidation;
import projet.commun.service.IServiceContenir;
import projet.commun.service.IServiceCulture;
import projet.commun.service.IServiceParcelle;
import projet.jsf.data.Culture;
import projet.jsf.data.mapper.IMapper;
import projet.jsf.util.UtilJsf;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ModelCulture implements Serializable {

	private List<Culture> liste;
	private Culture courant;

	@EJB
	private IServiceCulture serviceCulture;
	@EJB
	private IServiceContenir serviceContenir;
	@EJB
	private IServiceParcelle serviceParcelle;

	@Inject
	private IMapper mapper;

	private Integer idParcelleSelectionnee;
	
	private Double partSelectionnee ;
	
	private Double partRestante = 0.0;
	
	private Double surfaceDisponible = 0.0;

	
	// -------
	// Getters
	// -------
	public List<Culture> getListe() {
		if (liste == null) {
			liste = new ArrayList<>();
			for (DtoCulture dto : serviceCulture.listerTout()) {
				liste.add(mapper.map(dto));
			}
		}
		return liste;
	}

	public Culture getCourant() {
		if (courant == null) {
			courant = new Culture();
		}
		return courant;
	}

	public Double getPartRestante() {
	    return partRestante;
	}

	public Double getSurfaceDisponible() {
	    return surfaceDisponible;
	}
	// -------
	// Initialisations
	// -------
	public String actualiserCourant() {
		if (courant != null && courant.getId() != null) {
			DtoCulture dto = serviceCulture.retrouver(courant.getId());
			if (dto == null) {
				UtilJsf.messageError("La culture demandée n'existe pas.");
				return "liste";
			} else {
				courant = mapper.map(dto);
			}
		}
		return null;
	}

	// -------
	// Actions
	// -------
	
	public String validerMiseAJour() {
		DtoCulture dtoCulture = mapper.map(courant);
		try {
			if (courant.getId() == null) {
				//serviceCulture.inserer(mapper.map(courant));
				int id = serviceCulture.inserer(dtoCulture);   // returns the generated id
			    dtoCulture.setId(id);                          // keep working with the same DTO
			    courant.setId(id);  
			} else {
				serviceCulture.modifier(mapper.map(courant));
			}
			if (idParcelleSelectionnee != null) {
			    DtoContenir dtoContenir = new DtoContenir();
			    dtoContenir.setIdParcelle(idParcelleSelectionnee);
			    dtoContenir.setIdCulture(dtoCulture.getId());  // <-- l’id est fiable maintenant
			    dtoContenir.setPart(100.0);                    // ou une valeur venant du formulaire
			    serviceContenir.inserer(dtoContenir);
			}

			UtilJsf.messageInfo("Culture enregistrée avec succès.");
			liste = null;
			return "liste";
		} catch (ExceptionValidation e) {
			UtilJsf.messageError(e);
			return null;
		}
	}

	
	public String supprimer(Culture item) {
		try {
			serviceCulture.supprimer(item.getId());
			getListe().remove(item);
			UtilJsf.messageInfo("Culture supprimée avec succès.");
		} catch (ExceptionValidation e) {
			UtilJsf.messageError(e);
		}
		return null;
	}

	public List<Culture> rechercher(String nom) {
		List<Culture> resultats = new ArrayList<>();
		for (DtoCulture dto : serviceCulture.rechercherParNom(nom)) {
			resultats.add(mapper.map(dto));
		}
		return resultats;
	}

	public void actualiserDisponibiliteParcelle() {
	    if (idParcelleSelectionnee != null) {
	        partRestante = serviceContenir.getPartRestante(idParcelleSelectionnee);
	        var dtoParcelle = serviceParcelle.retrouver(idParcelleSelectionnee);
	        if (dtoParcelle != null) {
	            surfaceDisponible = dtoParcelle.getSurface() * (partRestante / 100);
	        }
	    } else {
	        partRestante = 0.0;
	        surfaceDisponible = 0.0;
	    }
	}
	
	public Integer getIdParcelleSelectionnee() {
		return idParcelleSelectionnee;
	}

	public void setIdParcelleSelectionnee(Integer idParcelleSelectionnee) {
		this.idParcelleSelectionnee = idParcelleSelectionnee;
	}

	public Double getPartSelectionnee() {
		return partSelectionnee;
	}

	public void setPartSelectionnee(Double partSelectionne) {
		this.partSelectionnee = partSelectionne;
	}

}
