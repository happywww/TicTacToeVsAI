/**
 * Created by Daniel on 2015/5/21.
 * �����X��AI��O����������ķ����¡�
 * ���ս����3�֣�AIʤ�����ʤ���;֡�
 * �� ���� �� �˳� ���ܡ�
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
        //��ʼ��
        setTitle("Tic Tac Toe vs AI ");
        setSize(300, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //��ȡcontent pane
        content = getContentPane();
        content.setBackground(Color.blue.darker());

        //����
        content.setLayout(new GridLayout(4, 3));

        //���ɰ�����handler
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

        //���ɽ����ǩ
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


        // ���������Һ�AI˭����
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

    // �ж�le��û��Ӯ
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


    // ��moveList��������һ�������ϵĿ�λ
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

        // ��moveList�д��������ϵĿ�λ
        if(len != 0)
        {
            final double d = Math.random();
            return possibleMoves[(int)(len*d)];
        }
        //��������
        else
            return -1;
    }


    // ����AI�����ĸ�
    public int getComputerMove(char[] board)
    {


        //�����Ӻ�ֱ��ʤ��
        for(int i = 0; i != 9; ++i)
        {
            //���Ƶ��µ�boardCopy
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

        //�赲��һ�ʤ
        for(int i = 0; i != 9; ++i)
        {

            //���Ƶ��µ�boardCopy
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

        //���Ƶ��µ�boardCopy
        char[] boardCopy = board.clone();

        //����ռ���ĸ���
        int[] corner = { 0, 2, 6, 8 };
        int move = chooseRandomMoveFromList(boardCopy, corner);
        if (move != -1)
        {
            return  move;
        }

        //����ռ������
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


            // ��ȡ���µ����̵���Ϣ
            JButton pressed=(JButton)(e.getSource());


            String text=pressed.getText();

            // �����µ���Ч
            if(text.equals("X") || text.equals("O"))
            {
                return;
            }

            // ���µ���Ч
            board[Integer.valueOf(text) - 1] = 'X';
            pressed.setText("X");
            pressed.setIcon(new ImageIcon(getClass().getResource("x.png")));


            // �ж��Ƿ����ʤ��
            if(isWinner(board, 'X')) {
                gameOver = true;
                JOptionPane.showMessageDialog(null, "You win!", "Game over", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            //�ж��Ƿ�ƽ��
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



            // ����Ҳ�ʤ��������δ����AI����
            int computerMove = getComputerMove(board);
            board[computerMove] = 'O';
            cells[computerMove].setText("O");
            cells[computerMove].setIcon(new ImageIcon(getClass().getResource("o.jpg")));

            //�ж�AI�Ƿ�ʤ��
            if(isWinner(board, 'O')) {
                gameOver = true;
                JOptionPane.showMessageDialog(null, "AI wins!", "Game over", JOptionPane.INFORMATION_MESSAGE);
            }

            //�ж��Ƿ�ƽ��
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