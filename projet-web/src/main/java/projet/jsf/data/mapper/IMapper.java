package projet.jsf.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import projet.commun.dto.DtoCompte;
import projet.commun.dto.DtoParcelle;
import projet.commun.dto.DtoCulture;
import projet.commun.dto.DtoMouvement;
import projet.commun.dto.DtoEntretien;
import projet.commun.dto.DtoContenir;
import projet.commun.dto.DtoConcerner;

import projet.jsf.data.Compte;
import projet.jsf.data.Parcelle;
import projet.jsf.data.Culture;
import projet.jsf.data.Mouvement;
import projet.jsf.data.Entretien;
import projet.jsf.data.Contenir;
import projet.jsf.data.Concerner;

@Mapper(componentModel = "cdi")
public interface IMapper {

    //------- 
    // Compte 
    //-------

    Compte map(DtoCompte source);
    DtoCompte map(Compte source);
    Compte duplicate(Compte source);
    Compte update(@MappingTarget Compte target, Compte source);

    //------- 
    // Parcelle 
    //-------

    Parcelle map(DtoParcelle source);
    DtoParcelle map(Parcelle source);
    Parcelle duplicate(Parcelle source);
    Parcelle update(@MappingTarget Parcelle target, Parcelle source);

    //------- 
    // Culture 
    //-------

    Culture map(DtoCulture source);
    DtoCulture map(Culture source);
    Culture duplicate(Culture source);
    Culture update(@MappingTarget Culture target, Culture source);

    //------- 
    // Mouvement 
    //-------

    Mouvement map(DtoMouvement source);
    DtoMouvement map(Mouvement source);
    Mouvement duplicate(Mouvement source);
    Mouvement update(@MappingTarget Mouvement target, Mouvement source);

    //------- 
    // Entretien 
    //-------

    Entretien map(DtoEntretien source);
    DtoEntretien map(Entretien source);
    Entretien duplicate(Entretien source);
    Entretien update(@MappingTarget Entretien target, Entretien source);

    //------- 
    // Contenir 
    //-------

    Contenir map(DtoContenir source);
    DtoContenir map(Contenir source);
    Contenir duplicate(Contenir source);
    Contenir update(@MappingTarget Contenir target, Contenir source);

    //------- 
    // Concerner 
    //-------

    Concerner map(DtoConcerner source);
    DtoConcerner map(Concerner source);
    Concerner duplicate(Concerner source);
    Concerner update(@MappingTarget Concerner target, Concerner source);
}
