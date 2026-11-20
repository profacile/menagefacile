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
class ClientRepositoryTest {
 @Test void respecterLeDiagrammeJpa() {MenageContract.verifierClient();}
 @Test void sauvegarderRelireEtRechercher() throws Exception {
  MenageContract.verifierClient();
  MenageContract.enumerations();
  try(var base=new BaseJpa(MenageFixtures.ENTITES)) {
   var objets=MenageFixtures.creer();
   var repository=base.creerRepository(ClientRepository.class);
   base.entityManager.getTransaction().begin();
   for(var entree:objets.entrySet()) {
    if(entree.getKey().equals("Client")) repository.saveAndFlush(entree.getValue());
    else base.entityManager.persist(entree.getValue());
   }
   base.entityManager.flush();
   Long id=(Long)lire(objets.get("Client"),"id");assertNotNull(id);
   base.entityManager.clear();
   Object relu=repository.findById(id).orElseThrow();
   assertEquals(lire(objets.get("Client"),"nom"),lire(relu,"nom"),"Valeur persistée de nom");
assertEquals(lire(objets.get("Client"),"prenom"),lire(relu,"prenom"),"Valeur persistée de prenom");

   if(relu instanceof Utilisateur) {
    assertEquals(lire(objets.get("Client"),"email"),lire(relu,"email"));
    assertEquals(lire(objets.get("Client"),"role"),lire(relu,"role"));
   }
   Object resultat=rechercher(repository,"findByEmail",String.class,"client@example.test");
   assertEquals(id,lire(((Optional<?>) resultat).orElseThrow(),"id"));
   base.entityManager.getTransaction().commit();
  }
 }
}
