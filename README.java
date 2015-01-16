package PikminPong;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {
	
	
	private static final long serialVersionUID = 1L;

	public static PlayerPaddle player;
	public static AIPaddle ai;
	public static Ball ball;
	KeyListener IH;

	JFrame frame; 
	public final int WIDTH = 400; 
	public final int HEIGHT = WIDTH / 16 * 9;
	public final Dimension gameSize = new Dimension(WIDTH, HEIGHT);
	public final String TITLE = "Pong InDev";

	BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

	static boolean gameRunning = false; 

	int p1Score, p2Score;

	public void run() {

		while (gameRunning) { 
			tick();
			render();

			try {
				Thread.sleep(7);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public synchronized void start() {
		gameRunning = true;
		new Thread(this).start();
	} 

	public static synchronized void stop() {
		gameRunning = false;
		System.exit(0);
	} 

	public Game() {
		frame = new JFrame();

		setMinimumSize(gameSize);
		setPreferredSize(gameSize);
		setMaximumSize(gameSize);

		frame.add(this, BorderLayout.CENTER);
		frame.pack();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setTitle(TITLE);
		frame.setLocationRelativeTo(null);

		IH = new KeyAdapter(){
			
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();

				// Player 1 controls
				if (keyCode == KeyEvent.VK_W) {
					Game.player.goingUp = true;
				}
				if (keyCode == KeyEvent.VK_S) {
					Game.player.goingDown = true;
				}

				// Player 2 controls
				if (keyCode == KeyEvent.VK_UP) {
					Game.ai.goingUp = true;
				}
				if (keyCode == KeyEvent.VK_DOWN) {
					Game.ai.goingDown = true;
				}

				// Other controls
				if (keyCode == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
			}

			public void keyReleased(KeyEvent e) {
				int keyCode = e.getKeyCode();

				// Player 1 controls
				if (keyCode == KeyEvent.VK_W) {
					Game.player.goingUp = false;
				}
				if (keyCode == KeyEvent.VK_S) {
					Game.player.goingDown = false;
				}

				// Player 2 controls
				if (keyCode == KeyEvent.VK_UP) {
					Game.ai.goingUp = false;
				}
				if (keyCode == KeyEvent.VK_DOWN) {
					Game.ai.goingDown = false;
				}

			}
		};
		addKeyListener(IH);

		player = new PlayerPaddle(10, 60);
		ai = new AIPaddle(getWidth() - 20, 60);
		ball = new Ball(getWidth() / 2, getHeight() / 2);

	}

	public void tick() {
		player.tick(this);
		ai.tick(this);
		ball.tick(this);
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();

		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

		g.setColor(Color.WHITE);

		g.drawString("Player 1: " + p1Score, 5, 10);
		g.drawString("Player 2: " + p2Score, getWidth() - 60, 10);

		player.render(g);
		ai.render(g);
		ball.render(g);

		g.dispose();
		bs.show();
	}
}
