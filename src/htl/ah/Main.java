package htl.ah;

public class Main {
    public static void main(String[] args) {
        DBManager dbManager = DBManager.getInstance();

        Benutzer benutzer1 = new Benutzer("Max", "Mustermann", "1990-01-01");
        dbManager.speichereBenutzer(benutzer1);

        Benutzer benutzer2 = new Benutzer("Erika", "Musterfrau", "1985-05-15");
        dbManager.speichereBenutzer(benutzer2);

        System.out.println("Benutzer geladen:");
        for (Benutzer b : dbManager.ladeBenutzer()) {
            System.out.println(b.getVorname() + " " + b.getNachname() + " " + b.getGeburtsdatum());
        }

        dbManager.loescheBenutzer(benutzer1);
    }
}
