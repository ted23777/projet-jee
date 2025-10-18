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
import projet.commun.service.IServiceCompte;
import projet.commun.service.IServiceParcelle;
import projet.jsf.data.Parcelle;
import projet.jsf.data.mapper.IMapper;
import projet.jsf.util.UtilJsf;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ModelParcelle implements Serializable {

    private List<Parcelle> liste;
    private Parcelle       courant;

    private List<DtoCompte> comptes; // pour la combo

    @EJB
    private IServiceParcelle serviceParcelle;

    @EJB
    private IServiceCompte serviceCompte;

    @Inject
    private IMapper mapper;

    // --- Getters
    public List<Parcelle> getListe() {
        if (liste == null) {
            liste = new ArrayList<>();
            for (DtoParcelle dto : serviceParcelle.listerTout()) {
                liste.add(mapper.map(dto));
            }
        }
        return liste;
    }

    public Parcelle getCourant() {
        if (courant == null) {
            courant = new Parcelle();
            courant.setLibre(true); // valeur par défaut
        }
        return courant;
    }

    public List<DtoCompte> getComptes() {
        if (comptes == null) {
            comptes = serviceCompte.listerTout();
        }
        return comptes;
    }

    // --- Init
    public String actualiserCourant() {
        if (courant != null && courant.getId() != null) {
            DtoParcelle dto = serviceParcelle.retrouver(courant.getId());
            if (dto == null) {
                UtilJsf.messageError("La parcelle demandée n'existe pas.");
                return "liste";
            } else {
                courant = mapper.map(dto);
            }
        }
        return null;
    }

    // --- Actions
    public String validerMiseAJour() {
        try {
            // petite validation côté vue
            if (courant.getSurface() <= 0) {
                UtilJsf.messageError("La surface doit être > 0.");
                return null;
            }
            if (courant.getId() == null) {
                serviceParcelle.inserer(mapper.map(courant));
            } else {
                serviceParcelle.modifier(mapper.map(courant));
            }
            UtilJsf.messageInfo("Mise à jour effectuée avec succès.");
            liste = null; // invalider cache
            return "liste";
        } catch (Exception e) {
            UtilJsf.messageError(e);
            return null;
        }
    }

    public String supprimer(Parcelle item) {
        try {
            serviceParcelle.supprimer(item.getId());
            if (liste != null) {
                liste.remove(item);
            }
            UtilJsf.messageInfo("Suppression effectuée avec succès.");
        } catch (Exception e) {
            UtilJsf.messageError(e);
        }
        return null;
    }
}
