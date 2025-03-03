package htl.ah;

public class Benutzer {
	  private String vorname;
	  private String nachname;
	  private String geburtsdatum; 

	  
	  public Benutzer(String vorname, String nachname, String geburtsdatum) {
	    this.vorname = vorname;
	    this.nachname = nachname;
	    this.geburtsdatum = geburtsdatum;
	  }

	 
	  public String getVorname() {
	    return vorname;
	  }

	  public String getNachname() {
	    return nachname;
	  }

	  public String getGeburtsdatum() {
	    return geburtsdatum;
	  }

	 
	  public void setVorname(String vorname) {
	    this.vorname = vorname;
	  }

	  public void setNachname(String nachname) {
	    this.nachname = nachname;
	  }

	  public void setGeburtsdatum(String geburtsdatum) {
	    this.geburtsdatum = geburtsdatum;
	  }

	  public void anzeigen() {
	    System.out.println("Benutzer: " + vorname + " " + nachname);
	    System.out.println("Geburtsdatum: " + geburtsdatum);
	  }
	}
