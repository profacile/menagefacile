package be.profacile.menagefacile.domain.support;
import be.profacile.menagefacile.domain.model.*;
import be.profacile.menagefacile.domain.repository.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static be.profacile.menagefacile.domain.support.JpaExerciseSupport.*;

public final class MenageContract {
 private MenageContract() {}
 private static void composition(Class<?> owner,String nom,String inverse,Class<?> element) {
  var field=champ(owner,nom,List.class);
  var mapping=field.getAnnotation(OneToMany.class);assertNotNull(mapping);
  assertEquals(inverse,mapping.mappedBy());assertTrue(mapping.orphanRemoval());
  assertTrue(Arrays.asList(mapping.cascade()).contains(CascadeType.ALL));
  assertEquals(element,((java.lang.reflect.ParameterizedType)field.getGenericType()).getActualTypeArguments()[0]);
 }
 private static void services() {
  var field=champ(AideDomicile.class,"services",Set.class);
  var mapping=field.getAnnotation(ManyToMany.class);assertNotNull(mapping);
  assertFalse(Arrays.asList(mapping.cascade()).contains(CascadeType.REMOVE));
  assertFalse(Arrays.asList(mapping.cascade()).contains(CascadeType.ALL));
  var join=field.getAnnotation(JoinTable.class);assertNotNull(join);
  assertEquals("aide_service",join.name());
  assertEquals("aide_id",join.joinColumns()[0].name());
  assertEquals("service_id",join.inverseJoinColumns()[0].name());
 }
 public static void enumerations() {
 assertArrayEquals(new String[]{"DEMANDEE","PLANIFIEE","EN_COURS","REALISEE","ANNULEE"},Arrays.stream(StatutPrestation.values()).map(Enum::name).toArray(String[]::new));
assertArrayEquals(new String[]{"UNE_FOIS","HEBDOMADAIRE","BIMENSUELLE"},Arrays.stream(Frequence.values()).map(Enum::name).toArray(String[]::new));
assertArrayEquals(new String[]{"EN_ATTENTE","VALIDEE","INDISPONIBLE","SUSPENDUE","REFUSEE"},Arrays.stream(StatutCompte.values()).map(Enum::name).toArray(String[]::new));
assertArrayEquals(new String[]{"APPARTEMENT","MAISON"},Arrays.stream(TypeLogement.values()).map(Enum::name).toArray(String[]::new));
assertArrayEquals(new String[]{"CLIENT_ABSENT","MATERIEL_MANQUANT","RETARD"},Arrays.stream(TypeSignalement.values()).map(Enum::name).toArray(String[]::new));
assertArrayEquals(new String[]{"LUNDI","MARDI","MERCREDI","JEUDI","VENDREDI","SAMEDI","DIMANCHE"},Arrays.stream(JourSemaine.values()).map(Enum::name).toArray(String[]::new));
assertArrayEquals(new String[]{"CLIENT","AIDE_DOMICILE","ADMINISTRATEUR"},Arrays.stream(RoleUtilisateur.values()).map(Enum::name).toArray(String[]::new));
}
public static void verifierUtilisateur() {
entite(Utilisateur.class, "utilisateur");
assertTrue(java.lang.reflect.Modifier.isAbstract(Utilisateur.class.getModifiers()));
assertNotNull(Utilisateur.class.getAnnotation(Inheritance.class));
assertEquals(InheritanceType.JOINED,Utilisateur.class.getAnnotation(Inheritance.class).strategy());
colonne(Utilisateur.class,"email",String.class,"email",false);
assertTrue(champ(Utilisateur.class,"email",String.class).getAnnotation(Column.class).unique());
assertEquals(150, champ(Utilisateur.class,"email",String.class).getAnnotation(Column.class).length());
colonne(Utilisateur.class,"motDePasse",String.class,"mot_de_passe_hash",false);
assertEquals(255, champ(Utilisateur.class,"motDePasse",String.class).getAnnotation(Column.class).length());
colonne(Utilisateur.class,"telephone",String.class,"telephone",true);
assertEquals(20, champ(Utilisateur.class,"telephone",String.class).getAnnotation(Column.class).length());
colonne(Utilisateur.class,"role",RoleUtilisateur.class,"role",false);
assertEquals(20, champ(Utilisateur.class,"role",RoleUtilisateur.class).getAnnotation(Column.class).length());
assertEquals(EnumType.STRING, Objects.requireNonNull(champ(Utilisateur.class,"role",RoleUtilisateur.class).getAnnotation(Enumerated.class), "Ajouter @Enumerated").value());
repository(UtilisateurRepository.class,Utilisateur.class);
assertDoesNotThrow(() -> UtilisateurRepository.class.getMethod("findByEmail", String.class));
}
public static void verifierClient() {
entite(Client.class, "client");
assertEquals(Utilisateur.class,Client.class.getSuperclass());
assertNotNull(Client.class.getAnnotation(PrimaryKeyJoinColumn.class));
assertEquals("utilisateur_id",Client.class.getAnnotation(PrimaryKeyJoinColumn.class).name());
colonne(Client.class,"nom",String.class,"nom",false);
assertEquals(80, champ(Client.class,"nom",String.class).getAnnotation(Column.class).length());
colonne(Client.class,"prenom",String.class,"prenom",false);
assertEquals(80, champ(Client.class,"prenom",String.class).getAnnotation(Column.class).length());
composition(Client.class,"adresses","client",Adresse.class);
repository(ClientRepository.class,Client.class);
assertDoesNotThrow(() -> ClientRepository.class.getMethod("findByEmail", String.class));
}
public static void verifierAdministrateur() {
entite(Administrateur.class, "administrateur");
assertEquals(Utilisateur.class,Administrateur.class.getSuperclass());
assertNotNull(Administrateur.class.getAnnotation(PrimaryKeyJoinColumn.class));
assertEquals("utilisateur_id",Administrateur.class.getAnnotation(PrimaryKeyJoinColumn.class).name());
repository(AdministrateurRepository.class,Administrateur.class);
assertDoesNotThrow(() -> AdministrateurRepository.class.getMethod("findByEmail", String.class));
}
public static void verifierAdresse() {
entite(Adresse.class, "adresse");
colonne(Adresse.class,"rue",String.class,"rue",false);
assertEquals(150, champ(Adresse.class,"rue",String.class).getAnnotation(Column.class).length());
colonne(Adresse.class,"codePostal",String.class,"code_postal",false);
assertEquals(4, champ(Adresse.class,"codePostal",String.class).getAnnotation(Column.class).length());
colonne(Adresse.class,"ville",String.class,"ville",false);
assertEquals(80, champ(Adresse.class,"ville",String.class).getAnnotation(Column.class).length());
colonne(Adresse.class,"typeLogement",TypeLogement.class,"type_logement",false);
assertEquals(20, champ(Adresse.class,"typeLogement",TypeLogement.class).getAnnotation(Column.class).length());
assertEquals(EnumType.STRING, Objects.requireNonNull(champ(Adresse.class,"typeLogement",TypeLogement.class).getAnnotation(Enumerated.class), "Ajouter @Enumerated").value());
colonne(Adresse.class,"surfaceM2",int.class,"surface_m2",false);
colonne(Adresse.class,"nbPieces",int.class,"nb_pieces",false);
colonne(Adresse.class,"animaux",boolean.class,"animaux",false);
colonne(Adresse.class,"consignesAcces",String.class,"consignes_acces_chiffrees",true);
relation(Adresse.class,"client",Client.class,"client_id");
repository(AdresseRepository.class,Adresse.class);
assertDoesNotThrow(() -> AdresseRepository.class.getMethod("findByClientId", Long.class));
}
public static void verifierService() {
entite(Service.class, "service");
colonne(Service.class,"libelle",String.class,"libelle",false);
assertTrue(champ(Service.class,"libelle",String.class).getAnnotation(Column.class).unique());
assertEquals(100, champ(Service.class,"libelle",String.class).getAnnotation(Column.class).length());
colonne(Service.class,"tarifIndicatif",BigDecimal.class,"tarif_indicatif",false);
colonne(Service.class,"dureeMinHeures",int.class,"duree_min_heures",false);
repository(ServiceRepository.class,Service.class);
assertDoesNotThrow(() -> ServiceRepository.class.getMethod("findByLibelleIgnoreCase", String.class));
}
public static void verifierAideDomicile() {
entite(AideDomicile.class, "aide_domicile");
assertEquals(Utilisateur.class,AideDomicile.class.getSuperclass());
assertNotNull(AideDomicile.class.getAnnotation(PrimaryKeyJoinColumn.class));
assertEquals("utilisateur_id",AideDomicile.class.getAnnotation(PrimaryKeyJoinColumn.class).name());
colonne(AideDomicile.class,"nom",String.class,"nom",false);
assertEquals(80, champ(AideDomicile.class,"nom",String.class).getAnnotation(Column.class).length());
colonne(AideDomicile.class,"prenom",String.class,"prenom",false);
assertEquals(80, champ(AideDomicile.class,"prenom",String.class).getAnnotation(Column.class).length());
colonne(AideDomicile.class,"statutCompte",StatutCompte.class,"statut_compte",false);
assertEquals(20, champ(AideDomicile.class,"statutCompte",StatutCompte.class).getAnnotation(Column.class).length());
assertEquals(EnumType.STRING, Objects.requireNonNull(champ(AideDomicile.class,"statutCompte",StatutCompte.class).getAnnotation(Enumerated.class), "Ajouter @Enumerated").value());
services();
composition(AideDomicile.class,"disponibilites","aide",Disponibilite.class);
composition(AideDomicile.class,"zonesIntervention","aide",ZoneIntervention.class);
composition(AideDomicile.class,"indisponibilites","aide",Indisponibilite.class);
repository(AideDomicileRepository.class,AideDomicile.class);
assertDoesNotThrow(() -> AideDomicileRepository.class.getMethod("findByStatutCompte", StatutCompte.class));
}
public static void verifierDisponibilite() {
entite(Disponibilite.class, "disponibilite");
colonne(Disponibilite.class,"jour",JourSemaine.class,"jour",false);
assertEquals(10, champ(Disponibilite.class,"jour",JourSemaine.class).getAnnotation(Column.class).length());
assertEquals(EnumType.STRING, Objects.requireNonNull(champ(Disponibilite.class,"jour",JourSemaine.class).getAnnotation(Enumerated.class), "Ajouter @Enumerated").value());
colonne(Disponibilite.class,"heureDebut",LocalTime.class,"heure_debut",false);
colonne(Disponibilite.class,"heureFin",LocalTime.class,"heure_fin",false);
relation(Disponibilite.class,"aide",AideDomicile.class,"aide_id");
repository(DisponibiliteRepository.class,Disponibilite.class);
assertDoesNotThrow(() -> DisponibiliteRepository.class.getMethod("findByAideId", Long.class));
}
public static void verifierZoneIntervention() {
entite(ZoneIntervention.class, "zone_intervention");
colonne(ZoneIntervention.class,"codePostal",String.class,"code_postal",false);
assertEquals(4, champ(ZoneIntervention.class,"codePostal",String.class).getAnnotation(Column.class).length());
relation(ZoneIntervention.class,"aide",AideDomicile.class,"aide_id");
repository(ZoneInterventionRepository.class,ZoneIntervention.class);
assertDoesNotThrow(() -> ZoneInterventionRepository.class.getMethod("findByCodePostal", String.class));
}
public static void verifierIndisponibilite() {
entite(Indisponibilite.class, "indisponibilite");
colonne(Indisponibilite.class,"dateDebut",LocalDate.class,"date_debut",false);
colonne(Indisponibilite.class,"dateFin",LocalDate.class,"date_fin",false);
colonne(Indisponibilite.class,"motif",String.class,"motif",true);
assertEquals(150, champ(Indisponibilite.class,"motif",String.class).getAnnotation(Column.class).length());
relation(Indisponibilite.class,"aide",AideDomicile.class,"aide_id");
repository(IndisponibiliteRepository.class,Indisponibilite.class);
assertDoesNotThrow(() -> IndisponibiliteRepository.class.getMethod("findByAideId", Long.class));
}
public static void verifierReservation() {
entite(Reservation.class, "reservation");
colonne(Reservation.class,"frequence",Frequence.class,"frequence",false);
assertEquals(20, champ(Reservation.class,"frequence",Frequence.class).getAnnotation(Column.class).length());
assertEquals(EnumType.STRING, Objects.requireNonNull(champ(Reservation.class,"frequence",Frequence.class).getAnnotation(Enumerated.class), "Ajouter @Enumerated").value());
colonne(Reservation.class,"dateDebut",LocalDate.class,"date_debut",false);
colonne(Reservation.class,"creneau",LocalTime.class,"creneau",false);
colonne(Reservation.class,"dureeHeures",int.class,"duree_heures",false);
colonne(Reservation.class,"memeAide",boolean.class,"meme_aide",false);
colonne(Reservation.class,"remarques",String.class,"remarques",true);
relation(Reservation.class,"client",Client.class,"client_id");
relation(Reservation.class,"service",Service.class,"service_id");
relation(Reservation.class,"adresse",Adresse.class,"adresse_id");
composition(Reservation.class,"prestations","reservation",Prestation.class);
repository(ReservationRepository.class,Reservation.class);
assertDoesNotThrow(() -> ReservationRepository.class.getMethod("findByClientId", Long.class));
}
public static void verifierPrestation() {
entite(Prestation.class, "prestation");
colonne(Prestation.class,"date",LocalDate.class,"date",false);
colonne(Prestation.class,"heureDebutPrevue",LocalTime.class,"heure_debut_prevue",false);
colonne(Prestation.class,"dureeHeures",int.class,"duree_heures",false);
colonne(Prestation.class,"statut",StatutPrestation.class,"statut",false);
assertEquals(20, champ(Prestation.class,"statut",StatutPrestation.class).getAnnotation(Column.class).length());
assertEquals(EnumType.STRING, Objects.requireNonNull(champ(Prestation.class,"statut",StatutPrestation.class).getAnnotation(Enumerated.class), "Ajouter @Enumerated").value());
colonne(Prestation.class,"heureDebutReelle",LocalDateTime.class,"heure_debut_reelle",true);
colonne(Prestation.class,"heureFinReelle",LocalDateTime.class,"heure_fin_reelle",true);
colonne(Prestation.class,"annulationTardive",boolean.class,"annulation_tardive",false);
colonne(Prestation.class,"noteEvaluation",Integer.class,"note_evaluation",true);
colonne(Prestation.class,"commentaireEvaluation",String.class,"commentaire_evaluation",true);
colonne(Prestation.class,"signalement",TypeSignalement.class,"signalement",true);
assertEquals(30, champ(Prestation.class,"signalement",TypeSignalement.class).getAnnotation(Column.class).length());
assertEquals(EnumType.STRING, Objects.requireNonNull(champ(Prestation.class,"signalement",TypeSignalement.class).getAnnotation(Enumerated.class), "Ajouter @Enumerated").value());
colonne(Prestation.class,"tarif",BigDecimal.class,"tarif",true);
relation(Prestation.class,"reservation",Reservation.class,"reservation_id");
var relation = champ(Prestation.class,"aide",AideDomicile.class);
assertNotNull(relation.getAnnotation(ManyToOne.class));
assertTrue(relation.getAnnotation(ManyToOne.class).optional());
assertNotNull(relation.getAnnotation(JoinColumn.class));
assertEquals("aide_id",relation.getAnnotation(JoinColumn.class).name());
assertTrue(relation.getAnnotation(JoinColumn.class).nullable());
repository(PrestationRepository.class,Prestation.class);
assertDoesNotThrow(() -> PrestationRepository.class.getMethod("findByReservationIdOrderByDateAsc", Long.class));
}
}
