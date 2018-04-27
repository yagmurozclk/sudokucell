import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.SwingConstants;








import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.awt.Font;


public class Frame extends JFrame {

	private Frame frame;
	private JSpinner spinner;
	private String path;
	private List<Thread> threads = new ArrayList<Thread>();
	public JPanel getPanel() {
		return panel;
	}

	private JPanel panel;
	private List<List<Integer>> sudokuMatrix;
	private SudokuSolver solver;
	
	private JPanel contentPane;
	public List<List<JTextField>> sudokuMatrixBtn;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Frame() {
		
		
		frame = this; 
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.BOLD, 11));
		getContentPane().add(textField, BorderLayout.NORTH);
		textField.setColumns(10);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 649);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(10, 43, 395, 395);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(10, 449, 395, 151);
		contentPane.add(textArea);
		
		JFileChooser fileChooser = new JFileChooser();
		
		
		fileChooser.setCurrentDirectory(new File("C:\\"));
		
		
		JButton btnFile = new JButton("A\u00E7..");
		btnFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = fileChooser.showOpenDialog(fileChooser.getParent());
				if (result == JFileChooser.APPROVE_OPTION) {
				    File selectedFile = fileChooser.getSelectedFile();
				    
				   
					path = selectedFile.getParent();
				
				    ReadFile fileRead = new ReadFile();
				    if(sudokuMatrix!=null) sudokuMatrix.clear();
				    sudokuMatrix = fileRead.read(selectedFile);
				   
					for(int i=0;i<9;i++){
						
						for(int j=0;j<9;j++){
							
							printElement(i,j,sudokuMatrix.get(i).get(j));
							
						}
					}
					
					frame.update(getGraphics());
				}
			}
		});
		btnFile.setBounds(10, 9, 89, 23);
		contentPane.add(btnFile);
		
		
		solver = new SudokuSolver(this);
		JButton btnStart = new JButton("Ba\u015Flat");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				for(int i=0;i<(int)spinner.getValue();i++){
					Thread t = new Thread(new SudokuSolver(frame), String.valueOf(i+1));
					threads.add(t);
					t.start();
				}
			
			}
		});
		
		btnStart.setBounds(109, 9, 89, 23);
		contentPane.add(btnStart);
		
		spinner = new JSpinner();
		spinner.setValue(7);
		spinner.setBounds(376, 10, 29, 20);
		contentPane.add(spinner);
		
		JLabel lblNewLabel = new JLabel("Thread Say\u0131s\u0131");
		lblNewLabel.setBounds(289, 13, 78, 14);
		contentPane.add(lblNewLabel);
		
		sudokuMatrixBtn = new ArrayList<List<JTextField>>(); 
		
		for(int j=0;j<9;j++){
			sudokuMatrixBtn.add(new ArrayList<JTextField>());
			for(int i=0;i<9;i++){
				JTextField btn = new JTextField();
				btn.setBounds(7 + i*43, 7 + j*43, 38, 38);
				btn.setEditable(false);
				btn.setHorizontalAlignment(SwingConstants.CENTER);
				btn.setFont(new Font("Tahoma", Font.BOLD, 20));				
				btn.setBackground(Color.WHITE);
				btn.setForeground(Color.BLACK);
				btn.setAlignmentX(CENTER_ALIGNMENT);
				btn.setAlignmentY(CENTER_ALIGNMENT);
				panel.add(btn);
				sudokuMatrixBtn.get(j).add(btn);
				
			}
		}
		
		
	}
	
	private void printElement(int i,int j,int value){
		if(value!=0){
			sudokuMatrixBtn.get(i).get(j).setText(String.valueOf(value));
			sudokuMatrixBtn.get(i).get(j).setBackground(Color.WHITE);
			sudokuMatrixBtn.get(i).get(j).setForeground(Color.BLACK);
		}
		else{
			sudokuMatrixBtn.get(i).get(j).setText(null);
			sudokuMatrixBtn.get(i).get(j).setBackground(Color.WHITE);
			sudokuMatrixBtn.get(i).get(j).setForeground(Color.BLACK);
		}
		frame.repaint();
		sudokuMatrixBtn.get(i).get(j).update(sudokuMatrixBtn.get(i).get(j).getGraphics());
	}
	
	public void setUIElement(int i,int j,int value,boolean isFound){
		if(isFound){
			sudokuMatrixBtn.get(i).get(j).setText(String.valueOf(value));
			sudokuMatrixBtn.get(i).get(j).setBackground(Color.RED);
			sudokuMatrixBtn.get(i).get(j).setForeground(Color.GREEN);
		}
		else{
			sudokuMatrixBtn.get(i).get(j).setText(String.valueOf(value));
			sudokuMatrixBtn.get(i).get(j).setBackground(Color.WHITE);
			sudokuMatrixBtn.get(i).get(j).setForeground(Color.BLACK);
		}
		
		sudokuMatrixBtn.get(i).get(j).update(sudokuMatrixBtn.get(i).get(j).getGraphics());
		
		
	}

	public List<List<Integer>> getSudokuMatrix() {
		return sudokuMatrix;
	}

	public List<Thread> getThreads() {
		return threads;
	}

	public String getPath() {
		return path;
	}
	
	
	
	
	
}
