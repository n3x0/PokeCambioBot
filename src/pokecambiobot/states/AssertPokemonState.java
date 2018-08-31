/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokecambiobot.states;

/**
 *
 * @author nexo_
 */
public class AssertPokemonState extends State {
    
    public static int STATE = 2;
    
    @Override
    public void on(){
        System.out.println("Solicitar pokedex - on");
    }
    @Override
    public void off(){
        System.out.println("Solicitar pokedex - off");
    }
    @Override
    public void ack(){
        System.out.println("Solicitar pokedex - ack");
    }
}
