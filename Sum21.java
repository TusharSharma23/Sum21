
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/** Date - 20/April/2019
 * @author Tushar Sharma
 */

class Node {
    
    ArrayList<Node> childern;
    ArrayList<Integer> availableMoves;
    ArrayList<Integer> ScorePerMove;
    
    Node() {
        childern = new ArrayList<Node>();
        availableMoves = new ArrayList<Integer>();
        ScorePerMove = new ArrayList<Integer>();
    }
    
}

public class Sum21 {
    
    ArrayList<Integer> currentAvailableMoves;
    int currScore;
    int currDepth;
    Node root;
    
    int aptScore(int depth, Node node) {
        if(depth%2 != 0) {              //AI Mode
            int min = 100;
            for(int i = 0; i < node.ScorePerMove.size(); i++) {
                if(node.ScorePerMove.get(i) < min) {
                    min = node.ScorePerMove.get(i);
                }
            }
            return min;
        } else {                        //Player Mode
            int max = -100;
            for(int i = 0; i < node.ScorePerMove.size(); i++) {
                if(node.ScorePerMove.get(i) > max) {
                    max = node.ScorePerMove.get(i);
                }
            }
            return max;
        }
    }
    
    int calculateScore(int depth, int move) {
        if(move > 21) {
            if(depth%2 != 0) {                  //Player
                return 10 - depth;
            } else {                            //AI
                return depth - 10;
            }
        } else {
            return 0;
        }
    }
    
    boolean checkIfGameOver(int move) {
        return move>=21;
    }
    
    int minimaxTree(Node node, int depth, int move) {
        if(checkIfGameOver(move)) {
            int score =  calculateScore(depth, move);
            node.availableMoves.clear();
            node.ScorePerMove.add(score);
            return score;
        }
        
        int scr = 0;
        
        for(int i = 0; i < node.availableMoves.size(); i++) {
            int currMove = node.availableMoves.get(i);
            node.childern.add(new Node());
            node.childern.get(i).availableMoves.addAll(node.availableMoves);
            node.childern.get(i).availableMoves.remove(i);
            scr = minimaxTree(node.childern.get(i), depth + 1 , move + currMove);
                
            node.ScorePerMove.add(i, aptScore(depth, node.childern.get(i)));
        }
        
        return scr;
    }
    
    int nextMoveAI(Node node) {
        int minIndex = -1;
        int min = 100;
        for(int i = 0; i < node.ScorePerMove.size(); i++) {
            if(node.ScorePerMove.get(i) < min) {
                min = node.ScorePerMove.get(i);
                minIndex = i;
            }
        }
        int move = node.availableMoves.get(minIndex);
        root = node.childern.get(minIndex);
        currDepth++;
        return move;
    }
    
    void nextMovePlayer(Node node, int move) {
        currDepth++;
        root = node.childern.get(node.availableMoves.indexOf(move));
    }
    
    boolean validateMove(int move) {
        for(int i = 0; i < currentAvailableMoves.size(); i++) {
            if(currentAvailableMoves.get(i) == move)
                return true;
        }
        return false;
    }
    
    void introduceRules() {
        System.out.println("\t\t----------");
        System.out.println("\t\t| SUM 21 |");
        System.out.println("\t\t----------");
        System.out.println(" Rules:");
        System.out.println(" 1. Game will be played between you and the computer: ");
        System.out.println(" 2. You can select a number between 1 and 9 without repetition.");
        System.out.println(" 3. If the sum of all selected numbers is greater than 21, the last player to select the number loses.");
        System.out.println(" 4. If the sum is equal to 21, it will be a tie.");
        System.out.println(" 5. If you enter an invalid move, you will be prompted to enter again.");
        System.out.println(" Press enter if ready.");
    }
    
    public static void main(String args[]) {
        Sum21 obj = new Sum21();
        obj.currDepth = 0;
        obj.currentAvailableMoves = new ArrayList<Integer>();
        for(int i = 1; i <= 9; i++) {
            obj.currentAvailableMoves.add(i);
        }
        obj.currScore = 0;
        obj.root = new Node();
        
        Scanner scan = new Scanner(System.in);
        int move = -1;
        
        obj.introduceRules();
        scan.nextLine();
        
        while(true) {
            try{
            do{
                System.out.print("Your move :  ");
                move = scan.nextInt();
            }while(!obj.validateMove(move));
            break;
            } catch(InputMismatchException e) {
                System.out.println("Please enter number only");
                scan.nextLine();
            }
        }
        
        obj.currScore += move;
        
        obj.currentAvailableMoves.remove(obj.currentAvailableMoves.indexOf(move));
        obj.root.availableMoves.addAll(obj.currentAvailableMoves);
        obj.minimaxTree(obj.root, obj.currDepth, move);
        
        while(obj.currScore < 21) {
            
            if(obj.currDepth%2 != 0) {          //Players turn
                System.out.println("\t\tCurrent Sum:  "+ obj.currScore);
                System.out.print("\t\tAvailable moves: ");
                for(int i = 0; i < obj.currentAvailableMoves.size(); i++) {
                    System.out.print(" " + obj.currentAvailableMoves.get(i));
                }
                System.out.println();
                while(true) {
                    try{
                        do{
                            System.out.print("Your move :  ");
                            move = scan.nextInt();
                        }while(!obj.validateMove(move));
                            obj.nextMovePlayer(obj.root, move);
                            break;
                    } catch(InputMismatchException e) {
                        System.out.println("Please enter number only");
                        scan.nextLine();
                    }
                }
            } else {    
                //AI turn
                System.out.print(" AI move:  ");
                move = obj.nextMoveAI(obj.root);
                System.out.println(move);
            }
            obj.currentAvailableMoves.remove(obj.currentAvailableMoves.indexOf(move));
            obj.currScore += move;
        }
        if(obj.currScore > 21) {
            if(obj.currDepth%2 == 0) {
                System.out.println("AI wins!!!");
            } else {
                System.out.println("You win!!!");
            }
        } else {
            System.out.println("Its a tie:(");
        }
    }
    
}
