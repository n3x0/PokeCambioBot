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
public class StandByState extends State{
    
    public static int STATE = 1;
    
    @Override
    public void on(){
        System.out.println("Estado inicial - on");
    }
    @Override
    public void off(){
        System.out.println("Estado inicial - off");
    }
    @Override
    public void ack(){
        System.out.println("Estado inicial - ack");
    }
}
