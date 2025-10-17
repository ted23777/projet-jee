package projet.ejb.data.mapper;

import org.mapstruct.Mapper;
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

    // Compte
    Compte map(DtoCompte source);
    DtoCompte map(Compte source);

    // Mouvement
    Mouvement map(DtoMouvement source);
    DtoMouvement map(Mouvement source);

    // Concerner
    Concerner map(DtoConcerner source);
    DtoConcerner map(Concerner source);

    // Parcelle
    Parcelle map(DtoParcelle source);
    DtoParcelle map(Parcelle source);

    // Culture
    Culture map(DtoCulture source);
    DtoCulture map(Culture source);

    // Entretien
    Entretien map(DtoEntretien source);
    DtoEntretien map(Entretien source);

    // Contenir
    Contenir map(DtoContenir source);
    DtoContenir map(Contenir source);
}
