import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.swing.JTextField;

public class SudokuSolver implements Runnable {

	private int startRow, startCol;
	private int size;
	private Frame frame;
	private List<Cell> steps = new ArrayList<Cell>();
	int N = 9;
	private Cell startCell;

	int grid[][] = new int[9][9];

	SudokuSolver(Frame frame) {
		this.frame = frame;

	}

	public void setGrid(List<List<Integer>> sudokuMatrix) {
		for (int i = 0; i < N; i++) {

			for (int j = 0; j < N; j++) {
				grid[i][j] = sudokuMatrix.get(i).get(j);
			}
		}
	}

	boolean isValid(Cell cell, int value) {

		if (grid[cell.row][cell.col] != 0) {
			throw new RuntimeException(
					"Cannot call for cell which already has a value");
		}

		// if v present row, return false
		for (int c = 0; c < 9; c++) {
			if (grid[cell.row][c] == value)
				return false;
		}

		for (int r = 0; r < 9; r++) {
			if (grid[r][cell.col] == value)
				return false;
		}

		int x1 = 3 * (cell.row / 3);
		int y1 = 3 * (cell.col / 3);
		int x2 = x1 + 2;
		int y2 = y1 + 2;

		for (int x = x1; x <= x2; x++)
			for (int y = y1; y <= y2; y++)
				if (grid[x][y] == value)
					return false;

		return true;
	}

	Cell getNextCell(Cell cur) {

		int row = cur.row;
		int col = cur.col;

		col++;

		if (col > 8) {

			col = 0;
			row++;
		}

		if (row > 8)
			row = 0;

		if (row == this.getStartRow() && col == this.getStartCol()) {
			return null;
		}
		Cell next = new Cell(row, col);

		return next;
	}

	boolean solve(Cell cur) {
		
		
		
		if (cur == null)
			return true;

		if (grid[cur.row][cur.col] != 0) {

			return solve(getNextCell(cur));
		}

		for (int i = 1; i <= 9; i++) {

			boolean valid = isValid(cur, i);

			if (!valid)
				continue;

			grid[cur.row][cur.col] = i;
			Cell celll = new Cell(cur.row, cur.col);
			celll.setValue(grid[cur.row][cur.col]);
			steps.add(celll);
			printSteps(Thread.currentThread().getName()+".txt");
			Cell nextCell = getNextCell(cur);
			boolean solved = solve(nextCell);
			
			if (solved) {

				// this.frame.setUIElement(cur.row,cur.col,grid[cur.row][cur.col],true);

				return true;
			} else {

				grid[cur.row][cur.col] = 0;
				Cell cell = new Cell(cur.row, cur.col);
				cell.setValue(grid[cur.row][cur.col]);
				steps.add(cell);
				printSteps(Thread.currentThread().getName()+".txt");
				// this.frame.setUIElement(cur.row,cur.col,grid[cur.row][cur.col],false);

			}

		}

		return false;
	}

	
	public List<Cell> getSteps() {
		return steps;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getStartCol() {
		return startCol;
	}

	public void setStartCol(int startCol) {
		this.startCol = startCol;
	}

	@Override
	public void run() {
		
		try{

			double startTime = System.nanoTime();
	
			System.out.println(Thread.currentThread().getName()
					+ ". thread Started at :" + new Date());
	
			SecureRandom rnd = new SecureRandom();
			int ii = rnd.nextInt(9);
			int jj = rnd.nextInt(9);
	
			setGrid(this.frame.getSudokuMatrix());
			setStartRow(ii);
			setStartCol(jj);
			getSteps().clear();
			startCell = new Cell(ii, jj);
	
			boolean isSolved = solve(startCell);
			
			if (isSolved) {
	
				double endTime = System.nanoTime();
				double elapsedTimeInSeconds = (endTime - startTime) / 1000000000;
				
				System.out.println(Thread.currentThread().getName()
						+ ". thread Ended at :" + new Date());
				System.out.println(Thread.currentThread().getName()
						+ ". thread Worked for :" + elapsedTimeInSeconds
						+ " seconds");
				printSteps("Solution_" + Thread.currentThread().getName()+".txt");
				showResult();
				for (int i = 0; i < this.frame.getThreads().size(); i++) {
					if(!Thread.currentThread().getName().equals(this.frame.getThreads().get(i).getName())){
						this.frame.getThreads().get(i).stop();
					}
					
				}
				
				
	
				
			}
		
		}
		catch(Exception ex){
            //thread was 'stopped' here
			System.out.println(ex.getMessage());
            
            
        }
	}
	
	private void showResult(){
		
		List<Cell> stepList = new ArrayList<Cell>();
		
		for(Cell cell : this.getSteps()){
			
			if(cell.getValue()!=0){		
				frame.sudokuMatrixBtn.get(cell.getRow()).get(cell.getCol()).setText(String.valueOf(cell.getValue()));
				frame.sudokuMatrixBtn.get(cell.getRow()).get(cell.getCol()).setBackground(Color.GREEN);
				frame.sudokuMatrixBtn.get(cell.getRow()).get(cell.getCol()).setForeground(Color.WHITE);
				
									
			}
			else{						
				frame.sudokuMatrixBtn.get(cell.getRow()).get(cell.getCol()).setText(null);
				frame.sudokuMatrixBtn.get(cell.getRow()).get(cell.getCol()).setBackground(Color.WHITE);
				frame.sudokuMatrixBtn.get(cell.getRow()).get(cell.getCol()).setForeground(Color.BLACK);
				
				
			}
			
			frame.sudokuMatrixBtn.get(cell.getRow()).get(cell.getCol()).update(frame.sudokuMatrixBtn.get(cell.getRow()).get(cell.getCol()).getGraphics());
			frame.sudokuMatrixBtn.get(startCell.getRow()).get(startCell.getCol()).setBackground(new Color(0,100,0));
			frame.sudokuMatrixBtn.get(startCell.getRow()).get(startCell.getCol()).setForeground(Color.WHITE);
			frame.sudokuMatrixBtn.get(startCell.getRow()).get(startCell.getCol()).update(frame.sudokuMatrixBtn.get(startCell.getRow()).get(startCell.getCol()).getGraphics());
			
			try {
				Thread.sleep(1);							
			} catch (InterruptedException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
		
		
	}
	
	

	private void printSteps(String fileName) {
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			fw = new FileWriter(this.frame.getPath()+"\\"+fileName);
			bw = new BufferedWriter(fw);

			List<Cell> stepList = new ArrayList<Cell>();

			int stepCount = 0;
			for (Cell cell : this.getSteps()) {
				if (cell.getValue() != 0) {
					stepList.add(cell);
				}

				if (cell.getValue() == 0) {
					for (int i = 0; i < stepList.size(); i++) {
						if (stepList.get(i).getRow() == cell.getRow()
								&& stepList.get(i).getCol() == cell.getCol()) {
							stepList.remove(i);
							break;
						}
					}

					stepCount++;
					String step = "";
					for (Cell stepCell : stepList) {
						step = step + stepCell.getValue() + ",";
					}
					if (step.length() > 0) {
						step = step.substring(0, step.length() - 1);
						// System.out.println(stepCount + ": " + step);
						bw.write(stepCount + ": " + step);
						bw.newLine();
					}
				}

			}

			stepCount++;
			String step = "";
			for (Cell stepCell : stepList) {
				step = step + stepCell.getValue() + ",";
			}
			if (step.length() > 0) {
				step = step.substring(0, step.length() - 1);
				// System.out.println(stepCount + ": " + step);
				bw.write(stepCount + ": " + step);
				bw.newLine();
			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}

}