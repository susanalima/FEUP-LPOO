package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import exception.InvalidOgreCountException;
import logic.Game;
import logic.GameStartSet;
import logic.GuardPersonality;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class MainWindow implements KeyListener, MouseListener {

	private JFrame frmGuidedProjectGui;
	
	private GameplayPanel gpPanel;
	private ConfigurationPanel configPanel;
	private	LevelCreationPanel lvlCreationPanel;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmGuidedProjectGui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frmGuidedProjectGui = new JFrame();

		frmGuidedProjectGui.getContentPane().setFont(new Font("Dialog", Font.PLAIN, 18));
		frmGuidedProjectGui.setTitle("Guided Project GUI");
		frmGuidedProjectGui.setResizable(false);
		frmGuidedProjectGui.setBounds(100, 100, 600, 480);
		frmGuidedProjectGui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmGuidedProjectGui.getContentPane().setLayout(null);

		initializeGameplayPanel();
		initializeConfigPanel();
		initializeLevelCreationPanel();
	}


	private void initializeConfigPanel() {

		configPanel = new ConfigurationPanel();
		frmGuidedProjectGui.getContentPane().add(configPanel);
		configPanel.setVisible(false);

		/**********************************************LISTENERS*********************************************/
		
		/********BTN*DONE************************************************************************************/
		
		configPanel.getBtnDone().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					GameStartSet gss = new GameStartSet(configPanel.getOgresNumber(), configPanel.getGuardType());
					gpPanel.setGameStartSet(gss);
					gpPanel.setVisible(true);
					configPanel.setVisible(false);
					
				} catch (InvalidOgreCountException e) {
					gpPanel.setGameStartSet(null);
				}
			}
		});
	}

	private void initializeLevelCreationPanel() {
		
		lvlCreationPanel = new LevelCreationPanel();
		frmGuidedProjectGui.getContentPane().add(lvlCreationPanel);
		lvlCreationPanel.setVisible(false);
		
		/**********************************************LISTENERS*********************************************/
		
		/********BTN*PLAY**********************************************************************************/
		
		lvlCreationPanel.getBtnPlayCreatedLvl().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				gpPanel.setVisible(true);
				configPanel.setVisible(false);
				lvlCreationPanel.setVisible(false);
				gpPanel.playMap(lvlCreationPanel.getMap());
			}
		});
		
		/********BTN*CREATELEVEL*********************************************************************************/
		
		lvlCreationPanel.getBtnCreateLevel().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				configPanel.setVisible(false);
				lvlCreationPanel.setVisible(true);
				
				if(lvlCreationPanel.setMapDimensions()) {
					gpPanel.setGame(lvlCreationPanel.getGss().startNewGame());
					lvlCreationPanel.getMapCreationPanel().setMap(gpPanel.getGame().toString());
					lvlCreationPanel.getMapCreationPanel().repaint();
				} else {
				}
				
			}
		});

		/********BTN*RESTART*********************************************************************************/
		
		lvlCreationPanel.getBtnRestart().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lvlCreationPanel.getBtnCreateLevel().doClick();
			}
		});

		/********BTN*RETURN*********************************************************************************/

		lvlCreationPanel.getBtnReturn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lvlCreationPanel.setVisible(false);
				configPanel.setVisible(true);	
			}
		});

	}
	

	private void initializeGameplayPanel() {
		gpPanel = new GameplayPanel();
		frmGuidedProjectGui.getContentPane().add(gpPanel);
		
		/**********************************************LISTENERS*********************************************/
		
		/********BTN*CONFIG**********************************************************************************/
		
		gpPanel.getBtnConfig().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				gpPanel.setVisible(false);
				configPanel.setVisible(true);
			}
		});
	}

	/**********************Aux Functions**************************/

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


}
