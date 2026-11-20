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
class RelationJpaTest {
 private Map<String,Object> persister(BaseJpa base) throws Exception {
  var objets=MenageFixtures.creer();base.entityManager.getTransaction().begin();
  for(Object objet:objets.values())base.entityManager.persist(objet);
  base.entityManager.flush();return objets;
 }
 @Test void relireLesServicesProposesParUneAide() throws Exception {
  MenageContract.verifierAideDomicile();
  try(var base=new BaseJpa(MenageFixtures.ENTITES)) {
   var objets=persister(base);
   Long id=(Long)lire(objets.get("AideDomicile"),"id"),serviceId=(Long)lire(objets.get("Service"),"id");
   base.entityManager.clear();
   Set<?> services=(Set<?>)lire(base.entityManager.find(AideDomicile.class,id),"services");
   assertEquals(1,services.size());assertEquals(serviceId,lire(services.iterator().next(),"id"));
   base.entityManager.getTransaction().commit();
  }
 }
 @Test void retirerLesEnfantsDuPlanningSansSupprimerLAide() throws Exception {
  MenageContract.verifierAideDomicile();
  try(var base=new BaseJpa(MenageFixtures.ENTITES)) {
   var objets=persister(base);Long id=(Long)lire(objets.get("AideDomicile"),"id");
   Long dispo=(Long)lire(objets.get("Disponibilite"),"id"),zone=(Long)lire(objets.get("ZoneIntervention"),"id"),
    indispo=(Long)lire(objets.get("Indisponibilite"),"id");
   base.entityManager.clear();
   Object aide=base.entityManager.find(AideDomicile.class,id);
   for(String collection:List.of("disponibilites","zonesIntervention","indisponibilites")) {
    List<?> enfants=(List<?>)lire(aide,collection);assertEquals(1,enfants.size());enfants.clear();
   }
   base.entityManager.flush();base.entityManager.clear();
   assertNull(base.entityManager.find(Disponibilite.class,dispo));assertNull(base.entityManager.find(ZoneIntervention.class,zone));
   assertNull(base.entityManager.find(Indisponibilite.class,indispo));assertNotNull(base.entityManager.find(AideDomicile.class,id));
   base.entityManager.getTransaction().commit();
  }
 }
 @Test void retirerUnePrestationOrphelineSansSupprimerLesReferentiels() throws Exception {
  MenageContract.verifierReservation();
  try(var base=new BaseJpa(MenageFixtures.ENTITES)) {
   var objets=persister(base);Long id=(Long)lire(objets.get("Reservation"),"id"),prestation=(Long)lire(objets.get("Prestation"),"id");
   Long client=(Long)lire(objets.get("Client"),"id"),service=(Long)lire(objets.get("Service"),"id");
   base.entityManager.clear();Object reservation=base.entityManager.find(Reservation.class,id);
   List<?> prestations=(List<?>)lire(reservation,"prestations");assertEquals(1,prestations.size());prestations.clear();
   base.entityManager.flush();base.entityManager.clear();
   assertNull(base.entityManager.find(Prestation.class,prestation));assertNotNull(base.entityManager.find(Reservation.class,id));
   assertNotNull(base.entityManager.find(Client.class,client));assertNotNull(base.entityManager.find(Service.class,service));
   base.entityManager.getTransaction().commit();
  }
 }
 @Test void affecterUneAideApresUneDemandeSansAide() throws Exception {
  MenageContract.verifierPrestation();
  try(var base=new BaseJpa(MenageFixtures.ENTITES)) {
   var objets=persister(base);Long id=(Long)lire(objets.get("Prestation"),"id"),aideId=(Long)lire(objets.get("AideDomicile"),"id");
   base.entityManager.clear();Object prestation=base.entityManager.find(Prestation.class,id);
   assertNull(lire(prestation,"aide"));
   prestation.getClass().getMethod("setAide",AideDomicile.class).invoke(prestation,base.entityManager.find(AideDomicile.class,aideId));
   base.entityManager.flush();base.entityManager.clear();
   assertEquals(aideId,lire(lire(base.entityManager.find(Prestation.class,id),"aide"),"id"));
   base.entityManager.getTransaction().commit();
  }
 }
}
