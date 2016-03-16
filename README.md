# rackfx
Programme de gestion de groupes musicaux

rackfx est une évolution de rack, le projet à implémenté plusieurs points:
- rackfx utilise à présent la library javaFx
- le projet utilise la library de composants custom JFXtras
- le projet utilise la library de composants custom controlsfx
- les entités sont à présent stockées dans une base de donnée gérée par MySQL
- le projet utilise le framework Hibernate pour sauver les entités en bdd
- le projet utilise le gestionnaire de projets Maven
- le champ de recherche est géré par Hibernate search
- le projet implémente plusieurs design pattern dont le modèle MVC et le singleton
- les contraintes des beans sont gérés par Hibernate Validator

Plusieurs points restent à faires:
- les tests de jUnit doivent être implémentés 
- la recherche d'entité avec hibernate search doit fonctionner avec un Analyzer
  qui prend en compte la recherche par approximation

</> anthony </>
