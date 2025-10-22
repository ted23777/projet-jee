package projet.ejb.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import projet.commun.dto.DtoCompte;
import projet.commun.dto.DtoMouvement;
import projet.commun.dto.DtoConcerner;
import projet.commun.dto.DtoParcelle;
import projet.commun.dto.DtoCulture;
import projet.commun.dto.DtoEntretien;
import projet.commun.dto.DtoContenir;

import projet.ejb.data.Compte;
import projet.ejb.data.Mouvement;
import projet.ejb.data.Concerner;
import projet.ejb.data.Parcelle;
import projet.ejb.data.Culture;
import projet.ejb.data.Entretien;
import projet.ejb.data.Contenir;

@Mapper(componentModel = "cdi")
public interface IMapperEjb {

    // Créer une instance statique du mapper
    static final IMapperEjb INSTANCE = Mappers.getMapper(IMapperEjb.class);

    // ============================================================
    // 📌 Compte
    // ============================================================
    Compte map(DtoCompte source);
    DtoCompte map(Compte source);

    // ============================================================
    // 📌 Mouvement
    // ============================================================
    @Mapping(target = "compte", source = "idCompte")
    Mouvement map(DtoMouvement source);

    @Mapping(target = "idCompte", source = "compte")
    DtoMouvement map(Mouvement source);

    // Méthodes de conversion personnalisées
    default Compte mapIdCompteToCompte(Integer idCompte) {
        if (idCompte == null) {
            return null;
        }
        Compte compte = new Compte();
        compte.setId(idCompte);
        return compte;
    }

    default Integer mapCompteToIdCompte(Compte compte) {
        return (compte != null) ? compte.getId() : null;
    }

    // ============================================================
    // 📌 Concerner
    // ============================================================
    Concerner map(DtoConcerner source);
    DtoConcerner map(Concerner source);

    // ============================================================
    // 📌 Parcelle
    // ============================================================
    @Mapping(target = "compte", source = "idCompte")
    Parcelle map(DtoParcelle source);

    @Mapping(target = "idCompte", source = "compte.id")
    DtoParcelle map(Parcelle source);

    // Méthodes utilitaires Parcelle <-> idParcelle
    default Parcelle mapIdParcelleToParcelle(Integer idParcelle) {
        if (idParcelle == null) return null;
        Parcelle parcelle = new Parcelle();
        parcelle.setId(idParcelle);
        return parcelle;
    }

    default Integer mapParcelleToIdParcelle(Parcelle parcelle) {
        return (parcelle != null) ? parcelle.getId() : null;
    }


    // ============================================================
    // 📌 Culture
    // ============================================================
    @Mapping(target = "contenirs", ignore = true)
    Culture map(DtoCulture source);

    DtoCulture map(Culture source);
    
    default Culture mapIdCultureToCulture(Integer idCulture) {
        if (idCulture == null) return null;
        Culture culture = new Culture();
        culture.setId(idCulture);
        return culture;
    }

    default Integer mapCultureToIdCulture(Culture culture) {
        return (culture != null) ? culture.getId() : null;
    }

    // ============================================================
    // 📌 Entretien
    // ============================================================
    Entretien map(DtoEntretien source);
    DtoEntretien map(Entretien source);

    // ============================================================
    // 📌 Contenir
    // ============================================================
    @Mapping(target = "idParcelle", source = "parcelle.id")
    @Mapping(target = "idCulture", source = "culture.id")   
    DtoContenir map(Contenir source);

    @Mapping(target = "parcelle", source = "idParcelle")
    @Mapping(target = "culture", source = "idCulture")
    Contenir map(DtoContenir source);
   
}
