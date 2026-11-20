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
class ReservationRepositoryTest {
 @Test void respecterLeDiagrammeJpa() {MenageContract.verifierReservation();}
 @Test void sauvegarderRelireEtRechercher() throws Exception {
  MenageContract.verifierReservation();
  MenageContract.enumerations();
  try(var base=new BaseJpa(MenageFixtures.ENTITES)) {
   var objets=MenageFixtures.creer();
   var repository=base.creerRepository(ReservationRepository.class);
   base.entityManager.getTransaction().begin();
   for(var entree:objets.entrySet()) {
    if(entree.getKey().equals("Reservation")) repository.saveAndFlush(entree.getValue());
    else base.entityManager.persist(entree.getValue());
   }
   base.entityManager.flush();
   Long id=(Long)lire(objets.get("Reservation"),"id");assertNotNull(id);
   base.entityManager.clear();
   Object relu=repository.findById(id).orElseThrow();
   assertEquals(lire(objets.get("Reservation"),"frequence"),lire(relu,"frequence"),"Valeur persistée de frequence");
assertEquals(lire(objets.get("Reservation"),"dateDebut"),lire(relu,"dateDebut"),"Valeur persistée de dateDebut");
assertEquals(lire(objets.get("Reservation"),"creneau"),lire(relu,"creneau"),"Valeur persistée de creneau");
assertEquals(lire(objets.get("Reservation"),"dureeHeures"),lire(relu,"dureeHeures"),"Valeur persistée de dureeHeures");
assertEquals(lire(objets.get("Reservation"),"memeAide"),lire(relu,"memeAide"),"Valeur persistée de memeAide");
assertEquals(lire(objets.get("Reservation"),"remarques"),lire(relu,"remarques"),"Valeur persistée de remarques");
assertEquals(lire(objets.get("Client"),"id"),lire(lire(relu,"client"),"id"));
assertEquals(lire(objets.get("Service"),"id"),lire(lire(relu,"service"),"id"));
assertEquals(lire(objets.get("Adresse"),"id"),lire(lire(relu,"adresse"),"id"));
   if(relu instanceof Utilisateur) {
    assertEquals(lire(objets.get("Reservation"),"email"),lire(relu,"email"));
    assertEquals(lire(objets.get("Reservation"),"role"),lire(relu,"role"));
   }
   Object resultat=rechercher(repository,"findByClientId",Long.class,lire(objets.get("Client"),"id"));
   assertEquals(List.of(id), ((List<?>) resultat).stream().map(o -> assertDoesNotThrow(() -> lire(o,"id"))).toList());
   base.entityManager.getTransaction().commit();
  }
 }
}
