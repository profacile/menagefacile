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
class ServiceRepositoryTest {
 @Test void respecterLeDiagrammeJpa() {MenageContract.verifierService();}
 @Test void sauvegarderRelireEtRechercher() throws Exception {
  MenageContract.verifierService();
  MenageContract.enumerations();
  try(var base=new BaseJpa(MenageFixtures.ENTITES)) {
   var objets=MenageFixtures.creer();
   var repository=base.creerRepository(ServiceRepository.class);
   base.entityManager.getTransaction().begin();
   for(var entree:objets.entrySet()) {
    if(entree.getKey().equals("Service")) repository.saveAndFlush(entree.getValue());
    else base.entityManager.persist(entree.getValue());
   }
   base.entityManager.flush();
   Long id=(Long)lire(objets.get("Service"),"id");assertNotNull(id);
   base.entityManager.clear();
   Object relu=repository.findById(id).orElseThrow();
   assertEquals(lire(objets.get("Service"),"libelle"),lire(relu,"libelle"),"Valeur persistée de libelle");
assertEquals(lire(objets.get("Service"),"tarifIndicatif"),lire(relu,"tarifIndicatif"),"Valeur persistée de tarifIndicatif");
assertEquals(lire(objets.get("Service"),"dureeMinHeures"),lire(relu,"dureeMinHeures"),"Valeur persistée de dureeMinHeures");

   if(relu instanceof Utilisateur) {
    assertEquals(lire(objets.get("Service"),"email"),lire(relu,"email"));
    assertEquals(lire(objets.get("Service"),"role"),lire(relu,"role"));
   }
   Object resultat=rechercher(repository,"findByLibelleIgnoreCase",String.class,"Menage");
   assertEquals(id,lire(((Optional<?>) resultat).orElseThrow(),"id"));
   base.entityManager.getTransaction().commit();
  }
 }
}
