## Entitások

**BaseEntity**:

- id

**Ticket**:
- Táblában (szűrés, rendezéssel, esetleges global szűrés, my ticket szűrés) 
- id : normális ticket azonosító (pl. : UCD-001) 
- title: hiba megnevezése
- description: hiba leírása
- type: (entity) hiba típusa
- clientMail: ügyfél emailcíme
- state:(entity) (még nem tudjuk hogy enumeráció, vagy sima string), lehetséges értékek , külön joggal szerkeszthető (szerkeszthető/nem szerkeszthető)
- comments (`List<Comment>`): ticket-hez tartozó kommentek
- history (`List<Event>`): változások a ticket-en
- bindedTo (`User`): azok az ügynökök kik láthatják, módosíthatják ezt a ticket-el
- company (`Company`): melyik céghez köthető ez a ticket (ez alapján fogjuk tudni kilistázni a cég adminisztrátorának a hozzájuk köthető ticket-eket)

**Comment**:

- author (`User`): ki üzent
- date: mikor hozták létre az üzenetet
- content (Lob, `String`): komment tartalma

**Event**:

- modified: időpont a változásról
- modifier (`User`): ki változtatott
- activity: mi csinált, értékei lehetnek
 - `OPEN`
 - `IN_PROGRESS`
 - `COMPLETED`
 - `CLOSED`
 - `COMMENTED`
- content (`Comment`): ha activity `COMMENTED`, akkor az értéke az a `Comment` lesz, amit létrehoztak, egyéb esetben null (reméljük jpa kezeli)

**TicketOrder**: ez az osztály fogja reprezentálni a ticket-ek sorrendjét

- ticket (`Ticket`): melyik ticketről beszélünk
- no: helye a sorrendben


User: meg fogjuk kapni az adminisztráciciós modultól maven dependencyként
(core-ba majd függőségként felvenni az adaminisztrációs modul core-ját)

**Reportok**: (tortadiagram)
- Személyes
- Manager (team tagok szerinti felosztásban)
- nyitott/zárt ticketek megjelnítése vonaldiagrammal 
