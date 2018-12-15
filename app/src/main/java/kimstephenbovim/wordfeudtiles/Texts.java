package kimstephenbovim.wordfeudtiles;

import android.content.res.Resources;
import android.support.v4.os.ConfigurationCompat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kimstephenbovim.wordfeudtiles.domain.Preferences;

public class Texts {

    public static Texts shared = new Texts();
    private Map<String, String[]> texts = new HashMap<>();
    private List<String> locales = Arrays.asList("en", "nb", "nl", "da", "sv", "es", "fr", "de", "fi", "pt");
    private String[] gameLanguages = {"englishUS", "norwegianBokmal", "dutch", "danish", "swedish", "englishIntl", "spanish", "french", "swedishStrict",
            "german", "norwegianNynorsk", "finnish", "portuguese"};

    /*func getMaxFontSize(text: String, maxWidth: CGFloat) -> CGFloat {
        var fontSize = 17
        while (fontSize > 10 && getTextWidth(text: text, font: UIFont.systemFont(ofSize: CGFloat(fontSize))) > maxWidth) {
            fontSize -= 1
        }
        return CGFloat(fontSize)
    }
    
    func getTextWidth(text: String, font: UIFont) -> CGFloat {
        let attributes = [NSAttributedStringKey.font: font]
        return text.size(withAttributes: attributes as Any as? [NSAttributedStringKey : Any]).width
    }*/

    public String getText(String key) {
        if (!texts.containsKey(key)) {
            return "";
        }
        return texts.get(key)[getLocaleIndex()];
    }

    boolean unsupportedLanguage(int ruleset) {
        return ruleset >= gameLanguages.length;
    }

    String getGameLanguage(int ruleset) {
        if (unsupportedLanguage(ruleset)) {
            return getText("unsupportedLanguage");
        }
        return getText(gameLanguages[ruleset]);
    }

    CharSequence[] getLocalizedLanguages() {
        CharSequence[] localizedLanguages = new CharSequence[10];
        localizedLanguages[0] = getText("englishLanguage");
        localizedLanguages[1] = getText("norwegianLanguage");
        localizedLanguages[2] = getText("dutchLanguage");
        localizedLanguages[3] = getText("danishLanguage");
        localizedLanguages[4] = getText("swedishLanguage");
        localizedLanguages[5] = getText("spanishLanguage");
        localizedLanguages[6] = getText("frenchLanguage");
        localizedLanguages[7] = getText("germanLanguage");
        localizedLanguages[8] = getText("finnishLanguage");
        localizedLanguages[9] = getText("portugueseLanguage");
        return localizedLanguages;
    }

    int getLocaleIndex() {
        Preferences preferences = WFTiles.instance.getPreferences();
        if (preferences != null) {
            return preferences.getLocaleIndex();
        } else {
            Locale locale = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0);
            int index = locales.indexOf(locale.getLanguage());
            return index == -1 ? 0 : index;
        }
    }

    private Texts() {
        texts.put("loginInfo", new String[]{"Log in using your Wordfeud username or email address below.", "Logg inn med ditt Wordfeud-brukernavn eller e-postadresse.", "Log hieronder in met je Wordfeud-gebruikersnaam of je e-mailadres.", "Log på med dit Wordfeud-brugernavn eller e-mailadresse herunder.", "Logga in med ditt Wordfeud-användarnamn eller e-postadress nedan.", "Inicia sesión con tu nombre de usuario Wordfeud o tu dirección de correo electrónico.", "Connecte-toi en utilisant ton nom d'utilisateur Wordfeud ou ton adresse e-mail ci-dessous.", "Gib denselben Wordfeud Benutzernamen bzw. dieselbe E-Mail-Adresse ein, den/die Du bei der Anmeldung verwendet hast.", "Rekisteröidy tai kirjaudu sisään sähköpostiosoitteella tai Wordfeud-käyttäjänimellä.", "Entre em Wordfeud usuando seu nome de usuário ou e-mail abaixo."});

        texts.put("usernameEmail", new String[]{"Username or email", "Brukernavn eller e-postadresse", "Gebruikersnaam of e-mail", "Brugernavn eller e-mail", "Användarnamn eller e-postadress", "Usuario o e-mail", "Nom d'utilisateur ou e-mail", "Benutzername oder E-Mail-Adresse", "Käyttäjänimi tai sähköpostiosoite", "Nome de usuário ou e-mail"});

        texts.put("password", new String[]{"Wordfeud password", "Wordfeud-passord", "Voer je wachtwoord in", "Indtast din adgangskode", "Ange ditt lösenord", "Introducir contraseña", "Saisis ton mot de passe", "Passwort eingeben", "Anna salasana", "Digite Sua Senha"});

        texts.put("next", new String[]{"Next", "Neste", "Volgende", "Næste", "Nästa", "Siguiente", "Suivant", "Weiter", "Seuraava", "Seguinte"});

        texts.put("login", new String[]{"Log in", "Logg inn", "Inloggen", "Log på", "Logga inn", "Iniciar sesión", "Connexion", "Anmelden", "Kirjaudu", "Entrar"});

        texts.put("logout", new String[]{"Log out", "Logg ut", "Uitloggen", "Log af", "Logga ut", "Desconectarse", "Se déconnecter", "Abmelden", "Kirjaudu ulos", "Sair"});

        texts.put("yourTurn", new String[]{"Your turn", "Din tur", "Jouw beurt", "Din tur", "Din tur", "Te toca", "Ton tour", "Du bist dran", "Sinun vuorosi", "Sua Vez"});

        texts.put("opponentsTurn", new String[]{"Their turn", "Motstanders tur", "Hun beurt", "Deres tur", "Motspelarens tur", "Su turno", "Leur tour", "Der Gegner ist dran", "Vastustajan vuoro", "É a Vez Deles"});

        texts.put("finishedGames", new String[]{"Finished games", "Avsluttede spill", "Voltooide spellen", "Afsluttede spil", "Avslutade matcher", "Partidas terminadas", "Parties terminées", "Beendete Spiele", "Päättyneet pelit", "Jogos Finalizados"});

        texts.put("englishUS", new String[]{"English game (US)", "Engelsk spill (amer.)", "Engels (VS) spel", "Spil på engelsk (USA)", "Engelsk match (amerikansk)", "Partida en inglés (EE.UU.)", "Partie en anglais (USA)", "Englisches Spiel (USA)", "Englanninkielinen peli (US)", "Jogo em inglês (EUA)"});

        texts.put("norwegianBokmal", new String[]{"Norwegian game (bokmål)", "Norsk spill (bokmål)", "Noors spel (bokmål)", "Spil på norsk (bokmål)", "Norsk match (bokmål)", "Partida en noruego (bokmål)", "Partie en norvégien (bokmål)", "Norwegisches Spiel (Bokmål)", "Norjankielinen peli (bokmål)", "Jogo em norueguês (bokmål)"});

        texts.put("dutch", new String[]{"Dutch game", "Nederlandsk spill", "Nederlands spel", "Spil på hollandsk", "Holländsk match", "Partida en holandés", "Partie en hollandais", "Niederländisches Spiel", "Hollanninkielinen peli", "Jogo em holandês"});

        texts.put("danish", new String[]{"Danish game", "Dansk spill", "Deens spel", "Spil på dansk", "Dansk match", "Partida en danés", "Partie en danois", "Dänisches Spiel", "Tanskankielinen peli", "Jogo em dinamarquês"});

        texts.put("swedish", new String[]{"Swedish game", "Svensk spill", "Zweeds spel", "Spil på svensk", "Svensk match", "Partida en sueco", "Partie en suédois", "Schwedisches Spiel", "Ruotsinkielinen peli", "Jogo em sueco"});

        texts.put("englishIntl", new String[]{"English game (Intl)", "Engelsk spill (int.)", "Engels (Intl) spel", "Spil på engelsk (intl.)", "Engelsk match (internationell)", "Partida en inglés (Int.)", "Partie en anglais (inter.)", "Englisches Spiel (int.)", "Englanninkielinen peli (kans.)", "Jogo em inglês (Inter)"});

        texts.put("spanish", new String[]{"Spanish game", "Spansk spill", "Spaans spel", "Spil på spansk", "Spansk match", "Partida en español", "Partie en espagnol", "Spanisches Spiel", "Espanjankielinen peli", "Jogo em espanhol"});

        texts.put("french", new String[]{"French game", "Fransk spill", "Frans spel", "Spil på fransk", "Fransk match", "Partida en francés", "Partie en français", "Französisches Spiel", "Ranskankielinen peli", "Jogo em francês"});

        texts.put("swedishStrict", new String[]{"Swedish game (strict)", "Svensk spill (strikt)", "Zweeds (strikt) spel", "Spil på svensk (eksakt)", "Svensk match (strikt)", "Partida en sueco (riguroso)", "Partie en suédois (strict)", "Schwedisches Spiel (strikt)", "Ruotsinkielinen peli (vain perusmuodot)", "Jogo em sueco (estrito)"});

        texts.put("german", new String[]{"German game", "Tysk spill", "Duits spel", "Spil på tysk", "Tysk match", "Partida en alemán", "Partie en allemand", "Deutsches Spiel", "Saksankielinen peli", "Jogo em alemão"});

        texts.put("norwegianNynorsk", new String[]{"Norwegian game (nynorsk)", "Norsk spill (nynorsk)", "Noors spel (nynorsk)", "Spil på norsk (nynorsk)", "Norsk match (nynorsk)", "Partida en noruego (nynorsk)", "Partie en norvégien (nynorsk)", "Norwegisches Spiel (Nynorsk)", "Norjankielinen peli (nynorsk)", "Jogo em norueguês (nynorsk)"});

        texts.put("finnish", new String[]{"Finnish game", "Finsk spill", "Fins spel", "Spil på finsk", "Finsk match", "Partida en finés", "Partie en finnois", "Finnisches Spiel", "Suomenkielinen peli", "Jogo em finlandês"});

        texts.put("portuguese", new String[]{"Portuguese game", "Portugisisk spill", "Portugees spel", "Spil på portugisisk", "Portugisisk match", "Partida en portugués", "Partie en portugais", "Portugiesisches Spiel", "Portugalinkielinen peli", "Jogo em português"});

        texts.put("youPlayed", new String[]{"You played %s for %d points", "Du la %s for %d poeng", "Je hebt %s gelegd en %d punten behaald", "Du spillede %s for %d point", "Du spelade %s för %d poäng", "Has puesto %s por %d puntos", "Tu as joué %s pour %d points", "Du hast %s gespielt und %d Punkte erzielt", "Pelasit sanan %s ja sait %d pistettä", "Você jogou %s por %d pontos"});

        texts.put("theyPlayed", new String[]{"%s played %s for %d points", "%s la %s for %d poeng", "%s heeft %s gelegd en %d punten behaald", "%s spillede %s for %d point", "%s spelade %s för %d poäng", "%s ha puesto %s por %d puntos", "%s a joué %s pour %d points", "%s hat %s gespielt und %d Punkte erzielt", "%s pelasi sanan %s ja sai %d pistettä", "%s jogou %s por %d pontos"});

        texts.put("youResigned", new String[]{"You resigned", "Du ga opp", "Je haakte af", "Du gav op", "Du gav upp", "Te has rendido", "Tu as abandonné", "Du hast aufgegeben", "Luovutit", "Você abandonou a partida"});

        texts.put("theyResigned", new String[]{"%s resigned", "%s ga opp", "%s haakte af", "%s gav op", "%s gav upp", "%s se ha rendido", "%s a abandonné", "%s hat aufgegeben", "%s luovutti", "%s abandonou"});

        texts.put("youPassed", new String[]{"You passed", "Du meldte pass", "Je hebt gepast", "Du meldte pas", "Du passade", "Has pasado", "Tu as passé", "Du hast gepasst", "Passasit", "Você passou"});

        texts.put("theyPassed", new String[]{"%s passed", "%s meldte pass", "%s paste", "%s meldte pas", "%s passade", "%s ha pasado", "%s a passé", "%s passt", "%s passasi", "%s passou"});

        texts.put("youSwapped", new String[]{"You swapped %d tiles", "Du byttet %d brikker", "Je hebt %d letters geruild", "Du byttede %d brikker", "Du bytte ut %d brickor", "Has cambiado %d fichas", "Tu as échangé %d pièces", "Du hast %d Spielsteine getauscht", "Vaihdoit %d laattaa", "Você trocou %d peças"});

        texts.put("youSwappedOne", new String[]{"You swapped %d tile", "Du byttet %d brikke", "Je hebt %d letter geruild", "Du byttede %d brikke", "Du bytte ut %d bricka", "Has cambiado %d ficha", "Tu as échangé %d pièce", "Du hast %d Spielstein getauscht", "Vaihdoit %d laatan", "Você trocou %d peça"});

        texts.put("theySwapped", new String[]{"%s swapped %d tiles", "%s byttet %d brikker", "%s heeft %d letters geruild", "%s byttede %d brikker", "%s bytte ut %d brickor", "%s ha cambiado %d fichas", "%s a échangé %d pièces", "%s hat %d Spielsteine getauscht", "%s vaihtoi %d laattaa", "%s trocou %d peças"});

        texts.put("theySwappedOne", new String[]{"%s swapped %d tile", "%s byttet %d brikke", "%s heeft %d letter geruild", "%s byttede %d brikke", "%s bytte ut %d bricka", "%s ha cambiado %d ficha", "%s a échangé %d pièce", "%s hat %d Spielstein getauscht", "%s vaihtoi %d laatan", "%s trocou %d peça"});

        texts.put("firstMoveYou", new String[]{"It's your turn against %s", "Det er din tur mot %s", "Het is jouw beurt tegen %s", "Det er din tur mod %s", "Det är din tur mot %s", "Te toca jugar contra %s", "C'est ton tour contre %s", "Du bist dran gegen %s", "%s odottaa - on sinun vuorosi", "É sua vez contra %s"});

        texts.put("firstMoveThem", new String[]{"Waiting for %s to make a move", "Venter på et trekk fra %s", "Wacht op een zet van %s", "Venter på, at %s foretager et træk", "Vänter på att %s ska göra ett drag", "Esperando a que %s mueva ficha", "En attente d'un coup de %s", "Warten auf Zug von %s", "Odotetaan, että %s tekee siirtonsa", "Esperando que %s faça um movimento"});

        texts.put("yourTiles", new String[]{"Your tiles", "Dine brikker", "Jouw letters", "Dine brikker", "Dina brickor", "Tus fichas", "Vos pièces", "Deine Spielsteine", "Teidän laatat", "Suas peças"});

        texts.put("remainingTiles", new String[]{"Remaining tiles", "Resterende brikker", "Resterende letters", "Resterende brikker", "Resterande brickor", "Fichas restantes", "Pièces restantes", "Verbleibende Spielsteine", "Jäljellä olevat laatat", "Peças restantes"});

        texts.put("remainingTilesBag", new String[]{"Remaining tiles (%d in the bag)", "Resterende brikker (%d i posen)", "Resterende letters (%d in het zakje)", "Resterende brikker (%d i posen)", "Resterande brickor (%d i påsen)", "Fichas restantes (%d en la bolsa)", "Pièces restantes (%d dans le sac)", "Verbleibende Spielsteine (%d in den Beutel)", "Jäljellä olevat laatat (%d pussissa)", "Peças restantes (%d no saco)"});

        texts.put("standard", new String[]{"Standard", "Standard", "Standaard", "Standard", "Standard", "Estándar", "Standard", "Standard", "Vakio", "Padrão"});

        texts.put("overview", new String[]{"Overview", "Oversikt", "Overzicht", "Oversigt", "Översikt", "Visión de conjunto", "Vue d'ensemble", "Überblick", "Yleiskatsaus", "Visão global"});

        texts.put("alphabetical", new String[]{"Alphabetical", "Alfabetisk", "Alfabetisch", "Alfabetisk", "Alfabetisk", "Alfabético", "Alphabétique", "Alphabetisch", "Aakkosellinen", "Alfabeticamente"});

        texts.put("vowelsConsonants", new String[]{"Vowels/consonants", "Vokaler/konsonanter", "Klinkers/medeklinkers", "Vokaler/konsonanter", "Vokaler/konsonanter", "Vocales/consonantes", "Voyelles/consonnes", "Vokale/Konsonanten", "Vokaalit/konsonantteja", "Vogais/consoantes"});

        texts.put("loggingIn", new String[]{"Logging in...", "Logger inn...", "Logt aan...", "Logger på...", "Loggar in...", "Iniciando sesión...", "Connexion en cours...", "Login läuft...", "Kirjaudutaan...", "Entrando..."});

        texts.put("loginFailed", new String[]{"Login failed", "Innlogging feilet", "Aanmelden mislukt", "Login mislykkedes", "Inloggningen misslyckades", "Error de inicio de sesion", "Échec de la connexion", "Anmeldung fehlgeschlagen", "Kirjautuminen epäonnistui", "Falha na autenticação"});

        texts.put("pleaseWait", new String[]{"Please wait...", "Vennligst vent...", "Even wachten...", "Vent et øjeblik...", "Vänligen vänta...", "Un momento, por favor...", "Un instant...", "Bitte warte...", "Odota...", "Por favor, aguarde..."});

        texts.put("loadingFailed", new String[]{"Loading failed", "Lasting feilet", "Laden mislukt", "Loading mislykkedes", "Hämtningen misslyckades", "La descarga falló", "Chargement échoué", "Laden fehlgeschlagen", "Lataus epäonnistui", "O carregamento falhou"});

        texts.put("connectionError", new String[]{"Connection error", "Feil ved oppkobling", "Verbindingsfout", "Forbindelsesfejl", "Anslutningsfel", "Error de conexión", "Erreur de connexion", "Verbindungsfehler", "Yhteysvirhe", "Erro de Conexão"});

        texts.put("unknownEmail", new String[]{"Unknown email", "Ukjent e-post", "Onbekende gebruiker", "Ukendt bruger", "Okänd användare", "Dirección de correo electrónico incorrecta", "Utilisateur inconnu", "Unbekannter Benutzer", "Tuntematon käyttäjä", "Usuário Desconhecido"});

        texts.put("unknownUsername", new String[]{"Unknown username", "Ukjent brukernavn", "Onbekende gebruiker", "Ukendt bruger", "Okänd användare", "Usuario desconocido", "Utilisateur inconnu", "Unbekannter Benutzer", "Tuntematon käyttäjä", "Usuário Desconhecido"});

        texts.put("wrongPassword", new String[]{"Wrong password", "Feil passord", "Wachtwoord onjuist", "Forkert adgangskode", "Ogiltigt lösenord", "Contraseña incorrecta", "Mot de passe erroné", "Falsches Passwort", "Virheellinen salasana", "Senha incorreta"});

        texts.put("ok", new String[]{"OK", "OK", "Oké", "OK", "OK", "Aceptar", "OK", "OK", "OK", "Ok"});

        texts.put("unsupportedLanguage", new String[]{"Unsupported language", "Språk som ikke støttes", "Niet-ondersteunde taal", "Ikke understøttet sprog", "Språk som inte stöds", "Lenguaje no compatible", "Langue non supportée", "Nicht unterstützte Sprache", "Ei tuettu kieltä", "Linguagem não suportada"});

        texts.put("language", new String[]{"Language", "Språk", "Taal", "Sprog", "Språk", "Idioma", "Langue", "Sprache", "Kieli", "Idioma"});

        texts.put("englishLanguage", new String[]{"English", "Engelsk", "Engels", "Engelsk", "Engelska", "Inglés", "Anglais", "Englisch", "Englanti", "Inglês"});

        texts.put("norwegianLanguage", new String[]{"Norwegian", "Norsk", "Noors", "Norsk", "Norska", "Noruego", "Norvégien", "Norwegisch", "Norja", "Norueguês"});

        texts.put("dutchLanguage", new String[]{"Dutch", "Nederlandsk", "Nederlands", "Hollandsk", "Holländska", "Holandés", "Hollandais", "Niederländisch", "Hollanti", "Holandês"});

        texts.put("danishLanguage", new String[]{"Danish", "Dansk", "Deens", "Dansk", "Danska", "Danés", "Danois", "Dänisch", "Tanskalainen", "Dinamarquês"});

        texts.put("swedishLanguage", new String[]{"Swedish", "Svensk", "Zweeds", "Svensk", "Svenska", "Sueco", "Suédois", "Schwedisch", "Ruotsi", "Sueco"});

        texts.put("spanishLanguage", new String[]{"Spanish", "Spansk", "Spaans", "Spansk", "Spanska", "Español", "Espagnol", "Spanisch", "Espanjalainen", "Espanhol"});

        texts.put("frenchLanguage", new String[]{"French", "Fransk", "Frans", "Fransk", "Franska", "Francés", "Français", "Französisch", "Ranskalainen", "Francês"});

        texts.put("germanLanguage", new String[]{"German", "Tysk", "Duits", "Tysk", "Tyska", "Alemán", "Allemand", "Deutsch", "Saksalainen", "Alemão"});

        texts.put("finnishLanguage", new String[]{"Finnish", "Finsk", "Fins", "Finsk", "Finska", "Finés", "Finnois", "Finnisch", "Suomalainen", "Finlandês"});

        texts.put("portugueseLanguage", new String[]{"Portuguese", "Portugisisk", "Portugees", "Portugisisk", "Portugisiska", "Portugués", "Portugais", "Portugiesisch", "Portugalilainen", "Português"});
    }
}
