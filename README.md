# SWT-UML-Application README

## Projektübersicht
Das SWT-UML-Application-Projekt ist eine gemeinsame Anstrengung unseres Teams, eine robuste Software zur Verwaltung und Visualisierung von UML-Diagrammen zu entwickeln. Jedes Teammitglied bringt spezielle Fähigkeiten ein, um unterschiedliche Aspekte der Anwendung zu bearbeiten.

### Team und Aufgaben:
- **Benjamin** - Organisation:
  - Verwaltung des Projekts
  - Unterstützung und Verbesserung des Codes

- **Daniel, Joost** - GUI:
  - Entwicklung der API zur Fenstererstellung
  - Implementierung verschiedener Fenstertypen wie Bestätigungs- und Zeichenfenster

- **Jakob, Patrick** - S/C Kommunikation:
  - Aufbau der Client-/Server-Kommunikation
  - Verwendung von RMI zur Interprozesskommunikation?
  - Serialisierung von Objekten

- **Andre, Mats** - Zeichenfunktionen:
  - Erstellung und Verwaltung von ziehbaren Objekten
  - Erzeugen und Löschen von Objekten über eine Seitenleiste

- **Yazar, Norbert** - Logik:
  - Logik zur Erstellung von Klassenräumen und Hinzufügen von Schülern
  - Logik für die Bildung von Gruppen und das Hinzufügen von Mitgliedern

## Richtlinien für GUI

### Allgemeine Anweisungen
- Alle GUI-Klassen folgen einer festgelegten Struktur: Konstruktor, setupGUI, updateGUI, setupActionListeners, eigene Methoden, Getter & Setter sowie Methoden für andere Gruppen.
- Vermeidung von hart codierten Werten, besonders im GUI-Designer.
- Jede Klasse erweitert `GUIHelper` und ist dadurch ein eigenständiges JPanel.

### GUI Designer
- Gestaltung von Borders, Fillrules und Komponentenspezifika sollte hier erfolgen. Verwenden Sie den GridLayoutManager von IntelliJ für das mainPanel.

### Konstruktor
- Der Konstruktor sollte sich auf das Wesentliche beschränken: Variableninitialisierungen, Sprachwechsel, Setup-Methoden und Popupklasseninitialisierungen.
- Der Konstruktor sollte nicht überladen sein! Die Zuweisung von Texten zu Komponenten gehört in `updateGUI`.

### SetupGUI
- Wird direkt durch den Sprachwechsel aufgerufen, um sprachspezifische Texte einzustellen.

### UpdateGUI
- Für jede Klasse individuell gestaltet, um Dropdown-Menüs zu füllen und angeschlossene Popupklassen zu steuern.

### SetupActionListeners
- Initialisierung aller Listener findet hier statt.

### Eigene Methoden
- Spezifische Methoden, die zur Ausführung von GUI-bezogenen Aufgaben erforderlich sind. Bei Hauptklassen (z.B. `ClassroomGUI`) wird hier auch die `initForAccountType`-Funktion implementiert.

### Getter & Setter
- Handgeschriebene Getter und Setter, die für die Anwendungslogik wichtig sind.

### Methoden für andere Gruppen
- Methoden, die durch Button-Drücke ausgelöst und von anderen Gruppen implementiert werden.

## Richtlinien für Drawable Objects
- Für jedes Objekt müssen zwei Klassen erstellt werden: _Object_ und _Object_ Button, die von den abstrakten Klassen `DrawableObject` und `DrawableObjectButton` erben.

### Implementierung der abstrakten Methoden
- Implementierung aller abstrakten Methoden ist erforderlich.
- Die `draw` Funktionen kümmern sich um die grafische Darstellung.
- Berechnungen zu Breite und Höhe müssen abhängig von 'scale' durchgeführt werden.

### Listeners und Popup-Funktionen
- Listener werden in der `setupListeners()`-Methode des Buttons implementiert.
- Popups werden durch die `createPopup()`-Methode im Objekt erzeugt.

Dieses README dokumentiert die grundlegenden Strukturen und Richtlinien unseres Projekts und soll als Anleitung für alle Entwickler dienen, die am Projekt mitwirken.
