package projet.jsf.model.standard;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import projet.commun.dto.DtoCompte;
import projet.commun.dto.DtoParcelle;
import projet.commun.exception.ExceptionValidation;
import projet.commun.service.IServiceCompte;
import projet.jsf.data.Compte;
import projet.jsf.data.Parcelle;
import projet.jsf.data.mapper.IMapper;
import projet.jsf.util.UtilJsf;


@SuppressWarnings("serial")
@Named
@ViewScoped
public class ModelCompte implements Serializable {

	//-------
	// Champs
	//-------
	
	private List<Compte>	liste;
	
	private Compte			courant;
	
	private List<Parcelle> parcellesDuCompte;
	
	@EJB
	private IServiceCompte	serviceCompte;
	
	@Inject
	private IMapper			mapper;

	//-------
	// Getters 
	//-------
	
	public List<Compte> getListe() {
		if ( liste == null ) {
			liste = new ArrayList<>();
			for ( DtoCompte dto : serviceCompte.listerTout() ) {
				liste.add( mapper.map( dto ) );
			}
		}
		return liste;
	}
	
		public Compte getCourant() {
			if ( courant == null ) {
				courant = new Compte();
			}
			return courant;
		}
		
		public void setCourant(Compte courant) {
		    this.courant = courant;
		}
	
		 public List<Parcelle> getParcellesDuCompte() {
		        if (courant != null && courant.getId() != null) {
		            parcellesDuCompte = new ArrayList<>();
		            for (DtoParcelle dto : serviceCompte.listerParcellesDuCompte(courant.getId())) {
		                parcellesDuCompte.add(mapper.map(dto));
		            }
		        }
		        return parcellesDuCompte;
		    }
	//-------
	// Initialisaitons
	//-------
	
	public String actualiserCourant() {
		if ( courant != null ) {
			DtoCompte dto = serviceCompte.retrouver( courant.getId() ); 
			if ( dto == null ) {
				UtilJsf.messageError( "Le compte demandé n'existe pas" );
				return "test/liste";
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
			if ( courant.getId() == null) {
				serviceCompte.inserer( mapper.map(courant) );
			} else {
				serviceCompte.modifier( mapper.map(courant) );
			}
			UtilJsf.messageInfo( "Mise à jour effectuée avec succès." );
			if (courant.isAdmin()) {
				return "liste";
			} else {
				return null;
			}
			
		} catch (ExceptionValidation e) {
			UtilJsf.messageError(e);
			return null;
		}
	}
	
	public String supprimer( Compte item ) {
		try {
			serviceCompte.supprimer( item.getId() );
			liste.remove(item);
			UtilJsf.messageInfo( "Suppression effectuée avec succès." );
		} catch (ExceptionValidation e) {
			UtilJsf.messageError( e ); 
		}
		return null;
	}
	
	  public String authentifier(String email, String motDePasse) {
	        DtoCompte dto = serviceCompte.validerAuthentification(email, motDePasse);
	        if (dto == null) {
	            UtilJsf.messageError("Identifiants invalides. Vérifiez votre email ou mot de passe.");
	            return null;
	        }
	        courant = mapper.map(dto);
	        UtilJsf.messageInfo("Bienvenue " + courant.getPrenom() + " !");
	        return "accueil"; // page d’accueil utilisateur
	    }

	    public boolean estAdmin() {
	        return courant != null && courant.isAdmin();
	    }

	    public void actualiserListe() {
	        liste = null; // forcera le rechargement au prochain getListe()
	    }
	
}
