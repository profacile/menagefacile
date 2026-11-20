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
class AdresseRepositoryTest {
 @Test void respecterLeDiagrammeJpa() {MenageContract.verifierAdresse();}
 @Test void sauvegarderRelireEtRechercher() throws Exception {
  MenageContract.verifierAdresse();
  MenageContract.enumerations();
  try(var base=new BaseJpa(MenageFixtures.ENTITES)) {
   var objets=MenageFixtures.creer();
   var repository=base.creerRepository(AdresseRepository.class);
   base.entityManager.getTransaction().begin();
   for(var entree:objets.entrySet()) {
    if(entree.getKey().equals("Adresse")) repository.saveAndFlush(entree.getValue());
    else base.entityManager.persist(entree.getValue());
   }
   base.entityManager.flush();
   Long id=(Long)lire(objets.get("Adresse"),"id");assertNotNull(id);
   base.entityManager.clear();
   Object relu=repository.findById(id).orElseThrow();
   assertEquals(lire(objets.get("Adresse"),"rue"),lire(relu,"rue"),"Valeur persistée de rue");
assertEquals(lire(objets.get("Adresse"),"codePostal"),lire(relu,"codePostal"),"Valeur persistée de codePostal");
assertEquals(lire(objets.get("Adresse"),"ville"),lire(relu,"ville"),"Valeur persistée de ville");
assertEquals(lire(objets.get("Adresse"),"typeLogement"),lire(relu,"typeLogement"),"Valeur persistée de typeLogement");
assertEquals(lire(objets.get("Adresse"),"surfaceM2"),lire(relu,"surfaceM2"),"Valeur persistée de surfaceM2");
assertEquals(lire(objets.get("Adresse"),"nbPieces"),lire(relu,"nbPieces"),"Valeur persistée de nbPieces");
assertEquals(lire(objets.get("Adresse"),"animaux"),lire(relu,"animaux"),"Valeur persistée de animaux");
assertEquals(lire(objets.get("Adresse"),"consignesAcces"),lire(relu,"consignesAcces"),"Valeur persistée de consignesAcces");
assertEquals(lire(objets.get("Client"),"id"),lire(lire(relu,"client"),"id"));
   if(relu instanceof Utilisateur) {
    assertEquals(lire(objets.get("Adresse"),"email"),lire(relu,"email"));
    assertEquals(lire(objets.get("Adresse"),"role"),lire(relu,"role"));
   }
   Object resultat=rechercher(repository,"findByClientId",Long.class,lire(objets.get("Client"),"id"));
   assertEquals(List.of(id), ((List<?>) resultat).stream().map(o -> assertDoesNotThrow(() -> lire(o,"id"))).toList());
   base.entityManager.getTransaction().commit();
  }
 }
}
