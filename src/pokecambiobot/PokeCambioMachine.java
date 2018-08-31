/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokecambiobot;

import pokecambiobot.states.AssertPokemonState;
import pokecambiobot.states.FinalState;
import pokecambiobot.states.StandByState;
import pokecambiobot.states.State;

/**
 *
 * @author nexo_
 */
public class PokeCambioMachine {

    private final State[] states = {new StandByState(), new AssertPokemonState(), new FinalState()};
    private final int[][] transitions = {
        {AssertPokemonState.STATE, StandByState.STATE},
        {FinalState.STATE, AssertPokemonState.STATE}
    };

    private int current = 0;

    private void next(int msg) {
        current = transitions[current][msg];
    }

    public void on() {
        states[current].on();
        next(0);
    }

    public void off() {
        states[current].off();
        next(1);
    }

    public void ack() {
        states[current].ack();
        next(2);
    }
}
