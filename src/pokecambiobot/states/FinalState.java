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
public class FinalState extends State {
    
    public static int STATE = 100;
    
    @Override
    public void on(){
        System.out.println("Estado final - on");
    }
    @Override
    public void off(){
        System.out.println("Estado final - off");
    }
    @Override
    public void ack(){
        System.out.println("Estado final - ack");
    }
}
