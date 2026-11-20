# MénageFacile — PROFACILE SRL

Backend pédagogique pour les services de ménage, repassage et aide à domicile présentés sur profacile.be.
Java 25, Spring Boot 4.1.2, Maven Wrapper 3.9.16.
Le projet historique dans le dossier voisin code est conservé.

## Exercice
Entités et enums vides dans domain/model ; interfaces vides dans domain/repository.
Services réservés au sprint suivant. DTO request/response séparés dans rest.
Tests JPA déjà implémentés, Hibernate/H2 et données fictives.
[Guide : champs, relations, répartition et étapes](docs/sprint-2.md).

Installer JDK 25 et définir JAVA_HOME, puis :
- .\mvnw.cmd -DskipTests package
- .\mvnw.cmd -pl bootstrap -am test -Dtest=MenageFacileApplicationTest -Dsurefire.failIfNoSpecifiedTests=false
- .\mvnw.cmd -pl domain test
- .\mvnw.cmd verify
Sous Linux, utiliser sh mvnw.

Le squelette compile et démarre sans serveur HTTP ni base de production.
Les 26 tests du domaine sont volontairement rouges jusqu'à complétion des exercices.
Aucun chiffrement réel, paiement, pointage opérationnel ou contrôle d'accès n'est livré à ce stade.
Les données des tests ne doivent contenir aucun véritable code d'accès à un domicile.

main et develop ont la même base initiale. Branches de ticket depuis develop et PR vers develop.
Les workflows séparent le socle technique des exercices. CODEOWNERS réserve les tests/configurations
à la revue du responsable ; protections bloquantes à configurer séparément selon l'offre GitHub.
Invitations GitHub traitées séparément.
