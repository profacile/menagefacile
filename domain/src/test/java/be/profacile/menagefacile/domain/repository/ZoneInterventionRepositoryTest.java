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
class ZoneInterventionRepositoryTest {
 @Test void respecterLeDiagrammeJpa() {MenageContract.verifierZoneIntervention();}
 @Test void sauvegarderRelireEtRechercher() throws Exception {
  MenageContract.verifierZoneIntervention();
  MenageContract.enumerations();
  try(var base=new BaseJpa(MenageFixtures.ENTITES)) {
   var objets=MenageFixtures.creer();
   var repository=base.creerRepository(ZoneInterventionRepository.class);
   base.entityManager.getTransaction().begin();
   for(var entree:objets.entrySet()) {
    if(entree.getKey().equals("ZoneIntervention")) repository.saveAndFlush(entree.getValue());
    else base.entityManager.persist(entree.getValue());
   }
   base.entityManager.flush();
   Long id=(Long)lire(objets.get("ZoneIntervention"),"id");assertNotNull(id);
   base.entityManager.clear();
   Object relu=repository.findById(id).orElseThrow();
   assertEquals(lire(objets.get("ZoneIntervention"),"codePostal"),lire(relu,"codePostal"),"Valeur persistée de codePostal");
assertEquals(lire(objets.get("AideDomicile"),"id"),lire(lire(relu,"aide"),"id"));
   if(relu instanceof Utilisateur) {
    assertEquals(lire(objets.get("ZoneIntervention"),"email"),lire(relu,"email"));
    assertEquals(lire(objets.get("ZoneIntervention"),"role"),lire(relu,"role"));
   }
   Object resultat=rechercher(repository,"findByCodePostal",String.class,"1000");
   assertEquals(List.of(id), ((List<?>) resultat).stream().map(o -> assertDoesNotThrow(() -> lire(o,"id"))).toList());
   base.entityManager.getTransaction().commit();
  }
 }
}
