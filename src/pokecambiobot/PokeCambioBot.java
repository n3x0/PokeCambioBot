/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokecambiobot;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 *
 * @author nexo_
 */
public class PokeCambioBot extends TelegramLongPollingBot {

    private String BOTTOKEN = "//";
    DB db;

    public PokeCambioBot() {
        db = new DB();
    }

    public void init() {
        try {
            db.init();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(PokeCambioBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getBotUsername() {
        return "PokeCambioBot";
    }

    @Override
    public String getBotToken() {
        return BOTTOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {

                SendMessage sendMessage = new SendMessage();

                sendMessage.setChatId(update.getMessage().getChatId());

                String command = update.getMessage().getText();
                String message = "Po na";

                if (command.matches("^/players")) {
                    message = db.getPlayers();
                } else if (command.matches("^/pokemon")) {
                    message = db.getPokemon();
                } else if (command.matches("^/has")) {
                    message = db.getHas();
                } else if (command.matches("^/wants")) {
                    message = db.getWants();
                } else if (command.matches("^/tengo \\w+")) {
                    message = db.addHas(update.getMessage());
                }else if (command.matches("^/quiero \\w+")){
                    message = db.addWants(update.getMessage());
                }                else if (command.matches("^/noquiero \\w+")){
                    message = db.delWants(update.getMessage());
                } else if (command.matches("^/register \\w+ \\w+ \\w+")) {
                    message = registerPlayer(update.getMessage());
                }else if (command.matches("^/ayuda") || (command.matches("^/help"))){
                    message = showHelp();
                }

                sendMessage.setText(message);

                execute(sendMessage); // Call method to send the message

            } catch (TelegramApiException | SQLException ex) {
                Logger.getLogger(PokeCambioBot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String registerPlayer(Message message) {
        try {
            String[] splited = message.getText().split("\\s+");
            db.register(message.getChat(), splited);
            return "Usuario registrado";
        } catch (SQLException ex) {
            Logger.getLogger(PokeCambioBot.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Error: Usuario no registrado";
    }

    private boolean checkPlayer(Message message) {
        try {
            Long playerID = message.getChatId();
            String playerName = message.getChat().getFirstName();
            return (db.checkPlayer(playerID, playerName));
        } catch (SQLException ex) {
            Logger.getLogger(PokeCambioBot.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private String showHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append("Este bot tiene 7 comandos:"
                + "\n/register nombre color códigoenunapalabra"
                + "\n/players"
                + "\n/pokemon"
                + "\n/has"
                + "\n/wants"
                + "\n/tengo nombrepokemon"
                + "\n/ayuda");
        return sb.toString();
    }
}
