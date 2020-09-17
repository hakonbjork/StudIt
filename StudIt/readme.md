#NTNU - Choose the right course?

This project is a simple three-layer application, with domain, user interface (UI) and persistence. The project consists of tests with a good coverage and is configured to use maven as build tool.


Organisering av koden
Prosjektet er organisert med 2 * 2 = 4 kildekodemapper, kode og ressurser for henholdsvis applikasjonen selv og testene:


src/main/java for koden til applikasjonen

src/main/resources for tilhørende ressurser, f.eks. data-filer og FXML-filer, som brukes av applikasjonen.

src/test/java for testkoden

src/test/resources for tilhørende ressurser, f.eks. data-filer og FXML-filer, som brukes av testene.

Dette er en vanlig organisering for prosjekter som bygges med maven (og gradle).

Domenelaget
Domenelaget inneholder alle klasser og logikk knyttet til dataene som applikasjonen handler om og håndterer. Dette laget skal være helt uavhengig av brukergrensesnittet eller lagingsteknikken som brukes.
Vår app handler om samlinger av såkalte geo-lokasjoner, altså steder identifisert med lengde- og breddegrader. Domenelaget inneholder klasser for å representere og håndtere slike, og disse finnes i simpleex.core-pakken.

Brukergrensesnittlaget
Brukergrensesnittlaget inneholder alle klasser og logikk knyttet til visning og handlinger på dataene i domenelaget. Brukergrensesnittet til vår app viser frem en liste av geo-lokasjoner, den som velges vises på et kart. En flytte og legge til geo-lokasjoner. Samlingen med geo-lokasjoner kan lagres og evt. leses inn igjen.
Brukergrensesnittet er laget med JavaFX og FXML og finnes i simpleex.ui-pakken (JavaFX-koden i src/main/java og FXML-filen i src/main/resources)

Persistenslaget
Persistenslaget inneholder alle klasser og logikk for lagring (skriving og lesing) av dataene i domenelaget. Vårt persistenslag implementerer fillagring med JSON-syntaks.
Persistenslaget finnes i simpleex.json-pakken.

Bygging med maven
For litt mer komplekse prosjekter, er det lurt å bruke et såkalt byggeverktøy, f.eks. maven eller gradle, for å automatisere oppgaver som kjøring av tester, sjekk av ulike typer kvalitet osv.
Vårt prosjekt er konfigurert til å bruke maven, og følgelig har prosjektet en pom.xml-fil for konfigurajon.
En pom.xml-fil inneholder ulike typer informasjon om prosjektet:


identifikasjon i form av groupId-, artifactId- og version-elementer

avhengigheter i form av *dependency-elementer

byggetillegg (plugins) i form av plugin-elementer, som igjen konfigureres med configuration-elementer

Vårt bygg har tillegg for:

oppsett av java (maven-compiler-plugin)
testing (maven-surefire-plugin)
kjøring av javafx (javafx-maven-plugin)
sjekking av kodekvalitet med checkstyle (maven-checkstyle-plugin) og spotbugs (spotbugs-maven-plugin)
testdekningsgrad med jacoco (jacoco-maven-plugin)

De fleste avhengighetene hentes fra de vanlige sentrale repo-ene, med unntak av FxMapControl som er lagt ved som jar-fil i libs-mappa.
Det siste krever manuell bruk av maven install, se tasks-delen av .gitpod.yml