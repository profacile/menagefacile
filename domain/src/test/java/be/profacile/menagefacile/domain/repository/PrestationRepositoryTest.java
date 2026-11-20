package be.profacile.menagefacile.domain.repository;
import be.profacile.menagefacile.domain.model.*;
import be.profacile.menagefacile.domain.repository.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static be.profacile.menagefacile.domain.support.JpaExerciseSupport.*;

import be.profacile.menagefacile.domain.support.*;
import org.junit.jupiter.api.Test;
class PrestationRepositoryTest {
 @Test void respecterLeDiagrammeJpa() {MenageContract.verifierPrestation();}
 @Test void sauvegarderRelireEtRechercher() throws Exception {
  MenageContract.verifierPrestation();
  MenageContract.enumerations();
  try(var base=new BaseJpa(MenageFixtures.ENTITES)) {
   var objets=MenageFixtures.creer();
   var repository=base.creerRepository(PrestationRepository.class);
   base.entityManager.getTransaction().begin();
   for(var entree:objets.entrySet()) {
    if(entree.getKey().equals("Prestation")) repository.saveAndFlush(entree.getValue());
    else base.entityManager.persist(entree.getValue());
   }
   base.entityManager.flush();
   Long id=(Long)lire(objets.get("Prestation"),"id");assertNotNull(id);
   base.entityManager.clear();
   Object relu=repository.findById(id).orElseThrow();
   assertEquals(lire(objets.get("Prestation"),"date"),lire(relu,"date"),"Valeur persistée de date");
assertEquals(lire(objets.get("Prestation"),"heureDebutPrevue"),lire(relu,"heureDebutPrevue"),"Valeur persistée de heureDebutPrevue");
assertEquals(lire(objets.get("Prestation"),"dureeHeures"),lire(relu,"dureeHeures"),"Valeur persistée de dureeHeures");
assertEquals(lire(objets.get("Prestation"),"statut"),lire(relu,"statut"),"Valeur persistée de statut");
assertEquals(lire(objets.get("Prestation"),"heureDebutReelle"),lire(relu,"heureDebutReelle"),"Valeur persistée de heureDebutReelle");
assertEquals(lire(objets.get("Prestation"),"heureFinReelle"),lire(relu,"heureFinReelle"),"Valeur persistée de heureFinReelle");
assertEquals(lire(objets.get("Prestation"),"annulationTardive"),lire(relu,"annulationTardive"),"Valeur persistée de annulationTardive");
assertEquals(lire(objets.get("Prestation"),"noteEvaluation"),lire(relu,"noteEvaluation"),"Valeur persistée de noteEvaluation");
assertEquals(lire(objets.get("Prestation"),"commentaireEvaluation"),lire(relu,"commentaireEvaluation"),"Valeur persistée de commentaireEvaluation");
assertEquals(lire(objets.get("Prestation"),"signalement"),lire(relu,"signalement"),"Valeur persistée de signalement");
assertEquals(lire(objets.get("Prestation"),"tarif"),lire(relu,"tarif"),"Valeur persistée de tarif");
assertEquals(lire(objets.get("Reservation"),"id"),lire(lire(relu,"reservation"),"id"));
assertNull(lire(relu,"aide"));
   if(relu instanceof Utilisateur) {
    assertEquals(lire(objets.get("Prestation"),"email"),lire(relu,"email"));
    assertEquals(lire(objets.get("Prestation"),"role"),lire(relu,"role"));
   }
   Object resultat=rechercher(repository,"findByReservationIdOrderByDateAsc",Long.class,lire(objets.get("Reservation"),"id"));
   assertEquals(List.of(id), ((List<?>) resultat).stream().map(o -> assertDoesNotThrow(() -> lire(o,"id"))).toList());
   base.entityManager.getTransaction().commit();
  }
 }
}
