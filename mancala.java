import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author AndyTian
 */
public class mancala {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String fileName = "";
        if(args[0].equals("-i")){
         fileName=args[1];
         }
        File file = new File(fileName);
        BufferedReader reader = null;
        FileWriter fw = null;
        int task;
        int player;
        int cutting_off_depth;
        int[] board;
        int size;
        try {
            reader = new BufferedReader(new FileReader(file));
            task = Integer.parseInt(reader.readLine());
            player = Integer.parseInt(reader.readLine());
            cutting_off_depth = Integer.parseInt(reader.readLine());
            String[] ss = reader.readLine().split(" ");
            size = ss.length * 2 + 2;
            board = new int[size];
            for (int i = 0; i < ss.length; i++) {
                board[size - 1 - i] = Integer.parseInt(ss[i]);
            }
            ss = reader.readLine().split(" ");
            for (int i = 0; i < ss.length; i++) {
                board[i + 1] = Integer.parseInt(ss[i]);
            }
            board[0] = Integer.parseInt(reader.readLine());
            board[size / 2] = Integer.parseInt(reader.readLine());
            reader.close();
            if (task == 1) {
                cutting_off_depth = 1;
            }
            GameAI ga = new GameAI(board, player, cutting_off_depth);
            ArrayList<String> traverse_log;
            switch (task) {
                case 1:
                    traverse_log = ga.Minimax();
                    break;
                case 2:
                    traverse_log = ga.Minimax();
                    break;
                default:
                    traverse_log = ga.ALpha_Beta_Pruning();
                    break;
            }

            FileOutputStream outSTr;
            BufferedOutputStream Buff;
            
            outSTr = new FileOutputStream(new File("traverse_log.txt"));
            Buff = new BufferedOutputStream(outSTr);
           
            for (String s:traverse_log) {
                Buff.write((s+ "\n").getBytes());
            }

            Buff.flush();
            Buff.close();

            outSTr = new FileOutputStream(new File("next_state.txt"));
            Buff = new BufferedOutputStream(outSTr);
            for (int i = size - 1; i > size/2+1; i--) {
                Buff.write((ga.nextstate.board[i] + " ").getBytes());
            }
             Buff.write((ga.nextstate.board[size/2+1] + "\n").getBytes());
          
            for (int i = 1; i < size/2-1; i++) {
                 Buff.write((ga.nextstate.board[i] + " ").getBytes());
            }
             Buff.write((ga.nextstate.board[size/2-1] + "\n").getBytes());    
             Buff.write((ga.nextstate.board[0] + "\n").getBytes()); 
             Buff.write((String.valueOf(ga.nextstate.board[size/2])).getBytes()); 
            Buff.flush();
            Buff.close();
            
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

    }

}
