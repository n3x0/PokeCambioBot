/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokecambiobot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

public class DB {

    static private Connection connection = null;
    private boolean DEBUG = true;

    public Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:./mydatabase.db");
                return connection;
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            return connection;
        }
        return null;
    }

    public void init() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");

        connection = DriverManager.getConnection("jdbc:sqlite:./mydatabase.db");
        Statement st = connection.createStatement();
        st.setQueryTimeout(10);  // set timeout to 30 sec.

//        st.executeUpdate("drop table if exists person");
        st.executeUpdate("drop table if exists player");
        st.executeUpdate("drop table if exists pokemon");
        st.executeUpdate("drop table if exists wants");
        st.executeUpdate("drop table if exists has");

//      statement.executeUpdate("create table person (id integer, name string)");
        st.executeUpdate("create table player (tg_user_id integer, name string, color string, code string)");
        st.executeUpdate("create table pokemon (id integer, desc string, pokedex int, gender integer, shiny integer)");
        st.executeUpdate("create table wants (id_player integer, id_pokemon, units)");
        st.executeUpdate("create table has (id_player integer, id_pokemon, units)");

        st.executeUpdate("insert into player values(1, 'ikarugamesh', 'amarillo', '123')");
        st.executeUpdate("insert into player values(2, 'masterpaco', 'rojo', '231')");
        st.executeUpdate("insert into player values(3, 'canguror', 'azul', '312')");

        st.executeUpdate("insert into pokemon values(1, 'bulbasaur', 1, 1, 0)");
        st.executeUpdate("insert into pokemon values(2, 'ivysaur', 2, 1, 0)");
        st.executeUpdate("insert into pokemon values(3, 'venusaur', 3, 1, 0)");
        st.executeUpdate("insert into pokemon values(4, 'squirtle', 4, 1, 0)");
        st.executeUpdate("insert into pokemon values(5, 'wartortle', 5, 1, 0)");
        st.executeUpdate("insert into pokemon values(6, 'blastoise', 6, 1, 0)");
        st.executeUpdate("insert into pokemon values(7, 'charmander',7, 1, 0)");
        st.executeUpdate("insert into pokemon values(8, 'charmeleon', 8, 1, 0)");
        st.executeUpdate("insert into pokemon values(9, 'charizard', 9, 1, 0)");

        st.executeUpdate("insert into wants values(1, 1, 1)");
        st.executeUpdate("insert into wants values(1, 2, 1)");
        st.executeUpdate("insert into wants values(1, 3, 1)");
        st.executeUpdate("insert into wants values(2, 4, 1)");
        st.executeUpdate("insert into wants values(2, 5, 1)");
        st.executeUpdate("insert into wants values(2, 6, 1)");
        st.executeUpdate("insert into wants values(3, 7, 1)");
        st.executeUpdate("insert into wants values(3, 8, 1)");
        st.executeUpdate("insert into wants values(3, 9, 1)");

        st.executeUpdate("insert into has values(3, 1, 1)");
        st.executeUpdate("insert into has values(3, 2, 1)");
        st.executeUpdate("insert into has values(3, 3, 1)");
        st.executeUpdate("insert into has values(1, 4, 1)");
        st.executeUpdate("insert into has values(1, 5, 1)");
        st.executeUpdate("insert into has values(1, 6, 1)");
        st.executeUpdate("insert into has values(2, 7, 1)");
        st.executeUpdate("insert into has values(2, 8, 1)");
        st.executeUpdate("insert into has values(2, 9, 1)");

    }

    public String getPlayers() throws SQLException {
        StringBuilder sb = new StringBuilder();
        connection = getConnection();
        Statement st = connection.createStatement();
        st.setQueryTimeout(10);

        ResultSet rs = st.executeQuery("select * from player");
        if (!DEBUG) {
            while (rs.next()) {
                // read the result set
                sb.append("name = ").append(rs.getString("name")).append("\n");
            }
        } else {
            while (rs.next()) {
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    sb.append(rs.getString(rs.getMetaData().getColumnName(i))).append(" ");
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    String getPokemon() throws SQLException {
        StringBuilder sb = new StringBuilder();
        connection = getConnection();
        Statement st = connection.createStatement();
        st.setQueryTimeout(10);

        ResultSet rs = st.executeQuery("select * from pokemon");
        while (rs.next()) {
            sb.append(rs.getString("pokedex")).append(": ").append(rs.getString("desc")).append("\n");

        }
        return sb.toString();
    }

    String getWants() throws SQLException {
        StringBuilder sb = new StringBuilder();
        connection = getConnection();
        Statement st = connection.createStatement();
        st.setQueryTimeout(10);

        ResultSet rs = st.executeQuery(" select player.name as name, pokemon.desc as poke"
                + " from player, pokemon, wants "
                + " where wants.id_player = player.tg_user_id and"
                + " wants.id_pokemon = pokemon.id ");
        while (rs.next()) {
            sb.append(rs.getString("name")).append(" quiere ").append(rs.getString("poke")).append("\n");
        }
        return sb.toString();
    }

    String getHas() throws SQLException {
        StringBuilder sb = new StringBuilder();
        connection = getConnection();
        Statement st = connection.createStatement();
        st.setQueryTimeout(10);

        ResultSet rs = st.executeQuery(" select player.name as name, pokemon.desc as poke "
                + " from player, pokemon, has "
                + " where has.id_player = player.tg_user_id and"
                + " has.id_pokemon = pokemon.id ");
        while (rs.next()) {
            sb.append(rs.getString("name")).append(" tiene ").append(rs.getString("poke")).append("\n");
        }
        return sb.toString();
    }

    void register(Chat chat, String[] splited) throws SQLException {
        connection = getConnection();
        Statement st = connection.createStatement();
        st.executeUpdate("insert into player values(" + chat.getId() + ", '" + splited[1] + "'"
                + ", '" + splited[2] + "', '" + splited[3] + "')");
    }

    String addHas(Message message) throws SQLException {
        String retMes = "No existe ese pokemon. Consulta los pokemon con el comando /pokemon";
        connection = getConnection();
        Statement st = connection.createStatement();
        String[] split = message.getText().split("\\s+");
        switch (split.length) {
            case 2:
                ResultSet rs = st.executeQuery(" select count(*) as count, * from pokemon where desc like '" + split[1] + "'");
                if (rs.next()) {
                    int pokemon_id = rs.getInt("id");
                    st.executeUpdate(" insert into has values (" + message.getChatId() + ",  " + pokemon_id + ", 1)");
                    retMes = " Ahora el usuario " + message.getChatId() + " tiene un " + split[1] + ".";
                }
                break;
            default:
        }

//        st.executeUpdate("insert into has values (" + );
        return retMes;
    }

    String addWants(Message message) throws SQLException {
        String retMes = "No existe ese pokemon. Consulta los pokemon con el comando /pokemon";
        connection = getConnection();
        Statement st = connection.createStatement();
        String[] split = message.getText().split("\\s+");
        switch (split.length) {
            case 2:
                ResultSet rs = st.executeQuery(" select count(*) as count, * from pokemon where desc like '" + split[1] + "'");
                if (rs.next()) {
                    int pokemon_id = rs.getInt("id");
                    st.executeUpdate(" insert into wants values (" + message.getChatId() + ",  " + pokemon_id + ", 1)");
                    retMes = " Ahora el usuario " + message.getChatId() + " quiere un " + split[1] + ".";
                }
                break;
            default:
        }

//        st.executeUpdate("insert into has values (" + );
        return retMes;
    }

    String delWants(Message message) throws SQLException {
        String retMes = "No existe ese pokemon.";
        connection = getConnection();
        Statement st = connection.createStatement();
        String[] split = message.getText().split("\\s+");
        switch (split.length) {
            case 2:
                ResultSet rs = st.executeQuery(" select count(*) as count, * from pokemon where desc like '" + split[1] + "'");
                if (rs.next()) {
                    int pokemon_id = rs.getInt("id");
                    st.executeUpdate(" delete from wants where id_pokemon = " + pokemon_id + " and id_player = " + message.getChatId() + ";");
                    retMes = " Ahora el usuario " + message.getChatId() + " ya no quiere un " + split[1] + ".";
                }
                break;
            default:
        }

//        st.executeUpdate("insert into has values (" + );
        return retMes;
    }

    boolean checkPlayer(Long playerID, String playerName) throws SQLException {
        boolean res = false;

        StringBuilder sb = new StringBuilder();
        connection = getConnection();
        Statement st = connection.createStatement();
        st.setQueryTimeout(10);

        ResultSet rs = st.executeQuery(" select name from player "
                + " where tg_user_id = " + playerID);

        if (rs.getFetchSize() == 1) {
            if (rs.getString("name").equals(playerName)) {
                res = true;
            }
        } else {
            res = false;
        }
        return res;
    }

}
