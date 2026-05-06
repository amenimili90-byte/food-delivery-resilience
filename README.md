 Food Delivery Resilience System
Ce projet est une démonstration technique de l'implémentation de modèles de résilience (Circuit Breaker et Bulkhead) au sein d'une architecture microservices utilisant Spring Boot et Resilience4j.

Architecture du Projet
Le système est composé de deux microservices principaux interagissant pour la gestion des commandes :

Order Service (Port 8081) : Gère la création des commandes et orchestre l'appel vers le service de livraison.

Delivery Service (Port 8051) : Gère l'attribution des livreurs et la logistique de livraison.

 Technologies Utilisées
Java 17+ & Spring Boot 3.

Resilience4j : Pour le Circuit Breaker, le Bulkhead et les mécanismes de Fallback.

Spring Data JPA / PostgreSQL : Persistance des données (Projet Bassim Clinic & Order Service).

Docker : Conteneurisation de l'infrastructure.

Git : Gestion de version sur GitHub.

 Implémentation de la Résilience
1. Circuit Breaker (Disjoncteur)
Le Circuit Breaker protège le Order Service contre les pannes du Delivery Service.

Configuration Clé :

slidingWindowSize: 10 : Analyse les 10 derniers appels.

failureRateThreshold: 50 : Ouvre le circuit si 50% des appels échouent.

waitDurationInOpenState: 10000ms : Temps d'attente avant de tenter une réouverture (Half-Open).

2. Bulkhead (Cloisonnement)
Le Bulkhead limite le nombre d'appels simultanés au Delivery Service pour éviter l'épuisement des threads.

Configuration Clé :

maxConcurrentCalls: 3 : Seules 3 requêtes peuvent être traitées en parallèle.

maxWaitDuration: 0 : Rejet immédiat si la limite est atteinte.

 Scénarios de Test et Validation
Scénario 1 : Échec du Circuit Breaker
Objectif : Vérifier la transition de l'état CLOSED vers OPEN.

Action : Envoyer 5 à 10 requêtes d'échec :

Bash
curl http://localhost:8081/api/test-failure
Résultat observé : Le circuit passe en état OPEN. Resilience4j intercepte les appels suivants avec le message : "is OPEN and does not permit further calls".

Vérification :

Bash
curl http://localhost:8081/api/status
Scénario 2 : Test de charge Bulkhead
Objectif : Valider le rejet des requêtes excédentaires.

Action : Envoyer 6 requêtes simultanées via PowerShell :

PowerShell
1..6 | ForEach-Object { Start-Job { curl.exe -s http://localhost:8051/api/semaphore/test } }; Get-Job | Wait-Job | Receive-Job
Résultat attendu :

3 requêtes : SUCCESS (traitées en 3s).

3 requêtes : REJECTED (rejet immédiat car les slots sont pleins).

Installation et Lancement
Cloner le projet :

Bash
git clone https://github.com/amenimili90-byte/food-delivery-resilience.git
Lancer les services :

Compiler avec Maven : ./mvnw clean install.

Lancer l'application : ./mvnw spring-boot:run.

 Auteur
Ameni Mili groupe 2



Projet réalisé dans le cadre d'un stage chez CREO (Août 2025).
