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
class IndisponibiliteRepositoryTest {
 @Test void respecterLeDiagrammeJpa() {MenageContract.verifierIndisponibilite();}
 @Test void sauvegarderRelireEtRechercher() throws Exception {
  MenageContract.verifierIndisponibilite();
  MenageContract.enumerations();
  try(var base=new BaseJpa(MenageFixtures.ENTITES)) {
   var objets=MenageFixtures.creer();
   var repository=base.creerRepository(IndisponibiliteRepository.class);
   base.entityManager.getTransaction().begin();
   for(var entree:objets.entrySet()) {
    if(entree.getKey().equals("Indisponibilite")) repository.saveAndFlush(entree.getValue());
    else base.entityManager.persist(entree.getValue());
   }
   base.entityManager.flush();
   Long id=(Long)lire(objets.get("Indisponibilite"),"id");assertNotNull(id);
   base.entityManager.clear();
   Object relu=repository.findById(id).orElseThrow();
   assertEquals(lire(objets.get("Indisponibilite"),"dateDebut"),lire(relu,"dateDebut"),"Valeur persistée de dateDebut");
assertEquals(lire(objets.get("Indisponibilite"),"dateFin"),lire(relu,"dateFin"),"Valeur persistée de dateFin");
assertEquals(lire(objets.get("Indisponibilite"),"motif"),lire(relu,"motif"),"Valeur persistée de motif");
assertEquals(lire(objets.get("AideDomicile"),"id"),lire(lire(relu,"aide"),"id"));
   if(relu instanceof Utilisateur) {
    assertEquals(lire(objets.get("Indisponibilite"),"email"),lire(relu,"email"));
    assertEquals(lire(objets.get("Indisponibilite"),"role"),lire(relu,"role"));
   }
   Object resultat=rechercher(repository,"findByAideId",Long.class,lire(objets.get("AideDomicile"),"id"));
   assertEquals(List.of(id), ((List<?>) resultat).stream().map(o -> assertDoesNotThrow(() -> lire(o,"id"))).toList());
   base.entityManager.getTransaction().commit();
  }
 }
}
