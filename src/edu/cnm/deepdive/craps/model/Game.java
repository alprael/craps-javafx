package edu.cnm.deepdive.craps.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * This is the main class that defines how the Craps is played
 */
public class Game {

  private final Object lock = new Object();
  private State state = State.COME_OUT;
  private int point;
  private Random rng;
  private List<Roll> rolls;
  private int wins;
  private int losses;

  /**
   *  Create randomness for rolls.
   * @param rng
   */
  public Game(Random rng) {
    this.rng = rng;
    rolls = new LinkedList<>();
    wins = 0;
    losses = 0;
  }

  /**
   * Reset the state of the game starting at the COME_OUT phase,
   * with a starting tally of 0, and with rolls cleared.
   */
  public void reset() {
    state = State.COME_OUT;
    point = 0;
    synchronized (lock) {
      rolls.clear();
    }
  }

  private State roll() {
    int[] dice = {
        1 + rng.nextInt(6),
        1 + rng.nextInt(6)
    };
    int total = dice[0] + dice[1];
    State state = this.state.roll(total, point);
    if (this.state == State.COME_OUT && state == State.POINT) {
      point = total;
    }
    this.state = state;
    synchronized (lock) {
      rolls.add(new Roll(dice, state));
    }
    return state;
  }

  /**
   * Tells game whether it's in a win or loss state, and increments that state.
   * It returns the original state in the end
   * @return
   */
  public State play() {
    reset();
    while (state != State.WIN && state != State.LOSS) {
      roll();
    }
    if (state == State.WIN) {
      wins++;
    } else {
      losses++;
    }
    return state;
  }

  /**
   * Returns state of the game, whether it's in play or not.
   * @return
   */
  public State getState() {
    return state;
  }

  /**
   * Returns rolls from a LinkedList of Roll and syncs them so there isn't any overlapping.
   * @return
   */
  public List<Roll> getRolls() {
    synchronized (lock) {
      return new LinkedList<>(rolls);
    }
  }

  /**
   * Returns the number of wins according to the parameters of the game.
   * @return
   */
  public int getWins() {
    return wins;
  }

  /**
   * Returns number of losses according to the parameters of the game.
   * @return
   */
  public int getLosses() {
    return losses;
  }

  /**
   * Nested class that defines the function of dice rolls according to the game.
    */
  public static class Roll {
    private final int[] dice;
    private final State state;

  private Roll(int[] dice, State state) {
    this.dice = Arrays.copyOf(dice, 2);
    this.state = state;
  }

    /**
     * Returns the dice after they've been rolled.
     * @return
     */
  public int[] getDice() {
    return Arrays.copyOf(dice, 2);
  }

    /**
     * Returns state of the game, whether it's in play or stop.
     * @return
     */
  public State getState() {
    return state;
  }

    @Override
    public String toString() {
     return String.format("%s %s%n", Arrays.toString(dice), state);
    }
  }

  /**
   * An ENUM that defines win, loss, and point states of the game
   * and what they each mean.
   */
  public enum State {
    COME_OUT {
      /**
       * Defines what the rolls mean during this state of the game.
       * @param total
       * @param point
       * @return
       */
      @Override
      public State roll(int total, int point) {
        switch (total) {
          case 2:
          case 3:
          case 12:
            return LOSS;
          case 7:
          case 11:
            return WIN;
          default:
            return POINT;
        }
      }
    },
    WIN,
    LOSS,
    POINT {
      /**
       * Also defines what rolls mean when not in any of the above states.
       * @param total
       * @param point
       * @return
       */
      @Override
      public State roll(int total, int point) {
        if (total == point) {
          return WIN;
        }else if (total == 7) {
          return LOSS;
        } else {
          return POINT;
        }
      }
    };

    /**
     * Returns the state to that to roll again.
     * @param total
     * @param point
     * @return
     */
    public State roll(int total, int point) {
      return this;
    }
  }

}
