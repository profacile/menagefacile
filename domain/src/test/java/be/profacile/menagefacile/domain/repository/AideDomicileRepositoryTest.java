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
class AideDomicileRepositoryTest {
 @Test void respecterLeDiagrammeJpa() {MenageContract.verifierAideDomicile();}
 @Test void sauvegarderRelireEtRechercher() throws Exception {
  MenageContract.verifierAideDomicile();
  MenageContract.enumerations();
  try(var base=new BaseJpa(MenageFixtures.ENTITES)) {
   var objets=MenageFixtures.creer();
   var repository=base.creerRepository(AideDomicileRepository.class);
   base.entityManager.getTransaction().begin();
   for(var entree:objets.entrySet()) {
    if(entree.getKey().equals("AideDomicile")) repository.saveAndFlush(entree.getValue());
    else base.entityManager.persist(entree.getValue());
   }
   base.entityManager.flush();
   Long id=(Long)lire(objets.get("AideDomicile"),"id");assertNotNull(id);
   base.entityManager.clear();
   Object relu=repository.findById(id).orElseThrow();
   assertEquals(lire(objets.get("AideDomicile"),"nom"),lire(relu,"nom"),"Valeur persistée de nom");
assertEquals(lire(objets.get("AideDomicile"),"prenom"),lire(relu,"prenom"),"Valeur persistée de prenom");
assertEquals(lire(objets.get("AideDomicile"),"statutCompte"),lire(relu,"statutCompte"),"Valeur persistée de statutCompte");

   if(relu instanceof Utilisateur) {
    assertEquals(lire(objets.get("AideDomicile"),"email"),lire(relu,"email"));
    assertEquals(lire(objets.get("AideDomicile"),"role"),lire(relu,"role"));
   }
   Object resultat=rechercher(repository,"findByStatutCompte",StatutCompte.class,MenageFixtures.valeur(StatutCompte.class,"VALIDEE"));
   assertEquals(List.of(id), ((List<?>) resultat).stream().map(o -> assertDoesNotThrow(() -> lire(o,"id"))).toList());
   base.entityManager.getTransaction().commit();
  }
 }
}
