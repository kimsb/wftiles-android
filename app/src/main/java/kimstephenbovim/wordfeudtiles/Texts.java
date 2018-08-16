package kimstephenbovim.wordfeudtiles;

import java.util.Map;

public class Texts {

    public static Texts shared = new Texts();
    private Map<String, String[]> texts;
    private String[] locales = {"en", "nb", "nl", "da", "sv", "es", "fr", "de", "fi", "pt"};
    private String[] languages = {"englishUS", "norwegianBokmal", "dutch", "danish", "swedish", "englishIntl", "spanish", "french", "swedishStrict",
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
    
    public boolean unsupportedLanguage(int ruleset) {
        return ruleset >= languages.length;
    }
    
    public String getGameLanguage(int ruleset) {
        if (unsupportedLanguage(ruleset)) {
            return getText("unsupportedLanguage");
        }
        return getText(languages[ruleset]);
    }

    
    //TODO
    private int getLocaleIndex() {
        return 1;
    }
    /*func getLocaleIndex() -> Int {
        if (AppData.shared.preferredLanguageIndex() != nil) {
            return AppData.shared.preferredLanguageIndex()!
        }
        var preferredLanguage = NSLocale.preferredLanguages[0]
        if preferredLanguage.contains("-") {
            preferredLanguage = preferredLanguage.components(separatedBy: "-")[0]
        }
        guard let index = locales.index(of: preferredLanguage) else {
            return 0
        }
        return index
    }*/

    private Texts() {
        texts.put("loginInfo", new String[]{"Log in using your Wordfeud username or email address below.", "Logg inn med ditt Wordfeud-brukernavn eller e-postadresse.", "Log hieronder in met je Wordfeud-gebruikersnaam of je e-mailadres.", "Log på med dit Wordfeud-brugernavn eller e-mailadresse herunder.", "Logga in med ditt Wordfeud-användarnamn eller e-postadress nedan.", "Inicia sesión con tu nombre de usuario Wordfeud o tu dirección de correo electrónico.", "Connecte-toi en utilisant ton nom d'utilisateur Wordfeud ou ton adresse e-mail ci-dessous.", "Gib denselben Wordfeud Benutzernamen bzw. dieselbe E-Mail-Adresse ein, den/die Du bei der Anmeldung verwendet hast.", "Rekisteröidy tai kirjaudu sisään sähköpostiosoitteella tai Wordfeud-käyttäjänimellä.", "Entre em Wordfeud usuando seu nome de usuário ou e-mail abaixo."});

        texts.put("usernameEmail", new String[]{"Username or email", "Brukernavn eller e-postadresse", "Gebruikersnaam of e-mail", "Brugernavn eller e-mail", "Användarnamn eller e-postadress", "Usuario o e-mail", "Nom d'utilisateur ou e-mail", "Benutzername oder E-Mail-Adresse", "Käyttäjänimi tai sähköpostiosoite", "Nome de usuário ou e-mail"});

        texts.put("password", new String[]{"Wordfeud password", "Wordfeud-passord", "Voer je wachtwoord in", "Indtast din adgangskode", "Ange ditt lösenord", "Introducir contraseña", "Saisis ton mot de passe", "Passwort eingeben", "Anna salasana", "Digite Sua Senha"});

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

        texts.put("youPlayed", new String[]{"You played %@ for %d points", "Du la %@ for %d poeng", "Je hebt %@ gelegd en %d punten behaald", "Du spillede %@ for %d point", "Du spelade %@ för %d poäng", "Has puesto %@ por %d puntos", "Tu as joué %@ pour %d points", "Du hast %@ gespielt und %d Punkte erzielt", "Pelasit sanan %@ ja sait %d pistettä", "Você jogou %@ por %d pontos"});

        texts.put("theyPlayed", new String[]{"%@ played %@ for %d points", "%@ la %@ for %d poeng", "%@ heeft %@ gelegd en %d punten behaald", "%@ spillede %@ for %d point", "%@ spelade %@ för %d poäng", "%@ ha puesto %@ por %d puntos", "%@ a joué %@ pour %d points", "%@ hat %@ gespielt und %d Punkte erzielt", "%@ pelasi sanan %@ ja sai %d pistettä", "%@ jogou %@ por %d pontos"});

        texts.put("youResigned", new String[]{"You resigned", "Du ga opp", "Je haakte af", "Du gav op", "Du gav upp", "Te has rendido", "Tu as abandonné", "Du hast aufgegeben", "Luovutit", "Você abandonou a partida"});

        texts.put("theyResigned", new String[]{"%@ resigned", "%@ ga opp", "%@ haakte af", "%@ gav op", "%@ gav upp", "%@ se ha rendido", "%@ a abandonné", "%@ hat aufgegeben", "%@ luovutti", "%@ abandonou"});

        texts.put("youPassed", new String[]{"You passed", "Du meldte pass", "Je hebt gepast", "Du meldte pas", "Du passade", "Has pasado", "Tu as passé", "Du hast gepasst", "Passasit", "Você passou"});

        texts.put("theyPassed", new String[]{"%@ passed", "%@ meldte pass", "%@ paste", "%@ meldte pas", "%@ passade", "%@ ha pasado", "%@ a passé", "%@ passt", "%@ passasi", "%@ passou"});

        texts.put("youSwapped", new String[]{"You swapped %d tiles", "Du byttet %d brikker", "Je hebt %d letters geruild", "Du byttede %d brikker", "Du bytte ut %d brickor", "Has cambiado %d fichas", "Tu as échangé %d pièces", "Du hast %d Spielsteine getauscht", "Vaihdoit %d laattaa", "Você trocou %d peças"});

        texts.put("youSwappedOne", new String[]{"You swapped %d tile", "Du byttet %d brikke", "Je hebt %d letter geruild", "Du byttede %d brikke", "Du bytte ut %d bricka", "Has cambiado %d ficha", "Tu as échangé %d pièce", "Du hast %d Spielstein getauscht", "Vaihdoit %d laatan", "Você trocou %d peça"});

        texts.put("theySwapped", new String[]{"%@ swapped %d tiles", "%@ byttet %d brikker", "%@ heeft %d letters geruild", "%@ byttede %d brikker", "%@ bytte ut %d brickor", "%@ ha cambiado %d fichas", "%@ a échangé %d pièces", "%@ hat %d Spielsteine getauscht", "%@ vaihtoi %d laattaa", "%@ trocou %d peças"});

        texts.put("theySwappedOne", new String[]{"%@ swapped %d tile", "%@ byttet %d brikke", "%@ heeft %d letter geruild", "%@ byttede %d brikke", "%@ bytte ut %d bricka", "%@ ha cambiado %d ficha", "%@ a échangé %d pièce", "%@ hat %d Spielstein getauscht", "%@ vaihtoi %d laatan", "%@ trocou %d peça"});

        texts.put("firstMoveYou", new String[]{"It's your turn against %@", "Det er din tur mot %@", "Het is jouw beurt tegen %@", "Det er din tur mod %@", "Det är din tur mot %@", "Te toca jugar contra %@", "C'est ton tour contre %@", "Du bist dran gegen %@", "%@ odottaa - on sinun vuorosi", "É sua vez contra %@"});

        texts.put("firstMoveThem", new String[]{"Waiting for %@ to make a move", "Venter på et trekk fra %@", "Wacht op een zet van %@", "Venter på, at %@ foretager et træk", "Vänter på att %@ ska göra ett drag", "Esperando a que %@ mueva ficha", "En attente d'un coup de %@", "Warten auf Zug von %@", "Odotetaan, että %@ tekee siirtonsa", "Esperando que %@ faça um movimento"});

        texts.put("yourTiles", new String[]{"Your tiles", "Dine brikker", "Jouw letters", "Dine brikker", "Dina brickor", "Tus fichas", "Vos pièces", "Deine Spielsteine", "Teidän laatat", "Suas peças"});

        texts.put("remainingTiles", new String[]{"Remaining tiles (%d in the bag)", "Resterende brikker (%d i posen)", "Resterende letters (%d in het zakje)", "Resterende brikker (%d i posen)", "Resterande brickor (%d i påsen)", "Fichas restantes (%d en la bolsa)", "Pièces restantes (%d dans le sac)", "Verbleibende Spielsteine (%d in den Beutel)", "Jäljellä olevat laatat (%d pussissa)", "Peças restantes (%d no saco)"});

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