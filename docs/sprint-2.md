# MénageFacile — Sprint 2 : modèle JPA et repositories

Backend pédagogique du service de ménage et aide à domicile présenté sur profacile.be.
Référence : diagrammes de classes, ERD et scénarios fournis le 20 novembre 2026.

## Répartition
- Belviane Kamtcha : Utilisateur, Client, Administrateur, Adresse, Reservation, Prestation et repositories.
- Audrey Toukam NGAMENI : Service, AideDomicile, Disponibilite, ZoneIntervention, Indisponibilite et repositories.
Chaque lot est découpé en deux tickets.

## Correspondance UML et ERD
Utilisateur est abstraite @Entity, @Table(name="utilisateur") et @Inheritance(strategy=JOINED).
Client, AideDomicile et Administrateur héritent d'Utilisateur, avec @PrimaryKeyJoinColumn(name="utilisateur_id").
Ne pas dupliquer id/email/motDePasse/telephone/role dans les sous-classes.
L'ERD ne dessine pas de table administrateur : table technique administrateur retenue pour préserver la spécialisation UML.
RoleUtilisateur est une enum technique pour la colonne role de l'ERD : CLIENT, AIDE_DOMICILE, ADMINISTRATEUR.
Le champ Java consignesAcces correspond à consignes_acces_chiffrees. Le test conserve une chaîne chiffrée fictive ;
il ne fournit aucun chiffrement réel et ne doit recevoir aucune adresse ou instruction d'accès réelle.
Le nom technique Service désigne une entité JPA ; ne pas confondre avec l'annotation Spring @Service.
BIMENSUELLE désigne ici toutes les deux semaines selon UC1.
Les heures du planning restent LocalTime/LocalDateTime selon les diagrammes ; le fuseau opérationnel reste à définir.
Tarif de Prestation est V2, facultatif, sans calcul de facture ou paiement.

## Contrat commun
Chaque racine d'entité : id Long privé, @Id et @GeneratedValue(strategy=IDENTITY), @Entity et @Table.
Les sous-classes héritent de l'identifiant. Constructeur sans argument et getters/setters publics.
Pour les booleans, ce contrat utilise getAnimaux/getMemeAide/getAnnulationTardive et leurs setters.
@Column avec nom, nullabilité et longueur ci-dessous. Enums @Enumerated(STRING).
BigDecimal : précision 10, échelle 2. Strings TEXT : @Column(columnDefinition="TEXT").
MotDePasse contient un hash fictif dans les tests, jamais un mot de passe réel.
La nullabilité n'est pas détaillée par les schémas : téléphone, consignesAcces, motif d'indisponibilité,
remarques, pointages réels, évaluation, signalement et tarif V2 sont facultatifs dans ce contrat explicite.
Prestation.aide est facultative : une prestation demandée peut ne pas être affectée.
Les autres champs/relations sont obligatoires.

## Dictionnaire et recherches

### Utilisateur — table utilisateur

| Champ | Type | Colonne SQL | Facultatif | Longueur | Unique |
|---|---|---|---|---|---|
| email | String | email | false | 150 | true |
| motDePasse | String | mot_de_passe_hash | false | 255 | false |
| telephone | String | telephone | true | 20 | false |
| role | RoleUtilisateur | role | false | 20 | false |

UtilisateurRepository extends JpaRepository<Utilisateur, Long> ; recherche : Optional<Utilisateur> findByEmail(String value).

### Client — table client

| Champ | Type | Colonne SQL | Facultatif | Longueur | Unique |
|---|---|---|---|---|---|
| nom | String | nom | false | 80 | false |
| prenom | String | prenom | false | 80 | false |

ClientRepository extends JpaRepository<Client, Long> ; recherche : Optional<Client> findByEmail(String value).

### Administrateur — table administrateur

| Champ | Type | Colonne SQL | Facultatif | Longueur | Unique |
|---|---|---|---|---|---|

AdministrateurRepository extends JpaRepository<Administrateur, Long> ; recherche : Optional<Administrateur> findByEmail(String value).

### Adresse — table adresse

| Champ | Type | Colonne SQL | Facultatif | Longueur | Unique |
|---|---|---|---|---|---|
| rue | String | rue | false | 150 | false |
| codePostal | String | code_postal | false | 4 | false |
| ville | String | ville | false | 80 | false |
| typeLogement | TypeLogement | type_logement | false | 20 | false |
| surfaceM2 | int | surface_m2 | false | — | false |
| nbPieces | int | nb_pieces | false | — | false |
| animaux | boolean | animaux | false | — | false |
| consignesAcces | String | consignes_acces_chiffrees | true | TEXT | false |

Relation client : @ManyToOne(optional=false) vers Client, @JoinColumn(name="client_id",nullable=false).

AdresseRepository extends JpaRepository<Adresse, Long> ; recherche : List<Adresse> findByClientId(Long value).

### Service — table service

| Champ | Type | Colonne SQL | Facultatif | Longueur | Unique |
|---|---|---|---|---|---|
| libelle | String | libelle | false | 100 | true |
| tarifIndicatif | BigDecimal | tarif_indicatif | false | — | false |
| dureeMinHeures | int | duree_min_heures | false | — | false |

ServiceRepository extends JpaRepository<Service, Long> ; recherche : Optional<Service> findByLibelleIgnoreCase(String value).

### AideDomicile — table aide_domicile

| Champ | Type | Colonne SQL | Facultatif | Longueur | Unique |
|---|---|---|---|---|---|
| nom | String | nom | false | 80 | false |
| prenom | String | prenom | false | 80 | false |
| statutCompte | StatutCompte | statut_compte | false | 20 | false |

AideDomicileRepository extends JpaRepository<AideDomicile, Long> ; recherche : List<AideDomicile> findByStatutCompte(StatutCompte value).

### Disponibilite — table disponibilite

| Champ | Type | Colonne SQL | Facultatif | Longueur | Unique |
|---|---|---|---|---|---|
| jour | JourSemaine | jour | false | 10 | false |
| heureDebut | LocalTime | heure_debut | false | — | false |
| heureFin | LocalTime | heure_fin | false | — | false |

Relation aide : @ManyToOne(optional=false) vers AideDomicile, @JoinColumn(name="aide_id",nullable=false).

DisponibiliteRepository extends JpaRepository<Disponibilite, Long> ; recherche : List<Disponibilite> findByAideId(Long value).

### ZoneIntervention — table zone_intervention

| Champ | Type | Colonne SQL | Facultatif | Longueur | Unique |
|---|---|---|---|---|---|
| codePostal | String | code_postal | false | 4 | false |

Relation aide : @ManyToOne(optional=false) vers AideDomicile, @JoinColumn(name="aide_id",nullable=false).

ZoneInterventionRepository extends JpaRepository<ZoneIntervention, Long> ; recherche : List<ZoneIntervention> findByCodePostal(String value).

### Indisponibilite — table indisponibilite

| Champ | Type | Colonne SQL | Facultatif | Longueur | Unique |
|---|---|---|---|---|---|
| dateDebut | LocalDate | date_debut | false | — | false |
| dateFin | LocalDate | date_fin | false | — | false |
| motif | String | motif | true | 150 | false |

Relation aide : @ManyToOne(optional=false) vers AideDomicile, @JoinColumn(name="aide_id",nullable=false).

IndisponibiliteRepository extends JpaRepository<Indisponibilite, Long> ; recherche : List<Indisponibilite> findByAideId(Long value).

### Reservation — table reservation

| Champ | Type | Colonne SQL | Facultatif | Longueur | Unique |
|---|---|---|---|---|---|
| frequence | Frequence | frequence | false | 20 | false |
| dateDebut | LocalDate | date_debut | false | — | false |
| creneau | LocalTime | creneau | false | — | false |
| dureeHeures | int | duree_heures | false | — | false |
| memeAide | boolean | meme_aide | false | — | false |
| remarques | String | remarques | true | TEXT | false |

Relation client : @ManyToOne(optional=false) vers Client, @JoinColumn(name="client_id",nullable=false).

Relation service : @ManyToOne(optional=false) vers Service, @JoinColumn(name="service_id",nullable=false).

Relation adresse : @ManyToOne(optional=false) vers Adresse, @JoinColumn(name="adresse_id",nullable=false).

ReservationRepository extends JpaRepository<Reservation, Long> ; recherche : List<Reservation> findByClientId(Long value).

### Prestation — table prestation

| Champ | Type | Colonne SQL | Facultatif | Longueur | Unique |
|---|---|---|---|---|---|
| date | LocalDate | date | false | — | false |
| heureDebutPrevue | LocalTime | heure_debut_prevue | false | — | false |
| dureeHeures | int | duree_heures | false | — | false |
| statut | StatutPrestation | statut | false | 20 | false |
| heureDebutReelle | LocalDateTime | heure_debut_reelle | true | — | false |
| heureFinReelle | LocalDateTime | heure_fin_reelle | true | — | false |
| annulationTardive | boolean | annulation_tardive | false | — | false |
| noteEvaluation | Integer | note_evaluation | true | — | false |
| commentaireEvaluation | String | commentaire_evaluation | true | TEXT | false |
| signalement | TypeSignalement | signalement | true | 30 | false |
| tarif | BigDecimal | tarif | true | — | false |

Relation reservation : @ManyToOne(optional=false) vers Reservation, @JoinColumn(name="reservation_id",nullable=false).

Relation aide : @ManyToOne(optional=true) vers AideDomicile, @JoinColumn(name="aide_id",nullable=true).

PrestationRepository extends JpaRepository<Prestation, Long> ; recherche : List<Prestation> findByReservationIdOrderByDateAsc(Long value).

## Énumérations

- StatutPrestation : DEMANDEE, PLANIFIEE, EN_COURS, REALISEE, ANNULEE.
- Frequence : UNE_FOIS, HEBDOMADAIRE, BIMENSUELLE.
- StatutCompte : EN_ATTENTE, VALIDEE, INDISPONIBLE, SUSPENDUE, REFUSEE.
- TypeLogement : APPARTEMENT, MAISON.
- TypeSignalement : CLIENT_ABSENT, MATERIEL_MANQUANT, RETARD.
- JourSemaine : LUNDI, MARDI, MERCREDI, JEUDI, VENDREDI, SAMEDI, DIMANCHE.
- RoleUtilisateur : CLIENT, AIDE_DOMICILE, ADMINISTRATEUR.

## Collections et composition
- Client.adresses : List<Adresse> initialisée, @OneToMany(mappedBy="client",cascade=ALL,orphanRemoval=true).
- AideDomicile.services : Set<Service> initialisé, @ManyToMany avec table aide_service,
  joinColumns=@JoinColumn(name="aide_id"), inverseJoinColumns=@JoinColumn(name="service_id").
  Ne pas cascader la suppression vers le catalogue partagé.
- AideDomicile.disponibilites, zonesIntervention, indisponibilites : List<type> initialisées,
  @OneToMany(mappedBy="aide",cascade=ALL,orphanRemoval=true).
- Reservation.prestations : List<Prestation> initialisée,
  @OneToMany(mappedBy="reservation",cascade=ALL,orphanRemoval=true).
Les enfants sont propriétaires de leurs clés étrangères.
La suppression d'une adresse déjà utilisée par une réservation n'est pas un parcours métier proposé :
il faudra définir la conservation d'historique/archivage avant un tel écran.

## Ordre du travail
1. Belviane prépare Utilisateur/Client/Administrateur/Adresse ; Audrey prépare Service.
2. Intégrer la racine Utilisateur dans develop pour qu'Audrey termine AideDomicile et son planning.
3. Belviane peut préparer Reservation après Client/Adresse/Service, puis Prestation après intégration du profil AideDomicile.
4. Chacun crée une branche de ticket depuis develop ; PR vers develop avec clé Jira, résumé et résultats de tests.
5. Les tests de structure sont autonomes. La persistance utilise tout le graphe et nécessite l'intégration des deux lots.
   Ne pas implémenter le lot de l'autre étudiant pour contourner la dépendance.
6. Play dans l'IDE sur les RepositoryTest et RelationJpaTest, ou .\mvnw.cmd -pl domain test.
   Sous Linux : sh mvnw -pl domain test. Validation finale : .\mvnw.cmd verify.
7. Attendu après intégration : 26 tests du domaine et un test de démarrage réussis.
   Ne pas modifier ni désactiver les tests fournis pour masquer une erreur.

Les tests fournis compilent sur les classes vides par réflexion et commencent volontairement en échec.
Ils vérifient les mappings, les valeurs persistées, les requêtes Spring Data et la relecture Hibernate/H2.
RelationJpaTest vérifie les services de l'aide, les enfants orphelins du planning, les prestations d'une réservation
et l'affectation différée d'une aide. H2 sert aux tests ; migrations et validation PostgreSQL viendront ensuite.

## Règles métier du futur sprint services
Réserver au minimum deux heures et respecter la durée du service. Générer une prestation ponctuelle
ou les occurrences des trois prochains mois. Donner priorité à la même aide si demandé.
Affecter uniquement une aide validée, disponible, couvrant le code postal et proposant le service ;
éviter les chevauchements en tenant compte du trajet. Après 24h sans acceptation, alerter l'administration.
Pointages uniquement pour l'aide affectée et le jour prévu ; consignes d'accès masquées dans les autres cas.
Annulation tardive à moins de 48h. Évaluation de 1 à 5 après réalisation. Indisponibilité : proposer un remplacement.
Ces traitements, chiffrement, autorisations, emails, application mobile et mode hors connexion restent à implémenter
dans de futurs sprints. Les noms genererPrestations/pointerDebut/pointerFin de l'UML ne sont pas à coder ici.
Services et DTO request/response restent vides pour la suite. Données fictives uniquement.
