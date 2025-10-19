package projet.jsf.model.standard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import projet.commun.dto.DtoCompte;
import projet.commun.dto.DtoMouvement;
import projet.commun.exception.ExceptionValidation;
import projet.commun.service.IServiceCompte;
import projet.commun.service.IServiceMouvement;
import projet.jsf.data.Compte;
import projet.jsf.data.Mouvement;
import projet.jsf.data.mapper.IMapper;
import projet.jsf.util.CompteActif;
import projet.jsf.util.UtilJsf;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ModelMouvement implements Serializable {

    //-------
    // Champs
    //-------

    private List<Mouvement> liste;
    private Mouvement courant;
    private String critereRecherche;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private BigDecimal montantMin;
    private BigDecimal montantMax;
    private boolean filtreActif;
    private Integer compteSelectionne;
    private List<Compte> comptesDisponibles;

    @EJB
    private IServiceMouvement serviceMouvement;

    @EJB
    private IServiceCompte serviceCompte;

    @Inject
    private IMapper mapper;

    @Inject
    private CompteActif compteActif;

    //-------
    // Getters & Setters
    //-------

    public List<Mouvement> getListe() {
        if (liste == null) {
            chargerListe();
        }
        return liste;
    }

    public Mouvement getCourant() {
        if (courant == null) {
            courant = new Mouvement();
        }
        return courant;
    }

    public void setCourant(Mouvement courant) {
        this.courant = courant;
    }

    public String getCritereRecherche() { return critereRecherche; }
    public void setCritereRecherche(String critereRecherche) { this.critereRecherche = critereRecherche; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public BigDecimal getMontantMin() { return montantMin; }
    public void setMontantMin(BigDecimal montantMin) { this.montantMin = montantMin; }

    public BigDecimal getMontantMax() { return montantMax; }
    public void setMontantMax(BigDecimal montantMax) { this.montantMax = montantMax; }

    public boolean isFiltreActif() { return filtreActif; }
    public void setFiltreActif(boolean filtreActif) { this.filtreActif = filtreActif; }

    public Integer getCompteSelectionne() { return compteSelectionne; }
    public void setCompteSelectionne(Integer compteSelectionne) { this.compteSelectionne = compteSelectionne; }

    public List<Compte> getComptesDisponibles() {
        if (comptesDisponibles == null) {
            chargerComptesDisponibles();
        }
        return comptesDisponibles;
    }

    //------------------
    // Méthodes internes
    //------------------

    /*private void chargerListe() {
        try {
            liste = new ArrayList<>();
            List<DtoMouvement> dtos;

            // Admin -> tout, User -> seulement ses mouvements
            if (compteActif.isAdmin()) {
                if (critereRecherche != null && !critereRecherche.trim().isEmpty()) {
                    dtos = serviceMouvement.rechercherParLibelle(critereRecherche.trim());
                } else {
                    dtos = serviceMouvement.listerTout();
                }
            } else {
                if (critereRecherche != null && !critereRecherche.trim().isEmpty()) {
                    dtos = serviceMouvement.rechercherParLibelle(critereRecherche.trim());
                } else {
                    dtos = serviceMouvement.listerTout();
                }
                final Integer idActif = compteActif.getId();
                if (idActif != null) {
                    dtos = dtos.stream()
                            .filter(dto -> dto.getIdCompte() == idActif)
                            .collect(Collectors.toList());
                }
            }

            // Filtre “compte sélectionné” — Admin uniquement
            if (compteSelectionne != null && compteSelectionne > 0) {
                dtos = dtos.stream()
                        .filter(dto -> dto.getIdCompte() == compteSelectionne)
                        .collect(Collectors.toList());
            }

            // Filtres avancés
            if (filtreActif) {
                dtos = dtos.stream()
                        .filter(dto -> {
                            if (dateDebut != null && dto.getDate().isBefore(dateDebut)) return false;
                            if (dateFin != null && dto.getDate().isAfter(dateFin)) return false;
                            if (montantMin != null && dto.getMontant().compareTo(montantMin) < 0) return false;
                            if (montantMax != null && dto.getMontant().compareTo(montantMax) > 0) return false;
                            return true;
                        })
                        .collect(Collectors.toList());
            }

            for (DtoMouvement dto : dtos) {
                liste.add(mapper.map(dto));
            }
        } catch (Exception e) {
            UtilJsf.messageError("Erreur lors du chargement des mouvements : " + e.getMessage());
            liste = new ArrayList<>();
        }
    }*/
    
    private void chargerListe() {
        try {
            liste = new ArrayList<>();
            List<DtoMouvement> dtos;

            // 1️⃣ Récupération de la base selon le rôle
            if (compteActif.isAdmin()) {
                dtos = serviceMouvement.listerTout();
            } else {
            	Integer idActif = compteActif.getId();
            	dtos = serviceMouvement.listerTout().stream()
            	        .filter(dto -> idActif != null && dto.getIdCompte() == idActif)
            	        .collect(Collectors.toList());
            }

            // 2️⃣ Filtrage par libellé
            if (critereRecherche != null && !critereRecherche.trim().isEmpty()) {
                String crit = critereRecherche.trim().toLowerCase();
                dtos = dtos.stream()
                        .filter(dto -> dto.getLibelle() != null &&
                                       dto.getLibelle().toLowerCase().contains(crit))
                        .collect(Collectors.toList());
            }

            // 3️⃣ Filtrage par compte (admin)
            if (compteSelectionne != null && compteSelectionne > 0) {
                dtos = dtos.stream()
                        .filter(dto -> dto.getIdCompte() == compteSelectionne)
                        .collect(Collectors.toList());
            }

            // 4️⃣ Filtrage avancé (date et montant)
            if (filtreActif) {
                dtos = dtos.stream()
                        .filter(dto -> {
                            LocalDate date = dto.getDate();
                            if (dateDebut != null && date.isBefore(dateDebut)) return false;
                            if (dateFin != null && date.isAfter(dateFin)) return false;
                            if (montantMin != null && dto.getMontant().compareTo(montantMin) < 0) return false;
                            if (montantMax != null && dto.getMontant().compareTo(montantMax) > 0) return false;
                            return true;
                        })
                        .collect(Collectors.toList());
            }

            // 5️⃣ Mapping final
            for (DtoMouvement dto : dtos) {
                liste.add(mapper.map(dto));
            }

        } catch (Exception e) {
            UtilJsf.messageError("Erreur lors du chargement des mouvements : " + e.getMessage());
            liste = new ArrayList<>();
        }
    }


    private void chargerComptesDisponibles() {
        try {
            comptesDisponibles = new ArrayList<>();
            if (compteActif.isAdmin()) {
                for (DtoCompte dto : serviceCompte.listerTout()) {
                    comptesDisponibles.add(mapper.map(dto));
                }
            } else {
                DtoCompte dto = serviceCompte.retrouver(compteActif.getId());
                if (dto != null) {
                    comptesDisponibles.add(mapper.map(dto));
                }
            }
        } catch (Exception e) {
            UtilJsf.messageError("Erreur lors du chargement des comptes : " + e.getMessage());
            comptesDisponibles = new ArrayList<>();
        }
    }

    //------------------
    // Initialisations
    //------------------

    public void initialiserMouvement(Compte compte) {
        if (courant == null) {
            courant = new Mouvement();
        }
        if (courant.getIdCompte() == null && compte != null && compte.getId() != null) {
            courant.setIdCompte(compte.getId());
        }
    }

    public String actualiserCourant() {
        if (courant != null && courant.getId() != null) {
            DtoMouvement dto = serviceMouvement.retrouver(courant.getId());
            if (dto == null) {
                UtilJsf.messageError("Le mouvement demandé n'existe pas.");
                return "liste";
            } else {
                courant = mapper.map(dto);
            }
        }
        return null;
    }

    //-------
    // Actions CRUD
    //-------

    public String validerMiseAJour() {
        try {
            if (courant.getDate() == null) {
                UtilJsf.messageError("La date du mouvement est obligatoire.");
                return null;
            }

            if (courant.getLibelle() == null || courant.getLibelle().trim().isEmpty()) {
                UtilJsf.messageError("Le libellé du mouvement est obligatoire.");
                return null;
            }

            if (courant.getMontant() == null || courant.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
                UtilJsf.messageError("Le montant du mouvement doit être positif.");
                return null;
            }

            if (courant.getIdCompte() == null || courant.getIdCompte() <= 0) {
                UtilJsf.messageError("Le compte associé au mouvement est obligatoire.");
                return null;
            }

            if (courant.getId() == null) {
                serviceMouvement.inserer(mapper.map(courant));
                UtilJsf.messageInfo("Mouvement créé avec succès !");
            } else {
                serviceMouvement.modifier(mapper.map(courant));
                UtilJsf.messageInfo("Mouvement modifié avec succès !");
            }

            liste = null;
            chargerListe();
            return "liste?faces-redirect=true";
            //return "liste";

        } catch (ExceptionValidation e) {
            UtilJsf.messageError(e.getMessage());
            return null;
        } catch (Exception e) {
            UtilJsf.messageError("Erreur lors de l'enregistrement : " + e.getMessage());
            return null;
        }
    }

    public String supprimer(Mouvement item) {
        try {
            serviceMouvement.supprimer(item.getId());
            if (liste != null) {
                liste.remove(item);
            }
            UtilJsf.messageInfo("Mouvement supprimé avec succès !");
        } catch (ExceptionValidation e) {
            UtilJsf.messageError(e.getMessage());
        } catch (Exception e) {
            UtilJsf.messageError("Erreur lors de la suppression : " + e.getMessage());
        }
        return null;
    }

    //-------
    // Utilitaires
    //-------

    public long getNombreTotal() {
        return serviceMouvement.compter();
    }

    public int getNombreAffiches() {
        return getListe().size();
    }

    public boolean isFiltresActifs() {
        return (critereRecherche != null && !critereRecherche.trim().isEmpty())
                || (compteSelectionne != null && compteSelectionne > 0)
                || (filtreActif && (dateDebut != null || dateFin != null || montantMin != null || montantMax != null));
    }

    public String getNomCompte(Mouvement mouvement) {
        try {
            DtoCompte compte = serviceCompte.retrouver(mouvement.getIdCompte());
            if (compte != null) {
                return compte.getNom() + " " + (compte.getPrenom() != null ? compte.getPrenom() : "");
            }
            return "Compte inconnu";
        } catch (Exception e) {
            return "Erreur";
        }
    }

    public BigDecimal getTotalAffiches() {
        return getListe().stream()
                .map(Mouvement::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    //-------
    // Recherche / Filtrage
    //-------

    public void rechercher() {
        liste = null;
        getListe();
    }

    public void reinitialiserRecherche() {
        critereRecherche = null;
        dateDebut = null;
        dateFin = null;
        montantMin = null;
        montantMax = null;
        filtreActif = false;
        compteSelectionne = null;
        liste = null;
        getListe();
    }

    public void toggleFiltre() {
        filtreActif = !filtreActif;
        liste = null;
        getListe();
    }
}
