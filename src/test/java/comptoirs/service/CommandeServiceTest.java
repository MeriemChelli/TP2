package comptoirs.service;

import comptoirs.entity.Commande;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
 // Ce test est basé sur le jeu de données dans "test_data.sql"
class CommandeServiceTest {
    private static final String ID_PETIT_CLIENT = "0COM";
    private static final String ID_GROS_CLIENT = "2COM";
    private static final String VILLE_PETIT_CLIENT = "Berlin";
    private static final BigDecimal REMISE_POUR_GROS_CLIENT = new BigDecimal("0.15");
    static final int NUMERO_COMMANDE_DEJA_LIVREE = 99999;
    @Autowired
    private CommandeService service;
    @Test
    void testCreerCommandePourGrosClient() {
        var commande = service.creerCommande(ID_GROS_CLIENT);
        assertNotNull(commande.getNumero(), "On doit avoir la clé de la commande");
        assertEquals(REMISE_POUR_GROS_CLIENT, commande.getRemise(),
            "Une remise de 15% doit être appliquée pour les gros clients");
    }

    @Test
    void testCreerCommandePourPetitClient() {
        var commande = service.creerCommande(ID_PETIT_CLIENT);
        assertNotNull(commande.getNumero());
        assertEquals(BigDecimal.ZERO, commande.getRemise(),
            "Aucune remise ne doit être appliquée pour les petits clients");
    }

    @Test
    void testCreerCommandeInitialiseAdresseLivraison() {
        var commande = service.creerCommande(ID_PETIT_CLIENT);
        assertEquals(VILLE_PETIT_CLIENT, commande.getAdresseLivraison().getVille(),
            "On doit recopier l'adresse du client dans l'adresse de livraison");
    }


    @Test
    void testEnregistrementdesDatesdExpeditions() {
        var commande = service.creerCommande(ID_GROS_CLIENT);
        // commande pas livré
        Commande CLivree = service.enregistreExpedition(commande.getNumero());
        //date enreg ? ou non ?
        assertEquals(CLivree.getEnvoyeele(), LocalDate.now(), "L'expédition n'a pas la commande ");
    }


    @Test
    void testEnvoyerCommandeDejaLivree(){
        assertThrows(IllegalStateException.class, () -> service.enregistreExpedition(NUMERO_COMMANDE_DEJA_LIVREE), "La commande est deja livré.");
    }


}
