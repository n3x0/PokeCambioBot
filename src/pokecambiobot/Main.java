/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokecambiobot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 *
 * @author nexo_
 */
public class Main {
    public static void main(String[] args) {

        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            PokeCambioBot pcb = new PokeCambioBot();
//            pcb.init();
            botsApi.registerBot(pcb);
            System.out.println("Bot corriendo");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
