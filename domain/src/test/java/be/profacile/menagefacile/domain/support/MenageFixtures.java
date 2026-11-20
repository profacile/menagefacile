package be.profacile.menagefacile.domain.support;
import be.profacile.menagefacile.domain.model.*;
import be.profacile.menagefacile.domain.repository.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static be.profacile.menagefacile.domain.support.JpaExerciseSupport.*;

public final class MenageFixtures {
 private MenageFixtures() {}
 public static final Class<?>[] ENTITES={Utilisateur.class,Client.class,Administrateur.class,Adresse.class,Service.class,AideDomicile.class,Disponibilite.class,ZoneIntervention.class,Indisponibilite.class,Reservation.class,Prestation.class};
 public static Object valeur(Class<?> type,String valeur) {
  return Arrays.stream(type.getEnumConstants()).filter(v -> ((Enum<?>)v).name().equals(valeur)).findFirst()
   .orElseThrow(() -> new AssertionError("Ajouter "+valeur+" dans "+type.getSimpleName()));
 }
 public static Map<String,Object> creer() throws Exception {
  Map<String,Object> objets=new LinkedHashMap<>();
 objets.put("Client",nouveau(Client.class,Map.ofEntries(Map.entry("email","client@example.test"),
Map.entry("motDePasse","hash-fictif"),
Map.entry("telephone","000000000"),
Map.entry("role",valeur(RoleUtilisateur.class,"CLIENT")),
Map.entry("nom","Exemple"),
Map.entry("prenom","Client"))));
objets.put("Administrateur",nouveau(Administrateur.class,Map.ofEntries(Map.entry("email","admin@example.test"),
Map.entry("motDePasse","hash-fictif"),
Map.entry("telephone","000000000"),
Map.entry("role",valeur(RoleUtilisateur.class,"ADMINISTRATEUR")))));
objets.put("Adresse",nouveau(Adresse.class,Map.ofEntries(Map.entry("rue","Rue-fictive"),
Map.entry("codePostal","1000"),
Map.entry("ville","Ville-fictive"),
Map.entry("typeLogement",valeur(TypeLogement.class,"APPARTEMENT")),
Map.entry("surfaceM2",80),
Map.entry("nbPieces",4),
Map.entry("animaux",false),
Map.entry("consignesAcces","chiffre-fictif"),
Map.entry("client",objets.get("Client")))));
objets.put("Service",nouveau(Service.class,Map.ofEntries(Map.entry("libelle","Menage"),
Map.entry("tarifIndicatif",new BigDecimal("25.00")),
Map.entry("dureeMinHeures",2))));
objets.put("AideDomicile",nouveau(AideDomicile.class,Map.ofEntries(Map.entry("email","aide@example.test"),
Map.entry("motDePasse","hash-fictif"),
Map.entry("telephone","000000000"),
Map.entry("role",valeur(RoleUtilisateur.class,"AIDE_DOMICILE")),
Map.entry("nom","Exemple"),
Map.entry("prenom","Aide"),
Map.entry("statutCompte",valeur(StatutCompte.class,"VALIDEE")),
Map.entry("services",new HashSet<>(Set.of(objets.get("Service")))))));
objets.put("Disponibilite",nouveau(Disponibilite.class,Map.ofEntries(Map.entry("jour",valeur(JourSemaine.class,"LUNDI")),
Map.entry("heureDebut",LocalTime.parse("09:00")),
Map.entry("heureFin",LocalTime.parse("17:00")),
Map.entry("aide",objets.get("AideDomicile")))));
objets.put("ZoneIntervention",nouveau(ZoneIntervention.class,Map.ofEntries(Map.entry("codePostal","1000"),
Map.entry("aide",objets.get("AideDomicile")))));
objets.put("Indisponibilite",nouveau(Indisponibilite.class,Map.ofEntries(Map.entry("dateDebut",LocalDate.parse("2026-12-01")),
Map.entry("dateFin",LocalDate.parse("2026-12-03")),
Map.entry("motif","Conge-fictif"),
Map.entry("aide",objets.get("AideDomicile")))));
objets.put("Reservation",nouveau(Reservation.class,Map.ofEntries(Map.entry("frequence",valeur(Frequence.class,"UNE_FOIS")),
Map.entry("dateDebut",LocalDate.parse("2026-11-23")),
Map.entry("creneau",LocalTime.parse("09:00")),
Map.entry("dureeHeures",2),
Map.entry("memeAide",true),
Map.entry("remarques","Remarque-fictive"),
Map.entry("client",objets.get("Client")),
Map.entry("service",objets.get("Service")),
Map.entry("adresse",objets.get("Adresse")))));
objets.put("Prestation",nouveau(Prestation.class,Map.ofEntries(Map.entry("date",LocalDate.parse("2026-11-23")),
Map.entry("heureDebutPrevue",LocalTime.parse("09:00")),
Map.entry("dureeHeures",2),
Map.entry("statut",valeur(StatutPrestation.class,"DEMANDEE")),
Map.entry("heureDebutReelle",LocalDateTime.of(2026,11,20,9,0)),
Map.entry("heureFinReelle",LocalDateTime.of(2026,11,20,9,0)),
Map.entry("annulationTardive",false),
Map.entry("noteEvaluation",4),
Map.entry("commentaireEvaluation","Commentaire-fictif"),
Map.entry("signalement",valeur(TypeSignalement.class,"RETARD")),
Map.entry("tarif",new BigDecimal("25.00")),
Map.entry("reservation",objets.get("Reservation")))));
 return objets;
 }
}
