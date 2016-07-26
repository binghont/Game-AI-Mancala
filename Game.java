import java.util.Arrays;

/**
 *
 * @author AndyTian
 */
public class Game {

    public int[] board;
    public int size;
    public int Mancala1;
    public int Mancala2;

    public String name;
    public int depth;
    public int val;
    public boolean nextMove;
    public boolean finalstate;

    public Game(int[] board) {
        size = board.length;
        this.board = board;
        Mancala1 = size / 2;
        Mancala2 = 0;
    }

    public Game(Game g) {
        size = g.board.length;
        board=new int [size];
        for (int i = 0; i < g.size; i++) {
            board[i] = g.board[i];
        }
        // this.board = Arrays.copyOf(g.board, size);
        Mancala1 = size / 2;
        Mancala2 = 0;
    }

    public String changeName(int position) {
        if (position < Mancala1) {
            return "B" + (position + 1);
        } else {
            return "A" + (size - position + 1);
        }

    }

    public void Move(int position, int player) {
        int sum = board[position];
        board[position] = 0;

        position = (position + 1) % size;
        while (sum != 1) {
            if (!(player == 1 && position == Mancala2) && !(player == 2 && position == Mancala1)) {
                sum--;
                board[position]++;
            }
            position = (position + 1) % size;
        }
        nextMove = lastpositionJudgement(position, player);
    }

    public boolean lastpositionJudgement(int position, int player) {
        if (player == 1 && position == Mancala1) {
            board[Mancala1]++;
            return !endJudgement();
        }
        if (player == 2 && position == Mancala2) {
            board[Mancala2]++;
            return !endJudgement();
        }
        if (player == 1 && position == Mancala2 || player == 2 && position == Mancala1) {
            position++;
        }
        if (player == 1 && position < Mancala1 && position > 0 && board[position] == 0) {
            // player == 1 && position < Mancala1 &&position>0&& board[position] == 0 && board[size - position] != 0
            board[Mancala1] += 1 + board[size - position];
            board[size - position] = 0;
        } else if (player == 2 && position > Mancala1 && position < size && board[position] == 0) {
            //player == 2 && position > Mancala1 && position<size&&board[position] == 0 && board[size - position] != 0
            board[Mancala2] += 1 + board[size - position];
            board[size - position] = 0;
        } else {
            board[position]++;
        }
        endJudgement();
        return false;

    }

    public boolean endJudgement() {
        int sum = 0;
        boolean over = true;

        for (int i = 1; i < Mancala1; i++) {
            if (board[i] != 0) {
                over = false;
                break;

            }
        }
        if (over) {
            for (int i = Mancala1 + 1; i < size; i++) {
                sum += board[i];
                board[i] = 0;
            }
            board[Mancala2] += sum;
            this.finalstate=true;
            return true;
        }
        over = true;
        for (int i = Mancala1 + 1; i < size; i++) {
            if (board[i] != 0) {
                over = false;
                break;
            }
        }
        if (over) {
            for (int i = 1; i < Mancala1; i++) {
                sum += board[i];
                board[i] = 0;
            }
            board[Mancala1] += sum;
            this.finalstate=true;
            return true;
        }
        this.finalstate=false;
        return false;
    }

    public int Evaluation(int player) {
        if (player == 1) {
            val = board[Mancala1] - board[Mancala2];
        } else {
            val = board[Mancala2] - board[Mancala1];
        }
        return val;

    }

    public void printState() {
        System.out.print(board[0] + " ");
        for (int i = size - 1; i >= Mancala1; i--) {
            System.out.print(board[i] + " ");
        }
        System.out.println();
        for (int i = 0; i <= Mancala1; i++) {
            System.out.print(board[i] + " ");
        }
        System.out.println();
        System.out.println();
    }
}
