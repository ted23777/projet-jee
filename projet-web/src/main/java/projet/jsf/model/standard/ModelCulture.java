package projet.jsf.model.standard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import projet.commun.dto.DtoCulture;
import projet.commun.service.IServiceCulture;   // <-- même package que pour Compte (commun.service)
import projet.jsf.data.Culture;
import projet.jsf.data.mapper.IMapper;
import projet.jsf.util.UtilJsf;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ModelCulture implements Serializable {

	//-------
	// Champs
	//-------
	
	private List<Culture>	liste;
	private Culture		courant;

	@EJB
	private IServiceCulture	serviceCulture;

	@Inject
	private IMapper			mapper;

	//-------
	// Getters
	//-------

	public List<Culture> getListe() {
		if ( liste == null ) {
			liste = new ArrayList<>();
			for ( DtoCulture dto : serviceCulture.listerTout() ) {
				liste.add( mapper.map( dto ) );
			}
		}
		return liste;
	}

	public Culture getCourant() {
		if ( courant == null ) {
			courant = new Culture();
		}
		return courant;
	}

	//------------------
	// Initialisations
	//------------------

	public String actualiserCourant() {
		if ( courant != null && courant.getId() != null ) {
			DtoCulture dto = serviceCulture.retrouver( courant.getId() );
			if ( dto == null ) {
				UtilJsf.messageError( "La culture demandée n'existe pas." );
				return "liste";
			} else {
				courant = mapper.map( dto );
			}
		}
		return null;
	}

	//-------
	// Actions
	//-------

	public String validerMiseAJour() {
		try {
			if ( courant.getId() == null ) {
				serviceCulture.inserer( mapper.map( courant ) );
			} else {
				serviceCulture.modifier( mapper.map( courant ) );
			}
			UtilJsf.messageInfo( "Mise à jour effectuée avec succès." );
			// Invalide le cache local pour refléter les changements au prochain affichage
			liste = null;
			return "liste";
		} catch ( Exception e ) {
			// Si IServiceCulture lance ExceptionValidation, elle sera attrapée ici aussi
			UtilJsf.messageError( e );
			return null;
		}
	}

	public String supprimer( Culture item ) {
		try {
			serviceCulture.supprimer( item.getId() );
			if ( liste != null ) {
				liste.remove( item );
			}
			UtilJsf.messageInfo( "Suppression effectuée avec succès." );
		} catch ( Exception e ) {
			UtilJsf.messageError( e );
		}
		return null;
	}
}
