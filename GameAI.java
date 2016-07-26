import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author AndyTian
 */
public class GameAI {

    public int[] board;
    public Game state;
    public Game nextstate;
    int player;
    int Mancala1;
    int Mancala2;
    int size;
    int cutting_off_depth;
    int maxfor1layer;
    ArrayList<String> traverse_log;

    public GameAI(int[] board, int player, int cutting_off_depth) {
        this.board = board;
        this.player = player;
        Mancala1 = board.length / 2;
        Mancala2 = 0;
        size = board.length;
        state = new Game(board);
        state.name = "root";
        this.cutting_off_depth = cutting_off_depth;
        traverse_log = new ArrayList<String>();
        maxfor1layer = Integer.MIN_VALUE;
    }

    public void changeMinimaxLog(Game g) {
        String s = g.name + "," + g.depth + ",";
        if (g.val == Integer.MIN_VALUE) {
            s += "-Infinity";
        } else if (g.val == Integer.MAX_VALUE) {
            s += "Infinity";
        } else {
            s += g.val;
        }
        traverse_log.add(s);
    }

    public void changePruningLog(Game g, int a, int b) {
        String s = g.name + "," + g.depth + ",";
        if (g.val == Integer.MIN_VALUE) {
            s += "-Infinity";
        } else if (g.val == Integer.MAX_VALUE) {
            s += "Infinity";
        } else {
            s += g.val;
        }
        s += ",";
        if (a == Integer.MIN_VALUE) {
            s += "-Infinity";
        } else {
            s += a;
        }
        s += ",";
        if (b == Integer.MAX_VALUE) {
            s += "Infinity";
        } else {
            s += b;
        }
        traverse_log.add(s);
    }

    public void getSuccessor(LinkedList<Game> states, Game g, int player) {
        if (player == 1) {
            for (int i = 1; i < Mancala1; i++) {
                if (g.board[i] != 0) {
                    Game g1 = new Game(g);
                    g1.name = g1.changeName(i);
                    g1.Move(i, player);
                    states.add(g1);

                }
            }
        } else {
            for (int i = size - 1; i > Mancala1; i--) {
                if (g.board[i] != 0) {
                    Game g1 = new Game(g);
                    g1.name = g1.changeName(i);
                    g1.Move(i, player);
                    states.add(g1);

                }
            }
        }
    }

    public ArrayList<String> ALpha_Beta_Pruning() {
        traverse_log.add("Node,Depth,Value,Alpha,Beta");
        int depth = 0;
        int v = Max_value(state, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
        this.nextstate.printState();
        return traverse_log;

    }

    public int Max_value(Game g, int a, int b, int depth) {
        if (g.finalstate || depth == cutting_off_depth) {
            g.Evaluation(player);
            changePruningLog(g, a, b);
            return g.val;
        }
        g.val = Integer.MIN_VALUE;

        changePruningLog(g, a, b);

        LinkedList<Game> states = new LinkedList<Game>();
        this.getSuccessor(states, g, player);
        while (!states.isEmpty()) {
            Game g1 = states.pop();
            g1.depth = depth + 1;
            int v = g.val;
            if (!g1.nextMove) {
                g.val = Math.max(g.val, Min_value(g1, a, b, depth + 1));
            } else {
                g.val = Math.max(g.val, Max_value(g1, a, b, depth));
            }
            if ( g.val >= b) {
                changePruningLog(g, a, b);
                return g.val;
            }
            a = Math.max(a, g.val);
            if (g1.depth == 1 && !g1.nextMove && g.val > maxfor1layer) {
                maxfor1layer = g.val;
                nextstate = g1;
            }
            changePruningLog(g, a, b);
        }

        return g.val;
    }

    public int Min_value(Game g, int a, int b, int depth) {
        if (g.finalstate|| depth == cutting_off_depth) {
            g.Evaluation(player);
            changePruningLog(g, a, b);
            return g.val;
        }
        g.val = Integer.MAX_VALUE;

        changePruningLog(g, a, b);

        LinkedList<Game> states = new LinkedList<Game>();
        this.getSuccessor(states, g, player % 2 + 1);
        while (!states.isEmpty()) {
            Game g1 = states.pop();
            g1.depth = depth + 1;
            if (!g1.nextMove) {
                g.val = Math.min(g.val, Max_value(g1, a, b, depth + 1));
            } else {
                g.val = Math.min(g.val, Min_value(g1, a, b, depth));
            }
            if ( g.val <= a) {
                changePruningLog(g, a, b);
                return g.val;
            }
            b = Math.min(b, g.val);
            changePruningLog(g, a, b);
        }
        return g.val;

    }

    public ArrayList<String> Minimax() {
        traverse_log.add("Node,Depth,Value");
        int depth = 0;
        int v = max_value(state, depth);
        this.nextstate.printState();

        return this.traverse_log;
    }

    public int max_value(Game g, int depth) {
        if (g.finalstate || depth == cutting_off_depth) {
            g.Evaluation(player);
            changeMinimaxLog(g);
            return g.val;

        }
        g.val = Integer.MIN_VALUE;

        changeMinimaxLog(g);

        LinkedList<Game> states = new LinkedList<Game>();
        this.getSuccessor(states, g, player);
        while (!states.isEmpty()) {
            Game g1 = states.pop();
            g1.depth = depth + 1;
            if (!g1.nextMove) {
                g.val = Math.max(g.val, min_value(g1, depth + 1));
            } else {
                g.val = Math.max(g.val, max_value(g1, depth));
            }

            if (g1.depth == 1 && !g1.nextMove && g.val > maxfor1layer) {
                maxfor1layer = g.val;
                nextstate = g1;
            }
            changeMinimaxLog(g);
        }

        return g.val;
    }

    public int min_value(Game g, int depth) {
        if (g.finalstate || depth == cutting_off_depth) {
            g.Evaluation(player);
            changeMinimaxLog(g);
            return g.val;
        }
        g.val = Integer.MAX_VALUE;

        changeMinimaxLog(g);

        LinkedList<Game> states = new LinkedList<Game>();
        this.getSuccessor(states, g, player % 2 + 1);
        while (!states.isEmpty()) {
            Game g1 = states.pop();
            g1.depth = depth + 1;
            if (!g1.nextMove) {
                g.val = Math.min(g.val, max_value(g1, depth + 1));
            } else {
                g.val = Math.min(g.val, min_value(g1, depth));
            }
            changeMinimaxLog(g);
        }
        return g.val;

    }
}
