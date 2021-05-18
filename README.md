# Richtlinien für GUI

# Generell
- Jede GUI Klasse folgt dem selben Aufbau: Konstruktor, setupGUI, updateGUI, setupActionListeners, eigene Methoden, getters & setters, Methoden für andere Gruppen
- Nichts hard coden was nicht hard gecoded werden muss! (Besonders im GUI Designer)
- Jede Klasse extended GUIHelper und ist damit ein eigenständiges JPanel

# GUI Designer
- Borders, Fillrules und alles Component spezifische hier erledigen. Nichts im Code an sich machen!
- GridLayoutManager von IntelliJ verwenden! (Zumindest für das mainPanel)

# Konstruktor
- möglichst nur das nötigste. Als nötig gelten: Variableninitializierungen, language switch (siehe andere Klassen), setup Methoden, Popupklasseninitialisierungen
- Konstruktor nicht überladen! Die setTexts von Components gehören ins updateGUI! (siehe andere Klassen)
- Hierher gehört auch immer!:
  `this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
  this.add(mainPanel);`
- Dieser Code stellt sicher, dass auch was angezeigt wird

# setupGUI
- wird vom language switch _direkt_ mit den zur Sprache passenden Texten aufgerufen (s.a.K.)
- NICHTS WEITER HIER REIN!

# updateGUI
- Für jede Klasse individuell! Hier werden ComboBoxes etc gefüllt. Bei angeschlossenen Popupklassen werden diese auch hiermit angesteuert

# setupActionListeners (Oder generell nur setupListeners)
- Hier kommen alle Listenerinitialisierungen hinein!

# Eigene Methoden
- Alle Methoden die für die Klasse benötigt werden um _GUI spezifische_ Dinge zu erledigen (Wichtig!)
- Falls die Klasse eine Hauptklasse ist (Also z.B. ClassroomGUI), dann kommt hier auch eine Funktion initForAccountType rein.

# Getters & Setters
- Hier alle (meist handgeschriebenen) Getter und Setter, die für die Anwendungslogik gebraucht werden
- z.B. getPassword bei dem LoginGUI damit die andere Gruppe direkt das Password erhalten kann.
- Hier also alle Methoden so angenehm wie möglich für ander Gruppen schreiben

# Methoden für andere Gruppen
- Hierzu gehören Methoden, die z.B. durch button presses ausgelöst werden, aber von anderen Gruppen implementiert werden
- z.B. die enterFunction wenn der Anwender auf den Beitreten Button einer Klasse drückt. (In die Methode kommt dann der Code von der anderen Gruppe)
- Manche Dinge können hier schon vorher implementiert werden. z.B. Frame Wechsel oder so