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
class UtilisateurRepositoryTest {
 @Test void respecterLeDiagrammeJpa() {MenageContract.verifierUtilisateur();}
 @Test void sauvegarderRelireEtRechercher() throws Exception {
  MenageContract.verifierUtilisateur();
  MenageContract.enumerations();
  try(var base=new BaseJpa(MenageFixtures.ENTITES)) {
   var objets=MenageFixtures.creer();
   var repository=base.creerRepository(UtilisateurRepository.class);
   base.entityManager.getTransaction().begin();
   for(var entree:objets.entrySet()) {
    if(entree.getKey().equals("Client")) repository.saveAndFlush(entree.getValue());
    else base.entityManager.persist(entree.getValue());
   }
   base.entityManager.flush();
   Long id=(Long)lire(objets.get("Client"),"id");assertNotNull(id);
   base.entityManager.clear();
   Object relu=repository.findById(id).orElseThrow();
   assertEquals(lire(objets.get("Client"),"email"),lire(relu,"email"),"Valeur persistée de email");
assertEquals(lire(objets.get("Client"),"motDePasse"),lire(relu,"motDePasse"),"Valeur persistée de motDePasse");
assertEquals(lire(objets.get("Client"),"telephone"),lire(relu,"telephone"),"Valeur persistée de telephone");
assertEquals(lire(objets.get("Client"),"role"),lire(relu,"role"),"Valeur persistée de role");

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
