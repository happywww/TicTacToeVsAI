/**
 * Created by Daniel on 2015/5/21.
 * 玩家是X，AI是O，随机决定哪方先下。
 * 最终结果分3种：AI胜、玩家胜、和局。
 * 有 重玩 和 退出 功能。
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Math;

public class TicTacToeVsAI extends JFrame {
    private Container content;
    private JLabel result;
    private JButton[] cells;
    private JButton exitButton;
    private JButton initButton;
    private CellButtonHandler[] cellHandlers;
    private ExitButtonHandler exitHandler;
    private InitButtonHandler initHandler;

    private char[] board = new char[9];

    private boolean noughts;
    private boolean gameOver;

    public TicTacToeVsAI() {
        //初始化
        setTitle("Tic Tac Toe vs AI ");
        setSize(300, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //获取content pane
        content = getContentPane();
        content.setBackground(Color.blue.darker());

        //布局
        content.setLayout(new GridLayout(4, 3));

        //生成按键和handler
        cells = new JButton[9];
        cellHandlers = new CellButtonHandler[9];
        for (int i = 0; i < 9; i++) {
            char ch = (char) ('0' + i + 1);
            cells[i] = new JButton("" + ch);
            cellHandlers[i] = new CellButtonHandler();
            cells[i].addActionListener(cellHandlers[i]);
        }


        exitButton = new JButton("EXIT");
        exitHandler = new ExitButtonHandler();
        exitButton.addActionListener(exitHandler);
        initButton = new JButton("<html>PLAY<br />AGAIN!</html>");
        initHandler = new InitButtonHandler();
        initButton.addActionListener(initHandler);

        //生成结果标签
        result = new JLabel("Result", SwingConstants.CENTER);
        result.setForeground(Color.white);


        for (int i = 0; i < 9; i++) {
            content.add(cells[i]);
        }
        content.add(initButton);
        content.add(result);
        content.add(exitButton);


        init();
    }

    public void init() {
        noughts = true;
        gameOver = false;

        for (int i = 0; i < 9; i++) {
            char ch = (char) ('0' + i + 1);
            board[i] = ch;
            cells[i].setText("" + ch);
            cells[i].setIcon(new ImageIcon(getClass().getResource("")));
        }

        result.setText("Result");


        // 随机决定玩家和AI谁先走
        if(Math.random() < 0.5) {
            int computerMove =  getComputerMove(board);
            board[computerMove] = 'O';
            cells[computerMove].setText("O");
            cells[computerMove].setIcon(new ImageIcon(getClass().getResource("o.jpg")));
            result.setText("<html>O(computer)<br /> first</html>");
        }
        else
        {
            result.setText("X(you) first");
        }



        setVisible(true);


    }

    // 判断le有没有赢
    public boolean isWinner(char[] bo, char le)
    {
        return ((bo[0] == le && bo[1] == le && bo[2] == le) ||
                (bo[3] == le && bo[4] == le && bo[5] == le) ||
                (bo[6] == le && bo[7] == le && bo[8] == le) ||
                (bo[0] == le && bo[3] == le && bo[6] == le) ||
                (bo[1] == le && bo[4] == le && bo[7] == le) ||
                (bo[2] == le && bo[5] == le && bo[8] == le) ||
                (bo[0] == le && bo[4] == le && bo[8] == le) ||
                (bo[2] == le && bo[4] == le && bo[6] == le));
    }


    public static void main(String[] args)
    {
        TicTacToeVsAI gui=new TicTacToeVsAI();
    }


    // 从moveList中随机获得一个棋盘上的空位
    public int chooseRandomMoveFromList(char[] board, int[] movesList)
    {
        int[] possibleMoves = new int[4];
        int len = 0;
        for (int i : movesList)
        {
            if (board[i] != 'X' && board[i] != 'O')
            {
                possibleMoves[len] = i;
                ++len;
            }
        }

        // 若moveList中存在棋盘上的空位
        if(len != 0)
        {
            final double d = Math.random();
            return possibleMoves[(int)(len*d)];
        }
        //若不存在
        else
            return -1;
    }


    // 计算AI下在哪格
    public int getComputerMove(char[] board)
    {


        //若落子后直接胜出
        for(int i = 0; i != 9; ++i)
        {
            //复制到新的boardCopy
            char[] boardCopy = board.clone();

            if(boardCopy[i] != 'X' && boardCopy[i] != 'O')
            {
                boardCopy[i] = 'O';
                if(isWinner(boardCopy, 'O'))
                {
                    return  i;
                }
            }
        }

        //阻挡玩家获胜
        for(int i = 0; i != 9; ++i)
        {

            //复制到新的boardCopy
            char[] boardCopy = board.clone();

            if(boardCopy[i] != 'X' && boardCopy[i] != 'O')
            {
                boardCopy[i] = 'X';
                if(isWinner(boardCopy, 'X'))
                {
                    return  i;
                }
            }
        }

        //复制到新的boardCopy
        char[] boardCopy = board.clone();

        //尝试占据四个角
        int[] corner = { 0, 2, 6, 8 };
        int move = chooseRandomMoveFromList(boardCopy, corner);
        if (move != -1)
        {
            return  move;
        }

        //尝试占领中心
        if(boardCopy[4] != 'X' && boardCopy[4] != 'O')
        {
            return 4;
        }

        int[] others = { 1, 3, 5, 7};
        return chooseRandomMoveFromList(boardCopy, others);
    }

    private class CellButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {

            if(gameOver)
            {
                return;
            }


            // 获取按下的棋盘的信息
            JButton pressed=(JButton)(e.getSource());


            String text=pressed.getText();

            // 若按下的无效
            if(text.equals("X") || text.equals("O"))
            {
                return;
            }

            // 按下的有效
            board[Integer.valueOf(text) - 1] = 'X';
            pressed.setText("X");
            pressed.setIcon(new ImageIcon(getClass().getResource("x.png")));


            // 判断是否玩家胜利
            if(isWinner(board, 'X')) {
                gameOver = true;
                JOptionPane.showMessageDialog(null, "You win!", "Game over", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            //判断是否平局
            boolean boardFull = true;
            for(char i : board) {
                if(i != 'X' && i != 'O') {
                    boardFull = false;
                    break;
                }
            }
            if(boardFull && !gameOver) {
                gameOver = true;
                JOptionPane.showMessageDialog(null, "No one wins!", "Game over", JOptionPane.INFORMATION_MESSAGE);
            }



            // 若玩家不胜利且棋盘未满，AI下棋
            int computerMove = getComputerMove(board);
            board[computerMove] = 'O';
            cells[computerMove].setText("O");
            cells[computerMove].setIcon(new ImageIcon(getClass().getResource("o.jpg")));

            //判断AI是否胜利
            if(isWinner(board, 'O')) {
                gameOver = true;
                JOptionPane.showMessageDialog(null, "AI wins!", "Game over", JOptionPane.INFORMATION_MESSAGE);
            }

            //判断是否平局
            boardFull = true;
            for(char i : board) {
                if(i != 'X' && i != 'O') {
                    boardFull = false;
                    break;
                }
            }
            if(boardFull && !gameOver) {
                gameOver = true;
                JOptionPane.showMessageDialog(null, "No one wins!", "Game over", JOptionPane.INFORMATION_MESSAGE);
            }

        }
    }

    private class ExitButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }
    }

    private class InitButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            init();
        }
    }
}